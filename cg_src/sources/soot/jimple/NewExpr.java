package soot.jimple;

import soot.RefType;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/NewExpr.class */
public interface NewExpr extends Expr, AnyNewExpr {
    RefType getBaseType();

    void setBaseType(RefType refType);

    @Override // soot.Value
    Type getType();

    @Override // soot.util.Switchable
    void apply(Switch r1);
}
