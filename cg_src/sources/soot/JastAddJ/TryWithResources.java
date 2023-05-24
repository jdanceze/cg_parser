package soot.JastAddJ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/TryWithResources.class */
public class TryWithResources extends TryStmt implements Cloneable, VariableScope {
    protected Map localLookup_String_values;
    protected Map localVariableDeclaration_String_values;
    protected Map isDAafter_Variable_values;
    protected Map catchableException_TypeDecl_values;
    protected Map handlesException_TypeDecl_values;
    protected TypeDecl typeError_value;
    protected TypeDecl typeRuntimeException_value;
    protected Map lookupVariable_String_values;
    protected boolean typeError_computed = false;
    protected boolean typeRuntimeException_computed = false;

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.localLookup_String_values = null;
        this.localVariableDeclaration_String_values = null;
        this.isDAafter_Variable_values = null;
        this.catchableException_TypeDecl_values = null;
        this.handlesException_TypeDecl_values = null;
        this.typeError_computed = false;
        this.typeError_value = null;
        this.typeRuntimeException_computed = false;
        this.typeRuntimeException_value = null;
        this.lookupVariable_String_values = null;
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public TryWithResources clone() throws CloneNotSupportedException {
        TryWithResources node = (TryWithResources) super.clone();
        node.localLookup_String_values = null;
        node.localVariableDeclaration_String_values = null;
        node.isDAafter_Variable_values = null;
        node.catchableException_TypeDecl_values = null;
        node.handlesException_TypeDecl_values = null;
        node.typeError_computed = false;
        node.typeError_value = null;
        node.typeRuntimeException_computed = false;
        node.typeRuntimeException_value = null;
        node.lookupVariable_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            TryWithResources node = clone();
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
    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.ASTNode
    public void exceptionHandling() {
        Iterator<ResourceDeclaration> it = getResourceList().iterator();
        while (it.hasNext()) {
            ResourceDeclaration resource = it.next();
            MethodDecl close = lookupClose(resource);
            if (close != null) {
                Iterator<Access> it2 = close.getExceptionList().iterator();
                while (it2.hasNext()) {
                    Access exception = it2.next();
                    TypeDecl exceptionType = exception.type();
                    if (!twrHandlesException(exceptionType)) {
                        error("automatic closing of resource " + resource.name() + " may raise the uncaught exception " + exceptionType.fullName() + "; it must be caught or declared as being thrown");
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.ASTNode
    public boolean reachedException(TypeDecl catchType) {
        boolean found = false;
        for (int i = 0; i < getNumCatchClause() && !found; i++) {
            if (getCatchClause(i).handles(catchType)) {
                found = true;
            }
        }
        if (!found && ((!hasFinally() || getFinally().canCompleteNormally()) && catchableException(catchType))) {
            return true;
        }
        for (int i2 = 0; i2 < getNumCatchClause(); i2++) {
            if (getCatchClause(i2).reachedException(catchType)) {
                return true;
            }
        }
        return hasFinally() && getFinally().reachedException(catchType);
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.ASTNode
    public void toString(StringBuffer sb) {
        sb.append(String.valueOf(indent()) + "try (");
        Iterator<ResourceDeclaration> it = getResourceList().iterator();
        while (it.hasNext()) {
            ResourceDeclaration resource = it.next();
            sb.append(resource.toString());
        }
        sb.append(") ");
        getBlock().toString(sb);
        Iterator<CatchClause> it2 = getCatchClauseList().iterator();
        while (it2.hasNext()) {
            CatchClause cc = it2.next();
            sb.append(Instruction.argsep);
            cc.toString(sb);
        }
        if (hasFinally()) {
            sb.append(" finally ");
            getFinally().toString(sb);
        }
    }

    public TryWithResources() {
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 0);
        setChild(new List(), 2);
        setChild(new Opt(), 3);
    }

    public TryWithResources(List<ResourceDeclaration> p0, Block p1, List<CatchClause> p2, Opt<Block> p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
        setChild(p3, 3);
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 4;
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setResourceList(List<ResourceDeclaration> list) {
        setChild(list, 0);
    }

    public int getNumResource() {
        return getResourceList().getNumChild();
    }

    public int getNumResourceNoTransform() {
        return getResourceListNoTransform().getNumChildNoTransform();
    }

    public ResourceDeclaration getResource(int i) {
        return getResourceList().getChild(i);
    }

    public void addResource(ResourceDeclaration node) {
        List<ResourceDeclaration> list = (this.parent == null || state == null) ? getResourceListNoTransform() : getResourceList();
        list.addChild(node);
    }

    public void addResourceNoTransform(ResourceDeclaration node) {
        List<ResourceDeclaration> list = getResourceListNoTransform();
        list.addChild(node);
    }

    public void setResource(ResourceDeclaration node, int i) {
        List<ResourceDeclaration> list = getResourceList();
        list.setChild(node, i);
    }

    public List<ResourceDeclaration> getResources() {
        return getResourceList();
    }

    public List<ResourceDeclaration> getResourcesNoTransform() {
        return getResourceListNoTransform();
    }

    public List<ResourceDeclaration> getResourceList() {
        List<ResourceDeclaration> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<ResourceDeclaration> getResourceListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.TryStmt
    public void setBlock(Block node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.TryStmt
    public Block getBlock() {
        return (Block) getChild(1);
    }

    @Override // soot.JastAddJ.TryStmt
    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.TryStmt
    public void setCatchClauseList(List<CatchClause> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.TryStmt
    public int getNumCatchClause() {
        return getCatchClauseList().getNumChild();
    }

    @Override // soot.JastAddJ.TryStmt
    public int getNumCatchClauseNoTransform() {
        return getCatchClauseListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.TryStmt
    public CatchClause getCatchClause(int i) {
        return getCatchClauseList().getChild(i);
    }

    @Override // soot.JastAddJ.TryStmt
    public void addCatchClause(CatchClause node) {
        List<CatchClause> list = (this.parent == null || state == null) ? getCatchClauseListNoTransform() : getCatchClauseList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.TryStmt
    public void addCatchClauseNoTransform(CatchClause node) {
        List<CatchClause> list = getCatchClauseListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.TryStmt
    public void setCatchClause(CatchClause node, int i) {
        List<CatchClause> list = getCatchClauseList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.TryStmt
    public List<CatchClause> getCatchClauses() {
        return getCatchClauseList();
    }

    @Override // soot.JastAddJ.TryStmt
    public List<CatchClause> getCatchClausesNoTransform() {
        return getCatchClauseListNoTransform();
    }

    @Override // soot.JastAddJ.TryStmt
    public List<CatchClause> getCatchClauseList() {
        List<CatchClause> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.TryStmt
    public List<CatchClause> getCatchClauseListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.TryStmt
    public void setFinallyOpt(Opt<Block> opt) {
        setChild(opt, 3);
    }

    @Override // soot.JastAddJ.TryStmt
    public boolean hasFinally() {
        return getFinallyOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.TryStmt
    public Block getFinally() {
        return getFinallyOpt().getChild(0);
    }

    @Override // soot.JastAddJ.TryStmt
    public void setFinally(Block node) {
        getFinallyOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.TryStmt
    public Opt<Block> getFinallyOpt() {
        return (Opt) getChild(3);
    }

    @Override // soot.JastAddJ.TryStmt
    public Opt<Block> getFinallyOptNoTransform() {
        return (Opt) getChildNoTransform(3);
    }

    public boolean catchHandlesException(TypeDecl exceptionType) {
        state();
        for (int i = 0; i < getNumCatchClause(); i++) {
            if (getCatchClause(i).handles(exceptionType)) {
                return true;
            }
        }
        return false;
    }

    public boolean twrHandlesException(TypeDecl exceptionType) {
        state();
        if (catchHandlesException(exceptionType)) {
            return true;
        }
        if (hasFinally() && !getFinally().canCompleteNormally()) {
            return true;
        }
        return handlesException(exceptionType);
    }

    public MethodDecl lookupClose(ResourceDeclaration resource) {
        state();
        TypeDecl resourceType = resource.getTypeAccess().type();
        for (MethodDecl method : resourceType.memberMethods("close")) {
            if (method.getNumParameter() == 0) {
                return method;
            }
        }
        return null;
    }

    public SimpleSet localLookup(String name) {
        if (this.localLookup_String_values == null) {
            this.localLookup_String_values = new HashMap(4);
        }
        if (this.localLookup_String_values.containsKey(name)) {
            return (SimpleSet) this.localLookup_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet localLookup_String_value = localLookup_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.localLookup_String_values.put(name, localLookup_String_value);
        }
        return localLookup_String_value;
    }

    private SimpleSet localLookup_compute(String name) {
        VariableDeclaration v = localVariableDeclaration(name);
        return v != null ? v : lookupVariable(name);
    }

    public VariableDeclaration localVariableDeclaration(String name) {
        if (this.localVariableDeclaration_String_values == null) {
            this.localVariableDeclaration_String_values = new HashMap(4);
        }
        if (this.localVariableDeclaration_String_values.containsKey(name)) {
            return (VariableDeclaration) this.localVariableDeclaration_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        VariableDeclaration localVariableDeclaration_String_value = localVariableDeclaration_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.localVariableDeclaration_String_values.put(name, localVariableDeclaration_String_value);
        }
        return localVariableDeclaration_String_value;
    }

    private VariableDeclaration localVariableDeclaration_compute(String name) {
        Iterator<ResourceDeclaration> it = getResourceList().iterator();
        while (it.hasNext()) {
            ResourceDeclaration resource = it.next();
            if (resource.declaresVariable(name)) {
                return resource;
            }
        }
        return null;
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt
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

    public boolean resourceClosingException(TypeDecl catchType) {
        state();
        Iterator<ResourceDeclaration> it = getResourceList().iterator();
        while (it.hasNext()) {
            ResourceDeclaration resource = it.next();
            MethodDecl close = lookupClose(resource);
            if (close != null) {
                Iterator<Access> it2 = close.getExceptionList().iterator();
                while (it2.hasNext()) {
                    Access exception = it2.next();
                    exception.type();
                    if (catchType.mayCatch(exception.type())) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public boolean resourceInitializationException(TypeDecl catchType) {
        state();
        Iterator<ResourceDeclaration> it = getResourceList().iterator();
        while (it.hasNext()) {
            ResourceDeclaration resource = it.next();
            if (resource.reachedException(catchType)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TryStmt
    public boolean catchableException(TypeDecl type) {
        if (this.catchableException_TypeDecl_values == null) {
            this.catchableException_TypeDecl_values = new HashMap(4);
        }
        if (this.catchableException_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.catchableException_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean catchableException_TypeDecl_value = catchableException_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.catchableException_TypeDecl_values.put(type, Boolean.valueOf(catchableException_TypeDecl_value));
        }
        return catchableException_TypeDecl_value;
    }

    private boolean catchableException_compute(TypeDecl type) {
        return getBlock().reachedException(type) || resourceClosingException(type) || resourceInitializationException(type);
    }

    @Override // soot.JastAddJ.TryStmt
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

    @Override // soot.JastAddJ.TryStmt
    public TypeDecl typeError() {
        if (this.typeError_computed) {
            return this.typeError_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeError_value = getParent().Define_TypeDecl_typeError(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeError_computed = true;
        }
        return this.typeError_value;
    }

    @Override // soot.JastAddJ.TryStmt
    public TypeDecl typeRuntimeException() {
        if (this.typeRuntimeException_computed) {
            return this.typeRuntimeException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeRuntimeException_value = getParent().Define_TypeDecl_typeRuntimeException(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeRuntimeException_computed = true;
        }
        return this.typeRuntimeException_value;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.VariableScope
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

    public boolean resourcePreviouslyDeclared(String name) {
        state();
        boolean resourcePreviouslyDeclared_String_value = getParent().Define_boolean_resourcePreviouslyDeclared(this, null, name);
        return resourcePreviouslyDeclared_String_value;
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getBlockNoTransform()) {
            return twrHandlesException(exceptionType);
        }
        if (caller == getResourceListNoTransform()) {
            caller.getIndexOfChild(child);
            return twrHandlesException(exceptionType);
        }
        return super.Define_boolean_handlesException(caller, child, exceptionType);
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getCatchClauseListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            for (int i = 0; i < childIndex; i++) {
                if (getCatchClause(i).handles(exceptionType)) {
                    return false;
                }
            }
            if (catchableException(exceptionType) || exceptionType.mayCatch(typeError()) || exceptionType.mayCatch(typeRuntimeException())) {
                return true;
            }
            return false;
        }
        return super.Define_boolean_reachableCatchClause(caller, child, exceptionType);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getBlockNoTransform()) {
            return localLookup(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        if (caller == getResourceListNoTransform()) {
            caller.getIndexOfChild(child);
            return this;
        }
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
        if (caller == getResourceListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            for (int i = 0; i < index; i++) {
                if (getResource(i).name().equals(name)) {
                    return true;
                }
            }
            return false;
        }
        return getParent().Define_boolean_resourcePreviouslyDeclared(this, caller, name);
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockNoTransform()) {
            return getNumResource() == 0 ? isDAbefore(v) : getResource(getNumResource() - 1).isDAafter(v);
        } else if (caller == getResourceListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            return index == 0 ? isDAbefore(v) : getResource(index - 1).isDAafter(v);
        } else {
            return super.Define_boolean_isDAbefore(caller, child, v);
        }
    }

    @Override // soot.JastAddJ.TryStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
