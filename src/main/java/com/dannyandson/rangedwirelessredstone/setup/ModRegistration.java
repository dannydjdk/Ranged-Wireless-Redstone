package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlock;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlockEntity;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlock;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockEntity;
import com.dannyandson.rangedwirelessredstone.items.WirelessFullItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRegistration {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RangedWirelessRedstone.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RangedWirelessRedstone.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RangedWirelessRedstone.MODID);
    private static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RangedWirelessRedstone.MODID);

    //BLOCKS
    public static final DeferredBlock<TransmitterBlock> TRANSMITTER_BLOCK = BLOCKS.registerBlock("redstone_transmitter",
            TransmitterBlock::new,
            prop->prop.sound(SoundType.STONE).strength(2.0f));
    public static final DeferredBlock<ReceiverBlock> RECEIVER_BLOCK = BLOCKS.registerBlock("redstone_receiver",
            ReceiverBlock::new,
            prop->prop.sound(SoundType.STONE).strength(2.0f));

    //BLOCK ENTITIES
    public static final Supplier<BlockEntityType<TransmitterBlockEntity>> TRANSMITTER_BLOCK_ENTITY =
            TILES.register("redstone_transmitter", () -> new BlockEntityType<>(TransmitterBlockEntity::new, TRANSMITTER_BLOCK.get()));
    public static final Supplier<BlockEntityType<ReceiverBlockEntity>> RECEIVER_BLOCK_ENTITY =
            TILES.register("redstone_receiver", () -> new BlockEntityType<>(ReceiverBlockEntity::new, RECEIVER_BLOCK.get()));

    //ITEMS
    public static final DeferredItem<Item> TRANSMITTER_ITEM = ITEMS.registerItem("redstone_transmitter",
            props -> new WirelessFullItem(TRANSMITTER_BLOCK.get(), props.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> RECEIVER_ITEM = ITEMS.registerItem("redstone_receiver",
            props -> new WirelessFullItem(RECEIVER_BLOCK.get(), props.useBlockDescriptionPrefix()));

    public static Supplier<CreativeModeTab> CREATIVE_TAB = TAB.register("tinygatestab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("rangedwirelessredstone"))
                    .icon(() -> new ItemStack(ModRegistration.TRANSMITTER_BLOCK.get()))
                    .displayItems((parameters, output) -> ITEMS.getEntries().forEach(o -> output.accept(o.get())))
                    .build());

    //called from main mod constructor
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        TILES.register(modEventBus);
        TAB.register(modEventBus);
    }
}
