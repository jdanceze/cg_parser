package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/InstanceInitializer.class */
public class InstanceInitializer extends BodyDecl implements Cloneable {
    protected boolean exceptions_computed = false;
    protected Collection exceptions_value;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map handlesException_TypeDecl_values;

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.exceptions_computed = false;
        this.exceptions_value = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.handlesException_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public InstanceInitializer clone() throws CloneNotSupportedException {
        InstanceInitializer node = (InstanceInitializer) super.clone();
        node.exceptions_computed = false;
        node.exceptions_value = null;
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
            InstanceInitializer node = clone();
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
        if (getBlock().getNumStmt() == 0) {
            return;
        }
        s.append(indent());
        getBlock().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    void checkUnreachableStmt() {
        if (!getBlock().canCompleteNormally()) {
            error("instance initializer in " + hostType().fullName() + " can not complete normally");
        }
    }

    public InstanceInitializer() {
    }

    @Override // soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public InstanceInitializer(Block p0) {
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

    public Collection exceptions() {
        if (this.exceptions_computed) {
            return this.exceptions_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.exceptions_value = exceptions_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.exceptions_computed = true;
        }
        return this.exceptions_value;
    }

    private Collection exceptions_compute() {
        HashSet set = new HashSet();
        collectExceptions(set, this);
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (!getBlock().reachedException(typeDecl)) {
                iter.remove();
            }
        }
        return set;
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
            if (hostType().isAnonymous() || !exceptionType.isUncheckedException()) {
                return true;
            }
            for (ConstructorDecl decl : hostType().constructors()) {
                if (!decl.throwsException(exceptionType)) {
                    return false;
                }
            }
            return true;
        }
        return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return this;
        }
        return getParent().Define_ASTNode_enclosingBlock(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return false;
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
