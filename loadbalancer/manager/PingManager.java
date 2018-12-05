package com.websystem.loadbalancer.manager;

import com.websystem.core.packets.impls.PingPacket;
import com.websystem.core.packets.listener.Listener;
import com.websystem.core.packets.listener.PacketReceivedHandler;
import com.websystem.loadbalancer.models.Webserver;
import io.netty.channel.Channel;

public class PingManager implements Listener {

    @PacketReceivedHandler
    public void onPing ( PingPacket pingPacket, Channel channel ) {
        Webserver webserver = WebserverManager.getWebserver( channel );
        if ( webserver != null ) webserver.setPing( System.currentTimeMillis() - pingPacket.getSent() );
    }
}
