package com.dannyandson.rangedwirelessredstone.logic;

import com.dannyandson.rangedwirelessredstone.Config;
import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockEntity;
import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.TransmitterCell;
import com.dannyandson.tinyredstone.api.IPanelCell;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.ModList;

import javax.annotation.CheckForNull;
import java.util.*;

public class ChannelData {

    public static Map<Integer, ChannelData> levelChannelDataMap = new HashMap<>();

    public static ChannelData getChannelData(ServerWorld level) {
        int dim = level.dimension().hashCode();
        if (!levelChannelDataMap.containsKey(dim))
            levelChannelDataMap.put(dim, new ChannelData(level));
        return levelChannelDataMap.get(dim);
    }

    private final ChannelSaveData saveData;

    private ChannelData(ServerWorld level) {
        this.saveData = level.getDataStorage().computeIfAbsent(ChannelSaveData::new, RangedWirelessRedstone.MODID);
    }

    /**
     * Set or change the channel of a transmitter.
     *
     * @param pos     Block position of the transmitter
     * @param channel The channel of the transmitter
     */
    public void setTransmitterChannel(BlockPos pos, int channel){
        setTransmitterChannel(getStringFromPos(pos),channel);
    }
    /**
     * Set or change the channel of a transmitter.
     *
     * @param pos     String representing the position of the transmitter
     * @param cellIndex Cell index of tiny redstone component
     * @param channel The channel of the transmitter
     */
    public void setTransmitterChannel(BlockPos pos,int cellIndex,int channel){
        setTransmitterChannel(getStringFromPos(pos) + ", " + cellIndex,channel);
    }
    private void setTransmitterChannel(String pos, int channel) {
        saveData.setTransmitterChannel(pos, channel);
        saveData.setDirty();
    }

    /**
     * Set the redstone value to be transmitted by a transmitter
     *
     * @param pos    Block position of the transmitter
     * @param signal The signal strength to be transmitted
     */
    public void setTransmitterSignal(BlockPos pos, int signal) {
        setTransmitterSignal(getStringFromPos(pos),signal);
    }
    /**
     * Set the redstone value to be transmitted by a transmitter
     *
     * @param pos    "ShortString" representing the position of the transmitter
     * @param cellIndex Cell index of tiny redstone component
     * @param signal The signal strength to be transmitted
     */
    public void setTransmitterSignal(BlockPos pos, int cellIndex, int signal) {
        setTransmitterSignal(getStringFromPos(pos) + ", " + cellIndex,signal);
    }
    private void setTransmitterSignal(String pos, int signal) {
        saveData.signalMap.put(pos, signal);
        saveData.setDirty();
    }

    /**
     * Get the redstone value to be received by a receiver at a given position and channel.
     * This represents the maximum value being transmitted by a transmitter on the same channel
     * and of which the receiver is within range.
     *
     * @param channel The channel of the receiver
     * @param pos     The position of the receiver
     * @return An integer representing the signal strength a receiver should output
     */
    public Integer getChannelSignal(int channel, BlockPos pos) {
        int signal = 0;
        if (saveData.channelPosMap.containsKey(channel)) {
            for (String tPos : saveData.channelPosMap.get(channel)) {
                int[] tPosValues = getXYZiFromPosString(tPos);
                int x = tPosValues[0], y = tPosValues[1], z = tPosValues[2];
                boolean isCell = tPosValues.length == 4;
                int range = isCell ? Config.RANGE_CELL.get() : Config.RANGE_BLOCK.get();
                if (
                        Math.abs(x - pos.getX()) <= range &&
                                Math.abs(y - pos.getY()) <= range &&
                                Math.abs(z - pos.getZ()) <= range
                ) {
                    Integer tSignal = saveData.signalMap.get(tPos);
                    if (tSignal != null && tSignal > signal)
                        signal = tSignal;
                }
            }
        }
        return signal;
    }

    public void removeTransmitter(BlockPos pos){
        removeTransmitter(getStringFromPos(pos));
    }
    public void removeTransmitter(BlockPos pos, int cellIndex){
        removeTransmitter(getStringFromPos(pos) + ", " + cellIndex);
    }
    private void removeTransmitter(String pos) {
        Integer channel = saveData.getTransmitterChannel(pos);
        if (channel != null)
            saveData.channelPosMap.get(channel).remove(pos);
        saveData.signalMap.remove(pos);
        saveData.setDirty();
    }


    /**
     * Check transmitter locations to make sure they all exist.
     * Remove any data for non-existent transmitters.
     * This should not be necessary under normal circumstances,
     * but world crashes could leave orphaned transmitter data.
     */
    public void cleanupTransmitters(IBlockReader blockGetter) {

        for (Map.Entry<Integer, List<String>> entry : saveData.channelPosMap.entrySet()) {
            for (String posString : entry.getValue()) {
                int[] coords = getXYZiFromPosString(posString);
                BlockPos pos = new BlockPos(coords[0], coords[1], coords[2]);
                TileEntity blockEntity = blockGetter.getBlockEntity(pos);
                if (ModList.get().isLoaded("tinyredstone") && coords.length == 4 && blockEntity instanceof PanelTile) {
                    PanelCellPos panelCellPos = PanelCellPos.fromIndex(((PanelTile) blockEntity), coords[3]);
                    IPanelCell panelCell = panelCellPos.getIPanelCell();
                    if (panelCell instanceof TransmitterCell) {
                        ((TransmitterCell) panelCell).setChannel(entry.getKey());
                    } else {
                        removeTransmitter(posString);
                    }

                } else if (blockEntity instanceof TransmitterBlockEntity) {
                    ((TransmitterBlockEntity) blockEntity).setChannel(entry.getKey());
                } else {
                    removeTransmitter(posString);
                }
            }
        }
        saveData.setDirty();
    }

    public CompoundNBT getChannelNBT(){
        CompoundNBT nbt =  saveData.save(new CompoundNBT());
        return nbt.getCompound("channeldata");
    }

    private static int[] getXYZiFromPosString(String pos){
        String[] posArray = pos.split(",\\s+");
        int[] posArrayInt = new int[posArray.length];
        for (int i =0 ; i<posArray.length ; i++)
            posArrayInt[i] = Integer.parseInt(posArray[i]);
        return posArrayInt;
    }

    private static String getStringFromPos(BlockPos pos){
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    private static class ChannelSaveData extends WorldSavedData {
        public Map<Integer, List<String>> channelPosMap = new HashMap<>();
        public Map<String, Integer> signalMap = new HashMap<>();


        public ChannelSaveData() {
            super(RangedWirelessRedstone.MODID);
        }

        @Override
        public void load(CompoundNBT nbt) {
            CompoundNBT channelData = nbt.getCompound("channeldata");
            CompoundNBT signalData = nbt.getCompound("signaldata");
            for (String key : channelData.getAllKeys()) {
                int channel = channelData.getInt(key);
                if (!channelPosMap.containsKey(channel))
                    channelPosMap.put(channel, new ArrayList<>());
                channelPosMap.get(channel).add(key);
                this.signalMap.put(key, signalData.getInt(key));
            }
        }

        @Override
        public CompoundNBT save(CompoundNBT nbt) {
            CompoundNBT channelData = new CompoundNBT(),
                    signalData = new CompoundNBT();

            for(Map.Entry<Integer, List<String>> entry : channelPosMap.entrySet()) {
                for (String pos : entry.getValue())
                    channelData.putInt(pos,entry.getKey());
            }

            for (Map.Entry<String, Integer> entry : signalMap.entrySet()) {
                signalData.putInt(entry.getKey(), entry.getValue());
            }

            nbt.put("channeldata", channelData);
            nbt.put("signaldata", signalData);
            return nbt;
        }

        public void setTransmitterChannel(String  pos, int channel) {
            Integer oldChannel = getTransmitterChannel(pos);
            if (oldChannel != null)
                channelPosMap.get(oldChannel).remove(pos);

            if (!channelPosMap.containsKey(channel))
                channelPosMap.put(channel, new ArrayList<>());
            channelPosMap.get(channel).add(pos);
        }

        @CheckForNull
        public Integer getTransmitterChannel(String pos){
            for(Map.Entry<Integer, List<String>> entry : channelPosMap.entrySet()){
                if (entry.getValue().contains(pos))
                    return entry.getKey();
            }
            return null;
        }

    }
}
