package com.dannyandson.rangedwirelessredstone.logic;

import net.minecraft.util.math.BlockPos;

public interface IWirelessComponent {

    int getStrongSignal();
    int getWeakSignal();

    void setSignals(int weak, int strong);

    int getChannel();
    void setChannel(int channel);

    BlockPos getPos();
    Integer getCellIndex();

}
