package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TransmitterBlockRenderer implements BlockEntityRenderer<TransmitterBlockEntity> {

    public TransmitterBlockRenderer(BlockEntityRendererProvider.Context context){}

    @Override
    public void render(TransmitterBlockEntity transmitterBlockEntity, float p_112308_, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        boolean signal = (transmitterBlockEntity.getStrongSignal() + transmitterBlockEntity.getWeakSignal() > 0);
        TextureAtlasSprite sprite = (signal) ? RenderHelper.SPRITE_PANEL_RED : RenderHelper.SPRITE_PANEL_DARKRED;
        VertexConsumer builder = buffer.getBuffer(RenderType.solid());
        if (signal)
            combinedLight = 15728880;

        poseStack.pushPose();

        poseStack.translate(0, 0, .5625);
        RenderHelper.drawRectangle(builder, poseStack, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-.4375, 0, .5625);
        RenderHelper.drawRectangle(builder, poseStack, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-.4375, 0, .5625);
        RenderHelper.drawRectangle(builder, poseStack, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-.4375, 0, .5625);
        RenderHelper.drawRectangle(builder, poseStack, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);


        poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        poseStack.translate(0,-.4375,0.875);
        RenderHelper.drawRectangle(builder, poseStack, 0.4375f, 0.5625f,0.4375f, 0.5625f, sprite, combinedLight, 1.0f);


        poseStack.popPose();

    }

}
