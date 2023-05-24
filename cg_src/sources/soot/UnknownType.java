package soot;

import soot.Singletons;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/UnknownType.class */
public class UnknownType extends Type {
    public UnknownType(Singletons.Global g) {
    }

    public static UnknownType v() {
        return G.v().soot_UnknownType();
    }

    public int hashCode() {
        return 1554928471;
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.Type
    public String toString() {
        return "unknown";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseUnknownType(this);
    }

    @Override // soot.Type
    public Type merge(Type other, Scene cm) {
        if (other instanceof RefType) {
            return other;
        }
        throw new RuntimeException("illegal type merge: " + this + " and " + other);
    }
}
