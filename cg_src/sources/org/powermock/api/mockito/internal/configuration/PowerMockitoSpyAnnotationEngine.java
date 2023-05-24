package org.powermock.api.mockito.internal.configuration;

import java.lang.reflect.Field;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/configuration/PowerMockitoSpyAnnotationEngine.class */
public class PowerMockitoSpyAnnotationEngine extends SpyAnnotationEngine {
    public void process(Class<?> context, Object testClass) {
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                try {
                    Whitebox.invokeMethod(this, Spy.class, field, new Class[]{Mock.class, Mock.class, Captor.class});
                    boolean wasAccessible = field.isAccessible();
                    field.setAccessible(true);
                    try {
                        try {
                            Object instance = field.get(testClass);
                            if (instance == null) {
                                throw new MockitoException("Cannot create a @Spy for '" + field.getName() + "' field because the *instance* is missing\nExample of correct usage of @Spy:\n   @Spy List mock = new LinkedList();\n");
                            }
                            field.set(testClass, PowerMockito.spy(instance));
                            field.setAccessible(wasAccessible);
                        } catch (IllegalAccessException e) {
                            throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                        }
                    } catch (Throwable th) {
                        field.setAccessible(wasAccessible);
                        throw th;
                    }
                } catch (RuntimeException e2) {
                    throw e2;
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }
}
