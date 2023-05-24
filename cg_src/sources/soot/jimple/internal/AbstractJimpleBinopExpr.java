package soot.jimple.internal;

import soot.Value;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractJimpleBinopExpr.class */
public abstract class AbstractJimpleBinopExpr extends AbstractBinopExpr {
    protected AbstractJimpleBinopExpr(Value op1, Value op2) {
        super(Jimple.v().newArgBox(op1), Jimple.v().newArgBox(op2));
    }
}
