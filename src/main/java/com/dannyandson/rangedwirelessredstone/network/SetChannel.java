package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.AbstractWirelessCell;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetChannel(BlockPos pos, int cellIndex, int channel, boolean hasCellIndex) implements CustomPacketPayload {

    public static final Type<SetChannel> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RangedWirelessRedstone.MODID, "set_channel"));

    public static final StreamCodec<ByteBuf, SetChannel> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SetChannel::pos,
            ByteBufCodecs.INT, SetChannel::cellIndex,
            ByteBufCodecs.INT, SetChannel::channel,
            ByteBufCodecs.BOOL, SetChannel::hasCellIndex,
            SetChannel::new
    );

    public SetChannel(BlockPos pos, Integer cellIndex, int channel) {
        this(pos, cellIndex != null ? cellIndex : -1, channel, cellIndex != null);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SetChannel pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BlockEntity blockEntity = ctx.player().level().getBlockEntity(pkt.pos());
            if (blockEntity instanceof AbstractWirelessEntity wirelessEntity)
                wirelessEntity.setChannel(pkt.channel());
            else if (pkt.hasCellIndex() && ModList.get().isLoaded("tinyredstone") && blockEntity instanceof PanelTile panelTile) {
                PanelCellPos panelCellPos = PanelCellPos.fromIndex(panelTile, pkt.cellIndex());
                if (panelCellPos.getIPanelCell() instanceof AbstractWirelessCell wirelessCell)
                    wirelessCell.setChannel(pkt.channel());
            }
        });
    }
}
