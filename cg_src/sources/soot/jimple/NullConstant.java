package soot.jimple;

import soot.G;
import soot.NullType;
import soot.Singletons;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/NullConstant.class */
public class NullConstant extends Constant {
    public NullConstant(Singletons.Global g) {
    }

    public static NullConstant v() {
        return G.v().soot_jimple_NullConstant();
    }

    public boolean equals(Object c) {
        return c == G.v().soot_jimple_NullConstant();
    }

    public int hashCode() {
        return 982;
    }

    public String toString() {
        return Jimple.NULL;
    }

    @Override // soot.Value
    public Type getType() {
        return NullType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseNullConstant(this);
    }
}
