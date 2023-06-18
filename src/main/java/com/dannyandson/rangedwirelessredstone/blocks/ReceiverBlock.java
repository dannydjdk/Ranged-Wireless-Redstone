package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.gui.ChannelSelectGUI;
import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ReceiverBlock extends BaseEntityBlock {

    private final static VoxelShape shape = Shapes.or(
            Shapes.or(
                Block.box(0,0,0,16,2,16),
                Block.box(3,2,4,4,12,5)
            ),
            Shapes.or(
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
                Properties.of()
                        .sound(SoundType.STONE)
                        .strength(2.0f));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ReceiverBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof ReceiverBlockEntity receiverEntity)
                receiverEntity.tick();
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.getHorizontalDirection());
    }

    /**
     * Called to determine whether to allow the block to handle its own indirect power rather than using the default rules.
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
    @Override
    public boolean shouldCheckWeakPower(BlockState state, SignalGetter world, BlockPos pos, Direction directionFromNeighborToThis) {
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
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        if (blockGetter.getBlockEntity(pos) instanceof IWirelessComponent component) {
            return component.getStrongSignal();
        }
        return super.getDirectSignal(blockState, blockGetter, pos, direction);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        if (blockGetter.getBlockEntity(pos) instanceof IWirelessComponent component) {
            return component.getWeakSignal();
        }
        return super.getSignal(blockState, blockGetter, pos, direction);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @org.jetbrains.annotations.Nullable Direction direction) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(pos) instanceof IWirelessComponent component){
            if (level.isClientSide())
                ChannelSelectGUI.open(component);
            return InteractionResult.CONSUME;
        }
        return super.use(blockState, level, pos, player, hand, blockHitResult);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        VoxelShape thisShape = shapeMap.get(blockState.getValue(BlockStateProperties.FACING));
        if (thisShape!=null)
            return thisShape;
        return shape;
    }

    private static VoxelShape rotateShape(Direction direction) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, Shapes.empty() };

        int times = (direction.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }
}
