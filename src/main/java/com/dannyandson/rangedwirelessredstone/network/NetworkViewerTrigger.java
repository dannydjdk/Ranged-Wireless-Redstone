package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.gui.NetworkViewerGUI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class NetworkViewerTrigger {

    CompoundTag nbt;

    public NetworkViewerTrigger(CompoundTag nbt) {
        this.nbt=nbt;
    }

    public NetworkViewerTrigger(FriendlyByteBuf buf){
        this.nbt= buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(nbt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(()-> {
            NetworkViewerGUI.open(nbt);
            ctx.get().setPacketHandled(true);
        });
        return true;
    }
}
