package soot.jimple.infoflow.android.callbacks.filters;

import java.util.Set;
import soot.FastHierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.typing.TypeUtils;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/filters/ApplicationCallbackFilter.class */
public class ApplicationCallbackFilter extends AbstractCallbackFilter {
    private final String applicationClass;
    private RefType activityLifecycleCallbacks;
    private RefType provideAssistDataListener;
    private RefType componentCallbacks;
    private RefType componentCallbacks2;

    public ApplicationCallbackFilter(Set<SootClass> entrypoints) {
        this(getApplicationClass(entrypoints));
    }

    private static String getApplicationClass(Set<SootClass> entrypoints) {
        SootClass scApplication = Scene.v().getSootClassUnsafe(AndroidEntryPointConstants.APPLICATIONCLASS);
        for (SootClass sc : entrypoints) {
            if (sc != null && scApplication != null && Scene.v().getOrMakeFastHierarchy().canStoreType(sc.getType(), scApplication.getType())) {
                return sc.getName();
            }
        }
        return null;
    }

    public ApplicationCallbackFilter(String applicationClass) {
        this.applicationClass = applicationClass;
        reset();
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public boolean accepts(SootClass component, SootClass callbackHandler) {
        if (this.applicationClass != null && component.getName().equals(this.applicationClass) && !callbackHandler.getName().equals(this.applicationClass)) {
            FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
            RefType callbackType = callbackHandler.getType();
            if (!TypeUtils.canStoreType(fh, callbackType, this.activityLifecycleCallbacks) && !TypeUtils.canStoreType(fh, callbackType, this.provideAssistDataListener) && !TypeUtils.canStoreType(fh, callbackType, this.componentCallbacks)) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public void reset() {
        this.activityLifecycleCallbacks = getRefTypeUnsafe(AndroidEntryPointConstants.ACTIVITYLIFECYCLECALLBACKSINTERFACE);
        this.provideAssistDataListener = getRefTypeUnsafe("android.app.Application$OnProvideAssistDataListener");
        this.componentCallbacks = getRefTypeUnsafe(AndroidEntryPointConstants.COMPONENTCALLBACKSINTERFACE);
        this.componentCallbacks2 = getRefTypeUnsafe(AndroidEntryPointConstants.COMPONENTCALLBACKS2INTERFACE);
    }

    private RefType getRefTypeUnsafe(String className) {
        SootClass sc = Scene.v().getSootClassUnsafe(className);
        if (sc == null) {
            return null;
        }
        return sc.getType();
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public boolean accepts(SootClass component, SootMethod callback) {
        if (component.getName().equals(this.applicationClass)) {
            return true;
        }
        String subSig = callback.getSubSignature();
        FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
        RefType callbackType = callback.getDeclaringClass().getType();
        if (AndroidEntryPointConstants.getActivityLifecycleCallbackMethods().contains(subSig)) {
            return fh.canStoreType(callbackType, this.activityLifecycleCallbacks);
        }
        if (AndroidEntryPointConstants.getComponentCallbackMethods().contains(subSig)) {
            return fh.canStoreType(callbackType, this.componentCallbacks);
        }
        if (AndroidEntryPointConstants.getComponentCallback2Methods().contains(subSig)) {
            return fh.canStoreType(callbackType, this.componentCallbacks2);
        }
        return true;
    }
}
