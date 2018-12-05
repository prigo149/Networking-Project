package com.websystem.loadbalancer.webcatcher;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.net.SocketException;

@AllArgsConstructor
public class SocketWriter extends Thread {

    private Socket sendingSocket, receivingSocket;

    @SneakyThrows
    public void run () {
        @Cleanup BufferedInputStream inputStream = new BufferedInputStream( sendingSocket.getInputStream() );
        @Cleanup BufferedOutputStream outputStream = new BufferedOutputStream( receivingSocket.getOutputStream() );

        while ( !interrupted() ) {
            try {
                int message;
                if ( sendingSocket.isClosed() || receivingSocket.isClosed() || ( message = inputStream.read() ) == -1 )
                    break;

                outputStream.write( message );
                outputStream.flush();
            } catch ( Exception e ) {
                if ( !( e instanceof SocketException ) && !e.getMessage().equalsIgnoreCase( "Socket closed" ) )e.printStackTrace();
                break;
            }
        }

        closeConnection();
    }

    private void closeConnection () {
        try {
            if ( !sendingSocket.isClosed() ) sendingSocket.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        try {
            if ( !receivingSocket.isClosed() ) receivingSocket.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
