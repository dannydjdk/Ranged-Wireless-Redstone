package com.dannyandson.rangedwirelessredstone.logic;

import net.minecraft.util.math.BlockPos;

public interface IWirelessComponent {

    int getSignal();
    void setSignal(int signal);

    int getChannel();
    void setChannel(int channel);

    BlockPos getPos();
    Integer getCellIndex();

}
