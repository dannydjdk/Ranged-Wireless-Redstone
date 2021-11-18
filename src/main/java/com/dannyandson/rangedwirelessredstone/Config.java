package com.dannyandson.rangedwirelessredstone;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {
    public static ForgeConfigSpec SERVER_CONFIG;
    public static final String CATEGORY_GENERAL = "balance";
    public static ForgeConfigSpec.IntValue RANGE_BLOCK;
    public static ForgeConfigSpec.IntValue RANGE_CELL;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("Balance Settings").push(CATEGORY_GENERAL);
        RANGE_BLOCK = SERVER_BUILDER.comment("Range of transmitter block. How many blocks should the signal travel in each direction?")
                .defineInRange("range_block", 256, 1, Integer.MAX_VALUE);
        RANGE_CELL = SERVER_BUILDER.comment("Range of tiny transmitter block. How many blocks should the signal travel in each direction?")
                .defineInRange("range_cell", 64, 1, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();

    }
}
