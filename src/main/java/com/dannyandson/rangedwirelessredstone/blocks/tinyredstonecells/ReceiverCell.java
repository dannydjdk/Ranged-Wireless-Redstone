package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.Side;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ReceiverCell extends AbstractWirelessCell{
    @Override
    public void render(MatrixStack poseStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, float alpha) {
        IVertexBuilder builder = buffer.getBuffer((alpha==1.0)? RenderType.solid():RenderType.translucent());

        //render slab
        RenderHelper.drawQuarterSlab(poseStack,builder,RenderHelper.SPRITE_PANEL_LIGHT,RenderHelper.SPRITE_PANEL_DARK,combinedLight,alpha);

        //render top faces
        poseStack.pushPose();
        poseStack.translate(0,0,.325);
        RenderHelper.drawRectangle(builder,poseStack,.125f,.875f,.5f,.8125f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,.55);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.6875f,.75f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.6875f,.75f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));

        //render back faces
        poseStack.pushPose();
        poseStack.translate(0,0,-.5);
        RenderHelper.drawRectangle(builder,poseStack,.125f,.875f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.1875);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(0,0,.875);
        RenderHelper.drawRectangle(builder,poseStack,.5f,.8125f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.0625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.5625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(-1,0,.8125);
        RenderHelper.drawRectangle(builder,poseStack,.125f,.875f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.0625);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(270));
        poseStack.translate(-1.3125,0,-.125);
        RenderHelper.drawRectangle(builder,poseStack,.5f,.8125f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(-.125,0,-.0625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.5625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,RenderHelper.SPRITE_PANEL_RED,combinedLight,combinedOverlay);
        poseStack.popPose();
    }

    @Override
    public boolean neighborChanged(PanelCellPos panelCellPos) {
        return false;
    }

    @Override
    public boolean isIndependentState() {
        return true;
    }

    @Override
    public int getWeakRsOutput(Side side) {
        return this.getSignal();
    }

    @Override
    public int getStrongRsOutput(Side side) {
        return this.getSignal();
    }

    @Override
    public boolean tick(PanelCellPos cellPos) {
        this.panelCellPos=cellPos;
        World world = cellPos.getPanelTile().getLevel();
        if (cellPos.getPanelTile().getLevel() instanceof ServerWorld){
            int signal = ChannelData.getChannelData((ServerWorld) world).getChannelSignal(getChannel(),cellPos.getPanelTile().getBlockPos());
            if (signal!=getSignal()){
                setSignal(signal);
                return true;
            }
        }
        return false;
    }
}
