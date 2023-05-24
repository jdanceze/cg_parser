package soot;

import soot.Singletons;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/VoidType.class */
public class VoidType extends Type {
    public VoidType(Singletons.Global g) {
    }

    public static VoidType v() {
        return G.v().soot_VoidType();
    }

    public int hashCode() {
        return 982257717;
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.Type
    public String toString() {
        return Jimple.VOID;
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseVoidType(this);
    }

    @Override // soot.Type
    public boolean isAllowedInFinalCode() {
        return true;
    }
}
