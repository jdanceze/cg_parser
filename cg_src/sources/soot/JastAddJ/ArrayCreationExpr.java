package soot.JastAddJ;

import java.util.ArrayList;
import soot.ArrayType;
import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ArrayCreationExpr.class */
public class ArrayCreationExpr extends PrimaryExpr implements Cloneable {
    protected TypeDecl type_value;
    protected int numArrays_value;
    protected boolean type_computed = false;
    protected boolean numArrays_computed = false;

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
        this.numArrays_computed = false;
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ArrayCreationExpr clone() throws CloneNotSupportedException {
        ArrayCreationExpr node = (ArrayCreationExpr) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.numArrays_computed = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ArrayCreationExpr node = clone();
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
        s.append("new ");
        getTypeAccess().toString(s);
        if (hasArrayInit()) {
            getArrayInit().toString(s);
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        if (hasArrayInit()) {
            return getArrayInit().eval(b);
        }
        ArrayList list = new ArrayList();
        getTypeAccess().addArraySize(b, list);
        if (numArrays() == 1) {
            Value size = (Value) list.get(0);
            return b.newNewArrayExpr(type().componentType().getSootType(), asImmediate(b, size), this);
        }
        return b.newNewMultiArrayExpr((ArrayType) type().getSootType(), list, this);
    }

    public ArrayCreationExpr() {
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new Opt(), 1);
    }

    public ArrayCreationExpr(Access p0, Opt<ArrayInit> p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
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

    public void setArrayInitOpt(Opt<ArrayInit> opt) {
        setChild(opt, 1);
    }

    public boolean hasArrayInit() {
        return getArrayInitOpt().getNumChild() != 0;
    }

    public ArrayInit getArrayInit() {
        return getArrayInitOpt().getChild(0);
    }

    public void setArrayInit(ArrayInit node) {
        getArrayInitOpt().setChild(node, 0);
    }

    public Opt<ArrayInit> getArrayInitOpt() {
        return (Opt) getChild(1);
    }

    public Opt<ArrayInit> getArrayInitOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    public boolean isDAafterCreation(Variable v) {
        state();
        return getTypeAccess().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return hasArrayInit() ? getArrayInit().isDAafter(v) : isDAafterCreation(v);
    }

    public boolean isDUafterCreation(Variable v) {
        state();
        return getTypeAccess().isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return hasArrayInit() ? getArrayInit().isDUafter(v) : isDUafterCreation(v);
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

    public int numArrays() {
        if (this.numArrays_computed) {
            return this.numArrays_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.numArrays_value = numArrays_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.numArrays_computed = true;
        }
        return this.numArrays_value;
    }

    private int numArrays_compute() {
        int i = type().dimension();
        Access typeAccess = getTypeAccess();
        while (true) {
            Access a = typeAccess;
            if (!(a instanceof ArrayTypeAccess) || (a instanceof ArrayTypeWithSizeAccess)) {
                break;
            }
            i--;
            typeAccess = ((ArrayTypeAccess) a).getAccess();
        }
        return i;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getArrayInitOptNoTransform()) {
            return isDAafterCreation(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getArrayInitOptNoTransform()) {
            return isDUafterCreation(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
        if (caller == getArrayInitOptNoTransform()) {
            return type();
        }
        return getParent().Define_TypeDecl_declType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
        if (caller == getArrayInitOptNoTransform()) {
            return type().componentType();
        }
        return getParent().Define_TypeDecl_expectedType(this, caller);
    }

    @Override // soot.JastAddJ.PrimaryExpr, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
