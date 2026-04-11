package com.dannyandson.rangedwirelessredstone;

import com.dannyandson.rangedwirelessredstone.setup.ClientSetup;
import com.dannyandson.rangedwirelessredstone.setup.ModRegistration;
import com.dannyandson.rangedwirelessredstone.setup.ModSetup;
import com.dannyandson.rangedwirelessredstone.setup.TinyRedstoneRegistration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RangedWirelessRedstone.MODID)
public class RangedWirelessRedstone
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "rangedwirelessredstone";

    public RangedWirelessRedstone(IEventBus modEventBus, ModContainer modContainer) {

        if (ModList.get().isLoaded("tinyredstone"))
            TinyRedstoneRegistration.register();
        ModRegistration.register(modEventBus);

        // Register the setup method for modloading
        modEventBus.addListener(ModSetup::init);

        if(FMLEnvironment.getDist().isClient()) {
            modEventBus.addListener(ClientSetup::init);
        }

        //load configs
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        // Note: TheOneProbe compat excluded from build until TOP is available for 26.1
    }
}
