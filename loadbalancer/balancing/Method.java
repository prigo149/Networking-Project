package com.websystem.loadbalancer.balancing;

import com.websystem.loadbalancer.models.Webserver;

public abstract class Method {

    public Method () {
    }

    public abstract int getId ();

    public abstract Webserver next ();
}
