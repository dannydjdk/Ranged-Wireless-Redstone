package com.dannyandson.rangedwirelessredstone.compat;

import com.dannyandson.rangedwirelessredstone.compat.theoneprobe.InfoProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;

public class CompatHandler {
    public static void register()  {
        if(ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", InfoProvider::new);
        }
    }
}
