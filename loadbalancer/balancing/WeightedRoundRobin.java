package com.websystem.loadbalancer.balancing;

import com.websystem.loadbalancer.manager.WebserverManager;
import com.websystem.loadbalancer.models.Webserver;
import lombok.Getter;

public class WeightedRoundRobin extends Method {

    @Getter
    private final int id = 2;

    private Webserver last;
    private int repetitions;

    @Override
    public Webserver next () {
        Webserver next;
        if ( repetitions <= 1 ) {
            next = nextInOrder();
            repetitions = next == null ? 0 : getRepetitions( next.getWeight() );
        } else {
            next = last;
            repetitions--;
        }

        last = next;

        return next;
    }

    private Webserver nextInOrder () {
        Webserver next = null;
        for ( int i = ( last == null ? 0 : last.getId() ) + 1; i < Integer.MAX_VALUE; i++ ) {
            if ( !WebserverManager.existsWebserver( i ) ) break;
            next = WebserverManager.getWebserver( i );
        }
        if ( next == null ) next = WebserverManager.getWebserver( 1 );

        return next;
    }

    private int getRepetitions ( int weight ) {
        return weight / getSmallestWeight();
    }

    private int getSmallestWeight () {
        int smallestWeight = Integer.MAX_VALUE;
        for ( Webserver webserver : WebserverManager.getWebservers() )
            if ( webserver.getWeight() < smallestWeight ) smallestWeight = webserver.getWeight();
        return smallestWeight;
    }
}
