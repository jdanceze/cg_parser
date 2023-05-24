package soot.jimple.toolkits.typing.fast;

import soot.BooleanType;
import soot.G;
import soot.IntegerType;
import soot.JavaBasicTypes;
import soot.PrimType;
import soot.Singletons;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/Integer1Type.class */
public class Integer1Type extends PrimType implements IntegerType {
    public static Integer1Type v() {
        return G.v().soot_jimple_toolkits_typing_fast_Integer1Type();
    }

    public Integer1Type(Singletons.Global g) {
    }

    @Override // soot.Type
    public String toString() {
        return "[0..1]";
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.PrimType, soot.Type
    public boolean isAllowedInFinalCode() {
        return false;
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        return JavaBasicTypes.JAVA_LANG_INTEGER;
    }

    @Override // soot.Type
    public Type getDefaultFinalType() {
        return BooleanType.v();
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Integer.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Integer.TYPE;
    }
}
