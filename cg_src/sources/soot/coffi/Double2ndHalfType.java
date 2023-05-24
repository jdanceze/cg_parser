package soot.coffi;

import soot.G;
import soot.Singletons;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/coffi/Double2ndHalfType.class */
public class Double2ndHalfType extends Type {
    public Double2ndHalfType(Singletons.Global g) {
    }

    public static Double2ndHalfType v() {
        return G.v().soot_coffi_Double2ndHalfType();
    }

    public boolean equals(Type otherType) {
        return otherType instanceof Double2ndHalfType;
    }

    @Override // soot.Type
    public String toString() {
        return "double2ndhalf";
    }
}
