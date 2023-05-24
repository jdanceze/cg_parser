package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ArrayTypeWithSizeAccess.class */
public class ArrayTypeWithSizeAccess extends ArrayTypeAccess implements Cloneable {
    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ArrayTypeWithSizeAccess clone() throws CloneNotSupportedException {
        ArrayTypeWithSizeAccess node = (ArrayTypeWithSizeAccess) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ArrayTypeWithSizeAccess node = clone();
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
    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getAccess().toString(s);
        s.append("[");
        getExpr().toString(s);
        s.append("]");
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public void typeCheck() {
        super.typeCheck();
        if (!getExpr().type().unaryNumericPromotion().isInt()) {
            error(String.valueOf(getExpr().type().typeName()) + " is not int after unary numeric promotion");
        }
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.Access
    public void addArraySize(Body b, ArrayList list) {
        getAccess().addArraySize(b, list);
        list.add(asImmediate(b, getExpr().eval(b)));
    }

    public ArrayTypeWithSizeAccess() {
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public ArrayTypeWithSizeAccess(Access p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ArrayTypeAccess
    public void setAccess(Access node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ArrayTypeAccess
    public Access getAccess() {
        return (Access) getChild(0);
    }

    @Override // soot.JastAddJ.ArrayTypeAccess
    public Access getAccessNoTransform() {
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

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess
    public void setPackage(String value) {
        this.tokenString_Package = value;
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return getExpr().isDAafter(v);
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return getExpr().isDUafter(v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if (caller == getExprNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isDest(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getExprNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getExprNoTransform()) {
            return getAccess().isDAafter(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getExprNoTransform()) {
            return getAccess().isDUafter(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if (caller == getExprNoTransform()) {
            return unqualifiedScope().lookupMethod(name);
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        if (caller == getExprNoTransform()) {
            return unqualifiedScope().hasPackage(packageName);
        }
        return getParent().Define_boolean_hasPackage(this, caller, packageName);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getExprNoTransform()) {
            return unqualifiedScope().lookupType(name);
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getExprNoTransform()) {
            return unqualifiedScope().lookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getExprNoTransform()) {
            return NameType.EXPRESSION_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ArrayTypeAccess, soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
