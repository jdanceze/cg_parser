package soot.baf;

import soot.G;
import soot.Singletons;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/DoubleWordType.class */
public class DoubleWordType extends Type {
    public DoubleWordType(Singletons.Global g) {
    }

    public static DoubleWordType v() {
        return G.v().soot_baf_DoubleWordType();
    }

    public boolean equals(Object t) {
        return this == t;
    }

    public int hashCode() {
        return -1572371553;
    }

    @Override // soot.Type
    public String toString() {
        return "dword";
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        throw new RuntimeException("invalid switch case");
    }
}
