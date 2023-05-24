package soot.jimple;

import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/AssignStmt.class */
public interface AssignStmt extends DefinitionStmt {
    void setLeftOp(Value value);

    void setRightOp(Value value);
}
