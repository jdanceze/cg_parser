package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.scalar.Pair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/UseFinder.class */
public class UseFinder {
    ReachableMethods rm;
    Map<SootClass, List> classToExtFieldAccesses;
    Map<SootClass, ArrayList> classToIntFieldAccesses;
    Map<SootClass, List> classToExtCalls;
    Map<SootClass, ArrayList> classToIntCalls;

    public UseFinder() {
        this.classToExtFieldAccesses = new HashMap();
        this.classToIntFieldAccesses = new HashMap();
        this.classToExtCalls = new HashMap();
        this.classToIntCalls = new HashMap();
        this.rm = Scene.v().getReachableMethods();
        doAnalysis();
    }

    public UseFinder(ReachableMethods rm) {
        this.classToExtFieldAccesses = new HashMap();
        this.classToIntFieldAccesses = new HashMap();
        this.classToExtCalls = new HashMap();
        this.classToIntCalls = new HashMap();
        this.rm = rm;
        doAnalysis();
    }

    public List getExtFieldAccesses(SootClass sc) {
        if (this.classToExtFieldAccesses.containsKey(sc)) {
            return this.classToExtFieldAccesses.get(sc);
        }
        throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
    }

    public List getIntFieldAccesses(SootClass sc) {
        if (this.classToIntFieldAccesses.containsKey(sc)) {
            return this.classToIntFieldAccesses.get(sc);
        }
        throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
    }

    public List getExtCalls(SootClass sc) {
        if (this.classToExtCalls.containsKey(sc)) {
            return this.classToExtCalls.get(sc);
        }
        throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
    }

    public List getIntCalls(SootClass sc) {
        if (this.classToIntCalls.containsKey(sc)) {
            return this.classToIntCalls.get(sc);
        }
        throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
    }

    public List<SootMethod> getExtMethods(SootClass sc) {
        if (this.classToExtCalls.containsKey(sc)) {
            List<Pair> extCalls = this.classToExtCalls.get(sc);
            List<SootMethod> extMethods = new ArrayList<>();
            for (Pair call : extCalls) {
                SootMethod calledMethod = ((Stmt) call.getO2()).getInvokeExpr().getMethod();
                if (!extMethods.contains(calledMethod)) {
                    extMethods.add(calledMethod);
                }
            }
            return extMethods;
        }
        throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
    }

    public List<SootField> getExtFields(SootClass sc) {
        if (this.classToExtFieldAccesses.containsKey(sc)) {
            List<Pair> extAccesses = this.classToExtFieldAccesses.get(sc);
            List<SootField> extFields = new ArrayList<>();
            for (Pair access : extAccesses) {
                SootField accessedField = ((Stmt) access.getO2()).getFieldRef().getField();
                if (!extFields.contains(accessedField)) {
                    extFields.add(accessedField);
                }
            }
            return extFields;
        }
        throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
    }

    private void doAnalysis() {
        Chain appClasses = Scene.v().getApplicationClasses();
        for (SootClass appClass : appClasses) {
            this.classToIntFieldAccesses.put(appClass, new ArrayList());
            this.classToExtFieldAccesses.put(appClass, new ArrayList());
            this.classToIntCalls.put(appClass, new ArrayList());
            this.classToExtCalls.put(appClass, new ArrayList());
        }
        for (SootClass appClass2 : appClasses) {
            for (SootMethod method : appClass2.getMethods()) {
                if (method.isConcrete() && this.rm.contains(method)) {
                    Body b = method.retrieveActiveBody();
                    Iterator unitsIt = b.getUnits().iterator();
                    while (unitsIt.hasNext()) {
                        Stmt s = (Stmt) unitsIt.next();
                        if (s.containsFieldRef()) {
                            FieldRef fr = s.getFieldRef();
                            if (fr.getFieldRef().resolve().getDeclaringClass() == appClass2) {
                                if (fr instanceof StaticFieldRef) {
                                    this.classToIntFieldAccesses.get(appClass2).add(new Pair(method, s));
                                } else if (fr instanceof InstanceFieldRef) {
                                    InstanceFieldRef ifr = (InstanceFieldRef) fr;
                                    if (!method.isStatic() && ifr.getBase().equivTo(b.getThisLocal())) {
                                        this.classToIntFieldAccesses.get(appClass2).add(new Pair(method, s));
                                    } else {
                                        this.classToExtFieldAccesses.get(appClass2).add(new Pair(method, s));
                                    }
                                }
                            } else {
                                List<Pair> otherClassList = this.classToExtFieldAccesses.get(fr.getFieldRef().resolve().getDeclaringClass());
                                if (otherClassList == null) {
                                    otherClassList = new ArrayList<>();
                                    this.classToExtFieldAccesses.put(fr.getFieldRef().resolve().getDeclaringClass(), otherClassList);
                                }
                                otherClassList.add(new Pair(method, s));
                            }
                        }
                        if (s.containsInvokeExpr()) {
                            InvokeExpr ie = s.getInvokeExpr();
                            if (ie.getMethodRef().resolve().getDeclaringClass() == appClass2) {
                                if (ie instanceof StaticInvokeExpr) {
                                    this.classToIntCalls.get(appClass2).add(new Pair(method, s));
                                } else if (ie instanceof InstanceInvokeExpr) {
                                    InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                                    if (!method.isStatic() && iie.getBase().equivTo(b.getThisLocal())) {
                                        this.classToIntCalls.get(appClass2).add(new Pair(method, s));
                                    } else {
                                        this.classToExtCalls.get(appClass2).add(new Pair(method, s));
                                    }
                                }
                            } else {
                                List<Pair> otherClassList2 = this.classToExtCalls.get(ie.getMethodRef().resolve().getDeclaringClass());
                                if (otherClassList2 == null) {
                                    otherClassList2 = new ArrayList<>();
                                    this.classToExtCalls.put(ie.getMethodRef().resolve().getDeclaringClass(), otherClassList2);
                                }
                                otherClassList2.add(new Pair(method, s));
                            }
                        }
                    }
                }
            }
        }
    }
}
