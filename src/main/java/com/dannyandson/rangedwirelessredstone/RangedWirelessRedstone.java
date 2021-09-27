package com.dannyandson.rangedwirelessredstone;

import com.dannyandson.rangedwirelessredstone.setup.ClientSetup;
import com.dannyandson.rangedwirelessredstone.setup.Registration;
import com.dannyandson.rangedwirelessredstone.setup.ModSetup;
import com.dannyandson.rangedwirelessredstone.setup.TinyRedstoneRegistration;
import com.dannyandson.rangedwirelessredstone.compat.CompatHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RangedWirelessRedstone.MODID)
public class RangedWirelessRedstone
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "rangedwirelessredstone";

    public RangedWirelessRedstone() {

        if (ModList.get().isLoaded("tinyredstone"))
            TinyRedstoneRegistration.register();
        Registration.register();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);

        if(FMLEnvironment.dist.isClient()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        }

        //load configs
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        CompatHandler.register();
    }


}
