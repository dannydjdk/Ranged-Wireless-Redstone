package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlockRenderer;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RangedWirelessRedstone.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        //ItemBlockRenderTypes.setRenderLayer(Registration.TRANSMITTER_BLOCK.get(), RenderType.solid());
        //ItemBlockRenderTypes.setRenderLayer(Registration.RECEIVER_BLOCK.get(), RenderType.solid());
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.RECEIVER_BLOCK_ENTITY.get(), ReceiverBlockRenderer::new);
        event.registerBlockEntityRenderer(Registration.TRANSMITTER_BLOCK_ENTITY.get(), TransmitterBlockRenderer::new);
    }


    }
