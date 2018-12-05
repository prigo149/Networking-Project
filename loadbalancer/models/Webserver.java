package com.websystem.loadbalancer.models;

import com.websystem.core.packets.Packet;
import com.websystem.core.packets.queue.PacketQueue;
import io.netty.channel.Channel;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
public class Webserver {

    private int id;
    @NonNull
    private String name, host;
    @NonNull
    private int port;
    @NonNull
    private int weight;
    @NonNull
    private Channel channel;
    private long ping;

    public void write ( Packet packet ) {
        PacketQueue.getSendingQueue().add( new PacketQueue.PacketSend( this.channel, packet ) );
    }
}
