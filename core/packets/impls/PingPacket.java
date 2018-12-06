package com.websystem.core.packets.impls;

import com.websystem.core.packets.Packet;
import lombok.Getter;

public class PingPacket extends Packet {

    @Getter
    private long sent = System.currentTimeMillis();
}
