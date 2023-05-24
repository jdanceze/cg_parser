package soot.jimple.infoflow.android.callbacks.filters;

import soot.jimple.infoflow.android.callbacks.ComponentReachableMethods;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/filters/AbstractCallbackFilter.class */
public abstract class AbstractCallbackFilter implements ICallbackFilter {
    protected ComponentReachableMethods reachableMethods = null;

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public void setReachableMethods(ComponentReachableMethods rm) {
        this.reachableMethods = rm;
    }
}
