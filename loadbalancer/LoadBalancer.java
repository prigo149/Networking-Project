package com.websystem.loadbalancer;

import com.websystem.core.configuration.ConfigurationLoader;
import com.websystem.core.logging.LogManager;
import com.websystem.core.netty.server.NettyServer;
import com.websystem.core.packets.handler.PacketHandler;
import com.websystem.loadbalancer.manager.PingManager;
import com.websystem.loadbalancer.manager.WebserverManager;
import com.websystem.loadbalancer.webcatcher.PortListener;
import lombok.Getter;

import java.util.Properties;

public class LoadBalancer {

    @Getter
    private static Properties loadBalancingConfig;
    @Getter
    private static NettyServer nettyServer;

    public static void main ( String[] args ) {
        /** Logging **/
        LogManager.init();

        /** Configuration **/
        System.out.println( "Checking Load Balancing Config..." );
        loadBalancingConfig = ConfigurationLoader.loadConfiguration( "config", "configs/loadbalancer" );
        if ( getLoadBalancingConfig() == null ) {
            System.out.println( "Load Balancing Config is missing or empty" );
            System.out.println( "Shutdown." );
            return;
        } else {
            System.out.println( "Load Balancing Config... OK" );
        }

        /** Listener **/
        PacketHandler.registerListener( new PingManager(), new WebserverManager() );

        /** Netty Connection **/
        System.out.println( "Initializing Netty Server" );
        nettyServer = new NettyServer( Integer.parseInt( getLoadBalancingConfig().getProperty( "port" ) ) );
        System.out.println( "Starting Netty Server on Port " + nettyServer.getPort() );
        nettyServer.start();

        /** Port Listener **/
        new PortListener( 80 ).start();
    }
}
