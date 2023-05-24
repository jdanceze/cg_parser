package soot.jimple.infoflow.android.callbacks.xml;

import java.util.Set;
import soot.SootClass;
import soot.jimple.infoflow.android.callbacks.AndroidCallbackDefinition;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/xml/CollectedCallbacks.class */
public class CollectedCallbacks {
    protected Set<SootClass> entryPoints;
    protected MultiMap<SootClass, AndroidCallbackDefinition> callbackMethods;
    protected MultiMap<SootClass, SootClass> fragmentClasses;

    public CollectedCallbacks() {
    }

    public CollectedCallbacks(Set<SootClass> entryPoints, MultiMap<SootClass, AndroidCallbackDefinition> callbackMethods, MultiMap<SootClass, SootClass> fragmentClasses) {
        this.entryPoints = entryPoints;
        this.callbackMethods = callbackMethods;
        this.fragmentClasses = fragmentClasses;
    }

    public Set<SootClass> getEntryPoints() {
        return this.entryPoints;
    }

    public MultiMap<SootClass, AndroidCallbackDefinition> getCallbackMethods() {
        return this.callbackMethods;
    }

    public MultiMap<SootClass, SootClass> getFragmentClasses() {
        return this.fragmentClasses;
    }
}
