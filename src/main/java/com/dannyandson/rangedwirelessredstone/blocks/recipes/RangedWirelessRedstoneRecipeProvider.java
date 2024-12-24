package com.dannyandson.rangedwirelessredstone.blocks.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.TrueCondition;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.dannyandson.rangedwirelessredstone.setup.Registration.RECEIVER_BLOCK;
import static com.dannyandson.rangedwirelessredstone.setup.Registration.TRANSMITTER_BLOCK;

public class RangedWirelessRedstoneRecipeProvider extends RecipeProvider {
    public RangedWirelessRedstoneRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ConditionalRecipe.builder()
            .addCondition(TrueCondition.INSTANCE)
            .addRecipe(
                ShapedRecipeBuilder.shaped(
                        RecipeCategory.REDSTONE,
                        TRANSMITTER_BLOCK.get())
                    .pattern(" P ")
                    .pattern(" R ")
                    .pattern("SRS")
                    .define('P', Items.ENDER_PEARL)
                    .define('R', Items.REDSTONE)
                    .define('S', Items.SMOOTH_STONE_SLAB)
                    .unlockedBy("has_redstone", has(Items.REDSTONE))
                    ::save)
            .generateAdvancement()
            .build(consumer, TRANSMITTER_BLOCK.getId());

        ConditionalRecipe.builder()
            .addCondition(TrueCondition.INSTANCE)
            .addRecipe(
                ShapedRecipeBuilder.shaped(
                        RecipeCategory.REDSTONE,
                        RECEIVER_BLOCK.get())
                    .pattern("T T")
                    .pattern("R R")
                    .pattern("STS")
                    .define('S',  Items.SMOOTH_STONE_SLAB)
                    .define('T',  Items.REDSTONE_TORCH)
                    .define('R', Items.REDSTONE)
                    .unlockedBy("has_redstone", has(Items.REDSTONE))
                    ::save)
            .generateAdvancement()
            .build(consumer, RECEIVER_BLOCK.getId());
    }
}
