package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AssignExpr.class */
public abstract class AssignExpr extends Expr implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public AssignExpr clone() throws CloneNotSupportedException {
        AssignExpr node = (AssignExpr) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean checkDUeverywhere(Variable v) {
        if (getDest().isVariable() && getDest().varDecl() == v && !getSource().isDAafter(v)) {
            return false;
        }
        return super.checkDUeverywhere(v);
    }

    public static Stmt asStmt(Expr left, Expr right) {
        return new ExprStmt(new AssignSimpleExpr(left, right));
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getDest().toString(s);
        s.append(printOp());
        getSource().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getDest().isVariable()) {
            error("left hand side is not a variable");
            return;
        }
        sourceType();
        getDest().type();
        if (getSource().type().isPrimitive() && getDest().type().isPrimitive()) {
            return;
        }
        error("can not assign " + getDest() + " of type " + getDest().type().typeName() + " a value of type " + sourceType().typeName());
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        TypeDecl type;
        TypeDecl dest = getDest().type();
        TypeDecl source = getSource().type();
        if (dest.isNumericType() && source.isNumericType()) {
            type = dest.binaryNumericPromotion(source);
        } else {
            type = dest;
        }
        Value lvalue = getDest().eval(b);
        Value v = lvalue instanceof Local ? lvalue : (Value) lvalue.clone();
        Value value = b.newTemp(dest.emitCastTo(b, v, type, this));
        Value rvalue = source.emitCastTo(b, getSource(), type);
        Value result = asImmediate(b, type.emitCastTo(b, createAssignOp(b, value, rvalue), dest, getDest()));
        getDest().emitStore(b, lvalue, result, this);
        return result;
    }

    public Value emitShiftExpr(Body b) {
        TypeDecl dest = getDest().type();
        TypeDecl source = getSource().type();
        TypeDecl type = dest.unaryNumericPromotion();
        Value lvalue = getDest().eval(b);
        Value v = lvalue instanceof Local ? lvalue : (Value) lvalue.clone();
        Value value = b.newTemp(dest.emitCastTo(b, v, type, getDest()));
        Value rvalue = source.emitCastTo(b, getSource(), typeInt());
        Value result = asImmediate(b, type.emitCastTo(b, createAssignOp(b, value, rvalue), dest, getDest()));
        getDest().emitStore(b, lvalue, result, this);
        return result;
    }

    public Value createAssignOp(Body b, Value fst, Value snd) {
        throw new Error("Operation createAssignOp is not implemented for " + getClass().getName());
    }

    public AssignExpr() {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public AssignExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setDest(Expr node) {
        setChild(node, 0);
    }

    public Expr getDest() {
        return (Expr) getChild(0);
    }

    public Expr getDestNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void setSource(Expr node) {
        setChild(node, 1);
    }

    public Expr getSource() {
        return (Expr) getChild(1);
    }

    public Expr getSourceNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return (getDest().isVariable() && getDest().varDecl() == v) || getSource().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        state();
        return isDAafter(v) || isFalse();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        state();
        return isDAafter(v) || isTrue();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return getSource().isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterTrue(Variable v) {
        state();
        return isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterFalse(Variable v) {
        state();
        return isDUafter(v);
    }

    public String printOp() {
        state();
        return " = ";
    }

    @Override // soot.JastAddJ.Expr
    public TypeDecl type() {
        if (this.type_computed) {
            return this.type_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.type_value = type_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.type_computed = true;
        }
        return this.type_value;
    }

    private TypeDecl type_compute() {
        return getDest().type();
    }

    public TypeDecl sourceType() {
        state();
        return getSource().type().isPrimitive() ? getSource().type() : unknownType();
    }

    @Override // soot.JastAddJ.Expr
    public boolean modifiedInScope(Variable var) {
        state();
        return getDest().isVariable(var);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if (caller == getSourceNoTransform()) {
            return false;
        }
        if (caller == getDestNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isDest(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getSourceNoTransform() || caller == getDestNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getDestNoTransform()) {
            return isDAbefore(v);
        }
        if (caller == getSourceNoTransform()) {
            return getDest().isDAafter(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getDestNoTransform()) {
            return isDUbefore(v);
        }
        if (caller == getSourceNoTransform()) {
            return getDest().isDUafter(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getDestNoTransform()) {
            return NameType.EXPRESSION_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
