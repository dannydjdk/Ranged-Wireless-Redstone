package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerNetworkTrigger {

    ServerTrigger trigger;
    public ServerNetworkTrigger(ServerTrigger trigger){
        this.trigger=trigger;
    }

    public ServerNetworkTrigger(PacketBuffer buffer){
        this.trigger=buffer.readEnum(ServerTrigger.class);
    }

    public void toBytes(PacketBuffer buffer){
        buffer.writeEnum(trigger);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (trigger == ServerTrigger.CLEANUP) {
                ChannelData.getChannelData(ctx.get().getSender().getLevel()).cleanupTransmitters(ctx.get().getSender().getLevel());
                CompoundNBT nbt = ChannelData.getChannelData(ctx.get().getSender().getLevel()).getChannelNBT();
                ModNetworkHandler.sendToClient(new NetworkViewerTrigger(nbt), ctx.get().getSender());
            } else if (trigger == ServerTrigger.NETWORK_VIEWER) {
                CompoundNBT nbt = ChannelData.getChannelData(ctx.get().getSender().getLevel()).getChannelNBT();
                ModNetworkHandler.sendToClient(new NetworkViewerTrigger(nbt), ctx.get().getSender());
            }
            ctx.get().setPacketHandled(true);
        });
        return true;
    }

    public enum ServerTrigger{
        CLEANUP,NETWORK_VIEWER
    }

}
