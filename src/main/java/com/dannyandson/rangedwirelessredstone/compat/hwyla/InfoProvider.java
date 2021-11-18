package com.dannyandson.rangedwirelessredstone.compat.hwyla;

import com.dannyandson.rangedwirelessredstone.RangedWirelessRedstone;
import com.dannyandson.rangedwirelessredstone.blocks.AbstractWirelessEntity;
import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;

@WailaPlugin(RangedWirelessRedstone.MODID)
public class InfoProvider implements IWailaPlugin, IComponentProvider {
    static final ResourceLocation RENDER_STRING = new ResourceLocation("string");
    static final ResourceLocation RENDER_ITEM_INLINE = new ResourceLocation("item_inline");
    static final ResourceLocation RENDER_INFO_STRING = new ResourceLocation("info_string");

    @Override
    public void register(IRegistrar registrar) {
        if(FMLEnvironment.dist.isClient()) {
            registrar.registerTooltipRenderer(RENDER_STRING, new TooltipRendererString());
            registrar.registerTooltipRenderer(RENDER_ITEM_INLINE, new TooltipRendererItemStackInline());
            registrar.registerTooltipRenderer(RENDER_INFO_STRING, new TooltipRendererInfoString());
        }
        registrar.registerComponentProvider(this, TooltipPosition.BODY, AbstractWirelessEntity.class);
    }

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(accessor.getBlock() != null) {
            BlockPos pos = accessor.getPosition();
            TileEntity tileEntity = accessor.getWorld().getBlockEntity(pos);

            if (tileEntity instanceof AbstractWirelessEntity) {
                AbstractWirelessEntity wirelessEntity = (AbstractWirelessEntity) tileEntity;

                tooltip.add(new RenderableTextComponent(
                        getStringRenderable("Channel: " + wirelessEntity.getChannel())
                ));
                tooltip.add(new RenderableTextComponent(
                        getItemStackRenderable(new ItemStack(Items.REDSTONE)),
                        getStringRenderable("Signal: " + wirelessEntity.getWeakSignal())
                ));

            }
        }
    }

    public static RenderableTextComponent getItemStackRenderable(ItemStack itemStack) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("id", itemStack.getItem().getRegistryName().toString());
        return new RenderableTextComponent(RENDER_ITEM_INLINE, tag);
    }

    public static RenderableTextComponent getStringRenderable(String string) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("string", string);
        return new RenderableTextComponent(RENDER_STRING, tag);
    }

    public static RenderableTextComponent getInfoStringRenderable(String string) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("string", string);
        return new RenderableTextComponent(RENDER_INFO_STRING, tag);
    }
}