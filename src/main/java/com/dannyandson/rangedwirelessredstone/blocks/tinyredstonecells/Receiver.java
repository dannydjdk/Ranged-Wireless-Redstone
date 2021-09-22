package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.rangedwirelessredstone.RenderHelper;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.PanelTileRenderer;
import com.dannyandson.tinyredstone.blocks.Side;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.server.level.ServerLevel;

public class Receiver extends AbstractWirelessCell{
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, float alpha) {
        VertexConsumer builder = buffer.getBuffer((alpha==1.0)? RenderType.solid():RenderType.translucent());
        TextureAtlasSprite sprite = RenderHelper.getSprite(PanelTileRenderer.TEXTURE);

        RenderHelper.drawQuarterSlab(poseStack,builder,sprite,sprite,combinedLight,alpha);
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
        if (cellPos.getPanelTile().getLevel() instanceof ServerLevel serverLevel){
            int signal = ChannelData.getChannelData(serverLevel).getChannelSignal(getChannel(),cellPos.getPanelTile().getBlockPos());
            if (signal!=getSignal()){
                setSignal(signal);
                return true;
            }
        }
        return false;
    }
}
