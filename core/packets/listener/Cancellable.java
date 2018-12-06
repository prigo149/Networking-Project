package com.websystem.core.packets.listener;

public interface Cancellable {

    boolean isCancelled ();

    void setCancelled ( boolean cancelled );
}
