package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BodyDecl.class */
public abstract class BodyDecl extends ASTNode<ASTNode> implements Cloneable {
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map isDAbefore_Variable_values;
    protected Map isDUbefore_Variable_values;
    protected boolean typeThrowable_computed = false;
    protected TypeDecl typeThrowable_value;
    protected Map lookupVariable_String_values;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.isDAbefore_Variable_values = null;
        this.isDUbefore_Variable_values = null;
        this.typeThrowable_computed = false;
        this.typeThrowable_value = null;
        this.lookupVariable_String_values = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public BodyDecl clone() throws CloneNotSupportedException {
        BodyDecl node = (BodyDecl) super.mo287clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.isDAbefore_Variable_values = null;
        node.isDUbefore_Variable_values = null;
        node.typeThrowable_computed = false;
        node.typeThrowable_value = null;
        node.lookupVariable_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectFinally(Stmt branchStmt, ArrayList list) {
    }

    public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
        throw new Error("Operation substitutedBodyDecl not supported for " + getClass().getName());
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify1phase2() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify2() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkWarnings() {
        if (hasIllegalAnnotationSafeVarargs()) {
            error("@SafeVarargs is only allowed for variable arity method and constructor declarations");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

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

    public boolean declaresType(String name) {
        state();
        return false;
    }

    public TypeDecl type(String name) {
        state();
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean addsIndentationLevel() {
        state();
        return true;
    }

    public boolean isVoid() {
        state();
        return false;
    }

    public boolean hasAnnotationSuppressWarnings(String s) {
        state();
        return false;
    }

    public boolean isDeprecated() {
        state();
        return false;
    }

    public boolean isEnumConstant() {
        state();
        return false;
    }

    public boolean visibleTypeParameters() {
        state();
        return true;
    }

    public boolean generate() {
        state();
        return true;
    }

    public boolean hasAnnotationSafeVarargs() {
        state();
        return false;
    }

    public boolean hasIllegalAnnotationSafeVarargs() {
        state();
        return hasAnnotationSafeVarargs();
    }

    public boolean isDAbefore(Variable v) {
        if (this.isDAbefore_Variable_values == null) {
            this.isDAbefore_Variable_values = new HashMap(4);
        }
        if (this.isDAbefore_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAbefore_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAbefore_Variable_value = getParent().Define_boolean_isDAbefore(this, null, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAbefore_Variable_values.put(v, Boolean.valueOf(isDAbefore_Variable_value));
        }
        return isDAbefore_Variable_value;
    }

    public boolean isDUbefore(Variable v) {
        if (this.isDUbefore_Variable_values == null) {
            this.isDUbefore_Variable_values = new HashMap(4);
        }
        if (this.isDUbefore_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUbefore_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUbefore_Variable_value = getParent().Define_boolean_isDUbefore(this, null, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUbefore_Variable_values.put(v, Boolean.valueOf(isDUbefore_Variable_value));
        }
        return isDUbefore_Variable_value;
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

    public Collection lookupMethod(String name) {
        state();
        Collection lookupMethod_String_value = getParent().Define_Collection_lookupMethod(this, null, name);
        return lookupMethod_String_value;
    }

    public TypeDecl lookupType(String packageName, String typeName) {
        state();
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    public SimpleSet lookupType(String name) {
        state();
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        return lookupType_String_value;
    }

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

    public NameType nameType() {
        state();
        NameType nameType_value = getParent().Define_NameType_nameType(this, null);
        return nameType_value;
    }

    public String hostPackage() {
        state();
        String hostPackage_value = getParent().Define_String_hostPackage(this, null);
        return hostPackage_value;
    }

    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return indent();
    }

    @Override // soot.JastAddJ.ASTNode
    public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return this;
    }

    @Override // soot.JastAddJ.ASTNode
    public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
