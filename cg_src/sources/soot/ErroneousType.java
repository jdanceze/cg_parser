package soot;

import soot.Singletons;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/ErroneousType.class */
public class ErroneousType extends Type {
    public ErroneousType(Singletons.Global g) {
    }

    public static ErroneousType v() {
        return G.v().soot_ErroneousType();
    }

    public int hashCode() {
        return -1840824321;
    }

    public boolean equals(Object t) {
        return this == t;
    }

    @Override // soot.Type
    public String toString() {
        return "<error>";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseErroneousType(this);
    }
}
