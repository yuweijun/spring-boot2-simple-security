package com.example.spring.boot2.simple.security.v6.core.parameters;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.PrioritizedParameterNameDiscoverer;

/**
 * @since 2022-07-08.
 */
public class DefaultSecurityParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer {

    public DefaultSecurityParameterNameDiscoverer() {
        addDiscoverer(new DefaultParameterNameDiscoverer());
    }

}
