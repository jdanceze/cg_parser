package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BreakStmt.class */
public class BreakStmt extends Stmt implements Cloneable {
    protected String tokenString_Label;
    public int Labelstart;
    public int Labelend;
    protected Stmt targetStmt_value;
    protected ArrayList finallyList_value;
    protected Map isDAafter_Variable_values;
    protected Map isDUafterReachedFinallyBlocks_Variable_values;
    protected Map isDAafterReachedFinallyBlocks_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean canCompleteNormally_value;
    protected boolean inSynchronizedBlock_value;
    protected Map lookupLabel_String_values;
    protected boolean targetStmt_computed = false;
    protected boolean finallyList_computed = false;
    protected boolean canCompleteNormally_computed = false;
    protected boolean inSynchronizedBlock_computed = false;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.targetStmt_computed = false;
        this.targetStmt_value = null;
        this.finallyList_computed = false;
        this.finallyList_value = null;
        this.isDAafter_Variable_values = null;
        this.isDUafterReachedFinallyBlocks_Variable_values = null;
        this.isDAafterReachedFinallyBlocks_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.inSynchronizedBlock_computed = false;
        this.lookupLabel_String_values = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public BreakStmt clone() throws CloneNotSupportedException {
        BreakStmt node = (BreakStmt) super.clone();
        node.targetStmt_computed = false;
        node.targetStmt_value = null;
        node.finallyList_computed = false;
        node.finallyList_value = null;
        node.isDAafter_Variable_values = null;
        node.isDUafterReachedFinallyBlocks_Variable_values = null;
        node.isDAafterReachedFinallyBlocks_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.inSynchronizedBlock_computed = false;
        node.lookupLabel_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            BreakStmt node = clone();
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

    @Override // soot.JastAddJ.ASTNode, soot.JastAddJ.BranchPropagation
    public void collectBranches(Collection c) {
        c.add(this);
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (!hasLabel() && !insideLoop() && !insideSwitch()) {
            error("break outside switch or loop");
        } else if (hasLabel()) {
            LabeledStmt label = lookupLabel(getLabel());
            if (label == null) {
                error("labeled break must have visible matching label");
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("break ");
        if (hasLabel()) {
            s.append(getLabel());
        }
        s.append(";");
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        ArrayList list = exceptionRanges();
        if (!inSynchronizedBlock()) {
            endExceptionRange(b, list);
        }
        Iterator iter = finallyList().iterator();
        while (iter.hasNext()) {
            FinallyHost stmt = (FinallyHost) iter.next();
            stmt.emitFinallyCode(b);
        }
        if (inSynchronizedBlock()) {
            endExceptionRange(b, list);
        }
        b.setLine(this);
        b.add(b.newGotoStmt(targetStmt().break_label(), this));
        beginExceptionRange(b, list);
    }

    public BreakStmt() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public BreakStmt(String p0) {
        setLabel(p0);
    }

    public BreakStmt(Symbol p0) {
        setLabel(p0);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
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

    public boolean hasLabel() {
        state();
        return !getLabel().equals("");
    }

    public Stmt targetStmt() {
        if (this.targetStmt_computed) {
            return this.targetStmt_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.targetStmt_value = targetStmt_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.targetStmt_computed = true;
        }
        return this.targetStmt_value;
    }

    private Stmt targetStmt_compute() {
        return branchTarget(this);
    }

    public ArrayList finallyList() {
        if (this.finallyList_computed) {
            return this.finallyList_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.finallyList_value = finallyList_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.finallyList_computed = true;
        }
        return this.finallyList_value;
    }

    private ArrayList finallyList_compute() {
        ArrayList list = new ArrayList();
        collectFinally(this, list);
        return list;
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
        return true;
    }

    public boolean isDUafterReachedFinallyBlocks(Variable v) {
        if (this.isDUafterReachedFinallyBlocks_Variable_values == null) {
            this.isDUafterReachedFinallyBlocks_Variable_values = new HashMap(4);
        }
        if (this.isDUafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUafterReachedFinallyBlocks_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUafterReachedFinallyBlocks_Variable_value = isDUafterReachedFinallyBlocks_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUafterReachedFinallyBlocks_Variable_values.put(v, Boolean.valueOf(isDUafterReachedFinallyBlocks_Variable_value));
        }
        return isDUafterReachedFinallyBlocks_Variable_value;
    }

    private boolean isDUafterReachedFinallyBlocks_compute(Variable v) {
        if (!isDUbefore(v) && finallyList().isEmpty()) {
            return false;
        }
        Iterator iter = finallyList().iterator();
        while (iter.hasNext()) {
            FinallyHost f = (FinallyHost) iter.next();
            if (!f.isDUafterFinally(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDAafterReachedFinallyBlocks(Variable v) {
        if (this.isDAafterReachedFinallyBlocks_Variable_values == null) {
            this.isDAafterReachedFinallyBlocks_Variable_values = new HashMap(4);
        }
        if (this.isDAafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafterReachedFinallyBlocks_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafterReachedFinallyBlocks_Variable_value = isDAafterReachedFinallyBlocks_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafterReachedFinallyBlocks_Variable_values.put(v, Boolean.valueOf(isDAafterReachedFinallyBlocks_Variable_value));
        }
        return isDAafterReachedFinallyBlocks_Variable_value;
    }

    private boolean isDAafterReachedFinallyBlocks_compute(Variable v) {
        if (isDAbefore(v)) {
            return true;
        }
        if (finallyList().isEmpty()) {
            return false;
        }
        Iterator iter = finallyList().iterator();
        while (iter.hasNext()) {
            FinallyHost f = (FinallyHost) iter.next();
            if (!f.isDAafterFinally(v)) {
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
        return false;
    }

    public boolean inSynchronizedBlock() {
        if (this.inSynchronizedBlock_computed) {
            return this.inSynchronizedBlock_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.inSynchronizedBlock_value = inSynchronizedBlock_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.inSynchronizedBlock_computed = true;
        }
        return this.inSynchronizedBlock_value;
    }

    private boolean inSynchronizedBlock_compute() {
        return !finallyList().isEmpty() && (finallyList().iterator().next() instanceof SynchronizedStmt);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return false;
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

    public boolean insideLoop() {
        state();
        boolean insideLoop_value = getParent().Define_boolean_insideLoop(this, null);
        return insideLoop_value;
    }

    public boolean insideSwitch() {
        state();
        boolean insideSwitch_value = getParent().Define_boolean_insideSwitch(this, null);
        return insideSwitch_value;
    }

    public ArrayList exceptionRanges() {
        state();
        ArrayList exceptionRanges_value = getParent().Define_ArrayList_exceptionRanges(this, null);
        return exceptionRanges_value;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
