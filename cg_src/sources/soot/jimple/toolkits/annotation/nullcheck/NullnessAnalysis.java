package soot.jimple.toolkits.annotation.nullcheck;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import soot.Immediate;
import soot.Local;
import soot.RefLikeType;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.internal.AbstractBinopExpr;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JInstanceOfExpr;
import soot.jimple.internal.JNeExpr;
import soot.shimple.PhiExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullnessAnalysis.class */
public class NullnessAnalysis extends ForwardBranchedFlowAnalysis<AnalysisInfo> {
    protected static final int BOTTOM = 0;
    protected static final int NULL = 1;
    protected static final int NON_NULL = 2;
    protected static final int TOP = 3;
    protected final HashMap<Value, Integer> valueToIndex;
    protected int used;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.BranchedFlowAnalysis
    public /* bridge */ /* synthetic */ void flowThrough(Object obj, Unit unit, List list, List list2) {
        flowThrough((AnalysisInfo) obj, unit, (List<AnalysisInfo>) list, (List<AnalysisInfo>) list2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullnessAnalysis$AnalysisInfo.class */
    public class AnalysisInfo extends BitSet {
        private static final long serialVersionUID = -9200043127757823764L;

        public AnalysisInfo() {
            super(NullnessAnalysis.this.used);
        }

        public AnalysisInfo(AnalysisInfo other) {
            super(NullnessAnalysis.this.used);
            or(other);
        }

        public int get(Value key) {
            if (!NullnessAnalysis.this.valueToIndex.containsKey(key)) {
                return 0;
            }
            int index = NullnessAnalysis.this.valueToIndex.get(key).intValue();
            return (get(index) ? 2 : 0) + (get(index + 1) ? 1 : 0);
        }

        public void put(Value key, int val) {
            int index;
            if (!NullnessAnalysis.this.valueToIndex.containsKey(key)) {
                index = NullnessAnalysis.this.used;
                NullnessAnalysis.this.used += 2;
                NullnessAnalysis.this.valueToIndex.put(key, Integer.valueOf(index));
            } else {
                index = NullnessAnalysis.this.valueToIndex.get(key).intValue();
            }
            set(index, (val & 2) == 2);
            set(index + 1, (val & 1) == 1);
        }
    }

    public NullnessAnalysis(UnitGraph graph) {
        super(graph);
        this.valueToIndex = new HashMap<>();
        this.used = 0;
        doAnalysis();
    }

    protected void flowThrough(AnalysisInfo in, Unit u, List<AnalysisInfo> fallOut, List<AnalysisInfo> branchOuts) {
        AnalysisInfo out = new AnalysisInfo(in);
        AnalysisInfo outBranch = new AnalysisInfo(in);
        Stmt s = (Stmt) u;
        if (s instanceof JIfStmt) {
            handleIfStmt((JIfStmt) s, in, out, outBranch);
        } else if (s instanceof MonitorStmt) {
            out.put(((MonitorStmt) s).getOp(), 2);
        }
        if (s.containsArrayRef()) {
            handleArrayRef(s.getArrayRef(), out);
        }
        if (s.containsFieldRef()) {
            handleFieldRef(s.getFieldRef(), out);
        }
        if (s.containsInvokeExpr()) {
            handleInvokeExpr(s.getInvokeExpr(), out);
        }
        if (s instanceof DefinitionStmt) {
            DefinitionStmt defStmt = (DefinitionStmt) s;
            if (defStmt.getLeftOp().getType() instanceof RefLikeType) {
                handleRefTypeAssignment(defStmt, out);
            }
        }
        for (AnalysisInfo next : fallOut) {
            copy(out, next);
        }
        for (AnalysisInfo next2 : branchOuts) {
            copy(outBranch, next2);
        }
    }

    protected boolean isAlwaysNonNull(Value v) {
        return false;
    }

    private void handleIfStmt(JIfStmt ifStmt, AnalysisInfo in, AnalysisInfo out, AnalysisInfo outBranch) {
        Value condition = ifStmt.getCondition();
        if (condition instanceof JInstanceOfExpr) {
            handleInstanceOfExpression((JInstanceOfExpr) condition, in, out, outBranch);
        } else if ((condition instanceof JEqExpr) || (condition instanceof JNeExpr)) {
            handleEqualityOrNonEqualityCheck((AbstractBinopExpr) condition, in, out, outBranch);
        }
    }

    private void handleEqualityOrNonEqualityCheck(AbstractBinopExpr eqExpr, AnalysisInfo in, AnalysisInfo out, AnalysisInfo outBranch) {
        Value left = eqExpr.getOp1();
        Value right = eqExpr.getOp2();
        Value val = null;
        if (left == NullConstant.v()) {
            if (right != NullConstant.v()) {
                val = right;
            }
        } else if (right == NullConstant.v() && left != NullConstant.v()) {
            val = left;
        }
        if (val instanceof Local) {
            if (eqExpr instanceof JEqExpr) {
                handleEquality(val, out, outBranch);
            } else if (eqExpr instanceof JNeExpr) {
                handleNonEquality(val, out, outBranch);
            } else {
                throw new IllegalStateException("unexpected condition: " + eqExpr.getClass());
            }
        }
    }

    private void handleNonEquality(Value val, AnalysisInfo out, AnalysisInfo outBranch) {
        out.put(val, 1);
        outBranch.put(val, 2);
    }

    private void handleEquality(Value val, AnalysisInfo out, AnalysisInfo outBranch) {
        out.put(val, 2);
        outBranch.put(val, 1);
    }

    private void handleInstanceOfExpression(JInstanceOfExpr expr, AnalysisInfo in, AnalysisInfo out, AnalysisInfo outBranch) {
        Value op = expr.getOp();
        outBranch.put(op, 2);
    }

    private void handleArrayRef(ArrayRef arrayRef, AnalysisInfo out) {
        Value array = arrayRef.getBase();
        out.put(array, 2);
    }

    private void handleFieldRef(FieldRef fieldRef, AnalysisInfo out) {
        if (fieldRef instanceof InstanceFieldRef) {
            InstanceFieldRef instanceFieldRef = (InstanceFieldRef) fieldRef;
            out.put(instanceFieldRef.getBase(), 2);
        }
    }

    private void handleInvokeExpr(InvokeExpr invokeExpr, AnalysisInfo out) {
        if (invokeExpr instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) invokeExpr;
            out.put(instanceInvokeExpr.getBase(), 2);
        }
    }

    private void handleRefTypeAssignment(DefinitionStmt assignStmt, AnalysisInfo out) {
        Value right = assignStmt.getRightOp();
        if (right instanceof JCastExpr) {
            right = ((JCastExpr) right).getOp();
        }
        Value left = assignStmt.getLeftOp();
        if (isAlwaysNonNull(right) || (right instanceof NewExpr) || (right instanceof NewArrayExpr) || (right instanceof NewMultiArrayExpr) || (right instanceof ThisRef) || (right instanceof StringConstant) || (right instanceof ClassConstant) || (right instanceof CaughtExceptionRef)) {
            out.put(left, 2);
        } else if (right == NullConstant.v()) {
            out.put(left, 1);
        } else if ((left instanceof Local) && (right instanceof Local)) {
            out.put(left, out.get(right));
        } else if ((left instanceof Local) && (right instanceof PhiExpr)) {
            handlePhiExpr(out, left, (PhiExpr) right);
        } else {
            out.put(left, 3);
        }
    }

    private void handlePhiExpr(AnalysisInfo out, Value left, PhiExpr right) {
        int curr = 0;
        for (Value v : right.getValues()) {
            switch (out.get(v)) {
                case 1:
                    if (curr == 0) {
                        curr = 1;
                        break;
                    } else if (curr != 1) {
                        out.put(left, 3);
                        return;
                    } else {
                        break;
                    }
                case 2:
                    if (curr == 0) {
                        curr = 2;
                        break;
                    } else if (curr != 2) {
                        out.put(left, 3);
                        return;
                    } else {
                        break;
                    }
                case 3:
                    out.put(left, 3);
                    return;
            }
        }
        out.put(left, curr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(AnalysisInfo s, AnalysisInfo d) {
        d.clear();
        d.or(s);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(AnalysisInfo in1, AnalysisInfo in2, AnalysisInfo out) {
        out.clear();
        out.or(in1);
        out.or(in2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public AnalysisInfo newInitialFlow() {
        return new AnalysisInfo();
    }

    public boolean isAlwaysNullBefore(Unit s, Immediate i) {
        return getFlowBefore(s).get(i) == 1;
    }

    public boolean isAlwaysNonNullBefore(Unit s, Immediate i) {
        return getFlowBefore(s).get(i) == 2;
    }
}
