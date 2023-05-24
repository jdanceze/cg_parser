package soot.jimple.infoflow.typing;

import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/typing/ITypeChecker.class */
public interface ITypeChecker {
    Type getMorePreciseType(Type type, Type type2);
}
