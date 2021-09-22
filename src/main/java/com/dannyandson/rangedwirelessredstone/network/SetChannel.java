package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class SetChannel {
    private final BlockPos pos;
    private final Integer channel;

    public SetChannel(BlockPos pos, int channel)
    {
        this.pos=pos;
        this.channel = channel;
    }

    public SetChannel(FriendlyByteBuf buffer)
    {
        this.pos= buffer.readBlockPos();
        this.channel=buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeInt(channel);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(()-> {
            if (ctx.get().getSender().getLevel().getBlockEntity(pos) instanceof AbstractWirelessEntity wirelessEntity)
            wirelessEntity.setChannel(channel);
            ctx.get().setPacketHandled(true);
        });
        return true;
    }
}
