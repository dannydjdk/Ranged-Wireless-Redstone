package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.rangedwirelessredstone.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class ReceiverBlockEntity extends AbstractWirelessEntity {

    public ReceiverBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(Registration.RECEIVER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void setSignals(int weak,int strong) {
        if (weak!=this.weakSignal || strong != this.strongSignal) {
            this.weakSignal = weak;
            this.strongSignal = strong;
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
            Map<String,Integer> signals = ChannelData.getChannelData(serverLevel).getChannelSignal(channel,getBlockPos());
            setSignals(signals.get("weak"),signals.get("strong"));
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
