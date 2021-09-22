package com.dannyandson.rangedwirelessredstone.gui;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import com.dannyandson.rangedwirelessredstone.network.ModNetworkHandler;
import com.dannyandson.rangedwirelessredstone.network.SetChannel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class ChannelSelectGUI extends Screen {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 70;

    private final IWirelessComponent component;
    private ModWidget channelWidget;

    private final ResourceLocation GUI = new ResourceLocation(RangedWirelessRedstone.MODID, "textures/gui/transparent.png");

    protected ChannelSelectGUI(IWirelessComponent component) {
        super(new TranslatableComponent("rangedwirelessredstone:channelSelectGUI"));
        this.component = component;
    }

    @Override
    protected void init() {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        Integer channel = component.getChannel();


        this.channelWidget = new ModWidget(relX,relY+21,WIDTH,20, Component.nullToEmpty(channel.toString()))
                .setTextHAlignment(ModWidget.HAlignment.CENTER).setTextVAlignment(ModWidget.VAlignment.MIDDLE);

        addRenderableWidget(new ModWidget(relX-1, relY-1, WIDTH+2, HEIGHT+2, 0xAA000000));
        addRenderableWidget(new ModWidget(relX, relY, WIDTH, HEIGHT, 0x88EEEEEE));
        addRenderableWidget(new Button(relX + 35, relY + 48, 80, 20, new TranslatableComponent("rangedwirelessredstone.gui.close"), button -> close()));
        addRenderableWidget(this.channelWidget);

        addRenderableWidget(new ModWidget(relX,relY+3,WIDTH-2,20,new TranslatableComponent("rangedwirelessredstone.gui.channel")))
                .setTextHAlignment(ModWidget.HAlignment.CENTER);
        addRenderableWidget(new Button(relX + 10, relY + 15, 20, 20, Component.nullToEmpty("--"), button -> changeChannel(-10)));
        addRenderableWidget(new Button(relX + 35, relY + 15, 20, 20, Component.nullToEmpty("-"), button -> changeChannel(-1)));

        addRenderableWidget(new Button(relX + 95, relY + 15, 20, 20, Component.nullToEmpty("+"), button -> changeChannel(1)));
        addRenderableWidget(new Button(relX + 125, relY + 15, 20, 20, Component.nullToEmpty("++"), button -> changeChannel(10)));
    }

    private void close() {
        minecraft.setScreen(null);
    }

    private void changeChannel(int change)
    {
        int channel = component.getChannel()+change;
        ModNetworkHandler.sendToServer(new SetChannel(component.getPos(), component.getCellIndex(), channel));
        component.setChannel(channel);

        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;

        this.removeWidget(this.channelWidget);
        this.channelWidget = new ModWidget(relX,relY+21,WIDTH,20, Component.nullToEmpty(channel + ""))
                .setTextHAlignment(ModWidget.HAlignment.CENTER).setTextVAlignment(ModWidget.VAlignment.MIDDLE);
        addRenderableWidget(this.channelWidget);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, GUI);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindForSetup(GUI);
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, WIDTH, HEIGHT);

        super.render(matrixStack,mouseX, mouseY, partialTicks);
    }


    public static void open(IWirelessComponent component) {
        Minecraft.getInstance().setScreen(new ChannelSelectGUI(component));
    }

}
