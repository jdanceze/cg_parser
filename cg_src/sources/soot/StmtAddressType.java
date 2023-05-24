package soot;

import soot.Singletons;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/StmtAddressType.class */
public class StmtAddressType extends Type {
    public StmtAddressType(Singletons.Global g) {
    }

    public static StmtAddressType v() {
        return G.v().soot_StmtAddressType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return 1962109137;
    }

    @Override // soot.Type
    public String toString() {
        return "address";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseStmtAddressType(this);
    }
}
