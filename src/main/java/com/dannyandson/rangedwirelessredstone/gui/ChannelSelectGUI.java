package com.dannyandson.rangedwirelessredstone.gui;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.logic.IWirelessComponent;
import com.dannyandson.rangedwirelessredstone.network.ModNetworkHandler;
import com.dannyandson.rangedwirelessredstone.network.SetChannel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ChannelSelectGUI extends Screen {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 70;

    private final IWirelessComponent component;
    private ModWidget channelWidget;

    private final ResourceLocation GUI = new ResourceLocation(RangedWirelessRedstone.MODID, "textures/gui/transparent.png");

    protected ChannelSelectGUI(IWirelessComponent component) {
        super(new TranslationTextComponent("rangedwirelessredstone:channelSelectGUI"));
        this.component = component;
    }

    @Override
    protected void init() {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        Integer channel = component.getChannel();


        this.channelWidget = new ModWidget(relX,relY+21,WIDTH,20, ITextComponent.nullToEmpty(channel.toString()))
                .setTextHAlignment(ModWidget.HAlignment.CENTER).setTextVAlignment(ModWidget.VAlignment.MIDDLE);

        addButton(new ModWidget(relX-1, relY-1, WIDTH+2, HEIGHT+2, 0xAA000000));
        addButton(new ModWidget(relX, relY, WIDTH, HEIGHT, 0x88EEEEEE));
        addButton(new Button(relX + 35, relY + 48, 80, 20, new TranslationTextComponent("rangedwirelessredstone.gui.close"), button -> close()));
        addButton(this.channelWidget);

        addButton(new ModWidget(relX,relY+3,WIDTH-2,20,new TranslationTextComponent("rangedwirelessredstone.gui.channel")))
                .setTextHAlignment(ModWidget.HAlignment.CENTER);
        addButton(new Button(relX + 10, relY + 15, 20, 20, ITextComponent.nullToEmpty("--"), button -> changeChannel(-10)));
        addButton(new Button(relX + 35, relY + 15, 20, 20, ITextComponent.nullToEmpty("-"), button -> changeChannel(-1)));

        addButton(new Button(relX + 95, relY + 15, 20, 20, ITextComponent.nullToEmpty("+"), button -> changeChannel(1)));
        addButton(new Button(relX + 125, relY + 15, 20, 20, ITextComponent.nullToEmpty("++"), button -> changeChannel(10)));
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

        this.buttons.remove(this.channelWidget);
        this.channelWidget = new ModWidget(relX,relY+21,WIDTH,20, ITextComponent.nullToEmpty(channel + ""))
                .setTextHAlignment(ModWidget.HAlignment.CENTER).setTextVAlignment(ModWidget.VAlignment.MIDDLE);
        addButton(this.channelWidget);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, WIDTH, HEIGHT);

        super.render(matrixStack,mouseX, mouseY, partialTicks);
    }


    public static void open(IWirelessComponent component) {
        Minecraft.getInstance().setScreen(new ChannelSelectGUI(component));
    }

}
