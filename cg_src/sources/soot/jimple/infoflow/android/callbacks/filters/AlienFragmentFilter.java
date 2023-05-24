package soot.jimple.infoflow.android.callbacks.filters;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/filters/AlienFragmentFilter.class */
public class AlienFragmentFilter extends AbstractCallbackFilter {
    private SootClass fragmentClass;
    private final MultiMap<SootClass, SootClass> fragmentToActivity;

    public AlienFragmentFilter(MultiMap<SootClass, SootClass> fragmentToActivity) {
        this.fragmentToActivity = fragmentToActivity;
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public boolean accepts(SootClass component, SootClass callbackHandler) {
        if (this.fragmentClass != null && Scene.v().getOrMakeFastHierarchy().canStoreType(callbackHandler.getType(), this.fragmentClass.getType()) && !this.fragmentToActivity.get(callbackHandler).contains(component)) {
            return false;
        }
        return true;
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public boolean accepts(SootClass component, SootMethod callback) {
        return true;
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public void reset() {
        this.fragmentClass = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.FRAGMENTCLASS);
    }
}
