package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Value;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CastExpr.class */
public class CastExpr extends Expr implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public CastExpr clone() throws CloneNotSupportedException {
        CastExpr node = (CastExpr) super.clone();
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
            CastExpr node = clone();
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
        getTypeAccess().toString(s);
        s.append(")");
        getExpr().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl expr = getExpr().type();
        TypeDecl type = getTypeAccess().type();
        if (!expr.isUnknown()) {
            if (!expr.castingConversionTo(type)) {
                error(String.valueOf(expr.typeName()) + " can not be cast into " + type.typeName());
            }
            if (!getTypeAccess().isTypeAccess()) {
                error(getTypeAccess() + " is not a type access in cast expression");
            }
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        if (isConstant()) {
            return emitConstant(constant());
        }
        Value operand = getExpr().eval(b);
        if (operand == NullConstant.v()) {
            return getExpr().type().emitCastTo(b, operand, type(), this);
        }
        return getExpr().type().emitCastTo(b, asLocal(b, operand), type(), this);
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkWarnings() {
        if (!withinSuppressWarnings("unchecked")) {
            checkUncheckedConversion(getExpr().type(), getTypeAccess().type());
        }
    }

    public CastExpr() {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public CastExpr(Access p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setTypeAccess(Access node) {
        setChild(node, 0);
    }

    public Access getTypeAccess() {
        return (Access) getChild(0);
    }

    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(0);
    }

    public void setExpr(Expr node) {
        setChild(node, 1);
    }

    public Expr getExpr() {
        return (Expr) getChild(1);
    }

    public Expr getExprNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return type().cast(getExpr().constant());
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        state();
        if (getExpr().isConstant()) {
            return getTypeAccess().type().isPrimitive() || getTypeAccess().type().isString();
        }
        return false;
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return getExpr().isDAafter(v);
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
        return getTypeAccess().type();
    }

    @Override // soot.JastAddJ.Expr
    public boolean staticContextQualifier() {
        state();
        return getExpr().staticContextQualifier();
    }

    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
