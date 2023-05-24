package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/EqualityExpr.class */
public abstract class EqualityExpr extends RelationalExpr implements Cloneable {
    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public EqualityExpr clone() throws CloneNotSupportedException {
        EqualityExpr node = (EqualityExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl left = getLeftOperand().type();
        TypeDecl right = getRightOperand().type();
        if (left.isNumericType() && right.isNumericType()) {
            return;
        }
        if (left.isBoolean() && right.isBoolean()) {
            return;
        }
        if ((left.isReferenceType() || left.isNull()) && ((right.isReferenceType() || right.isNull()) && (left.castingConversionTo(right) || right.castingConversionTo(left)))) {
            return;
        }
        error(String.valueOf(left.typeName()) + " can not be compared to " + right.typeName());
    }

    public EqualityExpr() {
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public EqualityExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary
    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.RelationalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
