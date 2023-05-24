package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BranchTargetStmt.class */
public abstract class BranchTargetStmt extends Stmt implements Cloneable, BranchPropagation {
    protected boolean reachableBreak_value;
    protected boolean reachableContinue_value;
    protected Collection targetBranches_value;
    protected Collection escapedBranches_value;
    protected Collection branches_value;
    protected Collection targetContinues_value;
    protected Collection targetBreaks_value;
    protected boolean reachableBreak_computed = false;
    protected boolean reachableContinue_computed = false;
    protected boolean targetBranches_computed = false;
    protected boolean escapedBranches_computed = false;
    protected boolean branches_computed = false;
    protected boolean targetContinues_computed = false;
    protected boolean targetBreaks_computed = false;

    @Override // soot.JastAddJ.BranchPropagation
    public abstract boolean targetOf(ContinueStmt continueStmt);

    @Override // soot.JastAddJ.BranchPropagation
    public abstract boolean targetOf(BreakStmt breakStmt);

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.reachableBreak_computed = false;
        this.reachableContinue_computed = false;
        this.targetBranches_computed = false;
        this.targetBranches_value = null;
        this.escapedBranches_computed = false;
        this.escapedBranches_value = null;
        this.branches_computed = false;
        this.branches_value = null;
        this.targetContinues_computed = false;
        this.targetContinues_value = null;
        this.targetBreaks_computed = false;
        this.targetBreaks_value = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public BranchTargetStmt clone() throws CloneNotSupportedException {
        BranchTargetStmt node = (BranchTargetStmt) super.clone();
        node.reachableBreak_computed = false;
        node.reachableContinue_computed = false;
        node.targetBranches_computed = false;
        node.targetBranches_value = null;
        node.escapedBranches_computed = false;
        node.escapedBranches_value = null;
        node.branches_computed = false;
        node.branches_value = null;
        node.targetContinues_computed = false;
        node.targetContinues_value = null;
        node.targetBreaks_computed = false;
        node.targetBreaks_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode, soot.JastAddJ.BranchPropagation
    public void collectBranches(Collection c) {
        c.addAll(escapedBranches());
    }

    @Override // soot.JastAddJ.ASTNode, soot.JastAddJ.BranchPropagation
    public Stmt branchTarget(Stmt branchStmt) {
        if (targetBranches().contains(branchStmt)) {
            return this;
        }
        return super.branchTarget(branchStmt);
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectFinally(Stmt branchStmt, ArrayList list) {
        if (targetBranches().contains(branchStmt)) {
            return;
        }
        super.collectFinally(branchStmt, list);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public boolean reachableBreak() {
        if (this.reachableBreak_computed) {
            return this.reachableBreak_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.reachableBreak_value = reachableBreak_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.reachableBreak_computed = true;
        }
        return this.reachableBreak_value;
    }

    private boolean reachableBreak_compute() {
        for (BreakStmt stmt : targetBreaks()) {
            if (stmt.reachable()) {
                return true;
            }
        }
        return false;
    }

    public boolean reachableContinue() {
        if (this.reachableContinue_computed) {
            return this.reachableContinue_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.reachableContinue_value = reachableContinue_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.reachableContinue_computed = true;
        }
        return this.reachableContinue_value;
    }

    private boolean reachableContinue_compute() {
        for (Stmt stmt : targetContinues()) {
            if (stmt.reachable()) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.BranchPropagation
    public Collection targetBranches() {
        if (this.targetBranches_computed) {
            return this.targetBranches_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.targetBranches_value = targetBranches_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.targetBranches_computed = true;
        }
        return this.targetBranches_value;
    }

    private Collection targetBranches_compute() {
        HashSet set = new HashSet();
        for (Object o : branches()) {
            if ((o instanceof ContinueStmt) && targetOf((ContinueStmt) o)) {
                set.add(o);
            }
            if ((o instanceof BreakStmt) && targetOf((BreakStmt) o)) {
                set.add(o);
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.BranchPropagation
    public Collection escapedBranches() {
        if (this.escapedBranches_computed) {
            return this.escapedBranches_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.escapedBranches_value = escapedBranches_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.escapedBranches_computed = true;
        }
        return this.escapedBranches_value;
    }

    private Collection escapedBranches_compute() {
        HashSet set = new HashSet();
        for (Object o : branches()) {
            if ((o instanceof ContinueStmt) && !targetOf((ContinueStmt) o)) {
                set.add(o);
            }
            if ((o instanceof BreakStmt) && !targetOf((BreakStmt) o)) {
                set.add(o);
            }
            if (o instanceof ReturnStmt) {
                set.add(o);
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.BranchPropagation
    public Collection branches() {
        if (this.branches_computed) {
            return this.branches_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.branches_value = branches_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.branches_computed = true;
        }
        return this.branches_value;
    }

    private Collection branches_compute() {
        HashSet set = new HashSet();
        super.collectBranches(set);
        return set;
    }

    @Override // soot.JastAddJ.BranchPropagation
    public Collection targetContinues() {
        if (this.targetContinues_computed) {
            return this.targetContinues_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.targetContinues_value = targetContinues_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.targetContinues_computed = true;
        }
        return this.targetContinues_value;
    }

    private Collection targetContinues_compute() {
        HashSet set = new HashSet();
        for (Object o : targetBranches()) {
            if (o instanceof ContinueStmt) {
                set.add(o);
            }
        }
        if (getParent() instanceof LabeledStmt) {
            for (Object o2 : ((LabeledStmt) getParent()).targetBranches()) {
                if (o2 instanceof ContinueStmt) {
                    set.add(o2);
                }
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.BranchPropagation
    public Collection targetBreaks() {
        if (this.targetBreaks_computed) {
            return this.targetBreaks_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.targetBreaks_value = targetBreaks_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.targetBreaks_computed = true;
        }
        return this.targetBreaks_value;
    }

    private Collection targetBreaks_compute() {
        HashSet set = new HashSet();
        for (Object o : targetBranches()) {
            if (o instanceof BreakStmt) {
                set.add(o);
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
