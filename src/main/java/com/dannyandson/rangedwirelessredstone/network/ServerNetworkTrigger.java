package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerNetworkTrigger(int triggerOrdinal) implements CustomPacketPayload {

    public static final Type<ServerNetworkTrigger> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RangedWirelessRedstone.MODID, "server_network_trigger"));

    public static final StreamCodec<ByteBuf, ServerNetworkTrigger> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ServerNetworkTrigger::triggerOrdinal,
            ServerNetworkTrigger::new
    );

    public ServerNetworkTrigger(ServerTrigger trigger) {
        this(trigger.ordinal());
    }

    public ServerTrigger trigger() {
        return ServerTrigger.values()[triggerOrdinal];
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ServerNetworkTrigger pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer) {
                ServerTrigger trigger = pkt.trigger();
                if (trigger == ServerTrigger.CLEANUP) {
                    ServerLevel serverLevel = (ServerLevel) serverPlayer.level();
                    ChannelData.getChannelData(serverLevel).cleanupTransmitters(serverLevel);
                    CompoundTag nbt = ChannelData.getChannelData(serverLevel).getChannelNBT();
                    PacketDistributor.sendToPlayer(serverPlayer, new NetworkViewerTrigger(nbt));
                } else if (trigger == ServerTrigger.NETWORK_VIEWER) {
                    CompoundTag nbt = ChannelData.getChannelData((ServerLevel) serverPlayer.level()).getChannelNBT();
                    PacketDistributor.sendToPlayer(serverPlayer, new NetworkViewerTrigger(nbt));
                }
            }
        });
    }

    public enum ServerTrigger {
        CLEANUP, NETWORK_VIEWER
    }
}
