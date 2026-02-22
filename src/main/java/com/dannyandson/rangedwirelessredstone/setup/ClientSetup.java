package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlockRenderer;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = RangedWirelessRedstone.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        // No longer needed for render layer registration in 1.21
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.RECEIVER_BLOCK_ENTITY.get(), ReceiverBlockRenderer::new);
        event.registerBlockEntityRenderer(Registration.TRANSMITTER_BLOCK_ENTITY.get(), TransmitterBlockRenderer::new);
    }

}
