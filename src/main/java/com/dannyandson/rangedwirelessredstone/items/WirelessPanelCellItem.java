package com.dannyandson.rangedwirelessredstone.items;

import com.dannyandson.tinyredstone.api.AbstractPanelCellItem;
import com.dannyandson.tinyredstone.blocks.PanelBlock;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class WirelessPanelCellItem extends AbstractPanelCellItem {

    public WirelessPanelCellItem(Item.Properties props) {
        super(props);
    }

    // onBlockStartBreak removed from vanilla Item - not an override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return player.level().getBlockState(pos).getBlock() instanceof PanelBlock;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> textConsumer, TooltipFlag flags) {
        if (isShiftKeyDown()) {
            textConsumer.accept(Component.translatable("message.item.redstone_panel_cell").withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            textConsumer.accept(Component.translatable("message." + this.getDescriptionId()).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
        } else
            textConsumer.accept(Component.translatable("tinyredstone.tooltip.press_shift").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
    }

    private static boolean isShiftKeyDown() {
        var window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT)
                || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
}