package com.example.spring.boot2.simple.security.v6.core.parameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.PrioritizedParameterNameDiscoverer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 2022-07-08.
 */
public class DefaultSecurityParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSecurityParameterNameDiscoverer.class);

    private static final String DATA_PARAM_CLASSNAME = "org.springframework.data.repository.query.Param";
    private static final boolean DATA_PARAM_PRESENT = ClassUtils.isPresent(DATA_PARAM_CLASSNAME,
        DefaultSecurityParameterNameDiscoverer.class.getClassLoader());

    public DefaultSecurityParameterNameDiscoverer() {
        this(Collections.<ParameterNameDiscoverer> emptyList());
    }

    public DefaultSecurityParameterNameDiscoverer(List<? extends ParameterNameDiscoverer> parameterNameDiscovers) {
        Assert.notNull(parameterNameDiscovers, "parameterNameDiscovers cannot be null");
        for (ParameterNameDiscoverer discover : parameterNameDiscovers) {
            addDiscoverer(discover);
        }

        Set<String> annotationClassesToUse = new HashSet<>(2);
        if (DATA_PARAM_PRESENT) {
            annotationClassesToUse.add(DATA_PARAM_CLASSNAME);
        }

        addDiscoverer(new AnnotationParameterNameDiscoverer(annotationClassesToUse));
        addDiscoverer(new DefaultParameterNameDiscoverer());
    }
}
