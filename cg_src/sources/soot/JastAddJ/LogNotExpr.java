package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/LogNotExpr.class */
public class LogNotExpr extends Unary implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public LogNotExpr clone() throws CloneNotSupportedException {
        LogNotExpr node = (LogNotExpr) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            LogNotExpr node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: fullCopy */
    public ASTNode<ASTNode> fullCopy2() {
        ASTNode<ASTNode> copy2 = copy2();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy2.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy2;
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getOperand().type().isBoolean()) {
            error("unary ! only operates on boolean types");
        }
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
    public Value eval(Body b) {
        return emitBooleanCondition(b);
    }

    @Override // soot.JastAddJ.Expr
    public void emitEvalBranch(Body b) {
        getOperand().emitEvalBranch(b);
    }

    public LogNotExpr() {
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public LogNotExpr(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.Unary
    public void setOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.Unary
    public Expr getOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.Unary
    public Expr getOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        state();
        return getOperand().isConstant();
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return Constant.create(!getOperand().constant().booleanValue());
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        state();
        return getOperand().isDAafterFalse(v) || isFalse();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        state();
        return getOperand().isDAafterTrue(v) || isTrue();
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return isDAafterTrue(v) && isDAafterFalse(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterTrue(Variable v) {
        state();
        return getOperand().isDUafterFalse(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterFalse(Variable v) {
        state();
        return getOperand().isDUafterTrue(v);
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return isDUafterTrue(v) && isDUafterFalse(v);
    }

    @Override // soot.JastAddJ.Unary
    public String printPreOp() {
        state();
        return "!";
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
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
        return true;
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeTrue() {
        state();
        return getOperand().canBeFalse();
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeFalse() {
        state();
        return getOperand().canBeTrue();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getOperandNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getOperandNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getOperandNoTransform()) {
            return true_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getOperandNoTransform()) {
            return false_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
