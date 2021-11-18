package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.tinyredstone.api.IOverlayBlockInfo;
import com.dannyandson.tinyredstone.blocks.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class TransmitterCell extends AbstractWirelessCell {

    boolean flagResetChannel = true;

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, float alpha) {
        VertexConsumer builder = buffer.getBuffer((alpha==1.0)? RenderType.solid():RenderType.translucent());

        //render slab
        RenderHelper.drawQuarterSlab(poseStack,builder,RenderHelper.SPRITE_PANEL_LIGHT,RenderHelper.SPRITE_PANEL_DARK,combinedLight,alpha);

        //render top faces
        poseStack.pushPose();
        poseStack.translate(0,0,.325);
        RenderHelper.drawRectangle(builder,poseStack,.25f,.75f,.25f,.75f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,.55);
        RenderHelper.drawRectangle(builder,poseStack,.4375f,.5625f,.4375f,.5625f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));

        poseStack.pushPose();
        poseStack.translate(0,0,-.25);
        drawSide(poseStack,builder,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(0,0,.75);
        drawSide(poseStack,builder,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(-1,0,.75);
        drawSide(poseStack,builder,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(270));
        poseStack.translate(-1,0,-.25);
        drawSide(poseStack,builder,combinedLight,combinedOverlay);
        poseStack.popPose();
    }
    private void drawSide(PoseStack poseStack,VertexConsumer builder,int combinedLight, int combinedOverlay){
        RenderHelper.drawRectangle(builder,poseStack,.25f,.75f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.1875);
        RenderHelper.drawRectangle(builder,poseStack,.4375f,.5625f,.3125f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.4375f,.5625f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
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
                bottomNeighbor = cellPos.getNeighbor(Side.BOTTOM);

        int sSignal = 0;
        int wSignal = 0;
        if (frontNeighbor != null) {
            sSignal = frontNeighbor.getStrongRsOutput();
            wSignal = frontNeighbor.getWeakRsOutput();
        }
        if (rightNeighbor != null) {
            sSignal = Math.max(sSignal, rightNeighbor.getStrongRsOutput());
            wSignal = Math.max(wSignal, rightNeighbor.getWeakRsOutput());
        }
        if (backNeighbor != null) {
            sSignal = Math.max(sSignal, backNeighbor.getStrongRsOutput());
            wSignal = Math.max(wSignal, backNeighbor.getWeakRsOutput());
        }
        if (leftNeighbor != null) {
            sSignal = Math.max(sSignal, leftNeighbor.getStrongRsOutput());
            wSignal = Math.max(wSignal, leftNeighbor.getWeakRsOutput());
        }
        if (bottomNeighbor != null) {
            sSignal = Math.max(sSignal, bottomNeighbor.getStrongRsOutput());
            wSignal = Math.max(wSignal, bottomNeighbor.getWeakRsOutput());
        }
        if (sSignal != this.getStrongSignal() || wSignal != this.getWeakSignal()) {
            if (cellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel) {
                ChannelData.getChannelData(serverLevel).setTransmitterStrongSignal(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), sSignal);
                ChannelData.getChannelData(serverLevel).setTransmitterWeakSignal(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), wSignal);
            }
            setSignals(wSignal,sSignal);
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

    @Override
    public boolean tick(PanelCellPos cellPos) {
        if (flagResetChannel) {
            this.panelCellPos = cellPos;
            if (panelCellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel) {
                ChannelData.getChannelData(serverLevel).setTransmitterChannel(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), getChannel());
                ChannelData.getChannelData(serverLevel).setTransmitterStrongSignal(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), getStrongSignal());
                ChannelData.getChannelData(serverLevel).setTransmitterWeakSignal(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), getWeakSignal());
            }
            flagResetChannel = false;
        }
        return false;
    }

    @Override
    public void onRemove(PanelCellPos cellPos) {
        if (cellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel)
            ChannelData.getChannelData(serverLevel).removeTransmitter(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex());
    }

    @Override
    public void addInfo(IOverlayBlockInfo overlayBlockInfo, PanelTile panelTile, PosInPanelCell posInPanelCell) {
        overlayBlockInfo.addText("Signal", this.getWeakSignal() + "");
        super.addInfo(overlayBlockInfo, panelTile, posInPanelCell);
    }

}
