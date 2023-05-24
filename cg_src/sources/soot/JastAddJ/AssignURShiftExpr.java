package soot.JastAddJ;

import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AssignURShiftExpr.class */
public class AssignURShiftExpr extends AssignShiftExpr implements Cloneable {
    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public AssignURShiftExpr clone() throws CloneNotSupportedException {
        AssignURShiftExpr node = (AssignURShiftExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            AssignURShiftExpr node = clone();
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

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr
    public Value eval(Body b) {
        return emitShiftExpr(b);
    }

    @Override // soot.JastAddJ.AssignExpr
    public Value createAssignOp(Body b, Value fst, Value snd) {
        return b.newUshrExpr(asImmediate(b, fst), asImmediate(b, snd), this);
    }

    public AssignURShiftExpr() {
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public AssignURShiftExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr
    public void setDest(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr
    public Expr getDest() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr
    public Expr getDestNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr
    public void setSource(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr
    public Expr getSource() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr
    public Expr getSourceNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.AssignExpr
    public String printOp() {
        state();
        return " >>>= ";
    }

    @Override // soot.JastAddJ.AssignShiftExpr, soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
