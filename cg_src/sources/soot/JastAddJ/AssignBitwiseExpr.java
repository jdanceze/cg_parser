package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AssignBitwiseExpr.class */
public abstract class AssignBitwiseExpr extends AssignExpr implements Cloneable {
    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public AssignBitwiseExpr clone() throws CloneNotSupportedException {
        AssignBitwiseExpr node = (AssignBitwiseExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl source = sourceType();
        TypeDecl dest = getDest().type();
        if (source.isIntegralType() && dest.isIntegralType()) {
            super.typeCheck();
        } else if (source.isBoolean() && dest.isBoolean()) {
            super.typeCheck();
        } else {
            error("Operator only operates on integral and boolean types");
        }
    }

    public AssignBitwiseExpr() {
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public AssignBitwiseExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.AssignExpr
    public void setDest(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.AssignExpr
    public Expr getDest() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.AssignExpr
    public Expr getDestNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.AssignExpr
    public void setSource(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.AssignExpr
    public Expr getSource() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.AssignExpr
    public Expr getSourceNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
