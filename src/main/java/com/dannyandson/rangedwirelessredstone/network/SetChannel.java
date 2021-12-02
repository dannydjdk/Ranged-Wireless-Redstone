package com.dannyandson.rangedwirelessredstone.network;

import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.AbstractWirelessCell;
import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SetChannel {
    private final BlockPos pos;
    private final Integer channel;
    private Integer cellIndex;

    public SetChannel(BlockPos pos, @Nullable Integer cellIndex, int channel)
    {
        this.pos=pos;
        this.channel = channel;
        this.cellIndex=cellIndex;
    }

    public SetChannel(FriendlyByteBuf buffer)
    {
        this.pos= buffer.readBlockPos();
        this.channel=buffer.readInt();
        try {
            this.cellIndex = buffer.readInt();
        }catch (IndexOutOfBoundsException e){
            this.cellIndex=null;
        }
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeInt(channel);
        if (cellIndex!=null)
            buf.writeInt(cellIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(()-> {
            BlockEntity blockEntity = ctx.get().getSender().getLevel().getBlockEntity(pos);
            if (blockEntity instanceof AbstractWirelessEntity wirelessEntity)
                wirelessEntity.setChannel(channel);
            else if (cellIndex!=null && ModList.get().isLoaded("tinyredstone") && blockEntity instanceof PanelTile panelTile) {
                PanelCellPos panelCellPos = PanelCellPos.fromIndex(panelTile,cellIndex);
                if (panelCellPos.getIPanelCell() instanceof AbstractWirelessCell wirelessCell)
                    wirelessCell.setChannel(channel);
            }
            ctx.get().setPacketHandled(true);
        });
        return true;
    }
}
