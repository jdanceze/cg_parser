package soot.jimple.infoflow.android.iccta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/IccInstrumentDestination.class */
public class IccInstrumentDestination {
    private static IccInstrumentDestination s = null;
    private static RefType INTENT_TYPE = RefType.v("android.content.Intent");
    private IccLink iccLink = null;

    private IccInstrumentDestination() {
    }

    public static IccInstrumentDestination v() {
        if (s == null) {
            s = new IccInstrumentDestination();
        }
        return s;
    }

    public SootClass instrumentDestinationForContentProvider(String destination) {
        return Scene.v().getSootClass(destination);
    }

    public SootMethod generateInitMethod(SootClass compSootClass, SootField intentSootField) {
        List<Type> parameters = new ArrayList<>();
        parameters.add(INTENT_TYPE);
        Type returnType = VoidType.v();
        SootMethod newConstructor = new SootMethod("<init>", parameters, returnType, 1);
        compSootClass.addMethod(newConstructor);
        Body b = Jimple.v().newBody(newConstructor);
        newConstructor.setActiveBody(b);
        LocalGenerator lg = Scene.v().createLocalGenerator(b);
        Local thisLocal = lg.generateLocal(compSootClass.getType());
        IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newThisRef(compSootClass.getType()));
        Value intentParameterLocal = lg.generateLocal(INTENT_TYPE);
        IdentityStmt newIdentityStmt2 = Jimple.v().newIdentityStmt(intentParameterLocal, Jimple.v().newParameterRef(INTENT_TYPE, 0));
        boolean noDefaultConstructMethod = false;
        InvokeStmt invokeStmt = null;
        try {
            invokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(thisLocal, compSootClass.getMethod("<init>", new ArrayList(), VoidType.v()).makeRef()));
        } catch (Exception e) {
            noDefaultConstructMethod = true;
        }
        if (noDefaultConstructMethod) {
            List<SootMethod> sootMethods = compSootClass.getMethods();
            for (SootMethod sm : sootMethods) {
                if (sm.getName().equals("<init>")) {
                    if (sm.getParameterCount() == 1 && sm.getParameterType(0).equals(INTENT_TYPE)) {
                        List<Value> args = new ArrayList<>();
                        args.add(intentParameterLocal);
                        invokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(thisLocal, sm.makeRef(), args));
                    } else {
                        List<Value> args2 = new ArrayList<>();
                        for (int i = 0; i < sm.getParameterCount(); i++) {
                            args2.add(NullConstant.v());
                        }
                        invokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(thisLocal, sm.makeRef(), args2));
                    }
                }
            }
        }
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(intentSootField.makeRef()), intentParameterLocal);
        b.getUnits().add((UnitPatchingChain) newIdentityStmt);
        b.getUnits().add((UnitPatchingChain) newIdentityStmt2);
        b.getUnits().add((UnitPatchingChain) invokeStmt);
        b.getUnits().add((UnitPatchingChain) newAssignStmt);
        b.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        return newConstructor;
    }

    public SootField getMessageForIPCField() {
        SootClass sc = this.iccLink.fromSM.getDeclaringClass();
        if (!sc.declaresField("message_for_ipc_static", RefType.v("android.os.Messenge"))) {
            fieldSendingMessage(this.iccLink.fromSM);
        }
        return sc.getFieldByName("message_for_ipc_static");
    }

    public Type extractBinderType(SootClass sootClass) {
        SootMethod onBindMethod = null;
        try {
            onBindMethod = sootClass.getMethodByName("onBind");
        } catch (RuntimeException e) {
        }
        if (onBindMethod == null) {
            return null;
        }
        Body body = onBindMethod.retrieveActiveBody();
        PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> iter = units.snapshotIterator();
        while (iter.hasNext()) {
            Stmt stmt = (Stmt) iter.next();
            if (stmt instanceof ReturnStmt) {
                ReturnStmt rtStmt = (ReturnStmt) stmt;
                Value rtValue = rtStmt.getOp();
                if (rtValue.toString().equals(Jimple.NULL)) {
                    return onBindMethod.getReturnType();
                }
                return rtValue.getType();
            }
        }
        return onBindMethod.getReturnType();
    }

    public void fieldSendingMessage(SootMethod fromSM) {
        SootClass fromC = fromSM.getDeclaringClass();
        if (fromC.declaresField("message_for_ipc_static", RefType.v("android.os.Message"))) {
            return;
        }
        SootField sf = Scene.v().makeSootField("message_for_ipc_static", RefType.v("android.os.Message"), 9);
        fromC.addField(sf);
        for (SootMethod sm : fromC.getMethods()) {
            if (sm.isConcrete()) {
                Body body = sm.retrieveActiveBody();
                PatchingChain<Unit> units = body.getUnits();
                Iterator<Unit> iter = units.snapshotIterator();
                while (iter.hasNext()) {
                    Stmt stmt = (Stmt) iter.next();
                    if (stmt.containsInvokeExpr()) {
                        SootMethod toBeCheckedMethod = stmt.getInvokeExpr().getMethod();
                        if (toBeCheckedMethod.getName().equals("send") && equalsOrSubclassOf(toBeCheckedMethod.getDeclaringClass(), Scene.v().getSootClass("android.os.Messenger"))) {
                            Value arg0 = stmt.getInvokeExpr().getArg(0);
                            Unit assignUnit = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(sf.makeRef()), arg0);
                            assignUnit.addTag(SimulatedCodeElementTag.TAG);
                            units.insertBefore(assignUnit, (Unit) stmt);
                        }
                    }
                }
            }
        }
    }

    public boolean equalsOrSubclassOf(SootClass testClass, SootClass parentClass) {
        if (testClass.getName().equals(parentClass.getName())) {
            return true;
        }
        if (testClass.hasSuperclass()) {
            return equalsOrSubclassOf(testClass.getSuperclass(), parentClass);
        }
        return false;
    }
}
