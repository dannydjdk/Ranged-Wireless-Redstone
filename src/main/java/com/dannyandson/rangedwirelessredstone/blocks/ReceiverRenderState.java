package com.dannyandson.rangedwirelessredstone.blocks;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class ReceiverRenderState extends BlockEntityRenderState {
    public boolean hasSignal = false;
    public Direction facing = Direction.NORTH;
}
