package org.junit.internal.runners.model;

import java.lang.reflect.InvocationTargetException;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/model/ReflectiveCallable.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/model/ReflectiveCallable.class */
public abstract class ReflectiveCallable {
    protected abstract Object runReflectiveCall() throws Throwable;

    public Object run() throws Throwable {
        try {
            return runReflectiveCall();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
