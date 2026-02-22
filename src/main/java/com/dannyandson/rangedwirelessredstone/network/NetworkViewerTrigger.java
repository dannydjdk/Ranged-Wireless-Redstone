package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.gui.NetworkViewerGUI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record NetworkViewerTrigger(CompoundTag nbt) implements CustomPacketPayload {

    public static final Type<NetworkViewerTrigger> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RangedWirelessRedstone.MODID, "network_viewer_trigger"));

    public static final StreamCodec<FriendlyByteBuf, NetworkViewerTrigger> STREAM_CODEC =
            StreamCodec.composite(
                    net.minecraft.network.codec.ByteBufCodecs.COMPOUND_TAG, NetworkViewerTrigger::nbt,
                    NetworkViewerTrigger::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(NetworkViewerTrigger pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            NetworkViewerGUI.open(pkt.nbt());
        });
    }
}
