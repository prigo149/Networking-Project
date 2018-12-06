package com.websystem.core.packets.events;

import com.websystem.core.packets.listener.Cancellable;
import com.websystem.core.packets.listener.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionCaughtEvent implements Event, Cancellable {

    private Throwable cause;
    private boolean cancelled;
}
