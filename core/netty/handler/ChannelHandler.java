package com.websystem.core.netty.handler;

import com.websystem.core.packets.Packet;
import com.websystem.core.packets.events.ChannelRegisteredEvent;
import com.websystem.core.packets.events.ChannelUnregisteredEvent;
import com.websystem.core.packets.events.ExceptionCaughtEvent;
import com.websystem.core.packets.handler.PacketHandler;
import com.websystem.core.packets.impls.PingPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead ( ChannelHandlerContext ctx, Object packet ) {
        if ( !( packet instanceof PingPacket ) ) System.out.println( "Received: " + packet.getClass().getSimpleName() );
        PacketHandler.callPacketReceived( ( Packet ) packet, ctx.channel() );
    }

    @Override
    public void channelActive ( ChannelHandlerContext ctx ) {
        System.out.println( "Channel active" );
    }

    @Override
    public void channelRegistered ( ChannelHandlerContext ctx ) {
        System.out.println( "Channel registered: " + ctx.channel().remoteAddress() );
        PacketHandler.callEvent( new ChannelRegisteredEvent( ctx.channel() ) );
    }

    @Override
    public void channelUnregistered ( ChannelHandlerContext ctx ) {
        System.out.println( "Channel unregistered: " + ctx.channel().remoteAddress() );
        PacketHandler.callEvent( new ChannelUnregisteredEvent( ctx.channel() ) );
    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx, Throwable e ) {
        ExceptionCaughtEvent exceptionCaughtEvent = new ExceptionCaughtEvent( e, true );
        PacketHandler.callEvent( exceptionCaughtEvent );
        if ( !exceptionCaughtEvent.isCancelled() ) e.printStackTrace();
    }
}
