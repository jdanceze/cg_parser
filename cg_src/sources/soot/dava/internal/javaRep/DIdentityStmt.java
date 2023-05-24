package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.internal.GIdentityStmt;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DIdentityStmt.class */
public class DIdentityStmt extends GIdentityStmt {
    public DIdentityStmt(Value local, Value identityValue) {
        super(local, identityValue);
    }

    @Override // soot.jimple.internal.JIdentityStmt, soot.Unit
    public void toString(UnitPrinter up) {
        getLeftOpBox().toString(up);
        up.literal(" := ");
        getRightOpBox().toString(up);
    }

    @Override // soot.jimple.internal.JIdentityStmt
    public String toString() {
        return String.valueOf(getLeftOpBox().getValue().toString()) + " = " + getRightOpBox().getValue().toString();
    }
}
