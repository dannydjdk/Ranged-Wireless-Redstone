package com.dannyandson.rangedwirelessredstone.compat.jade;

import com.dannyandson.rangedwirelessredstone.blocks.ReceiverBlock;
import com.dannyandson.rangedwirelessredstone.blocks.TransmitterBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(WirelessBlockProvider.INSTANCE, TransmitterBlock.class);
        registration.registerBlockComponent(WirelessBlockProvider.INSTANCE, ReceiverBlock.class);
    }
}