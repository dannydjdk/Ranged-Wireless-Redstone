package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.Receiver;
import com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells.Transmitter;
import com.dannyandson.rangedwirelessredstone.items.WirelessPanelCellItem;
import com.dannyandson.tinyredstone.TinyRedstone;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

public class TinyRedstoneRegistration {
    public static final RegistryObject<Item> TINY_TRANSMITTER_ITEM = Registration.ITEMS.register("tiny_redstone_transmitter", WirelessPanelCellItem::new);
    public static final RegistryObject<Item> TINY_RECEIVER_ITEM = Registration.ITEMS.register("tiny_redstone_receiver", WirelessPanelCellItem::new);

    public static void register(){}

    //called at FMLCommonSetupEvent in ModSetup
    public static void registerPanelCells(){
        TinyRedstone.registerPanelCell(Receiver.class, TINY_RECEIVER_ITEM.get());
        TinyRedstone.registerPanelCell(Transmitter.class, TINY_TRANSMITTER_ITEM.get());
    }
}
