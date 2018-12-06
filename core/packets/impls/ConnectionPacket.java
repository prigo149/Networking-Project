package com.websystem.core.packets.impls;

import com.websystem.core.packets.Packet;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConnectionPacket extends Packet {

    private String name;
    private int weight;
}
