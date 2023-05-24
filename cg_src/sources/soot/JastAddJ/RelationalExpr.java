package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/RelationalExpr.class */
public abstract class RelationalExpr extends Binary implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public RelationalExpr clone() throws CloneNotSupportedException {
        RelationalExpr node = (RelationalExpr) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getLeftOperand().type().isNumericType()) {
            error(String.valueOf(getLeftOperand().type().typeName()) + " is not numeric");
        }
        if (!getRightOperand().type().isNumericType()) {
            error(String.valueOf(getRightOperand().type().typeName()) + " is not numeric");
        }
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr
    public Value eval(Body b) {
        return emitBooleanCondition(b);
    }

    @Override // soot.JastAddJ.Expr
    public void emitEvalBranch(Body b) {
        Local l;
        b.setLine(this);
        if (isTrue()) {
            b.add(b.newGotoStmt(true_label(), this));
        } else if (isFalse()) {
            b.add(b.newGotoStmt(false_label(), this));
        } else if (getLeftOperand().type().isNumericType()) {
            TypeDecl type = binaryNumericPromotedType();
            Value left = getLeftOperand().type().emitCastTo(b, getLeftOperand(), type);
            Value right = getRightOperand().type().emitCastTo(b, getRightOperand(), type);
            if (type.isDouble() || type.isFloat() || type.isLong()) {
                if (type.isDouble() || type.isFloat()) {
                    if ((this instanceof GEExpr) || (this instanceof GTExpr)) {
                        l = asLocal(b, b.newCmplExpr(asImmediate(b, left), asImmediate(b, right), this));
                    } else {
                        l = asLocal(b, b.newCmpgExpr(asImmediate(b, left), asImmediate(b, right), this));
                    }
                } else {
                    l = asLocal(b, b.newCmpExpr(asImmediate(b, left), asImmediate(b, right), this));
                }
                b.add(b.newIfStmt(comparisonInv(b, l, BooleanType.emitConstant(false)), false_label(), this));
                b.add(b.newGotoStmt(true_label(), this));
                return;
            }
            b.add(b.newIfStmt(comparison(b, left, right), true_label(), this));
            b.add(b.newGotoStmt(false_label(), this));
        } else {
            b.add(b.newIfStmt(comparison(b, getLeftOperand().eval(b), getRightOperand().eval(b)), true_label(), this));
            b.add(b.newGotoStmt(false_label(), this));
        }
    }

    public Value comparison(Body b, Value left, Value right) {
        throw new Error("comparison not supported for " + getClass().getName());
    }

    public Value comparisonInv(Body b, Value left, Value right) {
        throw new Error("comparisonInv not supported for " + getClass().getName());
    }

    public RelationalExpr() {
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public RelationalExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.Binary
    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.Binary
    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.Binary
    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Binary
    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.Binary
    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.Binary
    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
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
        return typeBoolean();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getRightOperandNoTransform()) {
            return false_label();
        }
        if (caller == getLeftOperandNoTransform()) {
            return false_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getRightOperandNoTransform()) {
            return true_label();
        }
        if (caller == getLeftOperandNoTransform()) {
            return true_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
