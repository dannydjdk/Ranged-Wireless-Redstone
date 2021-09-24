package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class AbstractWirelessEntity  extends TileEntity implements IWirelessComponent {

    protected int signal = 0;
    protected int channel = 0;

    public AbstractWirelessEntity(TileEntityType<?extends AbstractWirelessEntity> type) {
        super(type);
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(),-1,this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        this.signal = nbt.getInt("signal");
        this.channel = nbt.getInt("channel");
    }


    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);
        this.signal = nbt.getInt("signal");
        this.channel = nbt.getInt("channel");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("signal",this.signal);
        nbt.putInt("channel",this.channel);
        return super.save(nbt);
    }
}
