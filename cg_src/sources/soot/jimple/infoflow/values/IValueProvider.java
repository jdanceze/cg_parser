package soot.jimple.infoflow.values;

import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/values/IValueProvider.class */
public interface IValueProvider {
    <T> T getValue(SootMethod sootMethod, Stmt stmt, Value value, Class<T> cls);
}
