package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class AbstractWirelessEntity  extends BlockEntity implements IWirelessComponent {

    protected int strongSignal = 0;
    protected int weakSignal = 0;
    protected int channel = 0;

    public AbstractWirelessEntity(BlockEntityType<?extends AbstractWirelessEntity> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public int getStrongSignal() {
        return strongSignal;
    }

    @Override
    public int getWeakSignal() {
        return weakSignal;
    }

    @Override
    public int getChannel() {
        return channel;
    }

    protected void sync()
    {
        if (!level.isClientSide)
            this.level.sendBlockUpdated(worldPosition,this.getBlockState(),this.getBlockState(), Block.UPDATE_CLIENTS);
        this.setChanged();
    }


    /**
     * Loading and saving block entity data from disk and syncing to client
     */

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // onDataPacket removed - NeoForge handles sync automatically

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        this.saveAdditional(nbt, registries);
        return nbt;
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.strongSignal = nbt.getInt("signal");
        this.weakSignal = nbt.getInt("weaksignal");
        this.channel = nbt.getInt("channel");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("signal", this.strongSignal);
        nbt.putInt("weaksignal", this.weakSignal);
        nbt.putInt("channel", this.channel);
    }
}
