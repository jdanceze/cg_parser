package org.powermock.core.spi;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/NewInvocationControl.class */
public interface NewInvocationControl<T> extends DefaultBehavior {
    Object invoke(Class<?> cls, Object[] objArr, Class<?>[] clsArr) throws Exception;

    T expectSubstitutionLogic(Object... objArr) throws Exception;
}
