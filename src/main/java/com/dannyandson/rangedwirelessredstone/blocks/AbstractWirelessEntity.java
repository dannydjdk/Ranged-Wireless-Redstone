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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

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
        if (!level.isClientSide())
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

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.strongSignal = input.getIntOr("signal", 0);
        this.weakSignal = input.getIntOr("weaksignal", 0);
        this.channel = input.getIntOr("channel", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("signal", this.strongSignal);
        output.putInt("weaksignal", this.weakSignal);
        output.putInt("channel", this.channel);
    }
}
