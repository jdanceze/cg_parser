package soot.shimple;

import soot.Unit;
import soot.Value;
import soot.toolkits.scalar.ValueUnitPair;
/* loaded from: gencallgraphv3.jar:soot/shimple/PiExpr.class */
public interface PiExpr extends ShimpleExpr {
    ValueUnitPair getArgBox();

    Value getValue();

    Unit getCondStmt();

    Object getTargetKey();

    void setValue(Value value);

    void setCondStmt(Unit unit);

    void setTargetKey(Object obj);
}
