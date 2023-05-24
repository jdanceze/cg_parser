package soot.coffi;

import soot.G;
import soot.Singletons;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/coffi/UnusuableType.class */
public class UnusuableType extends Type {
    public UnusuableType(Singletons.Global g) {
    }

    public static UnusuableType v() {
        return G.v().soot_coffi_UnusuableType();
    }

    public boolean equals(Type otherType) {
        return otherType instanceof UnusuableType;
    }

    @Override // soot.Type
    public String toString() {
        return "unusuable";
    }
}
