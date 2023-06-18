package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.logic.ChannelData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerNetworkTrigger {

    ServerTrigger trigger;
    public ServerNetworkTrigger(ServerTrigger trigger){
        this.trigger=trigger;
    }

    public ServerNetworkTrigger(FriendlyByteBuf buffer){
        this.trigger=buffer.readEnum(ServerTrigger.class);
    }

    public void toBytes(FriendlyByteBuf buffer){
        buffer.writeEnum(trigger);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (trigger == ServerTrigger.CLEANUP) {
                ServerLevel serverLevel = (ServerLevel) ctx.get().getSender().level();
                ChannelData.getChannelData(serverLevel).cleanupTransmitters(serverLevel);
                CompoundTag nbt = ChannelData.getChannelData(serverLevel).getChannelNBT();
                ModNetworkHandler.sendToClient(new NetworkViewerTrigger(nbt), ctx.get().getSender());
            } else if (trigger == ServerTrigger.NETWORK_VIEWER) {
                CompoundTag nbt = ChannelData.getChannelData((ServerLevel) ctx.get().getSender().level()).getChannelNBT();
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
