package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.tinyredstone.blocks.PanelCellNeighbor;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.Side;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TransmitterCell extends AbstractWirelessCell {

    @Override
    public void render(MatrixStack poseStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, float alpha) {
        IVertexBuilder builder = buffer.getBuffer((alpha == 1.0) ? RenderType.solid() : RenderType.translucent());

        //render slab
        RenderHelper.drawQuarterSlab(poseStack, builder, RenderHelper.SPRITE_PANEL_LIGHT, RenderHelper.SPRITE_PANEL_DARK, combinedLight, alpha);

        //render top faces
        poseStack.pushPose();
        poseStack.translate(0, 0, .325);
        RenderHelper.drawRectangle(builder, poseStack, .25f, .75f, .25f, .75f, RenderHelper.SPRITE_PANEL_DARK, combinedLight, combinedOverlay);
        poseStack.translate(0, 0, .55);
        RenderHelper.drawRectangle(builder, poseStack, .4375f, .5625f, .4375f, .5625f, RenderHelper.SPRITE_PANEL_RED, combinedLight, combinedOverlay);
        poseStack.popPose();

        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));

        poseStack.pushPose();
        poseStack.translate(0, 0, -.25);
        drawSide(poseStack, builder, combinedLight, combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(0, 0, .75);
        drawSide(poseStack, builder, combinedLight, combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(-1, 0, .75);
        drawSide(poseStack, builder, combinedLight, combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(270));
        poseStack.translate(-1, 0, -.25);
        drawSide(poseStack, builder, combinedLight, combinedOverlay);
        poseStack.popPose();
    }

    private void drawSide(MatrixStack poseStack, IVertexBuilder builder, int combinedLight, int combinedOverlay) {
        RenderHelper.drawRectangle(builder, poseStack, .25f, .75f, .25f, .325f, RenderHelper.SPRITE_PANEL_DARK, combinedLight, combinedOverlay);
        poseStack.translate(0, 0, -.1875);
        RenderHelper.drawRectangle(builder, poseStack, .4375f, .5625f, .3125f, .75f, RenderHelper.SPRITE_PANEL_LIGHT, combinedLight, combinedOverlay);
        RenderHelper.drawRectangle(builder, poseStack, .4375f, .5625f, .75f, .875f, RenderHelper.SPRITE_PANEL_RED, combinedLight, combinedOverlay);
    }

    @Override
    public boolean onPlace(PanelCellPos cellPos, PlayerEntity player) {
        this.panelCellPos = cellPos;
        World world = cellPos.getPanelTile().getLevel();
        if (world instanceof ServerWorld)
            ChannelData.getChannelData((ServerWorld) world).setTransmitterChannel(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), getChannel());
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
            World world = cellPos.getPanelTile().getLevel();
            if (world instanceof ServerWorld)
                ChannelData.getChannelData((ServerWorld) world).setTransmitterSignal(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex(), signal);
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
        if (panelCellPos != null) {
            if (channel != this.getChannel()) {
                World world = panelCellPos.getPanelTile().getLevel();
                if (world instanceof ServerWorld)
                    ChannelData.getChannelData((ServerWorld) world).setTransmitterChannel(panelCellPos.getPanelTile().getBlockPos(), panelCellPos.getIndex(), channel);
                super.setChannel(channel);
            }
        }
    }

    @Override
    public void onRemove(PanelCellPos cellPos) {
        World world = cellPos.getPanelTile().getLevel();
        if (world instanceof ServerWorld)
            ChannelData.getChannelData((ServerWorld) world).removeTransmitter(cellPos.getPanelTile().getBlockPos(), cellPos.getIndex());
    }
}
