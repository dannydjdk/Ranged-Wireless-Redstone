package com.dannyandson.rangedwirelessredstone.compat;

import com.dannyandson.rangedwirelessredstone.compat.theoneprobe.InfoProvider;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;

public class CompatHandler {
    public static void register()  {
        if(ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", InfoProvider::new);
        }
    }
}
