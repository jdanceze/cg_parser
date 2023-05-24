package org.powermock.core.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/MethodInvocationControl.class */
public interface MethodInvocationControl extends InvocationHandler, DefaultBehavior {
    boolean isMocked(Method method);
}
