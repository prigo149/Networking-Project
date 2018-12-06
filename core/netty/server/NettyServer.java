package com.websystem.core.netty.server;

import com.websystem.core.netty.handler.ChannelHandler;
import com.websystem.core.packets.Packet;
import com.websystem.core.packets.PacketSender;
import com.websystem.core.packets.queue.PacketQueue;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NettyServer extends Thread {

    @NonNull
    private int port;

    private EventLoopGroup bossGroup, workerGroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    public void run () {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group( bossGroup, workerGroup )
                    .channel( NioServerSocketChannel.class )
                    .childHandler( new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel ( SocketChannel ch ) {
                            ch.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder( ClassResolvers.cacheDisabled( getClass().getClassLoader() ) ),
                                    new ChannelHandler() );
                        }
                    } ).option( ChannelOption.SO_BACKLOG, 128 )
                    .childOption( ChannelOption.SO_KEEPALIVE, true );

            channelFuture = serverBootstrap.bind( port ).sync();

            new PacketSender().run();
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void write ( Channel channel, Packet packet ) {
        PacketQueue.getSendingQueue().add( new PacketQueue.PacketSend( channel, packet ) );
    }
}
