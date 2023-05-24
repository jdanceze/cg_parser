package soot.jimple.infoflow.android.callbacks.filters;

import java.util.Set;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/filters/AlienHostComponentFilter.class */
public class AlienHostComponentFilter extends AbstractCallbackFilter {
    private SootClass activityClass;
    private SootClass fragmentClass;
    private final Set<SootClass> components;

    public AlienHostComponentFilter(Set<SootClass> components) {
        this.components = components;
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public boolean accepts(SootClass component, SootClass callbackHandler) {
        if (callbackHandler == null || component == null) {
            return false;
        }
        if (this.activityClass == null || this.fragmentClass == null) {
            reset();
        }
        if (this.fragmentClass != null && this.activityClass != null && Scene.v().getOrMakeFastHierarchy().canStoreType(callbackHandler.getType(), this.fragmentClass.getType()) && !Scene.v().getOrMakeFastHierarchy().canStoreType(component.getType(), this.activityClass.getType())) {
            return false;
        }
        SootClass sootClass = callbackHandler;
        while (true) {
            SootClass curHandler = sootClass;
            if (!curHandler.isInnerClass()) {
                break;
            }
            SootClass outerClass = curHandler.getOuterClass();
            if (this.components.contains(outerClass) && !Scene.v().getOrMakeFastHierarchy().canStoreType(component.getType(), outerClass.getType())) {
                return false;
            }
            if (curHandler == outerClass) {
                break;
            }
            sootClass = outerClass;
        }
        if (this.components.contains(callbackHandler) && callbackHandler != component) {
            return false;
        }
        for (SootMethod cons : callbackHandler.getMethods()) {
            if (cons.isConstructor() && !cons.isPrivate()) {
                boolean isConstructorUsable = true;
                int i = 0;
                while (true) {
                    if (i >= cons.getParameterCount()) {
                        break;
                    }
                    Type paramType = cons.getParameterType(i);
                    if (paramType == component.getType() || !(paramType instanceof RefType) || !this.components.contains(((RefType) paramType).getSootClass())) {
                        i++;
                    } else {
                        isConstructorUsable = false;
                        break;
                    }
                }
                if (!isConstructorUsable) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public void reset() {
        this.activityClass = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.ACTIVITYCLASS);
        this.fragmentClass = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.FRAGMENTCLASS);
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public boolean accepts(SootClass component, SootMethod callback) {
        return true;
    }
}
