package soot;

import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/ShortType.class */
public class ShortType extends PrimType implements IntegerType {
    public static final int HASHCODE = -1954447917;

    public ShortType(Singletons.Global g) {
    }

    public static ShortType v() {
        return G.v().soot_ShortType();
    }

    public int hashCode() {
        return HASHCODE;
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.Type
    public String toString() {
        return "short";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseShortType(this);
    }

    @Override // soot.PrimType
    public String getTypeAsString() {
        if (Options.v().src_prec() == 7) {
            return DotnetBasicTypes.SYSTEM_INT16;
        }
        return JavaBasicTypes.JAVA_LANG_SHORT;
    }

    @Override // soot.PrimType
    public Class<?> getJavaBoxedType() {
        return Short.class;
    }

    @Override // soot.PrimType
    public Class<?> getJavaPrimitiveType() {
        return Short.TYPE;
    }
}
