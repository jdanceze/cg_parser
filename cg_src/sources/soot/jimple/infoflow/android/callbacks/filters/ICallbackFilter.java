package soot.jimple.infoflow.android.callbacks.filters;

import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.android.callbacks.ComponentReachableMethods;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/filters/ICallbackFilter.class */
public interface ICallbackFilter {
    boolean accepts(SootClass sootClass, SootClass sootClass2);

    boolean accepts(SootClass sootClass, SootMethod sootMethod);

    void reset();

    void setReachableMethods(ComponentReachableMethods componentReachableMethods);
}
