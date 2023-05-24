package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Hierarchy;
import soot.Local;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.TrapManager;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.InstanceInvokeEdgesPred;
import soot.jimple.toolkits.callgraph.Targets;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.options.SMBOptions;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/invoke/StaticMethodBinder.class */
public class StaticMethodBinder extends SceneTransformer {
    public StaticMethodBinder(Singletons.Global g) {
    }

    public static StaticMethodBinder v() {
        return G.v().soot_jimple_toolkits_invoke_StaticMethodBinder();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> opts) {
        Filter instanceInvokesFilter = new Filter(new InstanceInvokeEdgesPred());
        SMBOptions options = new SMBOptions(opts);
        String modifierOptions = PhaseOptions.getString(opts, "allowed-modifier-changes");
        HashMap<SootMethod, SootMethod> instanceToStaticMap = new HashMap<>();
        Scene scene = Scene.v();
        CallGraph cg = scene.getCallGraph();
        Hierarchy hierarchy = scene.getActiveHierarchy();
        for (SootClass c : scene.getApplicationClasses()) {
            LinkedList<SootMethod> methodsList = new LinkedList<>();
            Iterator<SootMethod> it = c.methodIterator();
            while (it.hasNext()) {
                SootMethod next = it.next();
                methodsList.add(next);
            }
            while (!methodsList.isEmpty()) {
                SootMethod container = methodsList.removeFirst();
                if (container.isConcrete() && instanceInvokesFilter.wrap(cg.edgesOutOf(container)).hasNext()) {
                    Body b = container.getActiveBody();
                    Chain<Unit> bUnits = b.getUnits();
                    Iterator it2 = new ArrayList(bUnits).iterator();
                    while (it2.hasNext()) {
                        Unit u = (Unit) it2.next();
                        Stmt s = (Stmt) u;
                        if (s.containsInvokeExpr()) {
                            InvokeExpr ie = s.getInvokeExpr();
                            if (!(ie instanceof StaticInvokeExpr) && !(ie instanceof SpecialInvokeExpr)) {
                                Targets targets = new Targets(instanceInvokesFilter.wrap(cg.edgesOutOf(s)));
                                if (targets.hasNext()) {
                                    SootMethod target = (SootMethod) targets.next();
                                    if (!targets.hasNext() && AccessManager.ensureAccess(container, target, modifierOptions)) {
                                        SootClass targetDeclClass = target.getDeclaringClass();
                                        if (targetDeclClass.isApplicationClass() && target.isConcrete() && targetDeclClass != scene.getSootClass(Scene.v().getObjectType().toString())) {
                                            if (!instanceToStaticMap.containsKey(target)) {
                                                List<Type> newParameterTypes = new ArrayList<>();
                                                newParameterTypes.add(RefType.v(targetDeclClass.getName()));
                                                newParameterTypes.addAll(target.getParameterTypes());
                                                String newName = target.getName();
                                                do {
                                                    newName = String.valueOf(newName) + "_static";
                                                } while (targetDeclClass.declaresMethod(newName, newParameterTypes, target.getReturnType()));
                                                SootMethod ct = scene.makeSootMethod(newName, newParameterTypes, target.getReturnType(), target.getModifiers() | 8, target.getExceptions());
                                                targetDeclClass.addMethod(ct);
                                                methodsList.addLast(ct);
                                                Body ctBody = (Body) target.getActiveBody().clone();
                                                ct.setActiveBody(ctBody);
                                                Iterator<Unit> oldUnits = target.getActiveBody().getUnits().iterator();
                                                Iterator<Unit> it3 = ctBody.getUnits().iterator();
                                                while (it3.hasNext()) {
                                                    Unit newStmt = it3.next();
                                                    Unit oldStmt = oldUnits.next();
                                                    Iterator<Edge> edges = cg.edgesOutOf(oldStmt);
                                                    while (edges.hasNext()) {
                                                        Edge e = edges.next();
                                                        cg.addEdge(new Edge(ct, newStmt, e.tgt(), e.kind()));
                                                        cg.removeEdge(e);
                                                    }
                                                }
                                                Chain<Unit> ctBodyUnits = ctBody.getUnits();
                                                Iterator<Unit> unitsIt = ctBodyUnits.snapshotIterator();
                                                while (unitsIt.hasNext()) {
                                                    Stmt st = (Stmt) unitsIt.next();
                                                    if (st instanceof IdentityStmt) {
                                                        IdentityStmt is = (IdentityStmt) st;
                                                        Value rightOp = is.getRightOp();
                                                        if (rightOp instanceof ThisRef) {
                                                            Jimple jimp = Jimple.v();
                                                            ctBodyUnits.swapWith(st, jimp.newIdentityStmt(is.getLeftOp(), jimp.newParameterRef(rightOp.getType(), 0)));
                                                        } else if (rightOp instanceof ParameterRef) {
                                                            ParameterRef ro = (ParameterRef) rightOp;
                                                            ro.setIndex(ro.getIndex() + 1);
                                                        }
                                                    }
                                                }
                                                instanceToStaticMap.put(target, ct);
                                            }
                                            Value invokeBase = ((InstanceInvokeExpr) ie).getBase();
                                            Value thisToAdd = invokeBase;
                                            if (options.insert_redundant_casts()) {
                                                SootClass localType = ((RefType) invokeBase.getType()).getSootClass();
                                                if (localType.isInterface() || hierarchy.isClassSuperclassOf(localType, targetDeclClass)) {
                                                    Jimple jimp2 = Jimple.v();
                                                    RefType targetDeclClassType = targetDeclClass.getType();
                                                    Local castee = jimp2.newLocal("__castee", targetDeclClassType);
                                                    b.getLocals().add(castee);
                                                    bUnits.insertBefore(jimp2.newAssignStmt(castee, jimp2.newCastExpr(invokeBase, targetDeclClassType)), s);
                                                    thisToAdd = castee;
                                                }
                                            }
                                            SootMethod clonedTarget = instanceToStaticMap.get(target);
                                            List<Value> newArgs = new ArrayList<>();
                                            newArgs.add(thisToAdd);
                                            newArgs.addAll(ie.getArgs());
                                            s.getInvokeExprBox().setValue(Jimple.v().newStaticInvokeExpr(clonedTarget.makeRef(), newArgs));
                                            cg.addEdge(new Edge(container, s, clonedTarget));
                                            if (options.insert_null_checks()) {
                                                Jimple jimp3 = Jimple.v();
                                                if (TrapManager.isExceptionCaughtAt(scene.getSootClass("java.lang.NullPointerException"), s, b)) {
                                                    IfStmt insertee = jimp3.newIfStmt(jimp3.newNeExpr(invokeBase, NullConstant.v()), s);
                                                    bUnits.insertBefore(insertee, (IfStmt) s);
                                                    insertee.setTarget(s);
                                                    ThrowManager.addThrowAfter(b.getLocals(), bUnits, insertee);
                                                } else {
                                                    bUnits.insertBefore(jimp3.newIfStmt(jimp3.newEqExpr(invokeBase, NullConstant.v()), ThrowManager.getNullPointerExceptionThrower(b)), s);
                                                }
                                            }
                                            if (target.isSynchronized()) {
                                                clonedTarget.setModifiers(clonedTarget.getModifiers() & (-33));
                                                SynchronizerManager.v().synchronizeStmtOn(s, b, (Local) invokeBase);
                                            }
                                            LocalNameStandardizer.v().transform(b, String.valueOf(phaseName) + ".lns");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
