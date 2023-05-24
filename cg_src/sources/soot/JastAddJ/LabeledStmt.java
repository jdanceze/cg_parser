package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/LabeledStmt.class */
public class LabeledStmt extends BranchTargetStmt implements Cloneable {
    protected String tokenString_Label;
    public int Labelstart;
    public int Labelend;
    protected Map targetOf_ContinueStmt_values;
    protected Map targetOf_BreakStmt_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean canCompleteNormally_value;
    protected soot.jimple.Stmt label_value;
    protected soot.jimple.Stmt end_label_value;
    protected Map lookupLabel_String_values;
    protected boolean canCompleteNormally_computed = false;
    protected boolean label_computed = false;
    protected boolean end_label_computed = false;

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.targetOf_ContinueStmt_values = null;
        this.targetOf_BreakStmt_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.label_computed = false;
        this.label_value = null;
        this.end_label_computed = false;
        this.end_label_value = null;
        this.lookupLabel_String_values = null;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public LabeledStmt clone() throws CloneNotSupportedException {
        LabeledStmt node = (LabeledStmt) super.clone();
        node.targetOf_ContinueStmt_values = null;
        node.targetOf_BreakStmt_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.label_computed = false;
        node.label_value = null;
        node.end_label_computed = false;
        node.end_label_value = null;
        node.lookupLabel_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            LabeledStmt node = clone();
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
    public void nameCheck() {
        LabeledStmt stmt = lookupLabel(getLabel());
        if (stmt != null && stmt.enclosingBodyDecl() == enclosingBodyDecl()) {
            error("Labels can not shadow labels in the same member");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append(String.valueOf(getLabel()) + ":");
        getStmt().toString(s);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        b.setLine(this);
        b.addLabel(label());
        getStmt().jimplify2(b);
        b.addLabel(end_label());
    }

    public LabeledStmt() {
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public LabeledStmt(String p0, Stmt p1) {
        setLabel(p0);
        setChild(p1, 0);
    }

    public LabeledStmt(Symbol p0, Stmt p1) {
        setLabel(p0);
        setChild(p1, 0);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setLabel(String value) {
        this.tokenString_Label = value;
    }

    public void setLabel(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setLabel is only valid for String lexemes");
        }
        this.tokenString_Label = (String) symbol.value;
        this.Labelstart = symbol.getStart();
        this.Labelend = symbol.getEnd();
    }

    public String getLabel() {
        return this.tokenString_Label != null ? this.tokenString_Label : "";
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
        return stmt.hasLabel() && stmt.getLabel().equals(getLabel());
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
        return stmt.hasLabel() && stmt.getLabel().equals(getLabel());
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
        if (!getStmt().isDAafter(v)) {
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
        if (!getStmt().isDUafter(v)) {
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
        return getStmt().canCompleteNormally() || reachableBreak();
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
        return getStmt().continue_label();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return getStmt().modifiedInScope(var);
    }

    public LabeledStmt lookupLabel(String name) {
        if (this.lookupLabel_String_values == null) {
            this.lookupLabel_String_values = new HashMap(4);
        }
        if (this.lookupLabel_String_values.containsKey(name)) {
            return (LabeledStmt) this.lookupLabel_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        LabeledStmt lookupLabel_String_value = getParent().Define_LabeledStmt_lookupLabel(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupLabel_String_values.put(name, lookupLabel_String_value);
        }
        return lookupLabel_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
        if (caller == getStmtNoTransform()) {
            return name.equals(getLabel()) ? this : lookupLabel(name);
        }
        return getParent().Define_LabeledStmt_lookupLabel(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getStmtNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getStmtNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
