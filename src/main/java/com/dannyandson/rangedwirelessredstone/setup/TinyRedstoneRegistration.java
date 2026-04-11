package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.ReceiverCell;
import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.TransmitterCell;
import com.dannyandson.rangedwirelessredstone.items.WirelessPanelCellItem;
import com.dannyandson.tinyredstone.TinyRedstone;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class TinyRedstoneRegistration {
    public static final DeferredItem<Item> TINY_TRANSMITTER_ITEM = ModRegistration.ITEMS.registerItem("tiny_redstone_transmitter", WirelessPanelCellItem::new);
    public static final DeferredItem<Item> TINY_RECEIVER_ITEM = ModRegistration.ITEMS.registerItem("tiny_redstone_receiver", WirelessPanelCellItem::new);

    public static void register(){}

    //called at FMLCommonSetupEvent in ModSetup
    public static void registerPanelCells(){
        TinyRedstone.registerPanelCell(ReceiverCell.class, TINY_RECEIVER_ITEM.get());
        TinyRedstone.registerPanelCell(TransmitterCell.class, TINY_TRANSMITTER_ITEM.get());
    }
}
