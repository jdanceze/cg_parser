package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BasicCatch.class */
public class BasicCatch extends CatchClause implements Cloneable {
    protected Map parameterDeclaration_String_values;
    protected boolean label_computed = false;
    protected soot.jimple.Stmt label_value;

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.parameterDeclaration_String_values = null;
        this.label_computed = false;
        this.label_value = null;
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode, beaver.Symbol
    public BasicCatch clone() throws CloneNotSupportedException {
        BasicCatch node = (BasicCatch) super.clone();
        node.parameterDeclaration_String_values = null;
        node.label_computed = false;
        node.label_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            BasicCatch node = clone();
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
        s.append("catch (");
        getParameter().toString(s);
        s.append(") ");
        getBlock().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getParameter().type().instanceOf(typeThrowable())) {
            error("*** The catch variable must extend Throwable");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        b.addLabel(label());
        Local local = b.newLocal(getParameter().name(), getParameter().type().getSootType());
        b.setLine(this);
        b.add(b.newIdentityStmt(local, b.newCaughtExceptionRef(getParameter()), this));
        getParameter().local = local;
        getBlock().jimplify2(b);
    }

    @Override // soot.JastAddJ.ASTNode
    void checkUnreachableStmt() {
        if (!getBlock().reachable() && reportUnreachable()) {
            error("the exception " + getParameter().type().fullName() + " is not thrown in the body of the try statement");
        }
    }

    public BasicCatch() {
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public BasicCatch(ParameterDeclaration p0, Block p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setParameter(ParameterDeclaration node) {
        setChild(node, 0);
    }

    public ParameterDeclaration getParameter() {
        return (ParameterDeclaration) getChild(0);
    }

    public ParameterDeclaration getParameterNoTransform() {
        return (ParameterDeclaration) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.CatchClause
    public void setBlock(Block node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.CatchClause
    public Block getBlock() {
        return (Block) getChild(1);
    }

    @Override // soot.JastAddJ.CatchClause
    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.CatchClause
    public boolean handles(TypeDecl exceptionType) {
        state();
        return !getParameter().type().isUnknown() && exceptionType.instanceOf(getParameter().type());
    }

    @Override // soot.JastAddJ.CatchClause
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
        return getParameter().name().equals(name) ? getParameter() : SimpleSet.emptySet;
    }

    public soot.jimple.Stmt label() {
        if (this.label_computed) {
            return this.label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_value = label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_computed = true;
        }
        return this.label_value;
    }

    private soot.jimple.Stmt label_compute() {
        return newLabel();
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getParameterNoTransform()) {
            return parameterDeclaration(name);
        }
        return super.Define_SimpleSet_lookupVariable(caller, child, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        if (caller == getParameterNoTransform()) {
            return this;
        }
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getParameterNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return reachableCatchClause(getParameter().type());
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isMethodParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isConstructorParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
        if (caller == getParameterNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
        if (caller == getParameterNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_variableArityValid(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
        if (caller == getParameterNoTransform()) {
            return getBlock().modifiedInScope(var);
        }
        return getParent().Define_boolean_inhModifiedInScope(this, caller, var);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
        if (caller == getParameterNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isCatchParam(this, caller);
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
