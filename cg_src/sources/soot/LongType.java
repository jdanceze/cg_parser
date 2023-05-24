package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/LongType.class */
public class LongType extends PrimType {
    public static final int HASHCODE = 37593207;

    public LongType(Singletons.Global g) {
    }

    public static LongType v() {
        return G.v().soot_LongType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return HASHCODE;
    }

    @Override // soot.Type
    public String toString() {
        return "long";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseLongType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_INT64;
        }
        return JavaBasicTypes.JAVA_LANG_LONG;
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Long.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Long.TYPE;
    }
}
