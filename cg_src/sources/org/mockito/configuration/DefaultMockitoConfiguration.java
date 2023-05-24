package org.mockito.configuration;

import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/configuration/DefaultMockitoConfiguration.class */
public class DefaultMockitoConfiguration implements IMockitoConfiguration {
    @Override // org.mockito.configuration.IMockitoConfiguration
    public Answer<Object> getDefaultAnswer() {
        return new ReturnsEmptyValues();
    }

    @Override // org.mockito.configuration.IMockitoConfiguration
    public AnnotationEngine getAnnotationEngine() {
        return new InjectingAnnotationEngine();
    }

    @Override // org.mockito.configuration.IMockitoConfiguration
    public boolean cleansStackTrace() {
        return true;
    }

    @Override // org.mockito.configuration.IMockitoConfiguration
    public boolean enableClassCache() {
        return true;
    }
}
