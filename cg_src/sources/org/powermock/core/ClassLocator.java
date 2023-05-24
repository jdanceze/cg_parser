package org.powermock.core;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/ClassLocator.class */
public class ClassLocator extends SecurityManager {
    public static Class getCallerClass() {
        return new ClassLocator().getClassContext()[5];
    }
}
