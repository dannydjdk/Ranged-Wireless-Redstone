package com.dannyandson.rangedwirelessredstone.compat.theoneprobe;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlockEntity;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;

public class InfoProvider implements IBlockDisplayOverride, Function<ITheOneProbe, Void>, IProbeInfoProvider {

    @Override
    public String getID() {
        return RangedWirelessRedstone.MODID + ":";
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerBlockDisplayOverride(this);
        theOneProbe.registerProvider(this);
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData probeHitData) {
        BlockPos pos = probeHitData.getPos();
        TileEntity tileEntity = world.getBlockEntity(pos);

        if (tileEntity instanceof AbstractWirelessEntity) {
            AbstractWirelessEntity wirelessEntity = (AbstractWirelessEntity) tileEntity;
            probeInfo.horizontal()
                    .text(CompoundText.createLabelInfo("Channel: ", wirelessEntity.getChannel()));
            if (tileEntity instanceof TransmitterBlockEntity)
                probeInfo.horizontal()
                        .text(CompoundText.createLabelInfo("Signal: ", wirelessEntity.getWeakSignal()));
        }
    }

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {
        return false;
    }
}
