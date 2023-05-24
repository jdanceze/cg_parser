package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BitwiseExpr.class */
public abstract class BitwiseExpr extends Binary implements Cloneable {
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
    public BitwiseExpr clone() throws CloneNotSupportedException {
        BitwiseExpr node = (BitwiseExpr) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl left = getLeftOperand().type();
        TypeDecl right = getRightOperand().type();
        if (left.isIntegralType() && right.isIntegralType()) {
            return;
        }
        if (left.isBoolean() && right.isBoolean()) {
            return;
        }
        error(String.valueOf(left.typeName()) + " is not compatible with " + right.typeName());
    }

    public BitwiseExpr() {
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public BitwiseExpr(Expr p0, Expr p1) {
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
        if (getLeftOperand().type().isIntegralType() && getRightOperand().type().isIntegralType()) {
            return getLeftOperand().type().binaryNumericPromotion(getRightOperand().type());
        }
        if (getLeftOperand().type().isBoolean() && getRightOperand().type().isBoolean()) {
            return typeBoolean();
        }
        return unknownType();
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
