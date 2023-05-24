package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Dims.class */
public class Dims extends ASTNode<ASTNode> implements Cloneable {
    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public Dims clone() throws CloneNotSupportedException {
        Dims node = (Dims) super.mo287clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            Dims node = clone();
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

    public Dims() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new Opt(), 0);
    }

    public Dims(Opt<Expr> p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setExprOpt(Opt<Expr> opt) {
        setChild(opt, 0);
    }

    public boolean hasExpr() {
        return getExprOpt().getNumChild() != 0;
    }

    public Expr getExpr() {
        return getExprOpt().getChild(0);
    }

    public void setExpr(Expr node) {
        getExprOpt().setChild(node, 0);
    }

    public Opt<Expr> getExprOpt() {
        return (Opt) getChild(0);
    }

    public Opt<Expr> getExprOptNoTransform() {
        return (Opt) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
