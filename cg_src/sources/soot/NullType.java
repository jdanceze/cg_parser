package soot;

import soot.Singletons;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/NullType.class */
public class NullType extends RefLikeType {
    public NullType(Singletons.Global g) {
    }

    public static NullType v() {
        return G.v().soot_NullType();
    }

    public int hashCode() {
        return -1735270431;
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.Type
    public String toString() {
        return Jimple.NULL_TYPE;
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseNullType(this);
    }

    @Override // soot.RefLikeType
    public Type getArrayElementType() {
        throw new RuntimeException("Attempt to get array base type of a non-array");
    }
}
