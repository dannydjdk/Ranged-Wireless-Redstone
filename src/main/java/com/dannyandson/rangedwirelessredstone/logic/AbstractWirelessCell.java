package com.dannyandson.rangedwirelessredstone.logic;

import com.dannyandson.tinyredstone.api.IOverlayBlockInfo;
import com.dannyandson.tinyredstone.api.IPanelCell;
import com.dannyandson.tinyredstone.api.IPanelCellInfoProvider;
import com.dannyandson.tinyredstone.blocks.PanelCellVoxelShape;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import com.dannyandson.tinyredstone.blocks.PosInPanelCell;
import com.dannyandson.tinyredstone.blocks.Side;
import net.minecraft.nbt.CompoundTag;

public abstract class AbstractWirelessCell implements IWirelessComponent, IPanelCell, IPanelCellInfoProvider {

    private int signal = 0;

    @Override
    public boolean needsSolidBase() {
        return true;
    }

    @Override
    public boolean canAttachToBaseOnSide(Side side) {
        return side==Side.BOTTOM;
    }

    @Override
    public Side getBaseSide() {
        return Side.BOTTOM;
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("signal",signal);
        return compoundTag;
    }

    @Override
    public void readNBT(CompoundTag compoundTag) {
        signal=compoundTag.getInt("signal");
    }

    @Override
    public PanelCellVoxelShape getShape() {
        return PanelCellVoxelShape.QUARTERCELLSLAB;
    }

    @Override
    public void addInfo(IOverlayBlockInfo overlayBlockInfo, PanelTile panelTile, PosInPanelCell posInPanelCell) {
        overlayBlockInfo.addText("Signal", this.signal + "");
        //overlayBlockInfo.setPowerOutput(0);
    }

    @Override
    public int getSignal() {
        return signal;
    }

    @Override
    public void setSignal(int signal) {
        this.signal = signal;
    }
}
