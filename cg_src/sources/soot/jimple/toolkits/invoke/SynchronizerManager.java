package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/invoke/SynchronizerManager.class */
public class SynchronizerManager {
    public HashMap<SootClass, SootField> classToClassField = new HashMap<>();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SynchronizerManager.class.desiredAssertionStatus();
    }

    public SynchronizerManager(Singletons.Global g) {
    }

    public static SynchronizerManager v() {
        return G.v().soot_jimple_toolkits_invoke_SynchronizerManager();
    }

    public Local addStmtsToFetchClassBefore(JimpleBody b, Stmt target) {
        return addStmtsToFetchClassBefore(b, target, false);
    }

    public Local addStmtsToFetchClassBefore(ShimpleBody b, Stmt target) {
        return addStmtsToFetchClassBefore(b, target, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Local addStmtsToFetchClassBefore(Body b, Stmt target) {
        if ($assertionsDisabled || (b instanceof JimpleBody) || (b instanceof ShimpleBody)) {
            return addStmtsToFetchClassBefore(b, target, b instanceof ShimpleBody);
        }
        throw new AssertionError();
    }

    private Local addStmtsToFetchClassBefore(Body b, Stmt target, boolean createNewAsShimple) {
        String n;
        SootClass sc = b.getMethod().getDeclaringClass();
        SootField classCacher = this.classToClassField.get(sc);
        if (classCacher == null) {
            String str = "class$" + sc.getName().replace('.', '$');
            while (true) {
                n = str;
                if (!sc.declaresFieldByName(n)) {
                    break;
                }
                str = String.valueOf('_') + n;
            }
            classCacher = Scene.v().makeSootField(n, RefType.v("java.lang.Class"), 8);
            sc.addField(classCacher);
            this.classToClassField.put(sc, classCacher);
        }
        Chain<Local> locals = b.getLocals();
        String str2 = "$uniqueClass";
        while (true) {
            String lName = str2;
            boolean oops = false;
            for (Local jbLocal : locals) {
                if (lName.equals(jbLocal.getName())) {
                    oops = true;
                }
            }
            if (oops) {
                str2 = String.valueOf('_') + lName;
            } else {
                Jimple jimp = Jimple.v();
                Local l = jimp.newLocal(lName, RefType.v("java.lang.Class"));
                locals.add(l);
                Chain<Unit> units = b.getUnits();
                units.insertBefore(jimp.newAssignStmt(l, jimp.newStaticFieldRef(classCacher.makeRef())), target);
                units.insertBefore(jimp.newIfStmt(jimp.newNeExpr(l, NullConstant.v()), target), target);
                units.insertBefore(jimp.newAssignStmt(l, jimp.newStaticInvokeExpr(getClassFetcherFor(sc, createNewAsShimple).makeRef(), Collections.singletonList(StringConstant.v(sc.getName())))), target);
                units.insertBefore(jimp.newAssignStmt(jimp.newStaticFieldRef(classCacher.makeRef()), l), target);
                return l;
            }
        }
    }

    public SootMethod getClassFetcherFor(SootClass c) {
        return getClassFetcherFor(c, false);
    }

    public SootMethod getClassFetcherFor(SootClass c, boolean createNewAsShimple) {
        String prefix = String.valueOf('<') + c.getName().replace('.', '$') + ": java.lang.Class ";
        String str = "class$";
        while (true) {
            String methodName = str;
            SootMethod m = c.getMethodByNameUnsafe(methodName);
            if (m == null) {
                return createClassFetcherFor(c, methodName, createNewAsShimple);
            }
            if ((String.valueOf(prefix) + methodName + "(java.lang.String)>").equals(m.getSignature())) {
                Iterator<Unit> unitsIt = m.retrieveActiveBody().getUnits().iterator();
                if (unitsIt.hasNext()) {
                    Stmt s = (Stmt) unitsIt.next();
                    if (s instanceof IdentityStmt) {
                        IdentityStmt is = (IdentityStmt) s;
                        Value ro = is.getRightOp();
                        if ((ro instanceof ParameterRef) && ((ParameterRef) ro).getIndex() == 0 && unitsIt.hasNext()) {
                            Stmt s2 = (Stmt) unitsIt.next();
                            if (s2 instanceof AssignStmt) {
                                AssignStmt as = (AssignStmt) s2;
                                if ((".staticinvoke <java.lang.Class: java.lang.Class forName(java.lang.String)>(" + is.getLeftOp() + ")").equals(as.getRightOp().toString()) && unitsIt.hasNext()) {
                                    Stmt s3 = (Stmt) unitsIt.next();
                                    if ((s3 instanceof ReturnStmt) && ((ReturnStmt) s3).getOp().equivTo(as.getLeftOp())) {
                                        return m;
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            str = String.valueOf('_') + methodName;
        }
    }

    public SootMethod createClassFetcherFor(SootClass c, String methodName) {
        return createClassFetcherFor(c, methodName, false);
    }

    public SootMethod createClassFetcherFor(SootClass c, String methodName, boolean createNewAsShimple) {
        RefType refTyString = RefType.v("java.lang.String");
        RefType refTypeClass = RefType.v("java.lang.Class");
        Scene scene = Scene.v();
        SootMethod method = scene.makeSootMethod(methodName, Collections.singletonList(refTyString), refTypeClass, 8);
        c.addMethod(method);
        Jimple jimp = Jimple.v();
        Body body = jimp.newBody(method);
        RefType refTypeClassNotFoundException = RefType.v("java.lang.ClassNotFoundException");
        RefType refTypeNoClassDefFoundError = RefType.v("java.lang.NoClassDefFoundError");
        Chain<Local> locals = body.getLocals();
        Local l_r0 = jimp.newLocal("r0", refTyString);
        locals.add(l_r0);
        Local l_r1 = jimp.newLocal("r1", refTypeClassNotFoundException);
        locals.add(l_r1);
        Local l_r2 = jimp.newLocal("$r2", refTypeClass);
        locals.add(l_r2);
        Local l_r3 = jimp.newLocal("$r3", refTypeClassNotFoundException);
        locals.add(l_r3);
        Local l_r4 = jimp.newLocal("$r4", refTypeNoClassDefFoundError);
        locals.add(l_r4);
        Local l_r5 = jimp.newLocal("$r5", refTyString);
        locals.add(l_r5);
        Chain<Unit> units = body.getUnits();
        units.add(jimp.newIdentityStmt(l_r0, jimp.newParameterRef(refTyString, 0)));
        AssignStmt asi = jimp.newAssignStmt(l_r2, jimp.newStaticInvokeExpr(scene.getMethod("<java.lang.Class: java.lang.Class forName(java.lang.String)>").makeRef(), Collections.singletonList(l_r0)));
        units.add(asi);
        units.add(jimp.newReturnStmt(l_r2));
        Stmt handlerStart = jimp.newIdentityStmt(l_r3, jimp.newCaughtExceptionRef());
        units.add(handlerStart);
        units.add(jimp.newAssignStmt(l_r1, l_r3));
        units.add(jimp.newAssignStmt(l_r4, jimp.newNewExpr(refTypeNoClassDefFoundError)));
        units.add(jimp.newAssignStmt(l_r5, jimp.newVirtualInvokeExpr(l_r1, scene.getMethod("<java.lang.Throwable: java.lang.String getMessage()>").makeRef(), Collections.emptyList())));
        units.add(jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(l_r4, scene.getMethod("<java.lang.NoClassDefFoundError: void <init>(java.lang.String)>").makeRef(), Collections.singletonList(l_r5))));
        units.add(jimp.newThrowStmt(l_r4));
        body.getTraps().add(jimp.newTrap(refTypeClassNotFoundException.getSootClass(), asi, handlerStart, handlerStart));
        if (createNewAsShimple) {
            body = Shimple.v().newBody(body);
        }
        method.setActiveBody(body);
        return method;
    }

    public void synchronizeStmtOn(Stmt stmt, JimpleBody b, Local lock) {
        synchronizeStmtOn(stmt, (Body) b, lock);
    }

    public void synchronizeStmtOn(Stmt stmt, ShimpleBody b, Local lock) {
        synchronizeStmtOn(stmt, (Body) b, lock);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void synchronizeStmtOn(Stmt stmt, Body b, Local lock) {
        if (!$assertionsDisabled && !(b instanceof JimpleBody) && !(b instanceof ShimpleBody)) {
            throw new AssertionError();
        }
        Jimple jimp = Jimple.v();
        Chain<Unit> units = b.getUnits();
        units.insertBefore(jimp.newEnterMonitorStmt(lock), stmt);
        Stmt exitMon = jimp.newExitMonitorStmt(lock);
        units.insertAfter(exitMon, stmt);
        Stmt newGoto = jimp.newGotoStmt(units.getSuccOf(exitMon));
        units.insertAfter(newGoto, exitMon);
        Local eRef = jimp.newLocal("__exception", Scene.v().getBaseExceptionType());
        b.getLocals().add(eRef);
        List<Unit> l = new ArrayList<>();
        Stmt handlerStmt = jimp.newIdentityStmt(eRef, jimp.newCaughtExceptionRef());
        l.add(handlerStmt);
        l.add((Unit) exitMon.clone());
        l.add(jimp.newThrowStmt(eRef));
        units.insertAfter((List<List<Unit>>) l, (List<Unit>) newGoto);
        b.getTraps().addFirst(jimp.newTrap(Scene.v().getSootClass(Scene.v().getBaseExceptionType().toString()), stmt, units.getSuccOf(stmt), handlerStmt));
    }
}
