package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/IfStmt.class */
public class IfStmt extends Stmt implements Cloneable {
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean canCompleteNormally_computed;
    protected boolean canCompleteNormally_value;
    protected boolean else_branch_label_computed;
    protected soot.jimple.Stmt else_branch_label_value;
    protected boolean then_branch_label_computed;
    protected soot.jimple.Stmt then_branch_label_value;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.else_branch_label_computed = false;
        this.else_branch_label_value = null;
        this.then_branch_label_computed = false;
        this.then_branch_label_value = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public IfStmt clone() throws CloneNotSupportedException {
        IfStmt node = (IfStmt) super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.else_branch_label_computed = false;
        node.else_branch_label_value = null;
        node.then_branch_label_computed = false;
        node.then_branch_label_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            IfStmt node = clone();
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

    public IfStmt(Expr cond, Stmt thenBranch) {
        this(cond, thenBranch, new Opt());
    }

    public IfStmt(Expr cond, Stmt thenBranch, Stmt elseBranch) {
        this(cond, thenBranch, new Opt(elseBranch));
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("if(");
        getCondition().toString(s);
        s.append(") ");
        getThen().toString(s);
        if (hasElse()) {
            s.append(indent());
            s.append("else ");
            getElse().toString(s);
        }
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
        soot.jimple.Stmt endBranch = newLabel();
        if (getCondition().isConstant()) {
            if (getCondition().isTrue()) {
                getThen().jimplify2(b);
            } else if (getCondition().isFalse() && hasElse()) {
                getElse().jimplify2(b);
            }
        } else {
            soot.jimple.Stmt elseBranch = else_branch_label();
            soot.jimple.Stmt thenBranch = then_branch_label();
            getCondition().emitEvalBranch(b);
            b.addLabel(thenBranch);
            getThen().jimplify2(b);
            if (getThen().canCompleteNormally() && hasElse()) {
                b.setLine(this);
                b.add(b.newGotoStmt(endBranch, this));
            }
            b.addLabel(elseBranch);
            if (hasElse()) {
                getElse().jimplify2(b);
            }
        }
        if (getThen().canCompleteNormally() && hasElse()) {
            b.addLabel(endBranch);
        }
    }

    public IfStmt() {
        this.canCompleteNormally_computed = false;
        this.else_branch_label_computed = false;
        this.then_branch_label_computed = false;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 2);
    }

    public IfStmt(Expr p0, Stmt p1, Opt<Stmt> p2) {
        this.canCompleteNormally_computed = false;
        this.else_branch_label_computed = false;
        this.then_branch_label_computed = false;
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setCondition(Expr node) {
        setChild(node, 0);
    }

    public Expr getCondition() {
        return (Expr) getChild(0);
    }

    public Expr getConditionNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void setThen(Stmt node) {
        setChild(node, 1);
    }

    public Stmt getThen() {
        return (Stmt) getChild(1);
    }

    public Stmt getThenNoTransform() {
        return (Stmt) getChildNoTransform(1);
    }

    public void setElseOpt(Opt<Stmt> opt) {
        setChild(opt, 2);
    }

    public boolean hasElse() {
        return getElseOpt().getNumChild() != 0;
    }

    public Stmt getElse() {
        return getElseOpt().getChild(0);
    }

    public void setElse(Stmt node) {
        getElseOpt().setChild(node, 0);
    }

    public Opt<Stmt> getElseOpt() {
        return (Opt) getChild(2);
    }

    public Opt<Stmt> getElseOptNoTransform() {
        return (Opt) getChildNoTransform(2);
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
        return hasElse() ? getThen().isDAafter(v) && getElse().isDAafter(v) : getThen().isDAafter(v) && getCondition().isDAafterFalse(v);
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
        return hasElse() ? getThen().isDUafter(v) && getElse().isDUafter(v) : getThen().isDUafter(v) && getCondition().isDUafterFalse(v);
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
        if ((!reachable() || hasElse()) && !getThen().canCompleteNormally()) {
            return hasElse() && getElse().canCompleteNormally();
        }
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return true;
    }

    public soot.jimple.Stmt else_branch_label() {
        if (this.else_branch_label_computed) {
            return this.else_branch_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.else_branch_label_value = else_branch_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.else_branch_label_computed = true;
        }
        return this.else_branch_label_value;
    }

    private soot.jimple.Stmt else_branch_label_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt then_branch_label() {
        if (this.then_branch_label_computed) {
            return this.then_branch_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.then_branch_label_value = then_branch_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.then_branch_label_computed = true;
        }
        return this.then_branch_label_value;
    }

    private soot.jimple.Stmt then_branch_label_compute() {
        return newLabel();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        if (getThen().modifiedInScope(var)) {
            return true;
        }
        return hasElse() && getElse().modifiedInScope(var);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getElseOptNoTransform()) {
            return getCondition().isDAafterFalse(v);
        }
        if (caller == getThenNoTransform()) {
            return getCondition().isDAafterTrue(v);
        }
        if (caller == getConditionNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getElseOptNoTransform()) {
            return getCondition().isDUafterFalse(v);
        }
        if (caller == getThenNoTransform()) {
            return getCondition().isDUafterTrue(v);
        }
        if (caller == getConditionNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getElseOptNoTransform()) {
            return reachable();
        }
        if (caller == getThenNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if (caller == getElseOptNoTransform()) {
            return reachable();
        }
        if (caller == getThenNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reportUnreachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getConditionNoTransform()) {
            return else_branch_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getConditionNoTransform()) {
            return then_branch_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
