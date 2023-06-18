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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RangedWirelessRedstone.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RangedWirelessRedstone.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RangedWirelessRedstone.MODID);
    private static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RangedWirelessRedstone.MODID);

    //BLOCKS
    public static final RegistryObject<TransmitterBlock> TRANSMITTER_BLOCK = BLOCKS.register("redstone_transmitter", TransmitterBlock::new);
    public static final RegistryObject<ReceiverBlock> RECEIVER_BLOCK = BLOCKS.register("redstone_receiver", ReceiverBlock::new);

    //BLOCK ENTITIES
    public static final RegistryObject<BlockEntityType<TransmitterBlockEntity>> TRANSMITTER_BLOCK_ENTITY =
            TILES.register("redstone_transmitter", () -> BlockEntityType.Builder.of(TransmitterBlockEntity::new, TRANSMITTER_BLOCK.get()).build(null));
        public static final RegistryObject<BlockEntityType<ReceiverBlockEntity>> RECEIVER_BLOCK_ENTITY =
            TILES.register("redstone_receiver", () -> BlockEntityType.Builder.of(ReceiverBlockEntity::new, RECEIVER_BLOCK.get()).build(null));

    //ITEMS
    public static final RegistryObject<Item> TRANSMITTER_ITEM = ITEMS.register("redstone_transmitter",()->new WirelessFullItem(TRANSMITTER_BLOCK.get()));
    public static final RegistryObject<Item> RECEIVER_ITEM = ITEMS.register("redstone_receiver", ()->new WirelessFullItem(RECEIVER_BLOCK.get()));

    public static RegistryObject<CreativeModeTab> CREATIVE_TAB = TAB.register("tinygatestab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("rangedwirelessredstone"))
                    .icon(() -> new ItemStack(Registration.TRANSMITTER_BLOCK.get()))
                    .displayItems((parameters,output) ->ITEMS.getEntries().forEach(o -> output.accept(o.get())))
                    .build());

    //called from main mod constructor
    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        TAB.register(FMLJavaModLoadingContext.get().getModEventBus());
    }




}
