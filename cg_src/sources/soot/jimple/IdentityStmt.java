package soot.jimple;

import soot.IdentityUnit;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/IdentityStmt.class */
public interface IdentityStmt extends DefinitionStmt, IdentityUnit {
    void setLeftOp(Value value);

    void setRightOp(Value value);
}
