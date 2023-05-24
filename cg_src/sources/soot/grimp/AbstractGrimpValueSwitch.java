package soot.grimp;

import soot.jimple.AbstractJimpleValueSwitch;
/* loaded from: gencallgraphv3.jar:soot/grimp/AbstractGrimpValueSwitch.class */
public abstract class AbstractGrimpValueSwitch<T> extends AbstractJimpleValueSwitch<T> implements GrimpValueSwitch {
    @Override // soot.grimp.GrimpValueSwitch
    public void caseNewInvokeExpr(NewInvokeExpr e) {
        defaultCase(e);
    }
}
