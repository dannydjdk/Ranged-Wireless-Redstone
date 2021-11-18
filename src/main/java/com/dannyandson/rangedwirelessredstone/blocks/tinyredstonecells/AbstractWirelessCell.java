package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.rangedwirelessredstone.gui.ChannelSelectGUI;
import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import com.dannyandson.rangedwirelessredstone.network.ModNetworkHandler;
import com.dannyandson.tinyredstone.api.IOverlayBlockInfo;
import com.dannyandson.tinyredstone.api.IPanelCell;
import com.dannyandson.tinyredstone.api.IPanelCellInfoProvider;
import com.dannyandson.tinyredstone.blocks.*;
import com.dannyandson.tinyredstone.network.PanelCellSync;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractWirelessCell implements IWirelessComponent, IPanelCell, IPanelCellInfoProvider {

    private int strongSignal = 0;
    private int weakSignal = 0;
    private int channel = 0;
    protected PanelCellPos panelCellPos = null;

    @Override
    public boolean needsSolidBase() {
        return true;
    }

    @Override
    public CompoundNBT writeNBT() {
        CompoundNBT compoundTag = new CompoundNBT();
        compoundTag.putInt("signal", strongSignal);
        compoundTag.putInt("weaksignal", weakSignal);
        compoundTag.putInt("channel",channel);
        return compoundTag;
    }

    @Override
    public void readNBT(CompoundNBT compoundTag) {
        strongSignal =compoundTag.getInt("signal");
        weakSignal =compoundTag.getInt("weaksignal");
        channel=compoundTag.getInt("channel");
    }

    @Override
    public PanelCellVoxelShape getShape() {
        return PanelCellVoxelShape.QUARTERCELLSLAB;
    }

    @Override
    public void addInfo(IOverlayBlockInfo overlayBlockInfo, PanelTile panelTile, PosInPanelCell posInPanelCell) {
        overlayBlockInfo.addText("Channel", this.channel + "");
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
    public void setSignals(int weak, int strong) {
        this.weakSignal = weak;
        this.strongSignal = strong;
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
        if (panelCellPos!=null && !panelCellPos.getPanelTile().getLevel().isClientSide)
            ModNetworkHandler.sendToClient(new PanelCellSync(panelCellPos.getPanelTile().getBlockPos(), panelCellPos.getIndex(), writeNBT()), panelCellPos.getPanelTile());
    }

    @Override
    public boolean onBlockActivated(PanelCellPos cellPos, PanelCellSegment segmentClicked, PlayerEntity player) {
        this.panelCellPos = cellPos;
        if (cellPos.getPanelTile().getLevel().isClientSide())
            ChannelSelectGUI.open(this);
        return IPanelCell.super.onBlockActivated(cellPos, segmentClicked, player);
    }

    @Override
    public boolean hasActivation() {
        return true;
    }

    @Override
    public BlockPos getPos() {
        if (panelCellPos!=null) return panelCellPos.getPanelTile().getBlockPos();
        return null;
    }

    @Override
    public Integer getCellIndex() {
        if (panelCellPos!=null) return panelCellPos.getIndex();
        return null;
    }

}