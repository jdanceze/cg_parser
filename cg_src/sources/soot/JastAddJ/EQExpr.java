package soot.JastAddJ;

import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/EQExpr.class */
public class EQExpr extends EqualityExpr implements Cloneable {
    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public EQExpr clone() throws CloneNotSupportedException {
        EQExpr node = (EQExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            EQExpr node = clone();
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

    @Override // soot.JastAddJ.RelationalExpr
    public Value comparison(Body b, Value left, Value right) {
        return b.newEqExpr(asImmediate(b, left), asImmediate(b, right), this);
    }

    @Override // soot.JastAddJ.RelationalExpr
    public Value comparisonInv(Body b, Value left, Value right) {
        return b.newNeExpr(asImmediate(b, left), asImmediate(b, right), this);
    }

    public EQExpr() {
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public EQExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return Constant.create(binaryNumericPromotedType().eqIsTrue(left(), right()));
    }

    @Override // soot.JastAddJ.Binary
    public String printOp() {
        state();
        return " == ";
    }

    @Override // soot.JastAddJ.EqualityExpr, soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
