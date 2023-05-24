package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/FloatType.class */
public class FloatType extends PrimType {
    public static final int HASHCODE = -1471974406;

    public FloatType(Singletons.Global g) {
    }

    public static FloatType v() {
        return G.v().soot_FloatType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return HASHCODE;
    }

    @Override // soot.Type
    public String toString() {
        return Jimple.FLOAT;
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseFloatType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_SINGLE;
        }
        return JavaBasicTypes.JAVA_LANG_FLOAT;
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Float.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Float.TYPE;
    }
}
