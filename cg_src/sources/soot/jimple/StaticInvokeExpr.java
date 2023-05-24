package soot.jimple;

import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/StaticInvokeExpr.class */
public interface StaticInvokeExpr extends InvokeExpr {
    @Override // soot.Value
    Type getType();

    @Override // soot.util.Switchable
    void apply(Switch r1);
}
