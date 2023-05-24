package soot.shimple;

import soot.jimple.AbstractExprSwitch;
/* loaded from: gencallgraphv3.jar:soot/shimple/AbstractShimpleExprSwitch.class */
public abstract class AbstractShimpleExprSwitch<T> extends AbstractExprSwitch<T> implements ShimpleExprSwitch {
    @Override // soot.shimple.ShimpleExprSwitch
    public void casePhiExpr(PhiExpr v) {
        defaultCase(v);
    }
}
