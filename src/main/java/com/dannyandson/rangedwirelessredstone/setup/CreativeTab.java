package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RangedWirelessRedstone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class CreativeTab {

    public static CreativeModeTab TAB;

    @SubscribeEvent
    public static void register(CreativeModeTabEvent.Register event) {
        TAB = event.registerCreativeModeTab(
                new ResourceLocation("rangedwirelessredstone", RangedWirelessRedstone.MODID), builder -> builder
                        .icon(() -> new ItemStack(com.dannyandson.rangedwirelessredstone.setup.Registration.TRANSMITTER_BLOCK.get()))
                        .displayItems((featureFlags, output, hasOp) -> Registration.ITEMS.getEntries().forEach(o -> output.accept(o.get())))
                        .title(Component.translatable("rangedwirelessredstone"))
        );
    }

}
