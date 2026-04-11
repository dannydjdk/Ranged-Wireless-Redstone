package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlockRenderer;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = RangedWirelessRedstone.MODID, value = Dist.CLIENT)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModRegistration.RECEIVER_BLOCK_ENTITY.get(), ReceiverBlockRenderer::new);
        event.registerBlockEntityRenderer(ModRegistration.TRANSMITTER_BLOCK_ENTITY.get(), TransmitterBlockRenderer::new);
    }

}
