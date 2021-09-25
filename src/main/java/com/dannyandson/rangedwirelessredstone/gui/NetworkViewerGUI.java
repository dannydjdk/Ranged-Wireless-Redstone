package com.dannyandson.rangedwirelessredstone.gui;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.network.ModNetworkHandler;
import com.dannyandson.rangedwirelessredstone.network.ServerNetworkTrigger;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkViewerGUI extends Screen {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 250;
    private static final ResourceLocation GUI = new ResourceLocation(RangedWirelessRedstone.MODID, "textures/gui/transparent.png");
    private int relX = (this.width - WIDTH) / 2;
    private int relY = (this.height - HEIGHT) / 2;

    private final CompoundNBT networkNBT;
    private List<String> channelList = new ArrayList<>();
    private List<String> positionList = new ArrayList<>();
    private int scrollIndex = 0;
    private List<ModWidget> listWidgets = new ArrayList<>();

    protected NetworkViewerGUI(CompoundNBT networkNBT) {
        super(new TranslationTextComponent("rangedwirelessredstone:networkViewerGUI"));
        this.networkNBT=networkNBT;
    }

    @Override
    protected void init() {
        relX = (this.width - WIDTH) / 2;
        relY = (this.height - HEIGHT) / 2;
        addButton(new ModWidget(relX - 1, relY - 1, WIDTH + 2, HEIGHT + 2, 0xAA000000));
        addButton(new ModWidget(relX, relY, WIDTH, HEIGHT, 0x88EEEEEE));
        addButton(new ModWidget(relX + 4, relY + 15, WIDTH - 8, 1, 0xAA000000));
        addButton(new ModWidget(relX + 4, relY + 15, 1, HEIGHT - 46, 0xAA222222));
        addButton(new ModWidget(relX + 4, relY + 16 + HEIGHT - 48, WIDTH - 8, 1, 0xAAFFFFFF));
        addButton(new ModWidget(relX + 5 + WIDTH - 10, relY + 15, 1, HEIGHT - 46, 0xAADDDDDD));
        addButton(new ModWidget(relX + 5, relY + 16, WIDTH - 10, HEIGHT - 48, 0x66888888));

        addButton(new ModWidget(relX + 5, relY + 5, WIDTH - 10, 12, ITextComponent.nullToEmpty("Transmitter xyz(i)")));
        addButton(new ModWidget(relX + 105, relY + 5, WIDTH - 10, 12, ITextComponent.nullToEmpty("Channel")));

        //create a sorted list
        Map<Integer, List<String>> networkList = new HashMap<>();
        for (String key : networkNBT.getAllKeys()) {
            int channel = networkNBT.getInt(key);
            if (!networkList.containsKey(channel))
                networkList.put(channel, new ArrayList<>());
            networkList.get(channel).add(key);
        }
        int index = 0;
        for (Object obj : networkList.keySet().stream().sorted().toArray()) {
            Integer channel = (Integer) obj;
            for (Object posObj : networkList.get(channel).stream().sorted().toArray()) {
                String posString = (String) posObj;
                channelList.add(index, channel.toString());
                positionList.add(index, posString);
                index++;
            }
        }

        setWidgetList();

        addButton(new Button(relX + WIDTH - 70, relY + HEIGHT - 25, 50, 20, new TranslationTextComponent("rangedwirelessredstone.gui.close"), button -> close()));
        if (this.minecraft.player.hasPermissions(2))
            addButton(new Button(relX + 20, relY + HEIGHT - 25, 50, 20, ITextComponent.nullToEmpty("Clean Up"), button -> cleanUpNetwork()));
    }

    private void setWidgetList(){
        for(ModWidget modWidget : this.listWidgets)
            this.buttons.remove(modWidget);
        this.listWidgets.clear();

        int wY = relY + 18;
        for (int i=0 ; (i+scrollIndex)<channelList.size() && i<20 ; i++) {
            this.listWidgets.add(new ModWidget(relX + 10, wY, 100, 12, ITextComponent.nullToEmpty(positionList.get(i+scrollIndex))));
            this.listWidgets.add(new ModWidget(relX + 105, wY, 40, 12, ITextComponent.nullToEmpty(channelList.get(i+scrollIndex))).setTextHAlignment(ModWidget.HAlignment.CENTER));
            wY += 10;
        }
        for(ModWidget modWidget : this.listWidgets) {
            addButton(modWidget);
        }
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        if (p_94688_>0) {
            if (scrollIndex > 0) {
                scrollIndex--;
                setWidgetList();
                return true;
            }
        }else{
            if (scrollIndex<(channelList.size()-20)){
                scrollIndex++;
                setWidgetList();
                return true;
            }
        }
        return false;
    }

    private void close() {
        minecraft.setScreen(null);
    }

    private void cleanUpNetwork()
    {
        ModNetworkHandler.sendToServer(new ServerNetworkTrigger(ServerNetworkTrigger.ServerTrigger.CLEANUP));
        close();
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


    public static void open(CompoundNBT networkNBT) {
        Minecraft.getInstance().setScreen(new NetworkViewerGUI(networkNBT));
    }

}
