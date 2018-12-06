package com.websystem.server.listener;

import com.websystem.core.packets.impls.ConnectionResultPacket;
import com.websystem.core.packets.impls.PingPacket;
import com.websystem.core.packets.listener.Listener;
import com.websystem.core.packets.listener.PacketReceivedHandler;
import com.websystem.server.Server;

public class ConnectionListener implements Listener {

    @PacketReceivedHandler
    public void onConnectionResultPacket ( ConnectionResultPacket connectionResultPacket ) {
        if ( connectionResultPacket.isSuccess() ) {
            System.out.println( "Connected successfully to load balancer" );
        } else {
            System.out.println( "Connection to load balancer failed:" );
            System.out.println( connectionResultPacket.getTitle() );
            System.out.println( connectionResultPacket.getError() );
        }
    }

    @PacketReceivedHandler
    public void onPingReceive ( PingPacket pingPacket ) {
        Server.getNettyClient().write( pingPacket );
    }
}
