package soot.javaToJimple;

import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Receiver;
import polyglot.ast.Stmt;
import polyglot.ast.Unary;
import soot.Local;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import soot.jimple.JimpleBody;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/AbstractJimpleBodyBuilder.class */
public abstract class AbstractJimpleBodyBuilder {
    protected JimpleBody body;
    private AbstractJimpleBodyBuilder ext = null;
    private AbstractJimpleBodyBuilder base = this;

    public void ext(AbstractJimpleBodyBuilder ext) {
        this.ext = ext;
        if (ext.ext != null) {
            throw new RuntimeException("Extensions created in wrong order.");
        }
        ext.base = this.base;
    }

    public AbstractJimpleBodyBuilder ext() {
        if (this.ext == null) {
            return this;
        }
        return this.ext;
    }

    public void base(AbstractJimpleBodyBuilder base) {
        this.base = base;
    }

    public AbstractJimpleBodyBuilder base() {
        return this.base;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JimpleBody createJimpleBody(Block block, List formals, SootMethod sootMethod) {
        return ext().createJimpleBody(block, formals, sootMethod);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value createAggressiveExpr(Expr expr, boolean reduceAggressively, boolean reverseCondIfNec) {
        return ext().createAggressiveExpr(expr, reduceAggressively, reverseCondIfNec);
    }

    protected void createStmt(Stmt stmt) {
        ext().createStmt(stmt);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean needsAccessor(Expr expr) {
        return ext().needsAccessor(expr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Local handlePrivateFieldAssignSet(Assign assign) {
        return ext().handlePrivateFieldAssignSet(assign);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Local handlePrivateFieldUnarySet(Unary unary) {
        return ext().handlePrivateFieldUnarySet(unary);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value getAssignRightLocal(Assign assign, Local leftLocal) {
        return ext().getAssignRightLocal(assign, leftLocal);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value getSimpleAssignRightLocal(Assign assign) {
        return ext().getSimpleAssignRightLocal(assign);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Local handlePrivateFieldSet(Expr expr, Value right, Value base) {
        return ext().handlePrivateFieldSet(expr, right, base);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootMethodRef getSootMethodRef(Call call) {
        return ext().getSootMethodRef(call);
    }

    protected Local generateLocal(Type sootType) {
        return ext().generateLocal(sootType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Local generateLocal(polyglot.types.Type polyglotType) {
        return ext().generateLocal(polyglotType);
    }

    protected Local getThis(Type sootType) {
        return ext().getThis(sootType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value getBaseLocal(Receiver receiver) {
        return ext().getBaseLocal(receiver);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value createLHS(Expr expr) {
        return ext().createLHS(expr);
    }

    protected FieldRef getFieldRef(Field field) {
        return ext().getFieldRef(field);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Constant getConstant(Type sootType, int val) {
        return ext().getConstant(sootType, val);
    }
}
