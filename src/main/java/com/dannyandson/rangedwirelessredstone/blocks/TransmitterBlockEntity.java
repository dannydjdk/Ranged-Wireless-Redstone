package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.rangedwirelessredstone.setup.Registration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class TransmitterBlockEntity extends AbstractWirelessEntity {

    public TransmitterBlockEntity() {
        super(Registration.TRANSMITTER_BLOCK_ENTITY.get());
    }

    @Override
    public void setSignals(int weak,int strong) {
        if (weak != this.weakSignal || strong != this.strongSignal) {
            if (this.level instanceof ServerWorld) {
                ChannelData.getChannelData((ServerWorld) level).setTransmitterWeakSignal(getBlockPos(), weak);
                ChannelData.getChannelData((ServerWorld) level).setTransmitterStrongSignal(getBlockPos(), strong);
            }
            this.weakSignal = weak;
            this.strongSignal = strong;
            sync();
        }
    }

    @Override
    public void setChannel(int channel) {
        if (channel != this.channel) {
            if (this.level instanceof ServerWorld)
                ChannelData.getChannelData((ServerWorld) this.level).setTransmitterChannel(getBlockPos(), channel);
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
