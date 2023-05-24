package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.types.MethodInstance;
import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.MethodSource;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.UnitPatchingChain;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/PrivateMethodAccMethodSource.class */
public class PrivateMethodAccMethodSource implements MethodSource {
    private MethodInstance methodInst;

    public PrivateMethodAccMethodSource(MethodInstance methInst) {
        this.methodInst = methInst;
    }

    public void setMethodInst(MethodInstance mi) {
        this.methodInst = mi;
    }

    private boolean isCallParamType(Type sootType) {
        for (polyglot.types.Type type : this.methodInst.formalTypes()) {
            Type compareType = Util.getSootType(type);
            if (compareType.equals(sootType)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.MethodSource
    public Body getBody(SootMethod sootMethod, String phaseName) {
        AssignStmt newInvokeStmt;
        ReturnStmt newReturnVoidStmt;
        Body body = Jimple.v().newBody(sootMethod);
        LocalGenerator lg = Scene.v().createLocalGenerator(body);
        Local base = null;
        ArrayList methParams = new ArrayList();
        ArrayList methParamsTypes = new ArrayList();
        int paramCounter = 0;
        for (Type sootType : sootMethod.getParameterTypes()) {
            Local paramLocal = lg.generateLocal(sootType);
            ParameterRef paramRef = Jimple.v().newParameterRef(sootType, paramCounter);
            body.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, paramRef));
            if (!isCallParamType(sootType)) {
                base = paramLocal;
            } else {
                methParams.add(paramLocal);
                methParamsTypes.add(paramLocal.getType());
            }
            paramCounter++;
        }
        Type type = Util.getSootType(this.methodInst.returnType());
        Local returnLocal = null;
        if (!(type instanceof VoidType)) {
            returnLocal = lg.generateLocal(type);
        }
        SootMethodRef meth = Scene.v().makeMethodRef(((RefType) Util.getSootType(this.methodInst.container())).getSootClass(), this.methodInst.name(), methParamsTypes, Util.getSootType(this.methodInst.returnType()), this.methodInst.flags().isStatic());
        InvokeExpr invoke = this.methodInst.flags().isStatic() ? Jimple.v().newStaticInvokeExpr(meth, methParams) : Jimple.v().newSpecialInvokeExpr(base, meth, methParams);
        if (!(type instanceof VoidType)) {
            newInvokeStmt = Jimple.v().newAssignStmt(returnLocal, invoke);
        } else {
            newInvokeStmt = Jimple.v().newInvokeStmt(invoke);
        }
        body.getUnits().add((UnitPatchingChain) newInvokeStmt);
        if (!(type instanceof VoidType)) {
            newReturnVoidStmt = Jimple.v().newReturnStmt(returnLocal);
        } else {
            newReturnVoidStmt = Jimple.v().newReturnVoidStmt();
        }
        body.getUnits().add((UnitPatchingChain) newReturnVoidStmt);
        return body;
    }
}
