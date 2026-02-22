package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = RangedWirelessRedstone.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworkHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(RangedWirelessRedstone.MODID).versioned("1.0");

        registrar.playToServer(SetChannel.TYPE, SetChannel.STREAM_CODEC, SetChannel::handle);
        registrar.playToServer(ServerNetworkTrigger.TYPE, ServerNetworkTrigger.STREAM_CODEC, ServerNetworkTrigger::handle);
        registrar.playToClient(NetworkViewerTrigger.TYPE, NetworkViewerTrigger.STREAM_CODEC, NetworkViewerTrigger::handle);
    }

    public static void sendToServer(Object packet) {
        if (packet instanceof SetChannel setChannel) {
            PacketDistributor.sendToServer(setChannel);
        } else if (packet instanceof ServerNetworkTrigger trigger) {
            PacketDistributor.sendToServer(trigger);
        }
    }

    public static void sendToClient(NetworkViewerTrigger packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}