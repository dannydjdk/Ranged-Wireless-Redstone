package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.ReceiverCell;
import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.TransmitterCell;
import com.dannyandson.rangedwirelessredstone.items.WirelessPanelCellItem;
import com.dannyandson.tinyredstone.TinyRedstone;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class TinyRedstoneRegistration {
    public static final RegistryObject<Item> TINY_TRANSMITTER_ITEM = Registration.ITEMS.register("tiny_redstone_transmitter", WirelessPanelCellItem::new);
    public static final RegistryObject<Item> TINY_RECEIVER_ITEM = Registration.ITEMS.register("tiny_redstone_receiver", WirelessPanelCellItem::new);

    public static void register(){}

    //called at FMLCommonSetupEvent in ModSetup
    public static void registerPanelCells(){
        TinyRedstone.registerPanelCell(ReceiverCell.class, TINY_RECEIVER_ITEM.get());
        TinyRedstone.registerPanelCell(TransmitterCell.class, TINY_TRANSMITTER_ITEM.get());
    }
}
