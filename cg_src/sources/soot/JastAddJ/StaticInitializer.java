package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/StaticInitializer.class */
public class StaticInitializer extends BodyDecl implements Cloneable {
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map handlesException_TypeDecl_values;

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.handlesException_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public StaticInitializer clone() throws CloneNotSupportedException {
        StaticInitializer node = (StaticInitializer) super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.handlesException_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            StaticInitializer node = clone();
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
    public void checkModifiers() {
        super.checkModifiers();
        if (hostType().isInnerClass()) {
            error("*** Inner classes may not declare static initializers");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        if (getBlock().getNumStmt() == 0) {
            return;
        }
        s.append(indent());
        s.append("static ");
        getBlock().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    void checkUnreachableStmt() {
        if (!getBlock().canCompleteNormally()) {
            error("static initializer in " + hostType().fullName() + " can not complete normally");
        }
    }

    public StaticInitializer() {
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public StaticInitializer(Block p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setBlock(Block node) {
        setChild(node, 0);
    }

    public Block getBlock() {
        return (Block) getChild(0);
    }

    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.BodyDecl
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
        return getBlock().isDAafter(v);
    }

    @Override // soot.JastAddJ.BodyDecl
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
        return getBlock().isDUafter(v);
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean visibleTypeParameters() {
        state();
        return false;
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

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getBlockNoTransform()) {
            return hostType().isAnonymous() ? handlesException(exceptionType) : !exceptionType.isUncheckedException();
        }
        return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return null;
        }
        return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
