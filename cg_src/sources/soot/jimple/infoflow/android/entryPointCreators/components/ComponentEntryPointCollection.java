package soot.jimple.infoflow.android.entryPointCreators.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ComponentEntryPointCollection.class */
public class ComponentEntryPointCollection {
    protected Map<SootClass, ComponentEntryPointInfo> componentToEntryPointInfo = new HashMap();

    public void put(SootClass component, ComponentEntryPointInfo info) {
        this.componentToEntryPointInfo.put(component, info);
    }

    public void put(SootClass component, SootMethod lifecycleMethod) {
        this.componentToEntryPointInfo.put(component, new ComponentEntryPointInfo(lifecycleMethod));
    }

    public ComponentEntryPointInfo get(SootClass component) {
        return this.componentToEntryPointInfo.get(component);
    }

    public Collection<SootMethod> getLifecycleMethods() {
        List<SootMethod> list = new ArrayList<>();
        for (ComponentEntryPointInfo info : this.componentToEntryPointInfo.values()) {
            list.add(info.getEntryPoint());
        }
        return list;
    }

    public Collection<SootField> getAdditionalFields() {
        List<SootField> list = new ArrayList<>();
        for (ComponentEntryPointInfo info : this.componentToEntryPointInfo.values()) {
            list.addAll(info.getAdditionalFields());
        }
        return list;
    }

    public SootMethod getEntryPoint(SootClass component) {
        ComponentEntryPointInfo info = this.componentToEntryPointInfo.get(component);
        if (info == null) {
            return null;
        }
        return info.getEntryPoint();
    }

    public void clear() {
        this.componentToEntryPointInfo.clear();
    }

    public boolean hasEntryPointForComponent(SootClass component) {
        return this.componentToEntryPointInfo.containsKey(component);
    }
}
