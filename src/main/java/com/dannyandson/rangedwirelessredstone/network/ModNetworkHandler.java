package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import com.dannyandson.tinyredstone.network.PanelCellSync;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworkHandler {
    private static SimpleChannel INSTANCE;
    private static int ID = 0;
    private static final String PROTOCOL_VERSION = "1.2";

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(RangedWirelessRedstone.MODID, "rangedwirelessredstone"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);

        INSTANCE.messageBuilder(SetChannel.class,nextID())
                .encoder(SetChannel::toBytes)
                .decoder(SetChannel::new)
                .consumer(SetChannel::handle)
                .add();

        INSTANCE.messageBuilder(ServerNetworkTrigger.class,nextID())
                .encoder(ServerNetworkTrigger::toBytes)
                .decoder(ServerNetworkTrigger::new)
                .consumer(ServerNetworkTrigger::handle)
                .add();

        INSTANCE.messageBuilder(NetworkViewerTrigger.class,nextID())
                .encoder(NetworkViewerTrigger::toBytes)
                .decoder(NetworkViewerTrigger::new)
                .consumer(NetworkViewerTrigger::handle)
                .add();

        if (ModList.get().isLoaded("tinyredstone")) {
            INSTANCE.messageBuilder(PanelCellSync.class, nextID())
                    .encoder(PanelCellSync::toBytes)
                    .decoder(PanelCellSync::new)
                    .consumer(PanelCellSync::handle)
                    .add();
        }

    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToClient(Object packet, PanelTile panelTile) {
        BlockPos pos = panelTile.getBlockPos();
        for (PlayerEntity player : panelTile.getLevel().players()) {
            if (player instanceof ServerPlayerEntity && player.distanceToSqr(pos.getX(),pos.getY(),pos.getZ()) < 64d) {
                INSTANCE.sendTo(packet, ((ServerPlayerEntity) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }

}