package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Dot.class */
public class Dot extends AbstractDot implements Cloneable {
    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public Dot clone() throws CloneNotSupportedException {
        Dot node = (Dot) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            Dot node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ASTNode<ASTNode> copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy;
    }

    public Dot lastDot() {
        Dot dot = this;
        while (true) {
            Dot node = dot;
            if (node.getRightNoTransform() instanceof Dot) {
                dot = (Dot) node.getRightNoTransform();
            } else {
                return node;
            }
        }
    }

    @Override // soot.JastAddJ.Expr
    public Dot qualifiesAccess(Access access) {
        Dot lastDot = lastDot();
        Expr l = lastDot.getRightNoTransform();
        Dot dot = new Dot(lastDot.getRightNoTransform(), access);
        dot.setStart(l.getStart());
        dot.setEnd(access.getEnd());
        lastDot.setRight(dot);
        return this;
    }

    private Access qualifyTailWith(Access expr) {
        if (getRight() instanceof AbstractDot) {
            AbstractDot dot = (AbstractDot) getRight();
            return expr.qualifiesAccess(dot.getRight());
        }
        return expr;
    }

    @Override // soot.JastAddJ.AbstractDot
    public Access extractLast() {
        return lastDot().getRightNoTransform();
    }

    @Override // soot.JastAddJ.AbstractDot
    public void replaceLast(Access access) {
        lastDot().setRight(access);
    }

    public Dot() {
    }

    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public Dot(Expr p0, Access p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.AbstractDot
    public void setLeft(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.AbstractDot
    public Expr getLeft() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.AbstractDot
    public Expr getLeftNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.AbstractDot
    public void setRight(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.AbstractDot
    public Access getRight() {
        return (Access) getChild(1);
    }

    @Override // soot.JastAddJ.AbstractDot
    public Access getRightNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.AbstractDot, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (!duringSyntacticClassification() && leftSide().isPackageAccess() && rightSide().isPackageAccess()) {
            state().duringNameResolution++;
            ASTNode result = rewriteRule0();
            state().duringNameResolution--;
            return result;
        } else if (!duringSyntacticClassification() && leftSide().isPackageAccess() && !((Access) leftSide()).hasPrevExpr() && (rightSide() instanceof TypeAccess)) {
            state().duringNameResolution++;
            ASTNode result2 = rewriteRule1();
            state().duringNameResolution--;
            return result2;
        } else {
            return super.rewriteTo();
        }
    }

    private Access rewriteRule0() {
        PackageAccess left = (PackageAccess) leftSide();
        PackageAccess right = (PackageAccess) rightSide();
        left.setPackage(String.valueOf(left.getPackage()) + "." + right.getPackage());
        left.setEnd(right.end());
        return qualifyTailWith(left);
    }

    private Access rewriteRule1() {
        PackageAccess left = (PackageAccess) leftSide();
        TypeAccess right = (TypeAccess) rightSide();
        right.setPackage(left.getPackage());
        right.setStart(left.start());
        return qualifyTailWith(right);
    }
}
