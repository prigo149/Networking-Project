package com.websystem.loadbalancer.webcatcher;

import com.websystem.loadbalancer.manager.MethodManager;
import com.websystem.loadbalancer.models.Webserver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.Socket;

@RequiredArgsConstructor
public class SocketForwarder extends Thread {

    @NonNull
    private Socket clientSocket;
    @NonNull
    private int forwardingPort;
    private Socket serverSocket;

    @SneakyThrows
    public void run () {
        System.out.println( "Load Balancing Algorithm: " + MethodManager.CURRENT_METHOD.getClass().getSimpleName() );
        Webserver selectedWebserver = MethodManager.CURRENT_METHOD.next();
        if ( selectedWebserver == null ) {
            System.out.println( "Couldn't find a webserver to forward to with method " + MethodManager.CURRENT_METHOD.getClass().getSimpleName() );
            return;
        }
        System.out.println( "Forwarding connection from port " + forwardingPort + " to webserver #" + selectedWebserver.getId() + " (" + selectedWebserver.getName() + ")" );
        serverSocket = new Socket( selectedWebserver.getHost(), forwardingPort );

        clientSocket.setKeepAlive( true );
        serverSocket.setKeepAlive( true );

        new SocketWriter( serverSocket, clientSocket ).start();
        new SocketWriter( clientSocket, serverSocket ).start();
    }
}
