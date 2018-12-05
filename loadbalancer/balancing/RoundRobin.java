package com.websystem.loadbalancer.balancing;

import com.websystem.loadbalancer.manager.WebserverManager;
import com.websystem.loadbalancer.models.Webserver;
import lombok.Getter;

public class RoundRobin extends Method {

    @Getter
    private final int id = 1;

    private int lastId = 0;

    @Override
    public Webserver next () {
        Webserver next = null;
        for ( int i = lastId + 1; i < Integer.MAX_VALUE; i++ ) {
            if ( !WebserverManager.existsWebserver( i ) ) break;
            next = WebserverManager.getWebserver( i );
            break;
        }
        if ( next == null ) next = WebserverManager.getWebserver( 1 );

        if ( next != null ) lastId = next.getId();

        return next;
    }
}
