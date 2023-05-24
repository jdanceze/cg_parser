package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/DoStmt.class */
public class DoStmt extends BranchTargetStmt implements Cloneable {
    protected Map targetOf_ContinueStmt_values;
    protected Map targetOf_BreakStmt_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map isDUbeforeCondition_Variable_values;
    protected boolean canCompleteNormally_value;
    protected soot.jimple.Stmt begin_label_value;
    protected soot.jimple.Stmt cond_label_value;
    protected soot.jimple.Stmt end_label_value;
    protected boolean canCompleteNormally_computed = false;
    protected boolean begin_label_computed = false;
    protected boolean cond_label_computed = false;
    protected boolean end_label_computed = false;

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.targetOf_ContinueStmt_values = null;
        this.targetOf_BreakStmt_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.isDUbeforeCondition_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.begin_label_computed = false;
        this.begin_label_value = null;
        this.cond_label_computed = false;
        this.cond_label_value = null;
        this.end_label_computed = false;
        this.end_label_value = null;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public DoStmt clone() throws CloneNotSupportedException {
        DoStmt node = (DoStmt) super.clone();
        node.targetOf_ContinueStmt_values = null;
        node.targetOf_BreakStmt_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.isDUbeforeCondition_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.begin_label_computed = false;
        node.begin_label_value = null;
        node.cond_label_computed = false;
        node.cond_label_value = null;
        node.end_label_computed = false;
        node.end_label_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            DoStmt node = clone();
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
        s.append(indent());
        s.append("do ");
        getStmt().toString(s);
        s.append("while(");
        getCondition().toString(s);
        s.append(");");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl cond = getCondition().type();
        if (!cond.isBoolean()) {
            error("the type of \"" + getCondition() + "\" is " + cond.name() + " which is not boolean");
        }
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        b.setLine(this);
        b.addLabel(begin_label());
        getStmt().jimplify2(b);
        b.addLabel(cond_label());
        getCondition().emitEvalBranch(b);
        if (canCompleteNormally()) {
            b.addLabel(end_label());
        }
    }

    public DoStmt() {
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public DoStmt(Stmt p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setStmt(Stmt node) {
        setChild(node, 0);
    }

    public Stmt getStmt() {
        return (Stmt) getChild(0);
    }

    public Stmt getStmtNoTransform() {
        return (Stmt) getChildNoTransform(0);
    }

    public void setCondition(Expr node) {
        setChild(node, 1);
    }

    public Expr getCondition() {
        return (Expr) getChild(1);
    }

    public Expr getConditionNoTransform() {
        return (Expr) getChildNoTransform(1);
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
        if (!getCondition().isDAafterFalse(v)) {
            return false;
        }
        for (BreakStmt stmt : targetBreaks()) {
            if (!stmt.isDAafterReachedFinallyBlocks(v)) {
                return false;
            }
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
        if (!isDUbeforeCondition(v) || !getCondition().isDUafterFalse(v)) {
            return false;
        }
        for (BreakStmt stmt : targetBreaks()) {
            if (!stmt.isDUafterReachedFinallyBlocks(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDUbeforeCondition(Variable v) {
        ASTNode.State.CircularValue _value;
        boolean new_isDUbeforeCondition_Variable_value;
        if (this.isDUbeforeCondition_Variable_values == null) {
            this.isDUbeforeCondition_Variable_values = new HashMap(4);
        }
        if (this.isDUbeforeCondition_Variable_values.containsKey(v)) {
            Object _o = this.isDUbeforeCondition_Variable_values.get(v);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.isDUbeforeCondition_Variable_values.put(v, _value);
            _value.value = true;
        }
        ASTNode.State state = state();
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
                state.CHANGE = false;
                new_isDUbeforeCondition_Variable_value = isDUbeforeCondition_compute(v);
                if (new_isDUbeforeCondition_Variable_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_isDUbeforeCondition_Variable_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.isDUbeforeCondition_Variable_values.remove(v);
                state.RESET_CYCLE = true;
                isDUbeforeCondition_compute(v);
                state.RESET_CYCLE = false;
            } else {
                this.isDUbeforeCondition_Variable_values.put(v, Boolean.valueOf(new_isDUbeforeCondition_Variable_value));
            }
            state.IN_CIRCLE = false;
            return new_isDUbeforeCondition_Variable_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_isDUbeforeCondition_Variable_value2 = isDUbeforeCondition_compute(v);
            if (state.RESET_CYCLE) {
                this.isDUbeforeCondition_Variable_values.remove(v);
            } else if (new_isDUbeforeCondition_Variable_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_isDUbeforeCondition_Variable_value2);
            }
            return new_isDUbeforeCondition_Variable_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean isDUbeforeCondition_compute(Variable v) {
        if (!getStmt().isDUafter(v)) {
            return false;
        }
        for (ContinueStmt stmt : targetContinues()) {
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
        if (!getStmt().canCompleteNormally() || (getCondition().isConstant() && getCondition().isTrue())) {
            return (reachableContinue() && !(getCondition().isConstant() && getCondition().isTrue())) || reachableBreak();
        }
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return true;
    }

    public soot.jimple.Stmt begin_label() {
        if (this.begin_label_computed) {
            return this.begin_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.begin_label_value = begin_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.begin_label_computed = true;
        }
        return this.begin_label_value;
    }

    private soot.jimple.Stmt begin_label_compute() {
        return newLabel();
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

    @Override // soot.JastAddJ.Stmt
    public soot.jimple.Stmt break_label() {
        state();
        return end_label();
    }

    @Override // soot.JastAddJ.Stmt
    public soot.jimple.Stmt continue_label() {
        state();
        return cond_label();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return getStmt().modifiedInScope(var);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getConditionNoTransform()) {
            if (!getStmt().isDAafter(v)) {
                return false;
            }
            for (ContinueStmt stmt : targetContinues()) {
                if (!stmt.isDAafterReachedFinallyBlocks(v)) {
                    return false;
                }
            }
            return true;
        } else if (caller == getStmtNoTransform()) {
            return isDAbefore(v);
        } else {
            return getParent().Define_boolean_isDAbefore(this, caller, v);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getConditionNoTransform()) {
            return isDUbeforeCondition(v);
        }
        if (caller == getStmtNoTransform()) {
            return isDUbefore(v) && getCondition().isDUafterTrue(v);
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
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reportUnreachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getConditionNoTransform()) {
            return end_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getConditionNoTransform()) {
            return begin_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
