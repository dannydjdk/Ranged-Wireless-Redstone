package com.dannyandson.rangedwirelessredstone.logic;

import com.dannyandson.rangedwirelessredstone.Config;
import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockEntity;
import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.TinyRedstoneHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.fml.ModList;

import org.jspecify.annotations.Nullable;
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
        this.saveData = level.getDataStorage().computeIfAbsent(ChannelSaveData.TYPE);
    }

    public void setTransmitterChannel(BlockPos pos, int channel) {
        setTransmitterChannel(pos.toShortString(), channel);
    }

    public void setTransmitterChannel(BlockPos pos, int cellIndex, int channel) {
        setTransmitterChannel(pos.toShortString() + ", " + cellIndex, channel);
    }

    private void setTransmitterChannel(String pos, int channel) {
        saveData.setTransmitterChannel(pos, channel);
        saveData.setDirty();
    }

    public void setTransmitterWeakSignal(BlockPos pos, int signal) {
        setTransmitterWeakSignal(pos.toShortString(), signal);
    }

    public void setTransmitterWeakSignal(BlockPos pos, int cellIndex, int signal) {
        setTransmitterWeakSignal(pos.toShortString() + ", " + cellIndex, signal);
    }

    private void setTransmitterWeakSignal(String pos, int signal) {
        saveData.weakSignalMap.put(pos, signal);
        saveData.setDirty();
    }

    public void setTransmitterStrongSignal(BlockPos pos, int signal) {
        setTransmitterStrongSignal(pos.toShortString(), signal);
    }

    public void setTransmitterStrongSignal(BlockPos pos, int cellIndex, int signal) {
        setTransmitterStrongSignal(pos.toShortString() + ", " + cellIndex, signal);
    }

    private void setTransmitterStrongSignal(String pos, int signal) {
        saveData.strongSignalMap.put(pos, signal);
        saveData.setDirty();
    }

    public Map<String, Integer> getChannelSignal(int channel, BlockPos pos) {
        Map<String, Integer> signals = new HashMap<>();
        signals.put("strong", 0);
        signals.put("weak", 0);
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
                    Integer sSignal = saveData.strongSignalMap.get(tPos);
                    Integer wSignal = saveData.weakSignalMap.get(tPos);
                    if (sSignal != null && sSignal > signals.get("strong"))
                        signals.put("strong", sSignal);
                    if (wSignal != null && wSignal > signals.get("weak"))
                        signals.put("weak", wSignal);
                }
            }
        }
        return signals;
    }

    public void removeTransmitter(BlockPos pos) {
        removeTransmitter(pos.toShortString());
    }

    public void removeTransmitter(BlockPos pos, int cellIndex) {
        removeTransmitter(pos.toShortString() + ", " + cellIndex);
    }

    private void removeTransmitter(String pos) {
        Integer channel = saveData.getTransmitterChannel(pos);
        if (channel != null)
            saveData.channelPosMap.get(channel).remove(pos);
        saveData.strongSignalMap.remove(pos);
        saveData.setDirty();
    }

    public void cleanupTransmitters(BlockGetter blockGetter) {
        boolean tinyRedstoneLoaded = ModList.get().isLoaded("tinyredstone");

        for (Map.Entry<Integer, List<String>> entry : saveData.channelPosMap.entrySet()) {
            for (String posString : entry.getValue()) {
                int[] coords = getXYZiFromPosString(posString);
                BlockPos pos = new BlockPos(coords[0], coords[1], coords[2]);
                BlockEntity blockEntity = blockGetter.getBlockEntity(pos);
                if (tinyRedstoneLoaded && coords.length == 4) {
                    if (!TinyRedstoneHelper.cleanupPanelTransmitter(blockEntity, coords[3], entry.getKey())) {
                        removeTransmitter(posString);
                    }
                } else if (blockEntity instanceof TransmitterBlockEntity transmitter) {
                    transmitter.setChannel(entry.getKey());
                } else {
                    removeTransmitter(posString);
                }
            }
        }
        saveData.setDirty();
    }

    public CompoundTag getChannelNBT() {
        CompoundTag fullTag = saveData.saveToTag();
        return fullTag.getCompound("channeldata").orElseGet(CompoundTag::new);
    }

    public static int[] getXYZiFromPosString(String pos) {
        String[] posArray = pos.split(",\\s+");
        int[] posArrayInt = new int[posArray.length];
        for (int i = 0; i < posArray.length; i++)
            posArrayInt[i] = Integer.parseInt(posArray[i]);
        return posArrayInt;
    }

    private static class ChannelSaveData extends SavedData {
        public Map<Integer, List<String>> channelPosMap = new HashMap<>();
        public Map<String, Integer> weakSignalMap = new HashMap<>();
        public Map<String, Integer> strongSignalMap = new HashMap<>();

        // Codec for SavedDataType — serializes all data via a CompoundTag wrapper
        public static final Codec<ChannelSaveData> CODEC = RecordCodecBuilder.<ChannelSaveData>mapCodec(instance ->
                instance.group(
                        CompoundTag.CODEC.optionalFieldOf("data", new CompoundTag())
                                .forGetter(ChannelSaveData::saveToTag)
                ).apply(instance, ChannelSaveData::loadFromTag)
        ).codec();

        public static final SavedDataType<ChannelSaveData> TYPE = new SavedDataType<>(
                Identifier.fromNamespaceAndPath(RangedWirelessRedstone.MODID, RangedWirelessRedstone.MODID),
                ChannelSaveData::new,
                CODEC,
                null
        );

        public ChannelSaveData() {
        }

        public static ChannelSaveData loadFromTag(CompoundTag nbt) {
            ChannelSaveData data = new ChannelSaveData();
            CompoundTag channelData = nbt.getCompound("channeldata").orElseGet(CompoundTag::new);
            CompoundTag signalData = nbt.getCompound("signaldata").orElseGet(CompoundTag::new);
            CompoundTag weakSignalData = nbt.getCompound("weaksignaldata").orElseGet(CompoundTag::new);
            for (String key : channelData.keySet()) {
                int channel = channelData.getIntOr(key, 0);
                if (!data.channelPosMap.containsKey(channel))
                    data.channelPosMap.put(channel, new ArrayList<>());
                data.channelPosMap.get(channel).add(key);
                data.strongSignalMap.put(key, signalData.getIntOr(key, 0));
                data.weakSignalMap.put(key, weakSignalData.getIntOr(key, 0));
            }
            return data;
        }

        public CompoundTag saveToTag() {
            CompoundTag nbt = new CompoundTag();
            CompoundTag channelData = new CompoundTag(),
                    strongSignalData = new CompoundTag(),
                    weakSignalData = new CompoundTag();

            for (Map.Entry<Integer, List<String>> entry : channelPosMap.entrySet()) {
                for (String pos : entry.getValue())
                    channelData.putInt(pos, entry.getKey());
            }

            for (Map.Entry<String, Integer> entry : strongSignalMap.entrySet()) {
                strongSignalData.putInt(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Integer> entry : weakSignalMap.entrySet()) {
                weakSignalData.putInt(entry.getKey(), entry.getValue());
            }

            nbt.put("channeldata", channelData);
            nbt.put("signaldata", strongSignalData);
            nbt.put("weaksignaldata", weakSignalData);
            return nbt;
        }

        public void setTransmitterChannel(String pos, int channel) {
            Integer oldChannel = getTransmitterChannel(pos);
            if (oldChannel != null)
                channelPosMap.get(oldChannel).remove(pos);

            if (!channelPosMap.containsKey(channel))
                channelPosMap.put(channel, new ArrayList<>());
            channelPosMap.get(channel).add(pos);
        }

        @Nullable
        public Integer getTransmitterChannel(String pos) {
            for (Map.Entry<Integer, List<String>> entry : channelPosMap.entrySet()) {
                if (entry.getValue().contains(pos))
                    return entry.getKey();
            }
            return null;
        }
    }
}
