package soot.JastAddJ;

import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MulExpr.class */
public class MulExpr extends MultiplicativeExpr implements Cloneable {
    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public MulExpr clone() throws CloneNotSupportedException {
        MulExpr node = (MulExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            MulExpr node = clone();
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

    @Override // soot.JastAddJ.Binary
    public Value emitOperation(Body b, Value left, Value right) {
        return asLocal(b, b.newMulExpr(asImmediate(b, left), asImmediate(b, right), this));
    }

    public MulExpr() {
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public MulExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return type().mul(getLeftOperand().constant(), getRightOperand().constant());
    }

    @Override // soot.JastAddJ.Binary
    public String printOp() {
        state();
        return " * ";
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
