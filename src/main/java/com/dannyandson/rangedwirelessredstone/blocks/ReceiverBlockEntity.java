package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.rangedwirelessredstone.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class ReceiverBlockEntity extends AbstractWirelessEntity implements ITickableTileEntity {

    public ReceiverBlockEntity() {
        super(Registration.RECEIVER_BLOCK_ENTITY.get());
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
                if (!neighborBlockState.isAir(level,neighborPos))
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

    @Override
    public void tick() {
        if (level instanceof ServerWorld){
            setSignal(ChannelData.getChannelData((ServerWorld)level).getChannelSignal(channel,getBlockPos()));
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
