package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MultiCatch.class */
public class MultiCatch extends CatchClause implements Cloneable {
    protected Map parameterDeclaration_String_values;

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.parameterDeclaration_String_values = null;
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode, beaver.Symbol
    public MultiCatch clone() throws CloneNotSupportedException {
        MultiCatch node = (MultiCatch) super.clone();
        node.parameterDeclaration_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            MultiCatch node = clone();
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
    public void toString(StringBuffer sb) {
        sb.append("catch (");
        getParameter().toString(sb);
        sb.append(") ");
        getBlock().toString(sb);
    }

    @Override // soot.JastAddJ.ASTNode
    void checkUnreachableStmt() {
        if (!getBlock().reachable() && reportUnreachable()) {
            error("the exception " + getParameter().type().fullName() + " is not thrown in the body of the try statement");
        }
    }

    public MultiCatch() {
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public MultiCatch(CatchParameterDeclaration p0, Block p1) {
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

    public void setParameter(CatchParameterDeclaration node) {
        setChild(node, 0);
    }

    public CatchParameterDeclaration getParameter() {
        return (CatchParameterDeclaration) getChild(0);
    }

    public CatchParameterDeclaration getParameterNoTransform() {
        return (CatchParameterDeclaration) getChildNoTransform(0);
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

    @Override // soot.JastAddJ.CatchClause
    public boolean handles(TypeDecl exceptionType) {
        state();
        CatchParameterDeclaration param = getParameter();
        for (int i = 0; i < param.getNumTypeAccess(); i++) {
            TypeDecl type = param.getTypeAccess(i).type();
            if (!type.isUnknown() && exceptionType.instanceOf(type)) {
                return true;
            }
        }
        return false;
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

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getParameterNoTransform()) {
            return parameterDeclaration(name);
        }
        return super.Define_SimpleSet_lookupVariable(caller, child, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            boolean anyReachable = false;
            CatchParameterDeclaration param = getParameter();
            for (int i = 0; i < param.getNumTypeAccess(); i++) {
                TypeDecl type = param.getTypeAccess(i).type();
                if (!reachableCatchClause(type)) {
                    error("The exception type " + type.fullName() + " can not be caught by this multi-catch clause");
                } else {
                    anyReachable = true;
                }
            }
            return anyReachable;
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.CatchClause, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
