package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

public class ReceiverBlockRenderer implements BlockEntityRenderer<ReceiverBlockEntity, ReceiverRenderState> {

    public ReceiverBlockRenderer(BlockEntityRendererProvider.Context context){}

    @Override
    public ReceiverRenderState createRenderState() {
        return new ReceiverRenderState();
    }

    @Override
    public void extractRenderState(ReceiverBlockEntity be, ReceiverRenderState state, float partialTick,
                                   Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPos, crumblingOverlay);
        state.hasSignal = (be.getStrongSignal() + be.getWeakSignal() > 0);
        state.facing = be.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public void submit(ReceiverRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {

        Direction facing = state.facing;
        boolean signal = state.hasSignal;
        TextureAtlasSprite sprite = (signal) ? RenderHelper.SPRITE_PANEL_RED : RenderHelper.SPRITE_PANEL_DARKRED;
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = bufferSource.getBuffer(Sheets.cutoutBlockSheet());
        int combinedLight = signal ? 15728880 : state.lightCoords;

        poseStack.pushPose();
        if (facing == Direction.WEST) {
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.translate(-1, 0, 0);
        } else if (facing == Direction.SOUTH) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.translate(-1, 0, -1);
        } else if (facing == Direction.EAST) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            poseStack.translate(0, 0, -1);
        }
        poseStack.translate(0, 0, .3125);

        RenderHelper.drawRectangle(builder, poseStack, 0.1875f, 0.25f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        RenderHelper.drawRectangle(builder, poseStack, 0.75f, 0.8125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-.6875, 0, 0.25f);
        RenderHelper.drawRectangle(builder, poseStack, 0.6875f, 0.75f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        poseStack.translate(0, 0, 0.5625f);
        RenderHelper.drawRectangle(builder, poseStack, 0.6875f, 0.75f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-.1875, 0, .75);
        RenderHelper.drawRectangle(builder, poseStack, 0.1875f, 0.25f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        RenderHelper.drawRectangle(builder, poseStack, 0.75f, 0.8125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(-.25, 0, .25);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        poseStack.translate(0, 0, 0.5625);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        poseStack.translate(0,-.1875, 0.875);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.75f, 0.8125f, sprite, combinedLight, 1.0f);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.1875f, 0.25f, sprite, combinedLight, 1.0f);

        poseStack.popPose();
    }
}
