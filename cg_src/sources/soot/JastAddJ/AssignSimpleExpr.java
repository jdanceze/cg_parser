package soot.JastAddJ;

import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AssignSimpleExpr.class */
public class AssignSimpleExpr extends AssignExpr implements Cloneable {
    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public AssignSimpleExpr clone() throws CloneNotSupportedException {
        AssignSimpleExpr node = (AssignSimpleExpr) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            AssignSimpleExpr node = clone();
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

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getDest().isVariable()) {
            error("left hand side is not a variable");
        } else if (!sourceType().assignConversionTo(getDest().type(), getSource()) && !sourceType().isUnknown()) {
            error("can not assign " + getDest() + " of type " + getDest().type().typeName() + " a value of type " + sourceType().typeName());
        }
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr
    public Value eval(Body b) {
        Value lvalue = getDest().eval(b);
        Value rvalue = asRValue(b, getSource().type().emitCastTo(b, getSource(), getDest().type()));
        return getDest().emitStore(b, lvalue, asImmediate(b, rvalue), this);
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkWarnings() {
        if (!withinSuppressWarnings("unchecked")) {
            checkUncheckedConversion(getSource().type(), getDest().type());
        }
    }

    public AssignSimpleExpr() {
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public AssignSimpleExpr(Expr p0, Expr p1) {
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

    @Override // soot.JastAddJ.AssignExpr
    public String printOp() {
        state();
        return " = ";
    }

    @Override // soot.JastAddJ.AssignExpr
    public TypeDecl sourceType() {
        state();
        return getSource().type();
    }

    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if (caller == getDestNoTransform()) {
            return true;
        }
        return super.Define_boolean_isDest(caller, child);
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getDestNoTransform()) {
            return false;
        }
        return super.Define_boolean_isSource(caller, child);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        if (caller == getSourceNoTransform()) {
            return getDest().type();
        }
        return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }

    @Override // soot.JastAddJ.AssignExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
