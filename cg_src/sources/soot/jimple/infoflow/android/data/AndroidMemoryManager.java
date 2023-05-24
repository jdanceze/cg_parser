package soot.jimple.infoflow.android.data;

import java.util.Set;
import soot.RefType;
import soot.SootClass;
import soot.Type;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.FlowDroidMemoryManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/AndroidMemoryManager.class */
public class AndroidMemoryManager extends FlowDroidMemoryManager {
    private final Set<SootClass> components;
    private boolean componentFilterApplied;

    public AndroidMemoryManager(boolean tracingEnabled, FlowDroidMemoryManager.PathDataErasureMode erasePathData, Set<SootClass> components) {
        super(tracingEnabled, erasePathData);
        this.componentFilterApplied = false;
        this.components = components;
    }

    @Override // soot.jimple.infoflow.data.FlowDroidMemoryManager
    public Abstraction handleMemoryObject(Abstraction obj) {
        Abstraction obj2 = super.handleMemoryObject(obj);
        if (obj2 != null && obj2.getAccessPath().getTaintSubFields()) {
            if (obj2.getAccessPath().isLocal()) {
                Type tp = obj2.getAccessPath().getPlainValue().getType();
                Type runtimeType = obj2.getAccessPath().getBaseType();
                if (isComponentType(tp) || isComponentType(runtimeType) || isFilteredSystemType(tp) || isFilteredSystemType(runtimeType)) {
                    this.componentFilterApplied = true;
                    return null;
                }
            }
            if (obj2.getAccessPath().isInstanceFieldRef()) {
                Type tp2 = obj2.getAccessPath().getLastField().getType();
                Type runtimeType2 = obj2.getAccessPath().getLastFieldType();
                if (isComponentType(tp2) || isComponentType(runtimeType2) || isFilteredSystemType(tp2) || isFilteredSystemType(runtimeType2)) {
                    this.componentFilterApplied = true;
                    return null;
                }
            }
        }
        return obj2;
    }

    private boolean isFilteredSystemType(Type tp) {
        return false;
    }

    private boolean isComponentType(Type tp) {
        if (tp != null && (tp instanceof RefType)) {
            RefType rt = (RefType) tp;
            if (this.components.contains(rt.getSootClass())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean getComponentFilterApplied() {
        return this.componentFilterApplied;
    }
}
