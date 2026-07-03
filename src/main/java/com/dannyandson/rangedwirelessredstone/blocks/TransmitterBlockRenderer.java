package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

public class TransmitterBlockRenderer implements BlockEntityRenderer<TransmitterBlockEntity, TransmitterRenderState> {

    public TransmitterBlockRenderer(BlockEntityRendererProvider.Context context){}

    @Override
    public TransmitterRenderState createRenderState() {
        return new TransmitterRenderState();
    }

    @Override
    public void extractRenderState(TransmitterBlockEntity be, TransmitterRenderState state, float partialTick,
                                   Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPos, crumblingOverlay);
        state.hasSignal = (be.getStrongSignal() + be.getWeakSignal() > 0);
    }

    @Override
    public void submit(TransmitterRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {

        boolean signal = state.hasSignal;
        TextureAtlasSprite sprite = (signal) ? RenderHelper.SPRITE_PANEL_RED : RenderHelper.SPRITE_PANEL_DARKRED;
        int combinedLight = signal ? 15728880 : state.lightCoords;

        collector.submitCustomGeometry(poseStack, RenderHelper.blockCutoutRenderType(), (pose, builder) -> {
            PoseStack ps = new PoseStack();
            ps.last().pose().mul(pose.pose());

            ps.translate(0, 0, .5625);
            RenderHelper.drawRectangle(builder, ps, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

            ps.mulPose(Axis.YP.rotationDegrees(90));
            ps.translate(-.4375, 0, .5625);
            RenderHelper.drawRectangle(builder, ps, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

            ps.mulPose(Axis.YP.rotationDegrees(90));
            ps.translate(-.4375, 0, .5625);
            RenderHelper.drawRectangle(builder, ps, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

            ps.mulPose(Axis.YP.rotationDegrees(90));
            ps.translate(-.4375, 0, .5625);
            RenderHelper.drawRectangle(builder, ps, 0.4375f, 0.5625f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

            ps.mulPose(Axis.XP.rotationDegrees(-90));
            ps.translate(0, -.4375, 0.875);
            RenderHelper.drawRectangle(builder, ps, 0.4375f, 0.5625f, 0.4375f, 0.5625f, sprite, combinedLight, 1.0f);
        });
    }
}