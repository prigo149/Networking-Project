package com.websystem.loadbalancer.manager;

import com.websystem.loadbalancer.LoadBalancer;
import com.websystem.loadbalancer.balancing.Method;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Optional;

public class MethodManager {

    public static final Method CURRENT_METHOD = loadCurrentMethod();

    @SneakyThrows
    private static Method loadCurrentMethod () {
        int searchedMethod = Integer.parseInt( LoadBalancer.getLoadBalancingConfig().getProperty( "load-balancing-method" ) );
        Reflections reflections = new Reflections( "com.websystem.loadbalancer.balancing" );

        Optional<Class<? extends Method>> method = reflections.getSubTypesOf( Method.class ).stream().filter( m -> {
            try {
                return m.newInstance().getId() == searchedMethod;
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return false;
        } ).findAny();

        return method.isPresent() ? method.get().newInstance() : null;
    }
}
