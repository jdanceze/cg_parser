package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/CharType.class */
public class CharType extends PrimType implements IntegerType {
    public static final int HASHCODE = 1939776628;

    public CharType(Singletons.Global g) {
    }

    public static CharType v() {
        return G.v().soot_CharType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.Type
    public String toString() {
        return "char";
    }

    public int hashCode() {
        return HASHCODE;
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseCharType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_CHAR;
        }
        return JavaBasicTypes.JAVA_LANG_CHARACTER;
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Character.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Character.TYPE;
    }
}
