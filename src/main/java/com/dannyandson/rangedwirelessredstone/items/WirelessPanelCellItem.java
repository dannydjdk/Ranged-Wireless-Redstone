package com.dannyandson.rangedwirelessredstone.items;

import com.dannyandson.tinyredstone.api.AbstractPanelCellItem;
import com.dannyandson.tinyredstone.blocks.PanelBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class WirelessPanelCellItem extends AbstractPanelCellItem {

    public WirelessPanelCellItem() {
        super(new Item.Properties());
    }

    // onBlockStartBreak removed from vanilla Item in 1.21 - remove @Override but keep method
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return player.level().getBlockState(pos).getBlock() instanceof PanelBlock;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flags) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("message.item.redstone_panel_cell").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("message." + this.getDescriptionId()).withStyle(ChatFormatting.DARK_AQUA));
        } else
            list.add(Component.translatable("tinyredstone.tooltip.press_shift").withStyle(ChatFormatting.DARK_GRAY));
    }
}
