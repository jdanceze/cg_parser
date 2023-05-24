package org.mockito.internal.configuration.injection.filter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/filter/OngoingInjector.class */
public interface OngoingInjector {
    public static final OngoingInjector nop = new OngoingInjector() { // from class: org.mockito.internal.configuration.injection.filter.OngoingInjector.1
        @Override // org.mockito.internal.configuration.injection.filter.OngoingInjector
        public Object thenInject() {
            return null;
        }
    };

    Object thenInject();
}
