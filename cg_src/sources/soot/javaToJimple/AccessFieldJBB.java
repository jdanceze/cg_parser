package soot.javaToJimple;

import java.util.ArrayList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Unary;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Type;
import soot.UnitPatchingChain;
import soot.Value;
import soot.javaToJimple.jj.ast.JjAccessField_c;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/AccessFieldJBB.class */
public class AccessFieldJBB extends AbstractJimpleBodyBuilder {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public boolean needsAccessor(Expr expr) {
        if (expr instanceof JjAccessField_c) {
            return true;
        }
        return ext().needsAccessor(expr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Local handlePrivateFieldUnarySet(Unary unary) {
        BinopExpr binExpr;
        if (unary.expr() instanceof JjAccessField_c) {
            JjAccessField_c accessField = (JjAccessField_c) unary.expr();
            Local baseLocal = (Local) base().getBaseLocal(accessField.field().target());
            Local fieldGetLocal = handleCall(accessField.field(), accessField.getMeth(), null, baseLocal);
            Local tmp = base().generateLocal(accessField.field().type());
            AssignStmt stmt1 = Jimple.v().newAssignStmt(tmp, fieldGetLocal);
            ext().body.getUnits().add((UnitPatchingChain) stmt1);
            Util.addLnPosTags(stmt1, unary.position());
            Value incVal = base().getConstant(Util.getSootType(accessField.field().type()), 1);
            if (unary.operator() == Unary.PRE_INC || unary.operator() == Unary.POST_INC) {
                binExpr = Jimple.v().newAddExpr(tmp, incVal);
            } else {
                binExpr = Jimple.v().newSubExpr(tmp, incVal);
            }
            Local tmp2 = generateLocal(accessField.field().type());
            AssignStmt assign = Jimple.v().newAssignStmt(tmp2, binExpr);
            ext().body.getUnits().add((UnitPatchingChain) assign);
            if (unary.operator() == Unary.PRE_INC || unary.operator() == Unary.PRE_DEC) {
                return base().handlePrivateFieldSet(accessField, tmp2, baseLocal);
            }
            base().handlePrivateFieldSet(accessField, tmp2, baseLocal);
            return tmp;
        }
        return ext().handlePrivateFieldUnarySet(unary);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Local handlePrivateFieldAssignSet(Assign assign) {
        if (assign.left() instanceof JjAccessField_c) {
            JjAccessField_c accessField = (JjAccessField_c) assign.left();
            Local baseLocal = (Local) base().getBaseLocal(accessField.field().target());
            if (assign.operator() == Assign.ASSIGN) {
                Value right = base().getSimpleAssignRightLocal(assign);
                return base().handlePrivateFieldSet(accessField, right, baseLocal);
            }
            Local leftLocal = handleCall(accessField.field(), accessField.getMeth(), null, baseLocal);
            Value right2 = base().getAssignRightLocal(assign, leftLocal);
            return handleFieldSet(accessField, right2, baseLocal);
        }
        return ext().handlePrivateFieldAssignSet(assign);
    }

    private Local handleCall(Field field, Call call, Value param, Local base) {
        InvokeExpr invoke;
        Type sootRecType = Util.getSootType(call.target().type());
        SootClass receiverTypeClass = Scene.v().getSootClass(Scene.v().getObjectType().toString());
        if (sootRecType instanceof RefType) {
            receiverTypeClass = ((RefType) sootRecType).getSootClass();
        }
        SootMethodRef methToCall = base().getSootMethodRef(call);
        List<Value> params = new ArrayList<>();
        if (param != null) {
            params.add(param);
        }
        Local baseLocal = base;
        if (base == null) {
            baseLocal = (Local) ext().getBaseLocal(call.target());
        }
        if (methToCall.isStatic()) {
            invoke = Jimple.v().newStaticInvokeExpr(methToCall, params);
        } else if (Modifier.isInterface(receiverTypeClass.getModifiers()) && call.methodInstance().flags().isAbstract()) {
            invoke = Jimple.v().newInterfaceInvokeExpr(baseLocal, methToCall, params);
        } else {
            invoke = Jimple.v().newVirtualInvokeExpr(baseLocal, methToCall, params);
        }
        Local retLocal = base().generateLocal(field.type());
        AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, invoke);
        ext().body.getUnits().add((UnitPatchingChain) assignStmt);
        return retLocal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Local handlePrivateFieldSet(Expr expr, Value right, Value baseLocal) {
        if (expr instanceof JjAccessField_c) {
            JjAccessField_c accessField = (JjAccessField_c) expr;
            return handleCall(accessField.field(), accessField.setMeth(), right, null);
        }
        return ext().handlePrivateFieldSet(expr, right, baseLocal);
    }

    private Local handleFieldSet(JjAccessField_c accessField, Value right, Local base) {
        return handleCall(accessField.field(), accessField.setMeth(), right, base);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value createAggressiveExpr(Expr expr, boolean redAgg, boolean revIfNec) {
        if (expr instanceof JjAccessField_c) {
            JjAccessField_c accessField = (JjAccessField_c) expr;
            return handleCall(accessField.field(), accessField.getMeth(), null, null);
        }
        return ext().createAggressiveExpr(expr, redAgg, revIfNec);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value createLHS(Expr expr) {
        if (expr instanceof JjAccessField_c) {
            JjAccessField_c accessField = (JjAccessField_c) expr;
            return handleCall(accessField.field(), accessField.getMeth(), null, null);
        }
        return ext().createLHS(expr);
    }
}
