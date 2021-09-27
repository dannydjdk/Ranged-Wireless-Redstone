package com.dannyandson.rangedwirelessredstone.compat.theoneprobe;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockEntity;
import mcjty.theoneprobe.api.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class InfoProvider implements IBlockDisplayOverride, Function<ITheOneProbe, Void>, IProbeInfoProvider {

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(RangedWirelessRedstone.MODID , "wireless_block");
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerBlockDisplayOverride(this);
        theOneProbe.registerProvider(this);
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData probeHitData) {
        BlockPos pos = probeHitData.getPos();
        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof AbstractWirelessEntity wirelessEntity) {
            probeInfo.horizontal()
                    .text(CompoundText.createLabelInfo("Channel: ", wirelessEntity.getChannel()));
            if (tileEntity instanceof TransmitterBlockEntity)
                probeInfo.horizontal()
                        .text(CompoundText.createLabelInfo("Signal: ", wirelessEntity.getSignal()));
        }
    }

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData) {
        return false;
    }
}
