package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.tinyredstone.blocks.PanelCellNeighbor;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.Side;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class Transmitter extends AbstractWirelessCell {

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, float v) {

    }

    @Override
    public boolean onPlace(PanelCellPos cellPos, Player player) {
        this.panelCellPos = cellPos;
        if (cellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel)
            ChannelData.getChannelData(serverLevel).setTransmitterChannel(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), getChannel());
        return neighborChanged(cellPos);
    }

    @Override
    public boolean neighborChanged(PanelCellPos cellPos) {
        this.panelCellPos = cellPos;
        PanelCellNeighbor rightNeighbor = cellPos.getNeighbor(Side.RIGHT),
                leftNeighbor = cellPos.getNeighbor(Side.LEFT),
                backNeighbor = cellPos.getNeighbor(Side.BACK),
                frontNeighbor = cellPos.getNeighbor(Side.FRONT),
                topNeighbor = cellPos.getNeighbor(Side.TOP),
                bottomNeighbor = cellPos.getNeighbor(Side.BOTTOM);

        int signal = 0;
        if (frontNeighbor != null) {
            signal = frontNeighbor.getStrongRsOutput();
        }
        if (rightNeighbor != null) {
            signal = Math.max(signal, rightNeighbor.getStrongRsOutput());
        }
        if (backNeighbor != null) {
            signal = Math.max(signal, backNeighbor.getStrongRsOutput());
        }
        if (leftNeighbor != null) {
            signal = Math.max(signal, leftNeighbor.getStrongRsOutput());
        }
        if (bottomNeighbor != null) {
            signal = Math.max(signal, bottomNeighbor.getStrongRsOutput());
        }
        if (signal != this.getSignal()) {
            if (cellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel)
                ChannelData.getChannelData(serverLevel).setTransmitterSignal(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), signal);
            setSignal(signal);
        }

        return false;
    }

    @Override
    public int getWeakRsOutput(Side side) {
        return 0;
    }

    @Override
    public int getStrongRsOutput(Side side) {
        return 0;
    }

    @Override
    public void setChannel(int channel) {
        if (panelCellPos!=null){
            if (channel != this.getChannel()) {
                if (panelCellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel)
                    ChannelData.getChannelData(serverLevel).setTransmitterChannel(panelCellPos.getPanelTile().getBlockPos(), panelCellPos.getIndex(), channel);
                super.setChannel(channel);
            }
        }
    }
}
