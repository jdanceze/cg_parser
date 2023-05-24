package soot.dava.toolkits.base.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JExitMonitorStmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.Chain;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/misc/ThrowFinder.class */
public class ThrowFinder {
    private HashSet<SootMethod> registeredMethods;
    private HashMap<Stmt, HashSet<SootClass>> protectionSet;
    private static final Logger logger = LoggerFactory.getLogger(ThrowFinder.class);
    public static boolean DEBUG = false;

    public ThrowFinder(Singletons.Global g) {
    }

    public static ThrowFinder v() {
        return G.v().soot_dava_toolkits_base_misc_ThrowFinder();
    }

    public void find() {
        CallGraph cg;
        logger.debug("Verifying exception handling.. ");
        this.registeredMethods = new HashSet<>();
        this.protectionSet = new HashMap<>();
        if (Scene.v().hasCallGraph()) {
            cg = Scene.v().getCallGraph();
        } else {
            new CallGraphBuilder().build();
            cg = Scene.v().getCallGraph();
            Scene.v().releaseCallGraph();
        }
        IterableSet worklist = new IterableSet();
        logger.debug("\b. ");
        for (SootClass sootClass : Scene.v().getApplicationClasses()) {
            Iterator<SootMethod> methodIt = sootClass.methodIterator();
            while (methodIt.hasNext()) {
                SootMethod m = methodIt.next();
                register_AreasOfProtection(m);
                worklist.add(m);
            }
        }
        HashMap<SootClass, IterableSet> subClassSet = new HashMap<>();
        HashMap<SootClass, IterableSet> superClassSet = new HashMap<>();
        HashSet<SootClass> applicationClasses = new HashSet<>();
        applicationClasses.addAll(Scene.v().getApplicationClasses());
        for (SootClass c : Scene.v().getApplicationClasses()) {
            IterableSet superClasses = superClassSet.get(c);
            if (superClasses == null) {
                superClasses = new IterableSet();
                superClassSet.put(c, superClasses);
            }
            IterableSet subClasses = subClassSet.get(c);
            if (subClasses == null) {
                IterableSet subClasses2 = new IterableSet();
                subClassSet.put(c, subClasses2);
            }
            if (c.hasSuperclass()) {
                SootClass superClass = c.getSuperclass();
                IterableSet superClassSubClasses = subClassSet.get(superClass);
                if (superClassSubClasses == null) {
                    superClassSubClasses = new IterableSet();
                    subClassSet.put(superClass, superClassSubClasses);
                }
                superClassSubClasses.add(c);
                superClasses.add(superClass);
            }
            for (SootClass interfaceClass : c.getInterfaces()) {
                IterableSet interfaceClassSubClasses = subClassSet.get(interfaceClass);
                if (interfaceClassSubClasses == null) {
                    interfaceClassSubClasses = new IterableSet();
                    subClassSet.put(interfaceClass, interfaceClassSubClasses);
                }
                interfaceClassSubClasses.add(c);
                superClasses.add(interfaceClass);
            }
        }
        HashMap<SootMethod, IterableSet> agreementMethodSet = new HashMap<>();
        Iterator worklistIt = worklist.iterator();
        while (worklistIt.hasNext()) {
            SootMethod m2 = (SootMethod) worklistIt.next();
            if (!m2.isAbstract() && !m2.isNative()) {
                List<SootClass> exceptionList = m2.getExceptions();
                IterableSet exceptionSet = new IterableSet(exceptionList);
                boolean changed = false;
                Iterator it = m2.retrieveActiveBody().getUnits().iterator();
                while (it.hasNext()) {
                    Unit u = it.next();
                    HashSet handled = this.protectionSet.get(u);
                    if (u instanceof ThrowStmt) {
                        Type t = ((ThrowStmt) u).getOp().getType();
                        if (t instanceof RefType) {
                            SootClass c2 = ((RefType) t).getSootClass();
                            if (!handled_Exception(handled, c2) && !exceptionSet.contains(c2)) {
                                PatchingChain list = m2.retrieveActiveBody().getUnits();
                                Unit pred = list.getPredOf((PatchingChain) u);
                                if (!(pred instanceof JExitMonitorStmt)) {
                                    exceptionSet.add(c2);
                                    changed = true;
                                    if (DEBUG) {
                                        System.out.println("Added exception which is explicitly thrown" + c2.getName());
                                    }
                                } else if (DEBUG) {
                                    System.out.println("Found monitor exit" + pred + " hence not adding");
                                }
                            }
                        }
                    }
                }
                Iterator it2 = cg.edgesOutOf(m2);
                while (it2.hasNext()) {
                    Edge e = it2.next();
                    Stmt callSite = e.srcStmt();
                    if (callSite != null) {
                        HashSet handled2 = this.protectionSet.get(callSite);
                        SootMethod target = e.tgt();
                        for (SootClass exception : target.getExceptions()) {
                            if (!handled_Exception(handled2, exception) && !exceptionSet.contains(exception)) {
                                exceptionSet.add(exception);
                                changed = true;
                            }
                        }
                    }
                }
                if (changed) {
                    exceptionList.clear();
                    exceptionList.addAll(exceptionSet);
                }
            }
            find_OtherMethods(m2, agreementMethodSet, subClassSet, applicationClasses);
            find_OtherMethods(m2, agreementMethodSet, superClassSet, applicationClasses);
        }
        while (!worklist.isEmpty()) {
            SootMethod m3 = (SootMethod) worklist.getFirst();
            worklist.removeFirst();
            IterableSet agreementMethods = agreementMethodSet.get(m3);
            if (agreementMethods != null) {
                Iterator amit = agreementMethods.iterator();
                while (amit.hasNext()) {
                    SootMethod otherMethod = (SootMethod) amit.next();
                    List<SootClass> otherExceptionsList = otherMethod.getExceptions();
                    IterableSet otherExceptionSet = new IterableSet(otherExceptionsList);
                    boolean changed2 = false;
                    for (SootClass exception2 : m3.getExceptions()) {
                        if (!otherExceptionSet.contains(exception2)) {
                            otherExceptionSet.add(exception2);
                            changed2 = true;
                        }
                    }
                    if (changed2) {
                        otherExceptionsList.clear();
                        otherExceptionsList.addAll(otherExceptionSet);
                        if (!worklist.contains(otherMethod)) {
                            worklist.addLast(otherMethod);
                        }
                    }
                }
            }
            Iterator it3 = cg.edgesOutOf(m3);
            while (it3.hasNext()) {
                Edge e2 = it3.next();
                Stmt callingSite = e2.srcStmt();
                if (callingSite != null) {
                    SootMethod callingMethod = e2.src();
                    List<SootClass> exceptionList2 = callingMethod.getExceptions();
                    IterableSet exceptionSet2 = new IterableSet(exceptionList2);
                    HashSet handled3 = this.protectionSet.get(callingSite);
                    boolean changed3 = false;
                    for (SootClass exception3 : m3.getExceptions()) {
                        if (!handled_Exception(handled3, exception3) && !exceptionSet2.contains(exception3)) {
                            exceptionSet2.add(exception3);
                            changed3 = true;
                        }
                    }
                    if (changed3) {
                        exceptionList2.clear();
                        exceptionList2.addAll(exceptionSet2);
                        if (!worklist.contains(callingMethod)) {
                            worklist.addLast(callingMethod);
                        }
                    }
                }
            }
        }
    }

    private void find_OtherMethods(SootMethod startingMethod, HashMap<SootMethod, IterableSet> methodMapping, HashMap<SootClass, IterableSet> classMapping, HashSet<SootClass> applicationClasses) {
        IterableSet worklist = (IterableSet) classMapping.get(startingMethod.getDeclaringClass()).clone();
        HashSet<SootClass> touchSet = new HashSet<>();
        touchSet.addAll(worklist);
        String signature = startingMethod.getSubSignature();
        while (!worklist.isEmpty()) {
            SootClass currentClass = (SootClass) worklist.getFirst();
            worklist.removeFirst();
            if (applicationClasses.contains(currentClass)) {
                if (currentClass.declaresMethod(signature)) {
                    IterableSet otherMethods = methodMapping.get(startingMethod);
                    if (otherMethods == null) {
                        otherMethods = new IterableSet();
                        methodMapping.put(startingMethod, otherMethods);
                    }
                    otherMethods.add(currentClass.getMethod(signature));
                } else {
                    IterableSet otherClasses = classMapping.get(currentClass);
                    if (otherClasses != null) {
                        Iterator ocit = otherClasses.iterator();
                        while (ocit.hasNext()) {
                            SootClass otherClass = (SootClass) ocit.next();
                            if (!touchSet.contains(otherClass)) {
                                worklist.addLast(otherClass);
                                touchSet.add(otherClass);
                            }
                        }
                    }
                }
            }
        }
    }

    private void register_AreasOfProtection(SootMethod m) {
        if (this.registeredMethods.contains(m)) {
            return;
        }
        this.registeredMethods.add(m);
        if (!m.hasActiveBody()) {
            return;
        }
        Body b = m.getActiveBody();
        Chain stmts = b.getUnits();
        for (Trap t : b.getTraps()) {
            SootClass exception = t.getException();
            Iterator sit = stmts.iterator(t.getBeginUnit(), stmts.getPredOf(t.getEndUnit()));
            while (sit.hasNext()) {
                Stmt s = (Stmt) sit.next();
                HashSet<SootClass> hashSet = this.protectionSet.get(s);
                HashSet<SootClass> handled = hashSet;
                if (hashSet == null) {
                    handled = new HashSet<>();
                    this.protectionSet.put(s, handled);
                }
                if (!handled.contains(exception)) {
                    handled.add(exception);
                }
            }
        }
    }

    private boolean handled_Exception(HashSet handledExceptions, SootClass c) {
        SootClass thrownException = c;
        if (is_HandledByRuntime(thrownException)) {
            return true;
        }
        if (handledExceptions == null) {
            return false;
        }
        while (!handledExceptions.contains(thrownException)) {
            if (!thrownException.hasSuperclass()) {
                return false;
            }
            thrownException = thrownException.getSuperclass();
        }
        return true;
    }

    private boolean is_HandledByRuntime(SootClass c) {
        SootClass runtimeException = Scene.v().getSootClass("java.lang.RuntimeException");
        SootClass error = Scene.v().getSootClass("java.lang.Error");
        for (SootClass thrownException = c; thrownException != runtimeException && thrownException != error; thrownException = thrownException.getSuperclass()) {
            if (!thrownException.hasSuperclass()) {
                return false;
            }
        }
        return true;
    }
}
