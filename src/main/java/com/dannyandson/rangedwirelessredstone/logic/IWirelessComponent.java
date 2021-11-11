package com.dannyandson.rangedwirelessredstone.logic;

import net.minecraft.core.BlockPos;

public interface IWirelessComponent {

    int getStrongSignal();
    int getWeakSignal();

    void setSignals(int weak, int song);

    int getChannel();
    void setChannel(int channel);

    BlockPos getPos();
    Integer getCellIndex();

}
