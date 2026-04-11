package com.dannyandson.rangedwirelessredstone.compat.jade;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum WirelessBlockProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final Identifier UID =
            Identifier.fromNamespaceAndPath(RangedWirelessRedstone.MODID, "wireless_block");

    @Override
    public Identifier getUid() {
        return UID;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof AbstractWirelessEntity wirelessEntity) {
            tooltip.add(Component.literal("Channel: " + wirelessEntity.getChannel()));
            tooltip.add(Component.literal("Signal: " + wirelessEntity.getWeakSignal()));
        }
    }
}