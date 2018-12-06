package com.websystem.core.packets.handler;

import com.websystem.core.packets.Packet;
import com.websystem.core.packets.listener.Event;
import com.websystem.core.packets.listener.EventHandler;
import com.websystem.core.packets.listener.Listener;
import com.websystem.core.packets.listener.PacketReceivedHandler;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PacketHandler {

    private static List<Listener> listeners = new ArrayList<>();

    public static void registerListener( Listener... eventListeners ) {
        for ( Listener eventListener : eventListeners )
            registerListener( eventListener );
    }

    public static void registerListener ( Listener eventListener ) {
        listeners.add( eventListener );
    }

    public static void unregisterListener ( Listener eventListener ) {
        listeners.remove( eventListener );
    }

    public static void callEvent ( Event event ) {
        listeners.forEach( eventListener -> {
            for ( Method method : eventListener.getClass().getMethods() ) {
                if ( !method.isAnnotationPresent( EventHandler.class ) || method.getParameterCount() != 1 || !method.getParameterTypes()[ 0 ].isInstance( event ) )
                    continue;
                try {
                    method.invoke( eventListener, event );
                } catch ( InvocationTargetException | IllegalAccessException e ) {
                    e.printStackTrace();
                }
            }
        } );
    }

    public static void callPacketReceived ( Packet packet, Channel channel ) {
        for ( Listener eventListener : listeners ) {
            for ( Method method : eventListener.getClass().getMethods() ) {
                if ( !method.isAnnotationPresent( PacketReceivedHandler.class ) || method.getParameterCount() < 1 || !method.getParameterTypes()[ 0 ].isInstance( packet ) )
                    continue;
                try {
                    if ( method.getParameterCount() == 1 ) {
                        method.invoke( eventListener, packet );
                    } else {
                        method.invoke( eventListener, packet, channel );
                    }
                } catch ( InvocationTargetException | IllegalAccessException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
}
