package com.dannyandson.rangedwirelessredstone.logic;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class ChannelData {

    public static Map<Integer, ChannelData> levelChannelDataMap = new HashMap<>();

    public static ChannelData getChannelData(ServerLevel level) {
        int dim = level.dimension().hashCode();
        if (!levelChannelDataMap.containsKey(dim))
            levelChannelDataMap.put(dim, new ChannelData(level));
        return levelChannelDataMap.get(dim);
    }

    private final ChannelSaveData saveData;

    private ChannelData(ServerLevel level) {
        this.saveData = level.getDataStorage().computeIfAbsent(ChannelSaveData::new, ChannelSaveData::new, RangedWirelessRedstone.MODID);
    }

    /**
     * Set or change the channel of a transmitter.
     *
     * @param pos     Block position of the transmitter
     * @param channel The channel of the transmitter
     */
    public void setTransmitterChannel(BlockPos pos,int channel){
        setTransmitterChannel(pos.toShortString(),channel);
    }
    /**
     * Set or change the channel of a transmitter.
     *
     * @param pos     String representing the position of the transmitter
     * @param cellIndex Cell index of tiny redstone component
     * @param channel The channel of the transmitter
     */
    public void setTransmitterChannel(BlockPos pos,int cellIndex,int channel){
        setTransmitterChannel(pos.toShortString() + ", " + cellIndex,channel);
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
        setTransmitterSignal(pos.toShortString(),signal);
    }
    /**
     * Set the redstone value to be transmitted by a transmitter
     *
     * @param pos    "ShortString" representing the position of the transmitter
     * @param cellIndex Cell index of tiny redstone component
     * @param signal The signal strength to be transmitted
     */
    public void setTransmitterSignal(BlockPos pos, int cellIndex, int signal) {
        setTransmitterSignal(pos.toShortString() + ", " + cellIndex,signal);
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
                int x = tPosValues[0], y = tPosValues[1] , z = tPosValues[2];
                boolean isCell = tPosValues.length==4;
                int range = isCell?32:128;
                if (
                        //TODO config
                        Math.abs(x - pos.getX()) <= range &&
                                Math.abs(y - pos.getY()) <= range &&
                                Math.abs(z - pos.getZ()) <= range
                ) {
                    Integer tSignal = saveData.signalMap.get(tPos);
                    if(tSignal!=null && tSignal>signal)
                        signal = tSignal;
                }
            }
        }
        return signal;
    }

    public void removeTransmitter(BlockPos pos){
        removeTransmitter(pos.toShortString());
    }
    public void removeTransmitter(BlockPos pos, int cellIndex){
        removeTransmitter(pos.toShortString() + ", " + cellIndex);
    }
    private void removeTransmitter(String pos) {
        Integer channel = saveData.posChannelMap.remove(pos);
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
    public void cleanupTransmitters() {
    }

    private static int[] getXYZiFromPosString(String pos){
        String[] posArray = pos.split(",\s+");
        int[] posArrayInt = new int[posArray.length];
        for (int i =0 ; i<posArray.length ; i++)
            posArrayInt[i] = Integer.parseInt(posArray[i]);
        return posArrayInt;
    }

    private static class ChannelSaveData extends SavedData {
        public Map<String, Integer> posChannelMap = new HashMap<>();
        public Map<Integer, List<String>> channelPosMap = new HashMap<>();
        public Map<String, Integer> signalMap = new HashMap<>();


        public ChannelSaveData() {
        }

        public ChannelSaveData(CompoundTag nbt) {
            CompoundTag channelData = nbt.getCompound("channeldata");
            CompoundTag signalData = nbt.getCompound("signaldata");
            for (String key : channelData.getAllKeys()) {
                this.setTransmitterChannel(key, channelData.getInt(key));
                this.signalMap.put(key, signalData.getInt(key));

            }
        }

        @Override
        public CompoundTag save(CompoundTag nbt) {
            CompoundTag channelData = new CompoundTag(),
                    signalData = new CompoundTag();

            for (Map.Entry<String, Integer> entry : posChannelMap.entrySet()) {
                channelData.putInt(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Integer> entry : signalMap.entrySet()) {
                signalData.putInt(entry.getKey(), entry.getValue());
            }

            nbt.put("channeldata", channelData);
            nbt.put("signaldata", signalData);
            return nbt;
        }

        public void setTransmitterChannel(String  pos, int channel) {
            if (posChannelMap.containsKey(pos)) {
                Integer oldChannel = posChannelMap.remove(pos);
                if (oldChannel != null && channelPosMap.get(oldChannel).contains(pos))
                    channelPosMap.get(oldChannel).remove(pos);
            }
            if (!channelPosMap.containsKey(channel))
                channelPosMap.put(channel, new ArrayList<>());

            channelPosMap.get(channel).add(pos);
            posChannelMap.put(pos, channel);
        }

    }
}
