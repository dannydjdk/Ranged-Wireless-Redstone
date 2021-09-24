package com.dannyandson.rangedwirelessredstone.setup;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlock;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlockEntity;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlock;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockEntity;
import com.dannyandson.rangedwirelessredstone.items.WirelessFullItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RangedWirelessRedstone.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RangedWirelessRedstone.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RangedWirelessRedstone.MODID);

    //BLOCKS
    public static final RegistryObject<TransmitterBlock> TRANSMITTER_BLOCK = BLOCKS.register("redstone_transmitter", TransmitterBlock::new);
    public static final RegistryObject<ReceiverBlock> RECEIVER_BLOCK = BLOCKS.register("redstone_receiver", ReceiverBlock::new);

    //BLOCK ENTITIES
    public static final RegistryObject<TileEntityType<TransmitterBlockEntity>> TRANSMITTER_BLOCK_ENTITY =
            TILES.register("redstone_transmitter", () -> TileEntityType.Builder.of(TransmitterBlockEntity::new, TRANSMITTER_BLOCK.get()).build(null));
        public static final RegistryObject<TileEntityType<ReceiverBlockEntity>> RECEIVER_BLOCK_ENTITY =
            TILES.register("redstone_receiver", () -> TileEntityType.Builder.of(ReceiverBlockEntity::new, RECEIVER_BLOCK.get()).build(null));

    //ITEMS
    public static final RegistryObject<Item> TRANSMITTER_ITEM = ITEMS.register("redstone_transmitter",()->new WirelessFullItem(TRANSMITTER_BLOCK.get()));
    public static final RegistryObject<Item> RECEIVER_ITEM = ITEMS.register("redstone_receiver", ()->new WirelessFullItem(RECEIVER_BLOCK.get()));

    //called from main mod constructor
    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }




}
