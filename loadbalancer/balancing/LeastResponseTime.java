package com.websystem.loadbalancer.balancing;

import com.websystem.loadbalancer.manager.WebserverManager;
import com.websystem.loadbalancer.models.Webserver;
import lombok.Getter;

import java.util.TreeMap;

public class LeastResponseTime extends Method {

    @Getter
    private final int id = 3;

    @Override
    public Webserver next () {
        TreeMap<Long, Webserver> currentPings = new TreeMap<>(  );
        WebserverManager.getWebservers().forEach( webserver -> currentPings.put( webserver.getPing(), webserver ) );
        
        return currentPings.firstEntry().getValue();
    }
}
