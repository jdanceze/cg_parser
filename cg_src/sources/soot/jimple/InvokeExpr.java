package soot.jimple;

import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/InvokeExpr.class */
public interface InvokeExpr extends Expr {
    void setMethodRef(SootMethodRef sootMethodRef);

    SootMethodRef getMethodRef();

    SootMethod getMethod();

    List<Value> getArgs();

    Value getArg(int i);

    int getArgCount();

    void setArg(int i, Value value);

    ValueBox getArgBox(int i);
}
