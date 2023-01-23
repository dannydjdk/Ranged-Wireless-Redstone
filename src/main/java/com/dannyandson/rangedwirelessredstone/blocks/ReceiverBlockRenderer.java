package com.dannyandson.rangedwirelessredstone.blocks;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ReceiverBlockRenderer  implements BlockEntityRenderer<ReceiverBlockEntity> {

    public ReceiverBlockRenderer(BlockEntityRendererProvider.Context context){}

    @Override
    public void render(ReceiverBlockEntity receiverBlockEntity, float p_112308_, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        Direction facing = receiverBlockEntity.getBlockState().getValue(BlockStateProperties.FACING);

        boolean signal = (receiverBlockEntity.getStrongSignal() + receiverBlockEntity.getWeakSignal() > 0);
        TextureAtlasSprite sprite = (signal) ? RenderHelper.SPRITE_PANEL_RED : RenderHelper.SPRITE_PANEL_DARKRED;
        VertexConsumer builder = buffer.getBuffer(RenderType.solid());
        if (signal)
            combinedLight = 15728880;

        poseStack.pushPose();
        if (facing == Direction.WEST) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            poseStack.translate(-1, 0, 0);
        } else if (facing == Direction.SOUTH) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            poseStack.translate(-1, 0, -1);
        } else if (facing == Direction.EAST) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
            poseStack.translate(0, 0, -1);
        }
        poseStack.translate(0, 0, .3125);

        RenderHelper.drawRectangle(builder, poseStack, 0.1875f, 0.25f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        RenderHelper.drawRectangle(builder, poseStack, 0.75f, 0.8125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(-.6875, 0, 0.25f);
        RenderHelper.drawRectangle(builder, poseStack, 0.6875f, 0.75f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        poseStack.translate(0, 0, 0.5625f);
        RenderHelper.drawRectangle(builder, poseStack, 0.6875f, 0.75f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(-.1875, 0, .75);
        RenderHelper.drawRectangle(builder, poseStack, 0.1875f, 0.25f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        RenderHelper.drawRectangle(builder, poseStack, 0.75f, 0.8125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.translate(-.25, 0, .25);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);
        poseStack.translate(0, 0, 0.5625);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.75f, 0.875f, sprite, combinedLight, 1.0f);

        poseStack.mulPose(Vector3f.XP.rotationDegrees(-90));
        poseStack.translate(0,-.1875, 0.875);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.75f, 0.8125f, sprite, combinedLight, 1.0f);
        RenderHelper.drawRectangle(builder, poseStack, 0.25f, 0.3125f, 0.1875f, 0.25f, sprite, combinedLight, 1.0f);


        poseStack.popPose();

    }

}
