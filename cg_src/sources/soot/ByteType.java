package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/ByteType.class */
public class ByteType extends PrimType implements IntegerType {
    public static final int HASHCODE = -2126703831;

    public ByteType(Singletons.Global g) {
    }

    public static ByteType v() {
        return G.v().soot_ByteType();
    }

    public int hashCode() {
        return HASHCODE;
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.Type
    public String toString() {
        return "byte";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseByteType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_BYTE;
        }
        return JavaBasicTypes.JAVA_LANG_BYTE;
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Byte.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Byte.TYPE;
    }
}
