package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {

    public static void init(final FMLCommonSetupEvent event) {
        // register everything
        if (ModList.get().isLoaded("tinyredstone"))
            TinyRedstoneRegistration.registerPanelCells();
    }
}
