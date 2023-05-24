package soot;

import java.util.List;
import soot.util.NumberedString;
/* loaded from: gencallgraphv3.jar:soot/SootMethodRef.class */
public interface SootMethodRef extends SootMethodInterface {
    @Deprecated
    SootClass declaringClass();

    @Deprecated
    String name();

    @Deprecated
    List<Type> parameterTypes();

    @Deprecated
    Type returnType();

    @Deprecated
    Type parameterType(int i);

    NumberedString getSubSignature();

    SootMethod resolve();

    SootMethod tryResolve();

    default boolean isConstructor() {
        return getReturnType() == VoidType.v() && "<init>".equals(getName());
    }
}
