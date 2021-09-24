package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.gui.ChannelSelectGUI;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;

public class TransmitterBlock extends BaseEntityBlock {

    public TransmitterBlock() {
        super(
                Properties.of(Material.STONE)
                        .sound(SoundType.STONE)
                        .strength(2.0f));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TransmitterBlockEntity(blockPos, blockState);
    }

    /**
     * Called to determine whether to allow the block to handle its own indirect power rather than using the default rules.
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
    @Override
    public boolean shouldCheckWeakPower(BlockState state, LevelReader world, BlockPos pos, Direction directionFromNeighborToThis) {
        //returning false to override default behavior to prevent redstone to output locally
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState p_60569_, boolean p_60570_) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof TransmitterBlockEntity transmitterEntity) {
            ChannelData.getChannelData(serverLevel).setTransmitterChannel(pos, 0);
            ChannelData.getChannelData(serverLevel).setTransmitterSignal(pos,0);
            int signal = level.getDirectSignalTo(pos);
            transmitterEntity.setSignal(signal);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        if (level instanceof ServerLevel serverLevel){
            ChannelData.getChannelData(serverLevel).removeTransmitter(pos);
        }
            super.onRemove(blockState, level, pos, p_60518_, p_60519_);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (level.getBlockEntity(pos) instanceof TransmitterBlockEntity transmitterEntity){
            if (level instanceof ServerLevel serverLevel) {
                int signal = level.getDirectSignalTo(pos);
                transmitterEntity.setSignal(signal);
            }
        }else{
            super.neighborChanged(blockState,level,pos,block,neighborPos,isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(pos) instanceof TransmitterBlockEntity transmitterEntity){
            if (level.isClientSide())
                ChannelSelectGUI.open(transmitterEntity);
            return InteractionResult.CONSUME;
        }
        return super.use(blockState, level, pos, player, hand, blockHitResult);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(0,0,0,16,2,16);
    }
}
