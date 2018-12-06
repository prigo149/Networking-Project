package com.websystem.core.packets.queue;

import com.websystem.core.packets.Packet;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketQueue {

    @Getter
    private static PacketQueue sendingQueue = new PacketQueue();

    @Getter
    private BlockingQueue<PacketSend> queue = new LinkedBlockingQueue<>();

    public boolean add ( PacketSend packetSend ) {
        return queue.offer( packetSend );
    }

    public PacketSend next () {
        try {
            return queue.take();
        } catch ( InterruptedException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public int size () {
        return queue.size();
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class PacketSend {
        private Channel channel;
        @NonNull
        private Packet packet;
    }
}
