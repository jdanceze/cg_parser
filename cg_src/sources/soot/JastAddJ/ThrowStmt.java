package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ThrowStmt.class */
public class ThrowStmt extends Stmt implements Cloneable {
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean canCompleteNormally_value;
    protected TypeDecl typeNullPointerException_value;
    protected Map handlesException_TypeDecl_values;
    protected TypeDecl typeThrowable_value;
    protected TypeDecl typeNull_value;
    protected boolean canCompleteNormally_computed = false;
    protected boolean typeNullPointerException_computed = false;
    protected boolean typeThrowable_computed = false;
    protected boolean typeNull_computed = false;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.typeNullPointerException_computed = false;
        this.typeNullPointerException_value = null;
        this.handlesException_TypeDecl_values = null;
        this.typeThrowable_computed = false;
        this.typeThrowable_value = null;
        this.typeNull_computed = false;
        this.typeNull_value = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public ThrowStmt clone() throws CloneNotSupportedException {
        ThrowStmt node = (ThrowStmt) super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.typeNullPointerException_computed = false;
        node.typeNullPointerException_value = null;
        node.handlesException_TypeDecl_values = null;
        node.typeThrowable_computed = false;
        node.typeThrowable_value = null;
        node.typeNull_computed = false;
        node.typeNull_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ThrowStmt node = clone();
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public void collectExceptions(Collection c, ASTNode target) {
        super.collectExceptions(c, target);
        TypeDecl exceptionType = getExpr().type();
        if (exceptionType == typeNull()) {
            exceptionType = typeNullPointerException();
        }
        c.add(exceptionType);
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("throw ");
        getExpr().toString(s);
        s.append(";");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getExpr().type().instanceOf(typeThrowable())) {
            error("*** The thrown expression must extend Throwable");
        }
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        b.setLine(this);
        b.add(b.newThrowStmt(asImmediate(b, getExpr().eval(b)), this));
    }

    public ThrowStmt() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ThrowStmt(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.ASTNode
    public void exceptionHandling() {
        Collection<TypeDecl> exceptionTypes = getExpr().throwTypes();
        Iterator<TypeDecl> it = exceptionTypes.iterator();
        while (it.hasNext()) {
            TypeDecl exceptionType = it.next();
            if (exceptionType == typeNull()) {
                exceptionType = typeNullPointerException();
            }
            if (!handlesException(exceptionType)) {
                error(this + " throws uncaught exception " + exceptionType.fullName());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean reachedException(TypeDecl catchType) {
        Collection<TypeDecl> exceptionTypes = getExpr().throwTypes();
        boolean reached = false;
        Iterator<TypeDecl> it = exceptionTypes.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            TypeDecl exceptionType = it.next();
            if (exceptionType == typeNull()) {
                exceptionType = typeNullPointerException();
            }
            if (catchType.mayCatch(exceptionType)) {
                reached = true;
                break;
            } else if (super.reachedException(catchType)) {
                reached = true;
                break;
            }
        }
        return reached;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean isDAafter(Variable v) {
        if (this.isDAafter_Variable_values == null) {
            this.isDAafter_Variable_values = new HashMap(4);
        }
        if (this.isDAafter_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafter_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafter_Variable_value = isDAafter_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, Boolean.valueOf(isDAafter_Variable_value));
        }
        return isDAafter_Variable_value;
    }

    private boolean isDAafter_compute(Variable v) {
        return true;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean isDUafter(Variable v) {
        if (this.isDUafter_Variable_values == null) {
            this.isDUafter_Variable_values = new HashMap(4);
        }
        if (this.isDUafter_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUafter_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUafter_Variable_value = isDUafter_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, Boolean.valueOf(isDUafter_Variable_value));
        }
        return isDUafter_Variable_value;
    }

    private boolean isDUafter_compute(Variable v) {
        return true;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean canCompleteNormally() {
        if (this.canCompleteNormally_computed) {
            return this.canCompleteNormally_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.canCompleteNormally_value = canCompleteNormally_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.canCompleteNormally_computed = true;
        }
        return this.canCompleteNormally_value;
    }

    private boolean canCompleteNormally_compute() {
        return false;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return false;
    }

    public TypeDecl typeNullPointerException() {
        if (this.typeNullPointerException_computed) {
            return this.typeNullPointerException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeNullPointerException_value = getParent().Define_TypeDecl_typeNullPointerException(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeNullPointerException_computed = true;
        }
        return this.typeNullPointerException_value;
    }

    public boolean handlesException(TypeDecl exceptionType) {
        if (this.handlesException_TypeDecl_values == null) {
            this.handlesException_TypeDecl_values = new HashMap(4);
        }
        if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
            return ((Boolean) this.handlesException_TypeDecl_values.get(exceptionType)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
        if (isFinal && num == state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, Boolean.valueOf(handlesException_TypeDecl_value));
        }
        return handlesException_TypeDecl_value;
    }

    public TypeDecl typeThrowable() {
        if (this.typeThrowable_computed) {
            return this.typeThrowable_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeThrowable_value = getParent().Define_TypeDecl_typeThrowable(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeThrowable_computed = true;
        }
        return this.typeThrowable_value;
    }

    public TypeDecl typeNull() {
        if (this.typeNull_computed) {
            return this.typeNull_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeNull_value = getParent().Define_TypeDecl_typeNull(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeNull_computed = true;
        }
        return this.typeNull_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getExprNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getExprNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
