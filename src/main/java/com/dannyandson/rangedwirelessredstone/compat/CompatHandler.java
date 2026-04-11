package com.dannyandson.rangedwirelessredstone.compat;

public class CompatHandler {
    public static void register()  {
        // TheOneProbe compat disabled until TOP is available for 26.1
        // When available, re-enable the theoneprobe exclusion in build.gradle
        // and restore: InterModComms.sendTo("theoneprobe", "getTheOneProbe", InfoProvider::new);
    }
}
