package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.Side;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;

public class ReceiverCell extends AbstractWirelessCell{
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, float alpha) {
        VertexConsumer builder = buffer.getBuffer((alpha==1.0)? RenderType.solid():RenderType.translucent());
        boolean hasSignal = this.getStrongSignal()+this.getWeakSignal()>0;
        TextureAtlasSprite redsprite = (hasSignal)?RenderHelper.SPRITE_PANEL_RED:RenderHelper.SPRITE_PANEL_DARKRED;
        int redCombinedLight = (hasSignal)?15728880:combinedLight;

        //render slab
        RenderHelper.drawQuarterSlab(poseStack,builder,RenderHelper.SPRITE_PANEL_LIGHT,RenderHelper.SPRITE_PANEL_DARK,combinedLight,alpha);

        //render top faces
        poseStack.pushPose();
        poseStack.translate(0,0,.325);
        RenderHelper.drawRectangle(builder,poseStack,.125f,.875f,.5f,.8125f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,.55);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.6875f,.75f,redsprite,redCombinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.6875f,.75f,redsprite,redCombinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));

        //render back faces
        poseStack.pushPose();
        poseStack.translate(0,0,-.5);
        RenderHelper.drawRectangle(builder,poseStack,.125f,.875f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.1875);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(0,0,.875);
        RenderHelper.drawRectangle(builder,poseStack,.5f,.8125f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.0625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
        poseStack.translate(0,0,-.5625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(-1,0,.8125);
        RenderHelper.drawRectangle(builder,poseStack,.125f,.875f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(0,0,-.0625);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.1875f,.25f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.75f,.8125f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(270));
        poseStack.translate(-1.3125,0,-.125);
        RenderHelper.drawRectangle(builder,poseStack,.5f,.8125f,.25f,.325f,RenderHelper.SPRITE_PANEL_DARK,combinedLight,combinedOverlay);
        poseStack.translate(-.125,0,-.0625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
        poseStack.translate(0,0,-.5625);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.325f,.75f,RenderHelper.SPRITE_PANEL_LIGHT,combinedLight,combinedOverlay);
        RenderHelper.drawRectangle(builder,poseStack,.6875f,.75f,.75f,.875f,redsprite,redCombinedLight,combinedOverlay);
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
        return this.getWeakSignal();
    }

    @Override
    public int getStrongRsOutput(Side side) {
        return this.getStrongSignal();
    }

    @Override
    public boolean tick(PanelCellPos cellPos) {
        this.panelCellPos=cellPos;
        if (cellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel){
            Map<String,Integer> signals = ChannelData.getChannelData(serverLevel).getChannelSignal(getChannel(),cellPos.getPanelTile().getBlockPos());
            if (signals.get("strong")!= getStrongSignal() || signals.get("weak")!= getWeakSignal()){
                setSignals(signals.get("weak"),signals.get("strong"));
                return true;
            }
        }
        return false;
    }
}
