package com.dannyandson.rangedwirelessredstone.blocks.tinyredstonecells;

import com.dannyandson.tinyredstone.blocks.PanelCellPos;
import com.dannyandson.tinyredstone.blocks.PanelTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Helper class that isolates all direct references to Tiny Redstone classes.
 * This class is ONLY loaded when Tiny Redstone is present in the mod list.
 * Other classes should call these methods behind a ModList.get().isLoaded("tinyredstone") check.
 */
public class TinyRedstoneHelper {

    /**
     * Try to set the channel on a wireless cell inside a panel tile.
     * @return true if the block entity was a PanelTile and the cell was found
     */
    public static boolean trySetCellChannel(BlockEntity blockEntity, int cellIndex, int channel) {
        if (blockEntity instanceof PanelTile panelTile) {
            PanelCellPos panelCellPos = PanelCellPos.fromIndex(panelTile, cellIndex);
            if (panelCellPos.getIPanelCell() instanceof AbstractWirelessCell wirelessCell) {
                wirelessCell.setChannel(channel);
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a block entity is a PanelTile with a TransmitterCell at the given index,
     * and if so, set its channel. Used during cleanup.
     * @return true if it was a valid transmitter cell, false if it should be removed
     */
    public static boolean cleanupPanelTransmitter(BlockEntity blockEntity, int cellIndex, int channel) {
        if (blockEntity instanceof PanelTile panelTile) {
            PanelCellPos panelCellPos = PanelCellPos.fromIndex(panelTile, cellIndex);
            if (panelCellPos.getIPanelCell() instanceof TransmitterCell transmitterCell) {
                transmitterCell.setChannel(channel);
                return true;
            }
        }
        return false;
    }

    /**
     * Send a packet to all players near a PanelTile.
     * Called from AbstractWirelessCell.setChannel().
     */
    public static void sendPanelCellSyncToNearby(BlockEntity blockEntity, Object packet) {
        if (blockEntity instanceof PanelTile panelTile) {
            BlockPos pos = panelTile.getBlockPos();
            for (var player : panelTile.getLevel().players()) {
                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer
                        && player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64d) {
                    if (packet instanceof net.minecraft.network.protocol.common.custom.CustomPacketPayload payload) {
                        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, payload);
                    }
                }
            }
        }
    }
}
