package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.gui.ChannelSelectGUI;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class TransmitterBlock extends Block {
    private final static VoxelShape shape = VoxelShapes.or(
            VoxelShapes.or(
                    Block.box(0,0,0,16,2,16),
                    Block.box(4,2,4,12,4,12)
            ),
            Block.box(7,2,7,9,14,9)
    );
    public TransmitterBlock() {
        super(
                Properties.of(Material.STONE)
                        .sound(SoundType.STONE)
                        .strength(2.0f));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TransmitterBlockEntity();
    }

    /**
     * Called to determine whether to allow the block to handle its own indirect power rather than using the default rules.
     *
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        //returning false to override default behavior to prevent redstone to allow block entity to determine output
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState blockState, World level, BlockPos pos, BlockState p_60569_, boolean p_60570_) {
        TileEntity te = level.getBlockEntity(pos);
        if (level instanceof ServerWorld && te instanceof TransmitterBlockEntity) {
            ServerWorld serverLevel = (ServerWorld) level;
            TransmitterBlockEntity transmitterEntity = (TransmitterBlockEntity) te;
            ChannelData.getChannelData(serverLevel).setTransmitterChannel(pos, 0);
            ChannelData.getChannelData(serverLevel).setTransmitterStrongSignal(pos,0);
            int direct = level.getDirectSignalTo(pos);
            int indirect = level.getBestNeighborSignal(pos);
            transmitterEntity.setSignals(indirect, direct);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState blockState, World level, BlockPos pos, BlockState p_60518_, boolean p_60519_) {
        if (level instanceof ServerWorld) {
            ChannelData.getChannelData((ServerWorld) level).removeTransmitter(pos);
        }
        super.onRemove(blockState, level, pos, p_60518_, p_60519_);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState blockState, World level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        TileEntity te = level.getBlockEntity(pos);
        if (te instanceof TransmitterBlockEntity) {
            if (level instanceof ServerWorld) {
                int direct = level.getDirectSignalTo(pos);
                int indirect = level.getBestNeighborSignal(pos);
                ((TransmitterBlockEntity) te).setSignals(indirect,direct);
            }
        } else {
            super.neighborChanged(blockState, level, pos, block, neighborPos, isMoving);
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState blockState, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockHitResult) {
        TileEntity te = level.getBlockEntity(pos);
        if (te instanceof IWirelessComponent) {
            if (level.isClientSide())
                ChannelSelectGUI.open((IWirelessComponent) te);
            return ActionResultType.CONSUME;
        }
        return super.use(blockState, level, pos, player, hand, blockHitResult);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return shape;
    }
}
