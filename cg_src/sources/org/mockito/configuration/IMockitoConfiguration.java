package org.mockito.configuration;

import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/configuration/IMockitoConfiguration.class */
public interface IMockitoConfiguration {
    Answer<Object> getDefaultAnswer();

    @Deprecated
    AnnotationEngine getAnnotationEngine();

    boolean cleansStackTrace();

    boolean enableClassCache();
}
