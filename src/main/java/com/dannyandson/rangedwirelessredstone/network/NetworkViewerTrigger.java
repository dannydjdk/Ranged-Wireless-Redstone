package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.gui.NetworkViewerGUI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NetworkViewerTrigger {

    CompoundNBT nbt;

    public NetworkViewerTrigger(CompoundNBT nbt) {
        this.nbt=nbt;
    }

    public NetworkViewerTrigger(PacketBuffer buf){
        this.nbt= buf.readNbt();
    }

    public void toBytes(PacketBuffer buf){
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
