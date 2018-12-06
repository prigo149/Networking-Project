package com.websystem.core.netty.client;

import com.websystem.core.netty.handler.ChannelHandler;
import com.websystem.core.packets.Packet;
import com.websystem.core.packets.PacketSender;
import com.websystem.core.packets.queue.PacketQueue;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.*;

@RequiredArgsConstructor
@Getter
public class NettyClient extends Thread {

    @NonNull
    private String host;
    @NonNull
    private int port;

    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;
    private boolean connected, error;

    @SneakyThrows
    public void run () {
        workerGroup = new NioEventLoopGroup();

        try {
            bootstrap = new Bootstrap()
                    .group( workerGroup )
                    .channel( NioSocketChannel.class )
                    .option( ChannelOption.SO_KEEPALIVE, true )
                    .handler( new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel ( SocketChannel ch ) {
                            ch.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder( ClassResolvers.cacheDisabled( ClassLoader.getSystemClassLoader() ) ),
                                    new ChannelHandler() );
                        }
                    } );

            channelFuture = bootstrap.connect( host, port ).sync();

            connected = true;

            new PacketSender( channelFuture.channel() ).run();
        } catch ( Exception e ) {
            error = true;
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void write ( Packet packet ) {
        PacketQueue.getSendingQueue().add( new PacketQueue.PacketSend( packet ) );
    }
}
