package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/DoubleType.class */
public class DoubleType extends PrimType {
    public static final int HASHCODE = 1268609602;

    public DoubleType(Singletons.Global g) {
    }

    public static DoubleType v() {
        return G.v().soot_DoubleType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return HASHCODE;
    }

    @Override // soot.Type
    public String toString() {
        return "double";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseDoubleType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_DOUBLE;
        }
        return JavaBasicTypes.JAVA_LANG_DOUBLE;
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Double.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Double.TYPE;
    }
}
