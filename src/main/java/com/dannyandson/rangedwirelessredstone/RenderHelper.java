package com.dannyandson.rangedwirelessredstone;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class RenderHelper {

    public static TextureAtlasSprite SPRITE_PANEL_LIGHT = RenderHelper.getSprite(new ResourceLocation(RangedWirelessRedstone.MODID,"block/panel_blank"));
    public static TextureAtlasSprite SPRITE_PANEL_DARK = RenderHelper.getSprite(new ResourceLocation(RangedWirelessRedstone.MODID,"block/panel_dark"));
    public static TextureAtlasSprite SPRITE_PANEL_RED = RenderHelper.getSprite(new ResourceLocation(RangedWirelessRedstone.MODID,"block/panel_red"));


    public static void drawQuarterSlab(PoseStack poseStack, VertexConsumer builder, TextureAtlasSprite sprite_top, TextureAtlasSprite sprite_side, int combinedLight, float alpha){
        poseStack.pushPose();

        poseStack.translate(0,0,0.25);

        //draw base top
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.translate(-1,-1,0);
        com.dannyandson.tinyredstone.blocks.RenderHelper.drawRectangle(builder,poseStack,0,1,0,1,sprite_top.getU1(), sprite_top.getU0(), sprite_top.getV0(), sprite_top.getV1(),combinedLight,0xFFFFFFFF,alpha);
        poseStack.popPose();


        //draw back side
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.translate(0,-0.25,0);
        com.dannyandson.tinyredstone.blocks.RenderHelper.drawRectangle(builder,poseStack,0,1,0,0.25f,sprite_side,combinedLight,alpha);

        //right side
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(0,0,1);
        com.dannyandson.tinyredstone.blocks.RenderHelper.drawRectangle(builder,poseStack,0,1,0,0.25f,sprite_side,combinedLight,alpha);

        //front side
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(0,0,1);
        com.dannyandson.tinyredstone.blocks.RenderHelper.drawRectangle(builder,poseStack,0,1,0,0.25f,sprite_side,combinedLight,alpha);

        //left side
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(0,0,1);
        com.dannyandson.tinyredstone.blocks.RenderHelper.drawRectangle(builder,poseStack,0,1,0,0.25f,sprite_side,combinedLight,alpha);

        poseStack.popPose();
    }

    public static void drawRectangle(VertexConsumer  builder, PoseStack poseStack, float x1, float x2, float y1, float y2, TextureAtlasSprite sprite, int combinedLight , float alpha){
        com.dannyandson.tinyredstone.blocks.RenderHelper.drawRectangle(builder,poseStack,x1,x2,y1,y2,sprite,combinedLight,alpha);
    }

    public static TextureAtlasSprite getSprite(ResourceLocation resourceLocation)
    {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(resourceLocation);
    }


}
