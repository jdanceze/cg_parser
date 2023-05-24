package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.coffi.Instruction;
import soot.jimple.IntConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/EnhancedForStmt.class */
public class EnhancedForStmt extends BranchTargetStmt implements Cloneable, VariableScope {
    protected Map targetOf_ContinueStmt_values;
    protected Map targetOf_BreakStmt_values;
    protected boolean canCompleteNormally_value;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected soot.jimple.Stmt cond_label_value;
    protected soot.jimple.Stmt update_label_value;
    protected soot.jimple.Stmt end_label_value;
    protected int extraLocalIndex_value;
    protected boolean canCompleteNormally_computed = false;
    protected boolean cond_label_computed = false;
    protected boolean update_label_computed = false;
    protected boolean end_label_computed = false;
    protected boolean extraLocalIndex_computed = false;

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.targetOf_ContinueStmt_values = null;
        this.targetOf_BreakStmt_values = null;
        this.canCompleteNormally_computed = false;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.cond_label_computed = false;
        this.cond_label_value = null;
        this.update_label_computed = false;
        this.update_label_value = null;
        this.end_label_computed = false;
        this.end_label_value = null;
        this.extraLocalIndex_computed = false;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public EnhancedForStmt clone() throws CloneNotSupportedException {
        EnhancedForStmt node = (EnhancedForStmt) super.clone();
        node.targetOf_ContinueStmt_values = null;
        node.targetOf_BreakStmt_values = null;
        node.canCompleteNormally_computed = false;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.cond_label_computed = false;
        node.cond_label_value = null;
        node.update_label_computed = false;
        node.update_label_value = null;
        node.end_label_computed = false;
        node.end_label_value = null;
        node.extraLocalIndex_computed = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            EnhancedForStmt node = clone();
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
    public void typeCheck() {
        if (!getExpr().type().isArrayDecl() && !getExpr().type().isIterable()) {
            error("type " + getExpr().type().name() + " of expression in foreach is neither array type nor java.lang.Iterable");
        } else if (getExpr().type().isArrayDecl() && !getExpr().type().componentType().assignConversionTo(getVariableDeclaration().type(), null)) {
            error("parameter of type " + getVariableDeclaration().type().typeName() + " can not be assigned an element of type " + getExpr().type().componentType().typeName());
        } else if (getExpr().type().isIterable() && !getExpr().type().isUnknown()) {
            MethodDecl iterator = (MethodDecl) getExpr().type().memberMethods("iterator").iterator().next();
            MethodDecl next = (MethodDecl) iterator.type().memberMethods("next").iterator().next();
            TypeDecl componentType = next.type();
            if (!componentType.assignConversionTo(getVariableDeclaration().type(), null)) {
                error("parameter of type " + getVariableDeclaration().type().typeName() + " can not be assigned an element of type " + componentType.typeName());
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("for (");
        getVariableDeclaration().getModifiers().toString(s);
        getVariableDeclaration().getTypeAccess().toString(s);
        s.append(Instruction.argsep + getVariableDeclaration().name());
        s.append(" : ");
        getExpr().toString(s);
        s.append(") ");
        getStmt().toString(s);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        if (getExpr().type().isArrayDecl()) {
            Local array = asLocal(b, getExpr().eval(b));
            Local index = asLocal(b, IntConstant.v(0));
            Local parameter = b.newLocal(getVariableDeclaration().name(), getVariableDeclaration().type().getSootType());
            getVariableDeclaration().local = parameter;
            b.setLine(this);
            b.addLabel(cond_label());
            b.add(b.newIfStmt(b.newGeExpr(asImmediate(b, index), asImmediate(b, b.newLengthExpr(asImmediate(b, array), this)), this), end_label(), this));
            b.add(b.newAssignStmt(parameter, asRValue(b, getExpr().type().elementType().emitCastTo(b, asLocal(b, b.newArrayRef(array, index, this)), getVariableDeclaration().type(), this)), this));
            getStmt().jimplify2(b);
            b.addLabel(update_label());
            b.add(b.newAssignStmt(index, b.newAddExpr(index, IntConstant.v(1), this), this));
            b.add(b.newGotoStmt(cond_label(), this));
            b.addLabel(end_label());
            return;
        }
        Local iterator = asLocal(b, b.newInterfaceInvokeExpr(asLocal(b, getExpr().eval(b)), iteratorMethod().sootRef(), new ArrayList(), this));
        Local parameter2 = b.newLocal(getVariableDeclaration().name(), getVariableDeclaration().type().getSootType());
        getVariableDeclaration().local = parameter2;
        b.addLabel(cond_label());
        b.add(b.newIfStmt(b.newEqExpr(asImmediate(b, b.newInterfaceInvokeExpr(iterator, hasNextMethod().sootRef(), new ArrayList(), this)), BooleanType.emitConstant(false), this), end_label(), this));
        b.add(b.newAssignStmt(parameter2, nextMethod().type().emitCastTo(b, b.newInterfaceInvokeExpr(iterator, nextMethod().sootRef(), new ArrayList(), this), getVariableDeclaration().type(), this), this));
        getStmt().jimplify2(b);
        b.addLabel(update_label());
        b.add(b.newGotoStmt(cond_label(), this));
        b.addLabel(end_label());
    }

    private MethodDecl iteratorMethod() {
        TypeDecl typeDecl = lookupType("java.lang", "Iterable");
        for (MethodDecl m : typeDecl.memberMethods("iterator")) {
            if (m.getNumParameter() == 0) {
                return m;
            }
        }
        throw new Error("Could not find java.lang.Iterable.iterator()");
    }

    private MethodDecl hasNextMethod() {
        TypeDecl typeDecl = lookupType("java.util", "Iterator");
        for (MethodDecl m : typeDecl.memberMethods("hasNext")) {
            if (m.getNumParameter() == 0) {
                return m;
            }
        }
        throw new Error("Could not find java.util.Collection.hasNext()");
    }

    private MethodDecl nextMethod() {
        TypeDecl typeDecl = lookupType("java.util", "Iterator");
        for (MethodDecl m : typeDecl.memberMethods("next")) {
            if (m.getNumParameter() == 0) {
                return m;
            }
        }
        throw new Error("Could not find java.util.Collection.next()");
    }

    public EnhancedForStmt() {
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
    }

    public EnhancedForStmt(VariableDeclaration p0, Expr p1, Stmt p2) {
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setVariableDeclaration(VariableDeclaration node) {
        setChild(node, 0);
    }

    public VariableDeclaration getVariableDeclaration() {
        return (VariableDeclaration) getChild(0);
    }

    public VariableDeclaration getVariableDeclarationNoTransform() {
        return (VariableDeclaration) getChildNoTransform(0);
    }

    public void setExpr(Expr node) {
        setChild(node, 1);
    }

    public Expr getExpr() {
        return (Expr) getChild(1);
    }

    public Expr getExprNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    public void setStmt(Stmt node) {
        setChild(node, 2);
    }

    public Stmt getStmt() {
        return (Stmt) getChild(2);
    }

    public Stmt getStmtNoTransform() {
        return (Stmt) getChildNoTransform(2);
    }

    public SimpleSet localLookupVariable(String name) {
        state();
        if (getVariableDeclaration().name().equals(name)) {
            return SimpleSet.emptySet.add(getVariableDeclaration());
        }
        return lookupVariable(name);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.BranchPropagation
    public boolean targetOf(ContinueStmt stmt) {
        if (this.targetOf_ContinueStmt_values == null) {
            this.targetOf_ContinueStmt_values = new HashMap(4);
        }
        if (this.targetOf_ContinueStmt_values.containsKey(stmt)) {
            return ((Boolean) this.targetOf_ContinueStmt_values.get(stmt)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean targetOf_ContinueStmt_value = targetOf_compute(stmt);
        if (isFinal && num == state().boundariesCrossed) {
            this.targetOf_ContinueStmt_values.put(stmt, Boolean.valueOf(targetOf_ContinueStmt_value));
        }
        return targetOf_ContinueStmt_value;
    }

    private boolean targetOf_compute(ContinueStmt stmt) {
        return !stmt.hasLabel();
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.BranchPropagation
    public boolean targetOf(BreakStmt stmt) {
        if (this.targetOf_BreakStmt_values == null) {
            this.targetOf_BreakStmt_values = new HashMap(4);
        }
        if (this.targetOf_BreakStmt_values.containsKey(stmt)) {
            return ((Boolean) this.targetOf_BreakStmt_values.get(stmt)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean targetOf_BreakStmt_value = targetOf_compute(stmt);
        if (isFinal && num == state().boundariesCrossed) {
            this.targetOf_BreakStmt_values.put(stmt, Boolean.valueOf(targetOf_BreakStmt_value));
        }
        return targetOf_BreakStmt_value;
    }

    private boolean targetOf_compute(BreakStmt stmt) {
        return !stmt.hasLabel();
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
        return reachable();
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
        if (!getExpr().isDAafter(v)) {
            return false;
        }
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
        if (!getExpr().isDUafter(v)) {
            return false;
        }
        for (BreakStmt stmt : targetBreaks()) {
            if (!stmt.isDUafterReachedFinallyBlocks(v)) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean continueLabel() {
        state();
        return true;
    }

    public soot.jimple.Stmt cond_label() {
        if (this.cond_label_computed) {
            return this.cond_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.cond_label_value = cond_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.cond_label_computed = true;
        }
        return this.cond_label_value;
    }

    private soot.jimple.Stmt cond_label_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt update_label() {
        if (this.update_label_computed) {
            return this.update_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.update_label_value = update_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.update_label_computed = true;
        }
        return this.update_label_value;
    }

    private soot.jimple.Stmt update_label_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt end_label() {
        if (this.end_label_computed) {
            return this.end_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.end_label_value = end_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.end_label_computed = true;
        }
        return this.end_label_value;
    }

    private soot.jimple.Stmt end_label_compute() {
        return newLabel();
    }

    public int extraLocalIndex() {
        if (this.extraLocalIndex_computed) {
            return this.extraLocalIndex_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.extraLocalIndex_value = extraLocalIndex_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.extraLocalIndex_computed = true;
        }
        return this.extraLocalIndex_value;
    }

    private int extraLocalIndex_compute() {
        return localNum();
    }

    @Override // soot.JastAddJ.Stmt
    public soot.jimple.Stmt break_label() {
        state();
        return end_label();
    }

    @Override // soot.JastAddJ.Stmt
    public soot.jimple.Stmt continue_label() {
        state();
        return update_label();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return getStmt().modifiedInScope(var);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.VariableScope
    public SimpleSet lookupVariable(String name) {
        state();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        return lookupVariable_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getStmtNoTransform()) {
            return localLookupVariable(name);
        }
        if (caller == getExprNoTransform()) {
            return localLookupVariable(name);
        }
        if (caller == getVariableDeclarationNoTransform()) {
            return localLookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getVariableDeclarationNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return this;
        }
        if (caller == getExprNoTransform()) {
            return this;
        }
        if (caller == getVariableDeclarationNoTransform()) {
            return this;
        }
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
        if (caller == getVariableDeclarationNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isMethodParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
        if (caller == getVariableDeclarationNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isConstructorParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
        if (caller == getVariableDeclarationNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getStmtNoTransform()) {
            return getExpr().isDAafter(v);
        }
        if (caller == getExprNoTransform()) {
            return v == getVariableDeclaration() || isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getStmtNoTransform()) {
            return getExpr().isDUafter(v);
        }
        if (caller == getExprNoTransform()) {
            return v != getVariableDeclaration() && isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_insideLoop(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public int Define_int_localNum(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return getVariableDeclaration().localNum() + getVariableDeclaration().type().size();
        }
        if (caller == getVariableDeclarationNoTransform()) {
            return localNum() + (getExpr().type().isArrayDecl() ? 2 : 1);
        }
        return getParent().Define_int_localNum(this, caller);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
