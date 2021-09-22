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
     * @param pos     The position of the transmitter
     * @param channel The channel of the transmitter
     */
    public void setTransmitterChannel(BlockPos pos, int channel) {
        saveData.setTransmitterChannel(pos, channel);
        saveData.setDirty();
    }

    /**
     * Set the redstone value to be transmitted by a transmitter
     *
     * @param pos    The position of the transmitter
     * @param signal The signal strength to be transmitted
     */
    public void setTransmitterSignal(BlockPos pos, int signal) {
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
            for (BlockPos tPos : saveData.channelPosMap.get(channel)) {
                if (
                        Math.abs(tPos.getX() - pos.getX()) <= 128 &&
                                Math.abs(tPos.getY() - pos.getY()) <= 128 &&
                                Math.abs(tPos.getZ() - pos.getZ()) <= 128
                ) {
                    Integer tSignal = saveData.signalMap.get(tPos);
                    if(tSignal!=null && tSignal>signal)
                        signal = tSignal;
                }
            }
        }
        return signal;
    }

    public void removeTransmitter(BlockPos pos) {
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

    private static class ChannelSaveData extends SavedData {
        public Map<BlockPos, Integer> posChannelMap = new HashMap<>();
        public Map<Integer, List<BlockPos>> channelPosMap = new HashMap<>();
        public Map<BlockPos, Integer> signalMap = new HashMap<>();

        public ChannelSaveData() {
        }

        public ChannelSaveData(CompoundTag nbt) {
            CompoundTag channelData = nbt.getCompound("channeldata");
            CompoundTag signalData = nbt.getCompound("signaldata");
            for (String key : channelData.getAllKeys()) {
                String[] posCoords = key.split(",\s+");
                if (posCoords.length == 3) {
                    BlockPos pos = new BlockPos(Integer.parseInt(posCoords[0]), Integer.parseInt(posCoords[1]), Integer.parseInt(posCoords[2]));
                    this.setTransmitterChannel(pos, channelData.getInt(key));
                    this.signalMap.put(pos, signalData.getInt(key));
                }
            }
        }

        @Override
        public CompoundTag save(CompoundTag nbt) {
            CompoundTag channelData = new CompoundTag(),
                    signalData = new CompoundTag();

            for (Map.Entry<BlockPos, Integer> entry : posChannelMap.entrySet()) {
                channelData.putInt(entry.getKey().toShortString(), entry.getValue());
            }
            for (Map.Entry<BlockPos, Integer> entry : signalMap.entrySet()) {
                signalData.putInt(entry.getKey().toShortString(), entry.getValue());
            }

            nbt.put("channeldata", channelData);
            nbt.put("signaldata", signalData);
            return nbt;
        }

        public void setTransmitterChannel(BlockPos pos, int channel) {
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
