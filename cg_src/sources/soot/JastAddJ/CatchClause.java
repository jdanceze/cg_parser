package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CatchClause.class */
public abstract class CatchClause extends ASTNode<ASTNode> implements Cloneable, VariableScope {
    protected Map parameterDeclaration_String_values;
    protected boolean typeThrowable_computed = false;
    protected TypeDecl typeThrowable_value;
    protected Map lookupVariable_String_values;
    protected Map reachableCatchClause_TypeDecl_values;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.parameterDeclaration_String_values = null;
        this.typeThrowable_computed = false;
        this.typeThrowable_value = null;
        this.lookupVariable_String_values = null;
        this.reachableCatchClause_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public CatchClause clone() throws CloneNotSupportedException {
        CatchClause node = (CatchClause) super.mo287clone();
        node.parameterDeclaration_String_values = null;
        node.typeThrowable_computed = false;
        node.typeThrowable_value = null;
        node.lookupVariable_String_values = null;
        node.reachableCatchClause_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public CatchClause() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public CatchClause(Block p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ASTNode
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

    public boolean handles(TypeDecl exceptionType) {
        state();
        return false;
    }

    public SimpleSet parameterDeclaration(String name) {
        if (this.parameterDeclaration_String_values == null) {
            this.parameterDeclaration_String_values = new HashMap(4);
        }
        if (this.parameterDeclaration_String_values.containsKey(name)) {
            return (SimpleSet) this.parameterDeclaration_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet parameterDeclaration_String_value = parameterDeclaration_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.parameterDeclaration_String_values.put(name, parameterDeclaration_String_value);
        }
        return parameterDeclaration_String_value;
    }

    private SimpleSet parameterDeclaration_compute(String name) {
        return SimpleSet.emptySet;
    }

    public boolean modifiedInScope(Variable var) {
        state();
        return getBlock().modifiedInScope(var);
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

    @Override // soot.JastAddJ.VariableScope
    public SimpleSet lookupVariable(String name) {
        if (this.lookupVariable_String_values == null) {
            this.lookupVariable_String_values = new HashMap(4);
        }
        if (this.lookupVariable_String_values.containsKey(name)) {
            return (SimpleSet) this.lookupVariable_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupVariable_String_values.put(name, lookupVariable_String_value);
        }
        return lookupVariable_String_value;
    }

    public boolean reachableCatchClause(TypeDecl exceptionType) {
        if (this.reachableCatchClause_TypeDecl_values == null) {
            this.reachableCatchClause_TypeDecl_values = new HashMap(4);
        }
        if (this.reachableCatchClause_TypeDecl_values.containsKey(exceptionType)) {
            return ((Boolean) this.reachableCatchClause_TypeDecl_values.get(exceptionType)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean reachableCatchClause_TypeDecl_value = getParent().Define_boolean_reachableCatchClause(this, null, exceptionType);
        if (isFinal && num == state().boundariesCrossed) {
            this.reachableCatchClause_TypeDecl_values.put(exceptionType, Boolean.valueOf(reachableCatchClause_TypeDecl_value));
        }
        return reachableCatchClause_TypeDecl_value;
    }

    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    public Collection<TypeDecl> caughtExceptions() {
        state();
        Collection<TypeDecl> caughtExceptions_value = getParent().Define_Collection_TypeDecl__caughtExceptions(this, null);
        return caughtExceptions_value;
    }

    public boolean reportUnreachable() {
        state();
        boolean reportUnreachable_value = getParent().Define_boolean_reportUnreachable(this, null);
        return reportUnreachable_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getBlockNoTransform()) {
            SimpleSet set = parameterDeclaration(name);
            return !set.isEmpty() ? set : lookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return this;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_reportUnreachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
