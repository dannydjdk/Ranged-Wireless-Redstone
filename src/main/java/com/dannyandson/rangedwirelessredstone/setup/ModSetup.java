package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.network.ModNetworkHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = RangedWirelessRedstone.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {
    public static final ItemGroup ITEM_GROUP = new ItemGroup(RangedWirelessRedstone.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.TRANSMITTER_BLOCK.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
        // register everything
        if (ModList.get().isLoaded("tinyredstone"))
            TinyRedstoneRegistration.registerPanelCells();
        ModNetworkHandler.registerMessages();
    }
}
