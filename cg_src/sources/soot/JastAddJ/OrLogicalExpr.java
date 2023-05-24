package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/OrLogicalExpr.class */
public class OrLogicalExpr extends LogicalExpr implements Cloneable {
    protected Map isDAafterTrue_Variable_values;
    protected Map isDAafterFalse_Variable_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean next_test_label_computed = false;
    protected soot.jimple.Stmt next_test_label_value;

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafterTrue_Variable_values = null;
        this.isDAafterFalse_Variable_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.next_test_label_computed = false;
        this.next_test_label_value = null;
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public OrLogicalExpr clone() throws CloneNotSupportedException {
        OrLogicalExpr node = (OrLogicalExpr) super.clone();
        node.isDAafterTrue_Variable_values = null;
        node.isDAafterFalse_Variable_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.next_test_label_computed = false;
        node.next_test_label_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            OrLogicalExpr node = clone();
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

    @Override // soot.JastAddJ.Expr
    public void emitEvalBranch(Body b) {
        b.setLine(this);
        getLeftOperand().emitEvalBranch(b);
        b.addLabel(next_test_label());
        if (getLeftOperand().canBeFalse()) {
            getRightOperand().emitEvalBranch(b);
            if (getRightOperand().canBeFalse()) {
                b.add(b.newGotoStmt(false_label(), this));
            }
        }
    }

    public OrLogicalExpr() {
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public OrLogicalExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary
    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary
    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary
    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary
    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary
    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary
    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return Constant.create(left().constant().booleanValue() || right().constant().booleanValue());
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        if (this.isDAafterTrue_Variable_values == null) {
            this.isDAafterTrue_Variable_values = new HashMap(4);
        }
        if (this.isDAafterTrue_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafterTrue_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafterTrue_Variable_value = isDAafterTrue_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafterTrue_Variable_values.put(v, Boolean.valueOf(isDAafterTrue_Variable_value));
        }
        return isDAafterTrue_Variable_value;
    }

    private boolean isDAafterTrue_compute(Variable v) {
        return (getLeftOperand().isDAafterTrue(v) && getRightOperand().isDAafterTrue(v)) || isFalse();
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        if (this.isDAafterFalse_Variable_values == null) {
            this.isDAafterFalse_Variable_values = new HashMap(4);
        }
        if (this.isDAafterFalse_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafterFalse_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafterFalse_Variable_value = isDAafterFalse_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafterFalse_Variable_values.put(v, Boolean.valueOf(isDAafterFalse_Variable_value));
        }
        return isDAafterFalse_Variable_value;
    }

    private boolean isDAafterFalse_compute(Variable v) {
        return getRightOperand().isDAafterFalse(v) || isTrue();
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr
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
        return isDAafterTrue(v) && isDAafterFalse(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterTrue(Variable v) {
        state();
        return getLeftOperand().isDUafterTrue(v) && getRightOperand().isDUafterTrue(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterFalse(Variable v) {
        state();
        return getRightOperand().isDUafterFalse(v);
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr
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
        return isDUafterTrue(v) && isDUafterFalse(v);
    }

    @Override // soot.JastAddJ.Binary
    public String printOp() {
        state();
        return " || ";
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeTrue() {
        state();
        return getLeftOperand().canBeTrue() || getRightOperand().canBeTrue();
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeFalse() {
        state();
        return getLeftOperand().canBeFalse() && getRightOperand().canBeFalse();
    }

    public soot.jimple.Stmt next_test_label() {
        if (this.next_test_label_computed) {
            return this.next_test_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.next_test_label_value = next_test_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.next_test_label_computed = true;
        }
        return this.next_test_label_value;
    }

    private soot.jimple.Stmt next_test_label_compute() {
        return newLabel();
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getRightOperandNoTransform()) {
            return getLeftOperand().isDAafterFalse(v);
        }
        if (caller == getLeftOperandNoTransform()) {
            return isDAbefore(v);
        }
        return super.Define_boolean_isDAbefore(caller, child, v);
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getRightOperandNoTransform()) {
            return getLeftOperand().isDUafterFalse(v);
        }
        if (caller == getLeftOperandNoTransform()) {
            return isDUbefore(v);
        }
        return super.Define_boolean_isDUbefore(caller, child, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getRightOperandNoTransform()) {
            return false_label();
        }
        if (caller == getLeftOperandNoTransform()) {
            return next_test_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getRightOperandNoTransform()) {
            return true_label();
        }
        if (caller == getLeftOperandNoTransform()) {
            return true_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.LogicalExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
