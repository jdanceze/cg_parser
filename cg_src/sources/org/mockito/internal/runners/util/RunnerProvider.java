package org.mockito.internal.runners.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.mockito.internal.runners.InternalRunner;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/runners/util/RunnerProvider.class */
public class RunnerProvider {
    public InternalRunner newInstance(String runnerClassName, Object... constructorArgs) throws Exception {
        try {
            Class<?> runnerClass = Class.forName(runnerClassName);
            if (runnerClass.getConstructors().length != 1) {
                throw new IllegalArgumentException("Expected " + runnerClassName + " to have exactly one constructor.");
            }
            Constructor<?> constructor = runnerClass.getConstructors()[0];
            try {
                return (InternalRunner) constructor.newInstance(constructorArgs);
            } catch (InvocationTargetException e) {
                throw e;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        } catch (Exception e3) {
            throw new RuntimeException(e3);
        }
    }
}
