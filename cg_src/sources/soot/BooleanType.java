package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/BooleanType.class */
public class BooleanType extends PrimType implements IntegerType {
    public static final int HASHCODE = 474318298;

    public BooleanType(Singletons.Global g) {
    }

    public static BooleanType v() {
        return G.v().soot_BooleanType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return HASHCODE;
    }

    @Override // soot.Type
    public String toString() {
        return "boolean";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseBooleanType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_BOOLEAN;
        }
        return JavaBasicTypes.JAVA_LANG_BOOLEAN;
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Boolean.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Boolean.TYPE;
    }
}
