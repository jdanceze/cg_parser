package soot.grimp;

import soot.RefType;
import soot.jimple.StaticInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/NewInvokeExpr.class */
public interface NewInvokeExpr extends StaticInvokeExpr {
    RefType getBaseType();

    void setBaseType(RefType refType);
}
