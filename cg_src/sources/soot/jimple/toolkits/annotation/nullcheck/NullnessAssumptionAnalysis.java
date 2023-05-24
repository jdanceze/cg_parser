package soot.jimple.toolkits.annotation.nullcheck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.Immediate;
import soot.Local;
import soot.RefLikeType;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JCastExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullnessAssumptionAnalysis.class */
public class NullnessAssumptionAnalysis extends BackwardFlowAnalysis<Unit, AnalysisInfo> {
    protected static final Object BOTTOM = new Object() { // from class: soot.jimple.toolkits.annotation.nullcheck.NullnessAssumptionAnalysis.1
        public String toString() {
            return "bottom";
        }
    };
    protected static final Object NULL = new Object() { // from class: soot.jimple.toolkits.annotation.nullcheck.NullnessAssumptionAnalysis.2
        public String toString() {
            return Jimple.NULL;
        }
    };
    protected static final Object NON_NULL = new Object() { // from class: soot.jimple.toolkits.annotation.nullcheck.NullnessAssumptionAnalysis.3
        public String toString() {
            return "non-null";
        }
    };
    protected static final Object TOP = new Object() { // from class: soot.jimple.toolkits.annotation.nullcheck.NullnessAssumptionAnalysis.4
        public String toString() {
            return "top";
        }
    };

    public NullnessAssumptionAnalysis(UnitGraph graph) {
        super(graph);
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(AnalysisInfo in, Unit unit, AnalysisInfo outValue) {
        AnalysisInfo out = new AnalysisInfo(in);
        Stmt s = (Stmt) unit;
        if (s instanceof MonitorStmt) {
            out.put(((MonitorStmt) s).getOp(), NON_NULL);
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
        for (Map.Entry<Value, Object> entry : out.entrySet()) {
            if (isAlwaysNonNull(entry.getKey())) {
                entry.setValue(NON_NULL);
            }
        }
        if (s instanceof DefinitionStmt) {
            DefinitionStmt defStmt = (DefinitionStmt) s;
            if (defStmt.getLeftOp().getType() instanceof RefLikeType) {
                handleRefTypeAssignment(defStmt, new AnalysisInfo(out), out);
            }
        }
        Iterator<Value> outIter = out.keySet().iterator();
        while (outIter.hasNext()) {
            Value v = outIter.next();
            if (!(v instanceof Local)) {
                outIter.remove();
            }
        }
        copy(out, outValue);
    }

    protected boolean isAlwaysNonNull(Value v) {
        return false;
    }

    private void handleArrayRef(ArrayRef arrayRef, AnalysisInfo out) {
        out.put(arrayRef.getBase(), NON_NULL);
    }

    private void handleFieldRef(FieldRef fieldRef, AnalysisInfo out) {
        if (fieldRef instanceof InstanceFieldRef) {
            InstanceFieldRef instanceFieldRef = (InstanceFieldRef) fieldRef;
            out.put(instanceFieldRef.getBase(), NON_NULL);
        }
    }

    private void handleInvokeExpr(InvokeExpr invokeExpr, AnalysisInfo out) {
        if (invokeExpr instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) invokeExpr;
            out.put(instanceInvokeExpr.getBase(), NON_NULL);
        }
    }

    private void handleRefTypeAssignment(DefinitionStmt assignStmt, AnalysisInfo rhsInfo, AnalysisInfo out) {
        Value right = assignStmt.getRightOp();
        if (right instanceof JCastExpr) {
            right = ((JCastExpr) right).getOp();
        }
        rhsInfo.put(right, BOTTOM);
        out.put(assignStmt.getLeftOp(), rhsInfo.get(right));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(AnalysisInfo source, AnalysisInfo dest) {
        dest.clear();
        dest.putAll(source);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public AnalysisInfo entryInitialFlow() {
        return new AnalysisInfo();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(AnalysisInfo in1, AnalysisInfo in2, AnalysisInfo out) {
        Object obj;
        HashSet<Value> values = new HashSet<>();
        values.addAll(in1.keySet());
        values.addAll(in2.keySet());
        out.clear();
        Iterator<Value> it = values.iterator();
        while (it.hasNext()) {
            Value v = it.next();
            HashSet<Object> leftAndRight = new HashSet<>();
            leftAndRight.add(in1.get(v));
            leftAndRight.add(in2.get(v));
            if (leftAndRight.contains(BOTTOM)) {
                obj = BOTTOM;
            } else if (leftAndRight.contains(NON_NULL)) {
                if (leftAndRight.contains(NULL)) {
                    obj = BOTTOM;
                } else {
                    obj = NON_NULL;
                }
            } else if (leftAndRight.contains(NULL)) {
                obj = NULL;
            } else {
                obj = BOTTOM;
            }
            Object result = obj;
            out.put(v, result);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public AnalysisInfo newInitialFlow() {
        return new AnalysisInfo();
    }

    public boolean isAssumedNullBefore(Unit s, Immediate i) {
        return getFlowBefore(s).get(i) == NULL;
    }

    public boolean isAssumedNonNullBefore(Unit s, Immediate i) {
        return getFlowBefore(s).get(i) == NON_NULL;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullnessAssumptionAnalysis$AnalysisInfo.class */
    public static class AnalysisInfo extends HashMap<Value, Object> {
        public AnalysisInfo() {
        }

        public AnalysisInfo(Map<Value, Object> m) {
            super(m);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Object get(Object key) {
            Object object = super.get(key);
            if (object == null) {
                return NullnessAssumptionAnalysis.BOTTOM;
            }
            return object;
        }
    }
}
