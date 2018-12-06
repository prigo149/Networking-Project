package com.websystem.core.packets.impls;

import com.websystem.core.packets.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConnectionResultPacket extends Packet {

    private boolean success;
    private String error, title;
}
