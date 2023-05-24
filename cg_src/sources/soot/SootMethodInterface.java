package soot;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/SootMethodInterface.class */
public interface SootMethodInterface {
    SootClass getDeclaringClass();

    String getName();

    List<Type> getParameterTypes();

    Type getParameterType(int i);

    Type getReturnType();

    boolean isStatic();

    String getSignature();
}
