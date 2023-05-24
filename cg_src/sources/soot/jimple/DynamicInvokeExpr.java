package soot.jimple;

import java.util.List;
import soot.SootMethodRef;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/DynamicInvokeExpr.class */
public interface DynamicInvokeExpr extends InvokeExpr {
    SootMethodRef getBootstrapMethodRef();

    List<Value> getBootstrapArgs();

    Value getBootstrapArg(int i);

    int getBootstrapArgCount();

    int getHandleTag();
}
