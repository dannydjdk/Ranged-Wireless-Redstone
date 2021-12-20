package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import com.dannyandson.tinyredstone.network.PanelCellSync;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
    public static void sendToClient(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToClient(Object packet, PanelTile panelTile) {
        BlockPos pos = panelTile.getBlockPos();
        for (Player player : panelTile.getLevel().players()) {
            if (player instanceof ServerPlayer && player.distanceToSqr(pos.getX(),pos.getY(),pos.getZ()) < 64d) {
                INSTANCE.sendTo(packet, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }
}