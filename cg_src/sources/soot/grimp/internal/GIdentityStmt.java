package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JIdentityStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GIdentityStmt.class */
public class GIdentityStmt extends JIdentityStmt {
    public GIdentityStmt(Value local, Value identityValue) {
        super(Grimp.v().newLocalBox(local), Grimp.v().newIdentityRefBox(identityValue));
    }

    @Override // soot.jimple.internal.JIdentityStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GIdentityStmt(Grimp.cloneIfNecessary(getLeftOp()), Grimp.cloneIfNecessary(getRightOp()));
    }
}
