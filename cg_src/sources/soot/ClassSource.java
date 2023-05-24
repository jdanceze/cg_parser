package soot;

import soot.javaToJimple.IInitialResolver;
/* loaded from: gencallgraphv3.jar:soot/ClassSource.class */
public abstract class ClassSource {
    protected final String className;

    public abstract IInitialResolver.Dependencies resolve(SootClass sootClass);

    public ClassSource(String className) {
        if (className == null) {
            throw new IllegalStateException("Error: The class name must not be null.");
        }
        this.className = className;
    }

    public void close() {
    }
}
