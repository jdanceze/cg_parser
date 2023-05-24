package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParExpr.class */
public class ParExpr extends PrimaryExpr implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParExpr clone() throws CloneNotSupportedException {
        ParExpr node = (ParExpr) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ParExpr node = clone();
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

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append("(");
        getExpr().toString(s);
        s.append(")");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (getExpr().isTypeAccess()) {
            error(getExpr() + " is a type and may not be used in parenthesized expression");
        }
    }

    @Override // soot.JastAddJ.Expr
    public void emitEvalBranch(Body b) {
        getExpr().emitEvalBranch(b);
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        return getExpr().eval(b);
    }

    public ParExpr() {
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ParExpr(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setExpr(Expr node) {
        setChild(node, 0);
    }

    public Expr getExpr() {
        return (Expr) getChild(0);
    }

    public Expr getExprNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return getExpr().constant();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        state();
        return getExpr().isConstant();
    }

    @Override // soot.JastAddJ.Expr
    public Variable varDecl() {
        state();
        return getExpr().varDecl();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        state();
        return getExpr().isDAafterTrue(v) || isFalse();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        state();
        return getExpr().isDAafterFalse(v) || isTrue();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return getExpr().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterTrue(Variable v) {
        state();
        return getExpr().isDUafterTrue(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterFalse(Variable v) {
        state();
        return getExpr().isDUafterFalse(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return getExpr().isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isSuperAccess() {
        state();
        return getExpr().isSuperAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isThisAccess() {
        state();
        return getExpr().isThisAccess();
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
        return getExpr().isTypeAccess() ? unknownType() : getExpr().type();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isVariable() {
        state();
        return getExpr().isVariable();
    }

    @Override // soot.JastAddJ.Expr
    public boolean staticContextQualifier() {
        state();
        return getExpr().staticContextQualifier();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return getParent().definesLabel();
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeTrue() {
        state();
        return getExpr().canBeTrue();
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeFalse() {
        state();
        return getExpr().canBeFalse();
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
