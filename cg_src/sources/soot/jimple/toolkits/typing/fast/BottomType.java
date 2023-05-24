package soot.jimple.toolkits.typing.fast;

import soot.G;
import soot.Singletons;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/BottomType.class */
public class BottomType extends Type {
    public static BottomType v() {
        return G.v().soot_jimple_toolkits_typing_fast_BottomType();
    }

    public BottomType(Singletons.Global g) {
    }

    @Override // soot.Type
    public String toString() {
        return "bottom_type";
    }

    public boolean equals(Object t) {
        return this == t;
    }
}
