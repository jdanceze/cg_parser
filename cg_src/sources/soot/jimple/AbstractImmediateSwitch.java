package soot.jimple;

import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/jimple/AbstractImmediateSwitch.class */
public abstract class AbstractImmediateSwitch<T> extends AbstractConstantSwitch<T> implements ImmediateSwitch {
    @Override // soot.jimple.ImmediateSwitch
    public void caseLocal(Local v) {
        defaultCase(v);
    }
}
