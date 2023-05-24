package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Unary.class */
public abstract class Unary extends Expr implements Cloneable {
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
    public Unary clone() throws CloneNotSupportedException {
        Unary node = (Unary) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(printPreOp());
        getOperand().toString(s);
        s.append(printPostOp());
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        return super.eval(b);
    }

    public Value emitPostfix(Body b, int constant) {
        Value lvalue = getOperand().eval(b);
        Value v = lvalue instanceof Local ? lvalue : (Value) lvalue.clone();
        TypeDecl type = getOperand().type().binaryNumericPromotion(typeInt());
        Value value = b.newTemp(getOperand().type().emitCastTo(b, v, type, getOperand()));
        Value rvalue = typeInt().emitCastTo(b, IntType.emitConstant(constant), type, this);
        Value sum = asRValue(b, type.emitCastTo(b, b.newAddExpr(asImmediate(b, value), asImmediate(b, rvalue), this), getOperand().type(), this));
        getOperand().emitStore(b, lvalue, sum, this);
        return value;
    }

    public Value emitPrefix(Body b, int constant) {
        Value lvalue = getOperand().eval(b);
        Value v = lvalue instanceof Local ? lvalue : (Value) lvalue.clone();
        TypeDecl type = getOperand().type().binaryNumericPromotion(typeInt());
        Value value = getOperand().type().emitCastTo(b, v, type, getOperand());
        Value rvalue = typeInt().emitCastTo(b, IntType.emitConstant(constant), type, this);
        Value result = asLocal(b, type.emitCastTo(b, b.newAddExpr(asImmediate(b, value), asImmediate(b, rvalue), this), getOperand().type(), this));
        getOperand().emitStore(b, lvalue, result, this);
        return result;
    }

    public Unary() {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public Unary(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setOperand(Expr node) {
        setChild(node, 0);
    }

    public Expr getOperand() {
        return (Expr) getChild(0);
    }

    public Expr getOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return getOperand().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return getOperand().isDUafter(v);
    }

    public String printPostOp() {
        state();
        return "";
    }

    public String printPreOp() {
        state();
        return "";
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
        return getOperand().type();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getOperandNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
