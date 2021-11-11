package com.dannyandson.rangedwirelessredstone.gui;

import com.dannyandson.rangedwirelessredstone.Config;
import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import com.dannyandson.rangedwirelessredstone.network.ModNetworkHandler;
import com.dannyandson.rangedwirelessredstone.network.ServerNetworkTrigger;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.*;

public class NetworkViewerGUI extends Screen {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 250;
    private static final ResourceLocation GUI = new ResourceLocation(RangedWirelessRedstone.MODID, "textures/gui/transparent.png");
    private int relX = (this.width - WIDTH) / 2;
    private int relY = (this.height - HEIGHT) / 2;

    private final CompoundTag networkNBT;
    private List<String> channelList = new ArrayList<>();
    private List<String> positionList = new ArrayList<>();
    private int scrollIndex = 0;
    private List<ModWidget> listWidgets = new ArrayList<>();

    protected NetworkViewerGUI(CompoundTag networkNBT) {
        super(new TranslatableComponent("rangedwirelessredstone:networkViewerGUI"));
        this.networkNBT=networkNBT;
    }

    @Override
    protected void init() {
        relX = (this.width - WIDTH) / 2;
        relY = (this.height - HEIGHT) / 2;
        addRenderableWidget(new ModWidget(relX - 1, relY - 1, WIDTH + 2, HEIGHT + 2, 0xAA000000));
        addRenderableWidget(new ModWidget(relX, relY, WIDTH, HEIGHT, 0x88EEEEEE));
        addRenderableWidget(new ModWidget(relX +4, relY + 15, WIDTH - 8, 1, 0xAA000000));
        addRenderableWidget(new ModWidget(relX +4, relY + 15, 1, HEIGHT - 46, 0xAA222222));
        addRenderableWidget(new ModWidget(relX +4, relY + 16 + HEIGHT - 48, WIDTH - 8, 1, 0xAAFFFFFF));
        addRenderableWidget(new ModWidget(relX + 5 + WIDTH - 10, relY + 15, 1, HEIGHT - 46, 0xAADDDDDD));
        addRenderableWidget(new ModWidget(relX+5, relY + 16, WIDTH - 10, HEIGHT - 48, 0x66888888));

        addRenderableWidget(new ModWidget(relX + 5, relY + 5, WIDTH - 10, 12, Component.nullToEmpty("Transmitter xyz(i)")));
        addRenderableWidget(new ModWidget(relX + 105, relY + 5, WIDTH - 10, 12, Component.nullToEmpty("Channel")));

        //create a sorted list
        Map<Integer, List<String>> networkList = new HashMap<>();
        for (String key : networkNBT.getAllKeys()) {
            int channel = networkNBT.getInt(key);
            if (!networkList.containsKey(channel))
                networkList.put(channel, new ArrayList<>());
            networkList.get(channel).add(key);
        }

        BlockPos queryBlock = null;
        if (minecraft.hitResult != null && minecraft.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult) minecraft.hitResult;
            queryBlock = blockhitresult.getBlockPos();
        }

        int index = 0;
        for (Integer channel : networkList.keySet().stream().sorted().toList()) {
            for (String posString : networkList.get(channel).stream().sorted().toList()) {

                int[] tPosValues = ChannelData.getXYZiFromPosString(posString);
                int x = tPosValues[0], y = tPosValues[1], z = tPosValues[2];
                boolean isCell = tPosValues.length == 4;
                int range = isCell ? Config.RANGE_CELL.get() : Config.RANGE_BLOCK.get();
                boolean inRange = (
                        queryBlock != null &&
                        Math.abs(x - queryBlock.getX()) <= range &&
                        Math.abs(y - queryBlock.getY()) <= range &&
                        Math.abs(z - queryBlock.getZ()) <= range
                );
                channelList.add(index, channel.toString());
                positionList.add(index, (inRange ? "✔" : "⁃ ") + posString);
                index++;
            }
        }

        setWidgetList();

        addRenderableWidget(new Button(relX + WIDTH - 70, relY + HEIGHT - 25, 50, 20, new TranslatableComponent("rangedwirelessredstone.gui.close"), button -> close()));
        if(this.minecraft.player.hasPermissions(2))
            addRenderableWidget(new Button(relX + 20, relY + HEIGHT - 25, 50, 20, Component.nullToEmpty("Clean Up"), button -> cleanUpNetwork()));
    }

    private void setWidgetList(){
        for(ModWidget modWidget : this.listWidgets)
            removeWidget(modWidget);
        this.listWidgets.clear();

        int wY = relY + 18;
        for (int i=0 ; (i+scrollIndex)<channelList.size() && i<20 ; i++) {
            this.listWidgets.add(new ModWidget(relX + 10, wY, 100, 12, Component.nullToEmpty(positionList.get(i+scrollIndex))));
            this.listWidgets.add(new ModWidget(relX + 105, wY, 40, 12, Component.nullToEmpty(channelList.get(i+scrollIndex))).setTextHAlignment(ModWidget.HAlignment.CENTER));
            wY += 10;
        }
        for(ModWidget modWidget : this.listWidgets) {
            addRenderableWidget(modWidget);
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, GUI);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindForSetup(GUI);
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, WIDTH, HEIGHT);

        super.render(matrixStack,mouseX, mouseY, partialTicks);
    }


    public static void open(CompoundTag networkNBT) {
        Minecraft.getInstance().setScreen(new NetworkViewerGUI(networkNBT));
    }

}
