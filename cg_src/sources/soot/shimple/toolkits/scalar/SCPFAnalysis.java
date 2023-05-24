package soot.shimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.shimple.toolkits.scalar.SEvaluator;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;
import soot.toolkits.scalar.Pair;
/* compiled from: SConstantPropagatorAndFolder.java */
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/SCPFAnalysis.class */
class SCPFAnalysis extends ForwardBranchedFlowAnalysis<FlowSet<Object>> {
    protected static final ArraySparseSet<Object> EMPTY_SET = new ArraySparseSet<>();
    protected final Map<Local, Constant> localToConstant;
    protected final Map<Stmt, GotoStmt> stmtToReplacement;
    protected final List<IfStmt> deadStmts;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.BranchedFlowAnalysis
    public /* bridge */ /* synthetic */ void flowThrough(Object obj, Unit unit, List list, List list2) {
        flowThrough((FlowSet) obj, unit, (List<FlowSet<Object>>) list, (List<FlowSet<Object>>) list2);
    }

    public SCPFAnalysis(UnitGraph graph) {
        super(graph);
        this.stmtToReplacement = new HashMap();
        this.deadStmts = new ArrayList();
        this.localToConstant = new HashMap((graph.size() * 2) + 1, 0.7f);
        Map<Local, Constant> ref = this.localToConstant;
        for (Local local : graph.getBody().getLocals()) {
            ref.put(local, SEvaluator.TopConstant.v());
        }
        doAnalysis();
    }

    public Map<Local, Constant> getResults() {
        return this.localToConstant;
    }

    public List<IfStmt> getDeadStmts() {
        return this.deadStmts;
    }

    public Map<Stmt, GotoStmt> getStmtsToReplace() {
        return this.stmtToReplacement;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public boolean treatTrapHandlersAsEntries() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Object> entryInitialFlow() {
        FlowSet<Object> entrySet = EMPTY_SET.emptySet();
        entrySet.add(SEvaluator.TopConstant.v());
        return entrySet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<Object> newInitialFlow() {
        return EMPTY_SET.emptySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<Object> in1, FlowSet<Object> in2, FlowSet<Object> out) {
        in1.union(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<Object> source, FlowSet<Object> dest) {
        source.copy(dest);
    }

    protected void flowThrough(FlowSet<Object> in, Unit s, List<FlowSet<Object>> fallOut, List<FlowSet<Object>> branchOuts) {
        if (in.isEmpty()) {
            return;
        }
        FlowSet<Object> fin = in.mo2534clone();
        Pair<Unit, Constant> pair = processDefinitionStmt(s);
        if (pair != null) {
            fin.add(pair);
        }
        if (!s.branches() && s.fallsThrough()) {
            for (FlowSet<Object> fallSet : fallOut) {
                fallSet.union(fin);
            }
            return;
        }
        boolean conservative = true;
        boolean fall = false;
        boolean branch = false;
        FlowSet<Object> oneBranch = null;
        if (s instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) s;
            Constant constant = SEvaluator.getFuzzyConstantValueOf(ifStmt.getCondition(), this.localToConstant);
            if (constant instanceof SEvaluator.TopConstant) {
                return;
            }
            if (constant instanceof SEvaluator.BottomConstant) {
                this.deadStmts.remove(ifStmt);
                this.stmtToReplacement.remove(ifStmt);
            } else {
                conservative = false;
                if (IntConstant.v(0).equals(constant)) {
                    fall = true;
                    this.deadStmts.add(ifStmt);
                } else if (IntConstant.v(1).equals(constant)) {
                    branch = true;
                    this.stmtToReplacement.put(ifStmt, Jimple.v().newGotoStmt(ifStmt.getTargetBox()));
                } else {
                    throw new RuntimeException("IfStmt condition must be 0 or 1! Found: " + constant);
                }
            }
        } else if (s instanceof TableSwitchStmt) {
            TableSwitchStmt table = (TableSwitchStmt) s;
            Constant keyC = SEvaluator.getFuzzyConstantValueOf(table.getKey(), this.localToConstant);
            if (keyC instanceof SEvaluator.TopConstant) {
                return;
            }
            if (keyC instanceof SEvaluator.BottomConstant) {
                this.stmtToReplacement.remove(table);
            } else if (keyC instanceof IntConstant) {
                conservative = false;
                int index = ((IntConstant) keyC).value - table.getLowIndex();
                UnitBox branchBox = (index < 0 || index > table.getHighIndex()) ? table.getDefaultTargetBox() : table.getTargetBox(index);
                this.stmtToReplacement.put(table, Jimple.v().newGotoStmt(branchBox));
                oneBranch = branchOuts.get(table.getUnitBoxes().indexOf(branchBox));
            }
        } else if (s instanceof LookupSwitchStmt) {
            LookupSwitchStmt lookup = (LookupSwitchStmt) s;
            Constant keyC2 = SEvaluator.getFuzzyConstantValueOf(lookup.getKey(), this.localToConstant);
            if (keyC2 instanceof SEvaluator.TopConstant) {
                return;
            }
            if (keyC2 instanceof SEvaluator.BottomConstant) {
                this.stmtToReplacement.remove(lookup);
            } else if (keyC2 instanceof IntConstant) {
                conservative = false;
                int index2 = lookup.getLookupValues().indexOf(keyC2);
                UnitBox branchBox2 = index2 < 0 ? lookup.getDefaultTargetBox() : lookup.getTargetBox(index2);
                this.stmtToReplacement.put(lookup, Jimple.v().newGotoStmt(branchBox2));
                oneBranch = branchOuts.get(lookup.getUnitBoxes().indexOf(branchBox2));
            }
        }
        if (conservative) {
            fall = s.fallsThrough();
            branch = s.branches();
        }
        if (fall) {
            for (FlowSet<Object> fallSet2 : fallOut) {
                fallSet2.union(fin);
            }
        }
        if (branch) {
            for (FlowSet<Object> branchSet : branchOuts) {
                branchSet.union(fin);
            }
        }
        if (oneBranch != null) {
            oneBranch.union(fin);
        }
    }

    protected Pair<Unit, Constant> processDefinitionStmt(Unit u) {
        if (u instanceof DefinitionStmt) {
            DefinitionStmt dStmt = (DefinitionStmt) u;
            Value value = dStmt.getLeftOp();
            if (value instanceof Local) {
                Local local = (Local) value;
                if (merge(local, SEvaluator.getFuzzyConstantValueOf(dStmt.getRightOp(), this.localToConstant))) {
                    return new Pair<>(u, this.localToConstant.get(local));
                }
                return null;
            }
            return null;
        }
        return null;
    }

    protected boolean merge(Local local, Constant constant) {
        Constant current = this.localToConstant.get(local);
        if (current instanceof SEvaluator.BottomConstant) {
            return false;
        }
        if (current instanceof SEvaluator.TopConstant) {
            this.localToConstant.put(local, constant);
            return true;
        } else if (current.equals(constant)) {
            return false;
        } else {
            this.localToConstant.put(local, SEvaluator.BottomConstant.v());
            return true;
        }
    }
}
