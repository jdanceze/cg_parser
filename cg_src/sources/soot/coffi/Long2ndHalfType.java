package soot.coffi;

import soot.G;
import soot.Singletons;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/coffi/Long2ndHalfType.class */
public class Long2ndHalfType extends Type {
    public Long2ndHalfType(Singletons.Global g) {
    }

    public static Long2ndHalfType v() {
        return G.v().soot_coffi_Long2ndHalfType();
    }

    public boolean equals(Type otherType) {
        return otherType instanceof Long2ndHalfType;
    }

    @Override // soot.Type
    public String toString() {
        return "long2ndhalf";
    }
}
