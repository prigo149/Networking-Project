package com.websystem.core.utils;

public abstract class CloudRunnable {

    private boolean cancelled;

    abstract void run ();

    public void cancel () {
    }

    public void runTaskLater ( long duration, boolean thread ) {

    }
}
