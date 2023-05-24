package soot.shimple;

import soot.jimple.AbstractJimpleValueSwitch;
/* loaded from: gencallgraphv3.jar:soot/shimple/AbstractShimpleValueSwitch.class */
public abstract class AbstractShimpleValueSwitch<T> extends AbstractJimpleValueSwitch<T> implements ShimpleValueSwitch {
    public void casePhiExpr(PhiExpr e) {
        defaultCase(e);
    }
}
