package com.dannyandson.rangedwirelessredstone.items;

import com.dannyandson.rangedwirelessredstone.setup.ModSetup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class WirelessFullItem extends BlockItem {
    public WirelessFullItem(Block block) {
        super(block,new Item.Properties().tab(ModSetup.ITEM_GROUP));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flags) {
        if (Screen.hasShiftDown()) {
            list.add(new TranslatableComponent("message." + this.getDescriptionId()).withStyle(ChatFormatting.DARK_AQUA));
        } else
            list.add(new TranslatableComponent("rangedwirelessredstone.tooltip.press_shift").withStyle(ChatFormatting.DARK_GRAY));
    }

}