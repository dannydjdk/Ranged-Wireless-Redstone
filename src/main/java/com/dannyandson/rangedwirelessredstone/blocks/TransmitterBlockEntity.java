package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.rangedwirelessredstone.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class TransmitterBlockEntity extends AbstractWirelessEntity {

    public TransmitterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registration.TRANSMITTER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void setSignals(int weak,int strong) {
        if (weak != this.weakSignal || strong != this.strongSignal) {
            if (this.level instanceof ServerLevel serverLevel) {
                ChannelData.getChannelData(serverLevel).setTransmitterWeakSignal(getBlockPos(), weak);
                ChannelData.getChannelData(serverLevel).setTransmitterStrongSignal(getBlockPos(), strong);
            }
            this.weakSignal = weak;
            this.strongSignal = strong;
            sync();
        }
    }

    @Override
    public void setChannel(int channel) {
        if (channel != this.channel) {
            if (this.level instanceof ServerLevel serverLevel)
                ChannelData.getChannelData(serverLevel).setTransmitterChannel(getBlockPos(), channel);
            this.channel = channel;
            sync();
        }
    }

    @Override
    public BlockPos getPos() {
        return this.worldPosition;
    }

    @Override
    public Integer getCellIndex() {
        return null;
    }
}
