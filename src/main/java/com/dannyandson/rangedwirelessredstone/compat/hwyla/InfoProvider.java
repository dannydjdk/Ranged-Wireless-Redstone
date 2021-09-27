package com.dannyandson.rangedwirelessredstone.compat.hwyla;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlock;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlock;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import mcp.mobius.waila.api.ui.IElementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

@WailaPlugin(RangedWirelessRedstone.MODID)
public class InfoProvider implements IWailaPlugin, IComponentProvider {
    static final ResourceLocation RENDER_STRING = new ResourceLocation("string");
    static final ResourceLocation RENDER_ITEM_INLINE = new ResourceLocation("item_inline");
    static final ResourceLocation RENDER_INFO_STRING = new ResourceLocation("info_string");

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(this, TooltipPosition.BODY, ReceiverBlock.class);
        registrar.registerComponentProvider(this, TooltipPosition.BODY, TransmitterBlock.class);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlock() != null) {
            BlockPos pos = accessor.getPosition();
            BlockEntity tileEntity = accessor.getLevel().getBlockEntity(pos);

            if (tileEntity instanceof AbstractWirelessEntity wirelessEntity) {
                IElementHelper helper = tooltip.getElementHelper();

                List<IElement> elements = new ArrayList<>();
                elements.add(helper.text(Component.nullToEmpty("Channel:")).tag(RENDER_STRING));
                elements.add(helper.text(Component.nullToEmpty(" " + wirelessEntity.getChannel())).tag(RENDER_INFO_STRING));
                elements.add(helper.text(Component.nullToEmpty(" Signal:")).tag(RENDER_STRING));
                elements.add(helper.text(Component.nullToEmpty(" " + wirelessEntity.getSignal())).tag(RENDER_INFO_STRING));
                tooltip.add(elements);

            }
        }
    }

}