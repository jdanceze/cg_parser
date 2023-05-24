package soot.dava.internal.javaRep;

import soot.RefType;
import soot.jimple.ThisRef;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DThisRef.class */
public class DThisRef extends ThisRef {
    public DThisRef(RefType thisType) {
        super(thisType);
    }

    @Override // soot.jimple.ThisRef
    public String toString() {
        return "this: " + getType();
    }

    @Override // soot.jimple.ThisRef, soot.Value
    public Object clone() {
        return new DThisRef((RefType) getType());
    }
}
