package com.websystem.server;

import com.websystem.core.configuration.ConfigurationLoader;
import com.websystem.core.logging.LogManager;
import com.websystem.core.netty.client.NettyClient;
import com.websystem.core.packets.handler.PacketHandler;
import com.websystem.core.packets.impls.ConnectionPacket;
import com.websystem.server.listener.ConnectionListener;
import com.websystem.server.manager.PingManager;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.util.Properties;

public class Server {

    @Getter
    private static Properties serverConfig;
    @Getter
    private static NettyClient nettyClient;

    @SneakyThrows
    public static void main ( String[] args ) {
        /** Logging **/
        LogManager.init();

        /** Configuration **/
        System.out.println( "Checking Webserver Config..." );
        serverConfig = ConfigurationLoader.loadConfiguration( "config", "configs/webserver" );
        if ( getServerConfig() == null ) {
            System.out.println( "Webserver Config is missing or empty" );
            System.out.println( "Shutdown." );
            return;
        } else {
            System.out.println( "Webserver Config... OK" );
        }

        /** Listener **/
        PacketHandler.registerListener( new ConnectionListener() );

        /** Ping Manager **/
        new PingManager().start();

        /** Netty Connection **/
        System.out.println( "Initializing Netty Client" );
        nettyClient = new NettyClient( getServerConfig().getProperty( "host" ), Integer.parseInt( getServerConfig().getProperty( "port" ) ) );
        System.out.println( "Starting Netty Client" );
        nettyClient.start();

        nettyClient.write( new ConnectionPacket( InetAddress.getLocalHost().getHostName(), Integer.parseInt( getServerConfig().getProperty( "weight" ) ) ) );
    }
}
