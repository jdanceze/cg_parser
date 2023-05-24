package soot.jimple;

import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/jimple/ImmediateSwitch.class */
public interface ImmediateSwitch extends ConstantSwitch {
    void caseLocal(Local local);
}
