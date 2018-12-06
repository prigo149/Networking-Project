package com.websystem.core.packets.events;

import com.websystem.core.packets.listener.Event;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChannelUnregisteredEvent implements Event {

    private Channel channel;
}
