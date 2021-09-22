package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.rangedwirelessredstone.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class ReceiverBlockEntity extends AbstractWirelessEntity {

    public ReceiverBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registration.RECEIVER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void setSignal(int signal) {
        if (signal != this.signal) {
            this.signal = signal;
            this.sync();
            this.level.updateNeighborsAt(worldPosition,getBlockState().getBlock());
            for(Direction dir : Direction.values()) {
                BlockPos neighborPos = worldPosition.relative(dir);
                BlockState neighborBlockState = level.getBlockState(neighborPos);
                if (!neighborBlockState.isAir())
                    this.level.updateNeighborsAtExceptFromFacing(neighborPos,neighborBlockState.getBlock(),dir.getOpposite());
            }
        }
    }

    @Override
    public void setChannel(int channel) {
        if (channel!=this.channel) {
            this.channel = channel;
            this.sync();
        }
    }

    public void tick() {
        if (level instanceof ServerLevel serverLevel){
            setSignal(ChannelData.getChannelData(serverLevel).getChannelSignal(channel,getBlockPos()));
        }
    }

    @Override
    public BlockPos getPos() {
        return this.worldPosition;
    }
}
