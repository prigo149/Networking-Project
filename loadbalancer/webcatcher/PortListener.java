package com.websystem.loadbalancer.webcatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;

@RequiredArgsConstructor
public class PortListener extends Thread {

    @NonNull
    private int port;
    private ServerSocket serverSocket;

    @SneakyThrows
    public void run () {
        serverSocket = new ServerSocket( port );

        while ( !interrupted() ) {
            try {
                Socket connectionSocket = serverSocket.accept();
                System.out.println( "Received new connection on port " + port );
                System.out.println( "Trying to forward connection on port " + port );
                new SocketForwarder( connectionSocket, port ).start();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }
}
