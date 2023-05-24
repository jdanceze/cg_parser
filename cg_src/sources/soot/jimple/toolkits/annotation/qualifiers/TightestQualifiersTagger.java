package soot.jimple.toolkits.annotation.qualifiers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.MethodOrMethodContext;
import soot.MethodToContexts;
import soot.Modifier;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/qualifiers/TightestQualifiersTagger.class */
public class TightestQualifiersTagger extends SceneTransformer {
    public static final int RESULT_PUBLIC = 0;
    public static final int RESULT_PACKAGE = 1;
    public static final int RESULT_PROTECTED = 2;
    public static final int RESULT_PRIVATE = 3;
    private final HashMap<SootMethod, Integer> methodResultsMap = new HashMap<>();
    private final HashMap<SootField, Integer> fieldResultsMap = new HashMap<>();
    private MethodToContexts methodToContexts;

    public TightestQualifiersTagger(Singletons.Global g) {
    }

    public static TightestQualifiersTagger v() {
        return G.v().soot_jimple_toolkits_annotation_qualifiers_TightestQualifiersTagger();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map options) {
        handleMethods();
        handleFields();
    }

    private void handleMethods() {
        String actual;
        for (SootClass appClass : Scene.v().getApplicationClasses()) {
            for (SootMethod sm : appClass.getMethods()) {
                if (Scene.v().getReachableMethods().contains(sm)) {
                    analyzeMethod(sm);
                }
            }
        }
        for (SootMethod meth : this.methodResultsMap.keySet()) {
            int result = this.methodResultsMap.get(meth).intValue();
            String sRes = "Public";
            if (result == 0) {
                sRes = "Public";
            } else if (result == 2) {
                sRes = "Protected";
            } else if (result == 1) {
                sRes = "Package";
            } else if (result == 3) {
                sRes = "Private";
            }
            if (Modifier.isPublic(meth.getModifiers())) {
                actual = "Public";
            } else if (Modifier.isProtected(meth.getModifiers())) {
                actual = "Protected";
            } else if (Modifier.isPrivate(meth.getModifiers())) {
                actual = "Private";
            } else {
                actual = "Package";
            }
            if (!sRes.equals(actual)) {
                if (meth.getName().equals("<init>")) {
                    meth.addTag(new StringTag("Constructor: " + meth.getDeclaringClass().getName() + " has " + actual + " level access, can have: " + sRes + " level access.", "Tightest Qualifiers"));
                } else {
                    meth.addTag(new StringTag("Method: " + meth.getName() + " has " + actual + " level access, can have: " + sRes + " level access.", "Tightest Qualifiers"));
                }
                meth.addTag(new ColorTag(255, 10, 0, true, "Tightest Qualifiers"));
            }
        }
    }

    private void analyzeMethod(SootMethod sm) {
        CallGraph cg = Scene.v().getCallGraph();
        if (this.methodToContexts == null) {
            this.methodToContexts = new MethodToContexts(Scene.v().getReachableMethods().listener());
        }
        for (MethodOrMethodContext momc : this.methodToContexts.get(sm)) {
            Iterator callerEdges = cg.edgesInto(momc);
            while (callerEdges.hasNext()) {
                Edge callEdge = callerEdges.next();
                if (callEdge.isExplicit()) {
                    SootMethod methodCaller = callEdge.src();
                    SootClass callingClass = methodCaller.getDeclaringClass();
                    if (Modifier.isPublic(sm.getModifiers())) {
                        analyzePublicMethod(sm, callingClass);
                    } else if (Modifier.isProtected(sm.getModifiers())) {
                        analyzeProtectedMethod(sm, callingClass);
                    } else if (!Modifier.isPrivate(sm.getModifiers())) {
                        analyzePackageMethod(sm, callingClass);
                    }
                }
            }
        }
    }

    private boolean analyzeProtectedMethod(SootMethod sm, SootClass callingClass) {
        SootClass methodClass = sm.getDeclaringClass();
        boolean insidePackageAccess = isCallSamePackage(callingClass, methodClass);
        boolean subClassAccess = isCallClassSubClass(callingClass, methodClass);
        boolean sameClassAccess = isCallClassMethodClass(callingClass, methodClass);
        if (!insidePackageAccess && subClassAccess) {
            this.methodResultsMap.put(sm, new Integer(2));
            return true;
        } else if (insidePackageAccess && !sameClassAccess) {
            updateToPackage(sm);
            return false;
        } else {
            updateToPrivate(sm);
            return false;
        }
    }

    private boolean analyzePackageMethod(SootMethod sm, SootClass callingClass) {
        SootClass methodClass = sm.getDeclaringClass();
        boolean insidePackageAccess = isCallSamePackage(callingClass, methodClass);
        isCallClassSubClass(callingClass, methodClass);
        boolean sameClassAccess = isCallClassMethodClass(callingClass, methodClass);
        if (insidePackageAccess && !sameClassAccess) {
            updateToPackage(sm);
            return true;
        }
        updateToPrivate(sm);
        return false;
    }

    private boolean analyzePublicMethod(SootMethod sm, SootClass callingClass) {
        SootClass methodClass = sm.getDeclaringClass();
        boolean insidePackageAccess = isCallSamePackage(callingClass, methodClass);
        boolean subClassAccess = isCallClassSubClass(callingClass, methodClass);
        boolean sameClassAccess = isCallClassMethodClass(callingClass, methodClass);
        if (!insidePackageAccess && !subClassAccess) {
            this.methodResultsMap.put(sm, new Integer(0));
            return true;
        } else if (!insidePackageAccess && subClassAccess) {
            updateToProtected(sm);
            return false;
        } else if (insidePackageAccess && !sameClassAccess) {
            updateToPackage(sm);
            return false;
        } else {
            updateToPrivate(sm);
            return false;
        }
    }

    private void updateToProtected(SootMethod sm) {
        if (!this.methodResultsMap.containsKey(sm)) {
            this.methodResultsMap.put(sm, new Integer(2));
        } else if (this.methodResultsMap.get(sm).intValue() != 0) {
            this.methodResultsMap.put(sm, new Integer(2));
        }
    }

    private void updateToPackage(SootMethod sm) {
        if (!this.methodResultsMap.containsKey(sm)) {
            this.methodResultsMap.put(sm, new Integer(1));
        } else if (this.methodResultsMap.get(sm).intValue() == 3) {
            this.methodResultsMap.put(sm, new Integer(1));
        }
    }

    private void updateToPrivate(SootMethod sm) {
        if (!this.methodResultsMap.containsKey(sm)) {
            this.methodResultsMap.put(sm, new Integer(3));
        }
    }

    private boolean isCallClassMethodClass(SootClass call, SootClass check) {
        if (call.equals(check)) {
            return true;
        }
        return false;
    }

    private boolean isCallClassSubClass(SootClass call, SootClass check) {
        if (call.hasSuperclass() && call.getSuperclass().equals(check)) {
            return true;
        }
        return false;
    }

    private boolean isCallSamePackage(SootClass call, SootClass check) {
        if (call.getPackageName().equals(check.getPackageName())) {
            return true;
        }
        return false;
    }

    private void handleFields() {
        String actual;
        for (SootClass appClass : Scene.v().getApplicationClasses()) {
            for (SootField sf : appClass.getFields()) {
                analyzeField(sf);
            }
        }
        for (SootField f : this.fieldResultsMap.keySet()) {
            int result = this.fieldResultsMap.get(f).intValue();
            String sRes = "Public";
            if (result == 0) {
                sRes = "Public";
            } else if (result == 2) {
                sRes = "Protected";
            } else if (result == 1) {
                sRes = "Package";
            } else if (result == 3) {
                sRes = "Private";
            }
            if (Modifier.isPublic(f.getModifiers())) {
                actual = "Public";
            } else if (Modifier.isProtected(f.getModifiers())) {
                actual = "Protected";
            } else if (Modifier.isPrivate(f.getModifiers())) {
                actual = "Private";
            } else {
                actual = "Package";
            }
            if (!sRes.equals(actual)) {
                f.addTag(new StringTag("Field: " + f.getName() + " has " + actual + " level access, can have: " + sRes + " level access.", "Tightest Qualifiers"));
                f.addTag(new ColorTag(255, 10, 0, true, "Tightest Qualifiers"));
            }
        }
    }

    private void analyzeField(SootField sf) {
        for (SootClass appClass : Scene.v().getApplicationClasses()) {
            for (SootMethod sm : appClass.getMethods()) {
                if (sm.hasActiveBody() && Scene.v().getReachableMethods().contains(sm)) {
                    Body b = sm.getActiveBody();
                    for (ValueBox vBox : b.getUseBoxes()) {
                        Value v = vBox.getValue();
                        if (v instanceof FieldRef) {
                            FieldRef fieldRef = (FieldRef) v;
                            SootField f = fieldRef.getField();
                            if (!f.equals(sf)) {
                                continue;
                            } else if (Modifier.isPublic(sf.getModifiers())) {
                                if (analyzePublicField(sf, appClass)) {
                                    return;
                                }
                            } else if (Modifier.isProtected(sf.getModifiers())) {
                                analyzeProtectedField(sf, appClass);
                            } else if (!Modifier.isPrivate(sf.getModifiers())) {
                                analyzePackageField(sf, appClass);
                            }
                        }
                    }
                    continue;
                }
            }
        }
    }

    private boolean analyzePublicField(SootField sf, SootClass callingClass) {
        SootClass fieldClass = sf.getDeclaringClass();
        boolean insidePackageAccess = isCallSamePackage(callingClass, fieldClass);
        boolean subClassAccess = isCallClassSubClass(callingClass, fieldClass);
        boolean sameClassAccess = isCallClassMethodClass(callingClass, fieldClass);
        if (!insidePackageAccess && !subClassAccess) {
            this.fieldResultsMap.put(sf, new Integer(0));
            return true;
        } else if (!insidePackageAccess && subClassAccess) {
            updateToProtected(sf);
            return false;
        } else if (insidePackageAccess && !sameClassAccess) {
            updateToPackage(sf);
            return false;
        } else {
            updateToPrivate(sf);
            return false;
        }
    }

    private boolean analyzeProtectedField(SootField sf, SootClass callingClass) {
        SootClass fieldClass = sf.getDeclaringClass();
        boolean insidePackageAccess = isCallSamePackage(callingClass, fieldClass);
        boolean subClassAccess = isCallClassSubClass(callingClass, fieldClass);
        boolean sameClassAccess = isCallClassMethodClass(callingClass, fieldClass);
        if (!insidePackageAccess && subClassAccess) {
            this.fieldResultsMap.put(sf, new Integer(2));
            return true;
        } else if (insidePackageAccess && !sameClassAccess) {
            updateToPackage(sf);
            return false;
        } else {
            updateToPrivate(sf);
            return false;
        }
    }

    private boolean analyzePackageField(SootField sf, SootClass callingClass) {
        SootClass fieldClass = sf.getDeclaringClass();
        boolean insidePackageAccess = isCallSamePackage(callingClass, fieldClass);
        isCallClassSubClass(callingClass, fieldClass);
        boolean sameClassAccess = isCallClassMethodClass(callingClass, fieldClass);
        if (insidePackageAccess && !sameClassAccess) {
            updateToPackage(sf);
            return true;
        }
        updateToPrivate(sf);
        return false;
    }

    private void updateToProtected(SootField sf) {
        if (!this.fieldResultsMap.containsKey(sf)) {
            this.fieldResultsMap.put(sf, new Integer(2));
        } else if (this.fieldResultsMap.get(sf).intValue() != 0) {
            this.fieldResultsMap.put(sf, new Integer(2));
        }
    }

    private void updateToPackage(SootField sf) {
        if (!this.fieldResultsMap.containsKey(sf)) {
            this.fieldResultsMap.put(sf, new Integer(1));
        } else if (this.fieldResultsMap.get(sf).intValue() == 3) {
            this.fieldResultsMap.put(sf, new Integer(1));
        }
    }

    private void updateToPrivate(SootField sf) {
        if (!this.fieldResultsMap.containsKey(sf)) {
            this.fieldResultsMap.put(sf, new Integer(3));
        }
    }
}
