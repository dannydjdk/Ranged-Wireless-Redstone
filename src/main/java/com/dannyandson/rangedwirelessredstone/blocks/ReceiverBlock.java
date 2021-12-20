package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.gui.ChannelSelectGUI;
import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ReceiverBlock extends Block {

    private final static VoxelShape shape = VoxelShapes.or(
            VoxelShapes.or(
                    Block.box(0,0,0,16,2,16),
                    Block.box(3,2,4,4,12,5)
            ),
            VoxelShapes.or(
                    Block.box(12,4,4,13,14,5),
                    Block.box(3,4,4,4,14,5)
            )
    );
    private final static Map<Direction,VoxelShape> shapeMap = new HashMap<>();
    static {
        shapeMap.put(Direction.NORTH,shape);
        shapeMap.put(Direction.EAST,rotateShape(Direction.EAST));
        shapeMap.put(Direction.SOUTH,rotateShape(Direction.SOUTH));
        shapeMap.put(Direction.WEST,rotateShape(Direction.WEST));
    }
    public ReceiverBlock() {
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
         return new ReceiverBlockEntity();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
         builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getHorizontalDirection());
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        //returning false to override default behavior to prevent redstone to allow block entity to determine output
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSignalSource(BlockState p_60571_) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getDirectSignal(BlockState blockState, IBlockReader blockGetter, BlockPos pos, Direction direction) {
        TileEntity te = blockGetter.getBlockEntity(pos);
        if (te instanceof IWirelessComponent) {
            return ((IWirelessComponent)te).getStrongSignal();
        }
        return super.getDirectSignal(blockState, blockGetter, pos, direction);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getSignal(BlockState blockState, IBlockReader blockGetter, BlockPos pos, Direction direction) {
        TileEntity tileEntity = blockGetter.getBlockEntity(pos);
        if (tileEntity instanceof IWirelessComponent) {
            IWirelessComponent component = (IWirelessComponent) tileEntity;
            return component.getWeakSignal();
        }
        return super.getSignal(blockState, blockGetter, pos, direction);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState blockState, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockHitResult) {
        TileEntity te = level.getBlockEntity(pos);
        if (level.getBlockEntity(pos) instanceof IWirelessComponent){
            if (level.isClientSide())
                ChannelSelectGUI.open((IWirelessComponent) te);
            return ActionResultType.CONSUME;
        }
        return super.use(blockState, level, pos, player, hand, blockHitResult);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        VoxelShape thisShape = shapeMap.get(blockState.getValue(BlockStateProperties.FACING));
        if (thisShape!=null)
            return thisShape;
        return shape;
    }

    private static VoxelShape rotateShape(Direction direction) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };

        int times = (direction.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.create(new AxisAlignedBB(1-maxZ, minY, minX, 1-minZ, maxY, maxX))));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }
}
