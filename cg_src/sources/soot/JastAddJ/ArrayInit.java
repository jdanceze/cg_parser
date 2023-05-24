package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ArrayInit.class */
public class ArrayInit extends Expr implements Cloneable {
    protected Map computeDABefore_int_Variable_values;
    protected Map computeDUbefore_int_Variable_values;
    protected TypeDecl type_value;
    protected TypeDecl declType_value;
    protected boolean type_computed = false;
    protected boolean declType_computed = false;

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.computeDABefore_int_Variable_values = null;
        this.computeDUbefore_int_Variable_values = null;
        this.type_computed = false;
        this.type_value = null;
        this.declType_computed = false;
        this.declType_value = null;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ArrayInit clone() throws CloneNotSupportedException {
        ArrayInit node = (ArrayInit) super.clone();
        node.computeDABefore_int_Variable_values = null;
        node.computeDUbefore_int_Variable_values = null;
        node.type_computed = false;
        node.type_value = null;
        node.declType_computed = false;
        node.declType_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ArrayInit node = clone();
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
        s.append("{ ");
        if (getNumInit() > 0) {
            getInit(0).toString(s);
            for (int i = 1; i < getNumInit(); i++) {
                s.append(", ");
                getInit(i).toString(s);
            }
        }
        s.append(" } ");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl initializerType = declType().componentType();
        if (initializerType.isUnknown()) {
            error("the dimension of the initializer is larger than the expected dimension");
        }
        for (int i = 0; i < getNumInit(); i++) {
            Expr e = getInit(i);
            if (!e.type().assignConversionTo(initializerType, e)) {
                error("the type " + e.type().name() + " of the initializer is not compatible with " + initializerType.name());
            }
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        Value size = IntType.emitConstant(getNumInit());
        Local array = asLocal(b, b.newNewArrayExpr(type().componentType().getSootType(), asImmediate(b, size), this));
        for (int i = 0; i < getNumInit(); i++) {
            Value rvalue = getInit(i).type().emitCastTo(b, getInit(i), expectedType());
            Value index = IntType.emitConstant(i);
            Value lvalue = b.newArrayRef(array, index, getInit(i));
            b.setLine(this);
            b.add(b.newAssignStmt(lvalue, asImmediate(b, rvalue), getInit(i)));
        }
        return array;
    }

    public ArrayInit() {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public ArrayInit(List<Expr> p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setInitList(List<Expr> list) {
        setChild(list, 0);
    }

    public int getNumInit() {
        return getInitList().getNumChild();
    }

    public int getNumInitNoTransform() {
        return getInitListNoTransform().getNumChildNoTransform();
    }

    public Expr getInit(int i) {
        return getInitList().getChild(i);
    }

    public void addInit(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getInitListNoTransform() : getInitList();
        list.addChild(node);
    }

    public void addInitNoTransform(Expr node) {
        List<Expr> list = getInitListNoTransform();
        list.addChild(node);
    }

    public void setInit(Expr node, int i) {
        List<Expr> list = getInitList();
        list.setChild(node, i);
    }

    public List<Expr> getInits() {
        return getInitList();
    }

    public List<Expr> getInitsNoTransform() {
        return getInitListNoTransform();
    }

    public List<Expr> getInitList() {
        List<Expr> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<Expr> getInitListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Expr
    public boolean representableIn(TypeDecl t) {
        state();
        for (int i = 0; i < getNumInit(); i++) {
            if (!getInit(i).representableIn(t)) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return getNumInit() == 0 ? isDAbefore(v) : getInit(getNumInit() - 1).isDAafter(v);
    }

    public boolean computeDABefore(int childIndex, Variable v) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(childIndex));
        arrayList.add(v);
        if (this.computeDABefore_int_Variable_values == null) {
            this.computeDABefore_int_Variable_values = new HashMap(4);
        }
        if (this.computeDABefore_int_Variable_values.containsKey(arrayList)) {
            return ((Boolean) this.computeDABefore_int_Variable_values.get(arrayList)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean computeDABefore_int_Variable_value = computeDABefore_compute(childIndex, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.computeDABefore_int_Variable_values.put(arrayList, Boolean.valueOf(computeDABefore_int_Variable_value));
        }
        return computeDABefore_int_Variable_value;
    }

    private boolean computeDABefore_compute(int childIndex, Variable v) {
        if (childIndex == 0) {
            return isDAbefore(v);
        }
        for (int index = childIndex - 1; index > 0 && getInit(index).isConstant(); index--) {
        }
        return getInit(childIndex - 1).isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return getNumInit() == 0 ? isDUbefore(v) : getInit(getNumInit() - 1).isDUafter(v);
    }

    public boolean computeDUbefore(int childIndex, Variable v) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(childIndex));
        arrayList.add(v);
        if (this.computeDUbefore_int_Variable_values == null) {
            this.computeDUbefore_int_Variable_values = new HashMap(4);
        }
        if (this.computeDUbefore_int_Variable_values.containsKey(arrayList)) {
            return ((Boolean) this.computeDUbefore_int_Variable_values.get(arrayList)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean computeDUbefore_int_Variable_value = computeDUbefore_compute(childIndex, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.computeDUbefore_int_Variable_values.put(arrayList, Boolean.valueOf(computeDUbefore_int_Variable_value));
        }
        return computeDUbefore_int_Variable_value;
    }

    private boolean computeDUbefore_compute(int childIndex, Variable v) {
        if (childIndex == 0) {
            return isDUbefore(v);
        }
        for (int index = childIndex - 1; index > 0 && getInit(index).isConstant(); index--) {
        }
        return getInit(childIndex - 1).isDUafter(v);
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
        return declType();
    }

    public TypeDecl declType() {
        if (this.declType_computed) {
            return this.declType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.declType_value = getParent().Define_TypeDecl_declType(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.declType_computed = true;
        }
        return this.declType_value;
    }

    public TypeDecl expectedType() {
        state();
        TypeDecl expectedType_value = getParent().Define_TypeDecl_expectedType(this, null);
        return expectedType_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getInitListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getInitListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            return computeDABefore(childIndex, v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getInitListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            return computeDUbefore(childIndex, v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
        if (caller == getInitListNoTransform()) {
            caller.getIndexOfChild(child);
            return declType().componentType();
        }
        return getParent().Define_TypeDecl_declType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        if (caller == getInitListNoTransform()) {
            caller.getIndexOfChild(child);
            return declType().componentType();
        }
        return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
        if (caller == getInitListNoTransform()) {
            caller.getIndexOfChild(child);
            return expectedType().componentType();
        }
        return getParent().Define_TypeDecl_expectedType(this, caller);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
