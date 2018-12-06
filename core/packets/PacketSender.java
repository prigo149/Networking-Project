package com.websystem.core.packets;

import com.websystem.core.packets.impls.PingPacket;
import com.websystem.core.packets.queue.PacketQueue;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PacketSender implements Runnable {

    private Channel channel;

    public void run () {
        while ( true ) {
            PacketQueue.PacketSend packetSend = PacketQueue.getSendingQueue().next();
            if ( packetSend == null ) continue;
            if ( !( packetSend.getPacket() instanceof PingPacket ) ) System.out.println( "Sending packet " + packetSend.getPacket().getClass().getSimpleName() + " (" + PacketQueue.getSendingQueue().size() + " remaining in queue)" );
            ( packetSend.getChannel() != null ? packetSend.getChannel() : channel ).writeAndFlush( packetSend.getPacket() );
        }
    }
}