package soot.jimple;

import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/DefinitionStmt.class */
public interface DefinitionStmt extends Stmt {
    Value getLeftOp();

    Value getRightOp();

    ValueBox getLeftOpBox();

    ValueBox getRightOpBox();
}
