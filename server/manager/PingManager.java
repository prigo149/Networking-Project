package com.websystem.server.manager;

import com.websystem.core.packets.impls.PingPacket;
import com.websystem.server.Server;

public class PingManager extends Thread {

    @Override
    public void run () {
        while ( !interrupted() ) {
            Server.getNettyClient().write( new PingPacket() );
            try {
                Thread.sleep( 1000 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
}
