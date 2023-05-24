package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/IntType.class */
public class IntType extends PrimType implements IntegerType {
    public static final int HASHCODE = -1220074593;

    public IntType(Singletons.Global g) {
    }

    public static IntType v() {
        return G.v().soot_IntType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return HASHCODE;
    }

    @Override // soot.Type
    public String toString() {
        return "int";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseIntType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_INT32;
        }
        return JavaBasicTypes.JAVA_LANG_INTEGER;
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
