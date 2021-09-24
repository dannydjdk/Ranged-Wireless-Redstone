package com.dannyandson.rangedwirelessredstone.items;

import com.dannyandson.rangedwirelessredstone.setup.ModSetup;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class WirelessFullItem extends BlockItem {
    public WirelessFullItem(Block block) {
        super(block, new Item.Properties().tab(ModSetup.ITEM_GROUP));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
        if (Screen.hasShiftDown()) {
            list.add(new TranslationTextComponent("message." + this.getDescriptionId()).withStyle(TextFormatting.DARK_AQUA));
        } else
            list.add(new TranslationTextComponent("rangedwirelessredstone.tooltip.press_shift").withStyle(TextFormatting.DARK_GRAY));
    }

}