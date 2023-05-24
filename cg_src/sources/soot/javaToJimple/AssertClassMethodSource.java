package soot.javaToJimple;

import java.util.ArrayList;
import soot.Body;
import soot.Local;
import soot.MethodSource;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.UnitPatchingChain;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Expr;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.ThrowStmt;
import soot.tagkit.ThrowCreatedByCompilerTag;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/AssertClassMethodSource.class */
public class AssertClassMethodSource implements MethodSource {
    @Override // soot.MethodSource
    public Body getBody(SootMethod sootMethod, String phaseName) {
        Body classBody = Jimple.v().newBody(sootMethod);
        ParameterRef paramRef = Jimple.v().newParameterRef(RefType.v("java.lang.String"), 0);
        Local paramLocal = Jimple.v().newLocal("$r0", RefType.v("java.lang.String"));
        classBody.getLocals().add(paramLocal);
        classBody.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, paramRef));
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(RefType.v("java.lang.String"));
        SootMethodRef methodToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.Class"), "forName", paramTypes, RefType.v("java.lang.Class"), true);
        Local invokeLocal = Jimple.v().newLocal("$r1", RefType.v("java.lang.Class"));
        classBody.getLocals().add(invokeLocal);
        ArrayList params = new ArrayList();
        params.add(paramLocal);
        Expr invokeExpr = Jimple.v().newStaticInvokeExpr(methodToInvoke, params);
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(invokeLocal, invokeExpr);
        classBody.getUnits().add((UnitPatchingChain) newAssignStmt);
        ReturnStmt newReturnStmt = Jimple.v().newReturnStmt(invokeLocal);
        classBody.getUnits().add((UnitPatchingChain) newReturnStmt);
        Local catchRefLocal = Jimple.v().newLocal("$r2", RefType.v("java.lang.ClassNotFoundException"));
        classBody.getLocals().add(catchRefLocal);
        CaughtExceptionRef caughtRef = Jimple.v().newCaughtExceptionRef();
        IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(catchRefLocal, caughtRef);
        classBody.getUnits().add((UnitPatchingChain) newIdentityStmt);
        Local noClassDefLocal = Jimple.v().newLocal("$r3", RefType.v("java.lang.NoClassDefFoundError"));
        classBody.getLocals().add(noClassDefLocal);
        Expr newExpr = Jimple.v().newNewExpr(RefType.v("java.lang.NoClassDefFoundError"));
        classBody.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(noClassDefLocal, newExpr));
        SootMethodRef initMethToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.NoClassDefFoundError"), "<init>", new ArrayList(), VoidType.v(), false);
        Expr initInvoke = Jimple.v().newSpecialInvokeExpr(noClassDefLocal, initMethToInvoke, new ArrayList());
        classBody.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(initInvoke));
        Local throwLocal = Jimple.v().newLocal("$r4", RefType.v("java.lang.Throwable"));
        classBody.getLocals().add(throwLocal);
        ArrayList paramTypes2 = new ArrayList();
        paramTypes2.add(RefType.v("java.lang.Throwable"));
        ArrayList params2 = new ArrayList();
        params2.add(catchRefLocal);
        SootMethodRef messageMethToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.Throwable"), "initCause", paramTypes2, RefType.v("java.lang.Throwable"), false);
        Expr messageInvoke = Jimple.v().newVirtualInvokeExpr(noClassDefLocal, messageMethToInvoke, params2);
        classBody.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(throwLocal, messageInvoke));
        ThrowStmt newThrowStmt = Jimple.v().newThrowStmt(throwLocal);
        newThrowStmt.addTag(new ThrowCreatedByCompilerTag());
        classBody.getUnits().add((UnitPatchingChain) newThrowStmt);
        Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.ClassNotFoundException"), newAssignStmt, newReturnStmt, newIdentityStmt);
        classBody.getTraps().add(trap);
        return classBody;
    }
}
