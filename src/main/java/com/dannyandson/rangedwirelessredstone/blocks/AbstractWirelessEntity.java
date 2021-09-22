package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class AbstractWirelessEntity  extends BlockEntity implements IWirelessComponent {

    protected int signal = 0;
    protected int channel = 0;

    public AbstractWirelessEntity(BlockEntityType<?extends AbstractWirelessEntity> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public int getSignal() {
        return signal;
    }

    @Override
    public int getChannel() {
        return channel;
    }

    protected void sync()
    {
        if (!level.isClientSide)
            this.level.sendBlockUpdated(worldPosition,this.getBlockState(),this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        this.setChanged();
    }


    /**
     * Loading and saving block entity data from disk and syncing to client
     */

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(),-1,this.getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.signal = nbt.getInt("signal");
        this.channel = nbt.getInt("channel");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putInt("signal",this.signal);
        nbt.putInt("channel",this.channel);
        return super.save(nbt);
    }
}
