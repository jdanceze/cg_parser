package soot.JastAddJ;

import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PostIncExpr.class */
public class PostIncExpr extends PostfixExpr implements Cloneable {
    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public PostIncExpr clone() throws CloneNotSupportedException {
        PostIncExpr node = (PostIncExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            PostIncExpr node = clone();
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

    @Override // soot.JastAddJ.Unary, soot.JastAddJ.Expr
    public Value eval(Body b) {
        return emitPostfix(b, 1);
    }

    public PostIncExpr() {
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public PostIncExpr(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary
    public void setOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary
    public Expr getOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary
    public Expr getOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Unary
    public String printPostOp() {
        state();
        return "++";
    }

    @Override // soot.JastAddJ.PostfixExpr, soot.JastAddJ.Unary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
