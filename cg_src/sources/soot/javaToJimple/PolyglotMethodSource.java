package soot.javaToJimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.FieldDecl;
import soot.Body;
import soot.BooleanType;
import soot.Local;
import soot.MethodSource;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.UnitPatchingChain;
import soot.javaToJimple.jj.Topics;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.FieldRef;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/PolyglotMethodSource.class */
public class PolyglotMethodSource implements MethodSource {
    private Block block;
    private List formals;
    private ArrayList<FieldDecl> fieldInits;
    private ArrayList<FieldDecl> staticFieldInits;
    private ArrayList<Block> initializerBlocks;
    private ArrayList<Block> staticInitializerBlocks;
    private Local outerClassThisInit;
    private boolean hasAssert;
    private ArrayList<SootField> finalsList;
    private HashMap newToOuterMap;
    private AbstractJimpleBodyBuilder ajbb;

    public PolyglotMethodSource() {
        this.hasAssert = false;
        this.block = null;
        this.formals = null;
    }

    public PolyglotMethodSource(Block block, List formals) {
        this.hasAssert = false;
        this.block = block;
        this.formals = formals;
    }

    @Override // soot.MethodSource
    public Body getBody(SootMethod sm, String phaseName) {
        JimpleBody jb = this.ajbb.createJimpleBody(this.block, this.formals, sm);
        PackManager.v().getPack(Topics.jj).apply(jb);
        return jb;
    }

    public void setJBB(AbstractJimpleBodyBuilder ajbb) {
        this.ajbb = ajbb;
    }

    public void setFieldInits(ArrayList<FieldDecl> fieldInits) {
        this.fieldInits = fieldInits;
    }

    public void setStaticFieldInits(ArrayList<FieldDecl> staticFieldInits) {
        this.staticFieldInits = staticFieldInits;
    }

    public ArrayList<FieldDecl> getFieldInits() {
        return this.fieldInits;
    }

    public ArrayList<FieldDecl> getStaticFieldInits() {
        return this.staticFieldInits;
    }

    public void setStaticInitializerBlocks(ArrayList<Block> staticInits) {
        this.staticInitializerBlocks = staticInits;
    }

    public void setInitializerBlocks(ArrayList<Block> inits) {
        this.initializerBlocks = inits;
    }

    public ArrayList<Block> getStaticInitializerBlocks() {
        return this.staticInitializerBlocks;
    }

    public ArrayList<Block> getInitializerBlocks() {
        return this.initializerBlocks;
    }

    public void setOuterClassThisInit(Local l) {
        this.outerClassThisInit = l;
    }

    public Local getOuterClassThisInit() {
        return this.outerClassThisInit;
    }

    public boolean hasAssert() {
        return this.hasAssert;
    }

    public void hasAssert(boolean val) {
        this.hasAssert = val;
    }

    public void addAssertInits(Body body) {
        SootClass assertStatusClass = body.getMethod().getDeclaringClass();
        HashMap<SootClass, InnerClassInfo> innerMap = InitialResolver.v().getInnerClassInfoMap();
        while (innerMap != null && innerMap.containsKey(assertStatusClass)) {
            assertStatusClass = innerMap.get(assertStatusClass).getOuterClass();
        }
        String paramName = assertStatusClass.getName();
        String fieldName = "class$" + assertStatusClass.getName().replaceAll(".", "$");
        if (assertStatusClass.isInterface()) {
            assertStatusClass = InitialResolver.v().specialAnonMap().get(assertStatusClass);
        }
        SootFieldRef field = Scene.v().makeFieldRef(assertStatusClass, fieldName, RefType.v("java.lang.Class"), true);
        Local fieldLocal = Jimple.v().newLocal("$r0", RefType.v("java.lang.Class"));
        body.getLocals().add(fieldLocal);
        FieldRef fieldRef = Jimple.v().newStaticFieldRef(field);
        AssignStmt fieldAssignStmt = Jimple.v().newAssignStmt(fieldLocal, fieldRef);
        body.getUnits().add((UnitPatchingChain) fieldAssignStmt);
        ConditionExpr cond = Jimple.v().newNeExpr(fieldLocal, NullConstant.v());
        NopStmt nop1 = Jimple.v().newNopStmt();
        IfStmt ifStmt = Jimple.v().newIfStmt(cond, nop1);
        body.getUnits().add((UnitPatchingChain) ifStmt);
        Local invokeLocal = Jimple.v().newLocal("$r1", RefType.v("java.lang.Class"));
        body.getLocals().add(invokeLocal);
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(RefType.v("java.lang.String"));
        SootMethodRef methodToInvoke = Scene.v().makeMethodRef(assertStatusClass, "class$", paramTypes, RefType.v("java.lang.Class"), true);
        ArrayList params = new ArrayList();
        params.add(StringConstant.v(paramName));
        StaticInvokeExpr invoke = Jimple.v().newStaticInvokeExpr(methodToInvoke, params);
        AssignStmt invokeAssign = Jimple.v().newAssignStmt(invokeLocal, invoke);
        body.getUnits().add((UnitPatchingChain) invokeAssign);
        AssignStmt fieldRefAssign = Jimple.v().newAssignStmt(fieldRef, invokeLocal);
        body.getUnits().add((UnitPatchingChain) fieldRefAssign);
        NopStmt nop2 = Jimple.v().newNopStmt();
        GotoStmt goto1 = Jimple.v().newGotoStmt(nop2);
        body.getUnits().add((UnitPatchingChain) goto1);
        body.getUnits().add((UnitPatchingChain) nop1);
        AssignStmt fieldRefAssign2 = Jimple.v().newAssignStmt(invokeLocal, fieldRef);
        body.getUnits().add((UnitPatchingChain) fieldRefAssign2);
        body.getUnits().add((UnitPatchingChain) nop2);
        Local boolLocal1 = Jimple.v().newLocal("$z0", BooleanType.v());
        body.getLocals().add(boolLocal1);
        Local boolLocal2 = Jimple.v().newLocal("$z1", BooleanType.v());
        body.getLocals().add(boolLocal2);
        SootMethodRef vMethodToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.Class"), "desiredAssertionStatus", new ArrayList(), BooleanType.v(), false);
        VirtualInvokeExpr vInvoke = Jimple.v().newVirtualInvokeExpr(invokeLocal, vMethodToInvoke, new ArrayList());
        AssignStmt testAssign = Jimple.v().newAssignStmt(boolLocal1, vInvoke);
        body.getUnits().add((UnitPatchingChain) testAssign);
        ConditionExpr cond2 = Jimple.v().newNeExpr(boolLocal1, IntConstant.v(0));
        NopStmt nop3 = Jimple.v().newNopStmt();
        IfStmt ifStmt2 = Jimple.v().newIfStmt(cond2, nop3);
        body.getUnits().add((UnitPatchingChain) ifStmt2);
        AssignStmt altAssign = Jimple.v().newAssignStmt(boolLocal2, IntConstant.v(1));
        body.getUnits().add((UnitPatchingChain) altAssign);
        NopStmt nop4 = Jimple.v().newNopStmt();
        GotoStmt goto2 = Jimple.v().newGotoStmt(nop4);
        body.getUnits().add((UnitPatchingChain) goto2);
        body.getUnits().add((UnitPatchingChain) nop3);
        AssignStmt conAssign = Jimple.v().newAssignStmt(boolLocal2, IntConstant.v(0));
        body.getUnits().add((UnitPatchingChain) conAssign);
        body.getUnits().add((UnitPatchingChain) nop4);
        SootFieldRef fieldD = Scene.v().makeFieldRef(body.getMethod().getDeclaringClass(), "$assertionsDisabled", BooleanType.v(), true);
        FieldRef fieldRefD = Jimple.v().newStaticFieldRef(fieldD);
        AssignStmt fAssign = Jimple.v().newAssignStmt(fieldRefD, boolLocal2);
        body.getUnits().add((UnitPatchingChain) fAssign);
    }

    public void setFinalsList(ArrayList<SootField> list) {
        this.finalsList = list;
    }

    public ArrayList<SootField> getFinalsList() {
        return this.finalsList;
    }

    public void setNewToOuterMap(HashMap map) {
        this.newToOuterMap = map;
    }

    public HashMap getNewToOuterMap() {
        return this.newToOuterMap;
    }
}
