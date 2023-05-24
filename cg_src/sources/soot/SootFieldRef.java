package soot;
/* loaded from: gencallgraphv3.jar:soot/SootFieldRef.class */
public interface SootFieldRef {
    SootClass declaringClass();

    String name();

    Type type();

    boolean isStatic();

    String getSignature();

    SootField resolve();
}
