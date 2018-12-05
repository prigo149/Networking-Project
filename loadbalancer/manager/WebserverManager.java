package com.websystem.loadbalancer.manager;

import com.websystem.core.packets.events.ChannelUnregisteredEvent;
import com.websystem.core.packets.impls.ConnectionPacket;
import com.websystem.core.packets.impls.ConnectionResultPacket;
import com.websystem.core.packets.listener.EventHandler;
import com.websystem.core.packets.listener.Listener;
import com.websystem.core.packets.listener.PacketReceivedHandler;
import com.websystem.core.utils.ListIteratorCopy;
import com.websystem.loadbalancer.models.Webserver;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class WebserverManager implements Listener {

    private static List<Webserver> webservers = new ArrayList<>();

    public static Webserver registerWebserver ( Webserver webserver ) {
        webserver.setId( nextId() );
        System.out.println( "Registering webserver #" + webserver.getId() + " (" + webserver.getName() + ")" );
        webservers.add( webserver );
        return webserver;
    }

    public static void unregisterWebserver ( Webserver webserver ) {
        System.out.println( "Unregistering webserver #" + webserver.getId() + " (" + webserver.getName() + ")" );
        webservers.remove( getWebserver( webserver.getId() ) );
        webserver.setId( 0 );
    }

    private static int nextId () {
        for ( int i = 1; i < Integer.MAX_VALUE; i++ )
            if ( !existsWebserver( i ) ) return i;
        return -1;
    }

    public static List<Webserver> getWebservers () {
        return ListIteratorCopy.copyList( webservers );
    }

    public static boolean existsWebserver ( int id ) {
        return getWebserver( id ) != null;
    }

    public static Webserver getWebserver ( int id ) {
        for ( Webserver webserver : getWebservers() )
            if ( webserver.getId() == id ) return webserver;
        return null;
    }

    public static Webserver getWebserver ( Channel channel ) {
        for ( Webserver webserver : getWebservers() )
            if ( webserver.getChannel() == channel ) return webserver;
        return null;
    }

    @PacketReceivedHandler
    public void onConnectionReceive ( ConnectionPacket connectionPacket, Channel channel ) {
        InetSocketAddress inetSocketAddress = ( InetSocketAddress ) channel.remoteAddress();
        registerWebserver( new Webserver( connectionPacket.getName(), inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort(), connectionPacket.getWeight(), channel ) );
        getWebserver( channel ).write( new ConnectionResultPacket( true, null, null ) );
    }

    @EventHandler
    public void onConnectionLoss ( ChannelUnregisteredEvent e ) {
        Webserver webserver = getWebserver( e.getChannel() );
        System.out.println( "A server lost the connection." );
        if ( webserver == null ) return;

        unregisterWebserver( webserver );
    }
}
