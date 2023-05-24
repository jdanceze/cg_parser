package soot.jimple.toolkits.annotation.parity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AddExpr;
import soot.jimple.ArithmeticConstant;
import soot.jimple.BinopExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.MulExpr;
import soot.jimple.SubExpr;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.LiveLocals;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/parity/ParityAnalysis.class */
public class ParityAnalysis extends ForwardFlowAnalysis<Unit, Map<Value, Parity>> {
    private final Body body;
    private final LiveLocals filter;

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/parity/ParityAnalysis$Parity.class */
    public enum Parity {
        TOP,
        BOTTOM,
        EVEN,
        ODD;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Parity[] valuesCustom() {
            Parity[] valuesCustom = values();
            int length = valuesCustom.length;
            Parity[] parityArr = new Parity[length];
            System.arraycopy(valuesCustom, 0, parityArr, 0, length);
            return parityArr;
        }

        static Parity valueOf(int v) {
            return v % 2 == 0 ? EVEN : ODD;
        }

        static Parity valueOf(long v) {
            return v % 2 == 0 ? EVEN : ODD;
        }
    }

    public ParityAnalysis(UnitGraph g, LiveLocals filter) {
        super(g);
        this.body = g.getBody();
        this.filter = filter;
        this.filterUnitToBeforeFlow = new HashMap();
        this.filterUnitToAfterFlow = new HashMap();
        buildBeforeFilterMap();
        doAnalysis();
    }

    public ParityAnalysis(UnitGraph g) {
        super(g);
        this.body = g.getBody();
        this.filter = null;
        doAnalysis();
    }

    private void buildBeforeFilterMap() {
        Iterator<Unit> it = this.body.getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            HashMap hashMap = new HashMap();
            for (Local l : this.filter.getLiveLocalsBefore(s)) {
                hashMap.put(l, Parity.BOTTOM);
            }
            this.filterUnitToBeforeFlow.put(s, hashMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(Map<Value, Parity> inMap1, Map<Value, Parity> inMap2, Map<Value, Parity> outMap) {
        for (Value var1 : inMap1.keySet()) {
            Parity inVal1 = inMap1.get(var1);
            Parity inVal2 = inMap2.get(var1);
            if (inVal2 == null) {
                outMap.put(var1, inVal1);
            } else if (Parity.BOTTOM.equals(inVal1)) {
                outMap.put(var1, inVal2);
            } else if (Parity.BOTTOM.equals(inVal2)) {
                outMap.put(var1, inVal1);
            } else if (Parity.EVEN.equals(inVal1) && Parity.EVEN.equals(inVal2)) {
                outMap.put(var1, Parity.EVEN);
            } else if (Parity.ODD.equals(inVal1) && Parity.ODD.equals(inVal2)) {
                outMap.put(var1, Parity.ODD);
            } else {
                outMap.put(var1, Parity.TOP);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(Map<Value, Parity> sourceIn, Map<Value, Parity> destOut) {
        destOut.clear();
        destOut.putAll(sourceIn);
    }

    private Parity getParity(Map<Value, Parity> in, Value val) {
        if ((val instanceof AddExpr) | (val instanceof SubExpr)) {
            Parity resVal1 = getParity(in, ((BinopExpr) val).getOp1());
            Parity resVal2 = getParity(in, ((BinopExpr) val).getOp2());
            if (Parity.TOP.equals(resVal1) | Parity.TOP.equals(resVal2)) {
                return Parity.TOP;
            }
            if (Parity.BOTTOM.equals(resVal1) | Parity.BOTTOM.equals(resVal2)) {
                return Parity.BOTTOM;
            }
            if (resVal1.equals(resVal2)) {
                return Parity.EVEN;
            }
            return Parity.ODD;
        } else if (val instanceof MulExpr) {
            Parity resVal12 = getParity(in, ((BinopExpr) val).getOp1());
            Parity resVal22 = getParity(in, ((BinopExpr) val).getOp2());
            if (Parity.TOP.equals(resVal12) | Parity.TOP.equals(resVal22)) {
                return Parity.TOP;
            }
            if (Parity.BOTTOM.equals(resVal12) | Parity.BOTTOM.equals(resVal22)) {
                return Parity.BOTTOM;
            }
            if (resVal12.equals(resVal22)) {
                return resVal12;
            }
            return Parity.EVEN;
        } else if (val instanceof IntConstant) {
            int value = ((IntConstant) val).value;
            return Parity.valueOf(value);
        } else if (val instanceof LongConstant) {
            long value2 = ((LongConstant) val).value;
            return Parity.valueOf(value2);
        } else {
            Parity p = in.get(val);
            return p != null ? p : Parity.TOP;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(Map<Value, Parity> in, Unit s, Map<Value, Parity> out) {
        out.putAll(in);
        if (s instanceof DefinitionStmt) {
            DefinitionStmt sDefStmt = (DefinitionStmt) s;
            Value left = sDefStmt.getLeftOp();
            if (left instanceof Local) {
                Type leftType = left.getType();
                if ((leftType instanceof IntegerType) || (leftType instanceof LongType)) {
                    out.put(left, getParity(out, sDefStmt.getRightOp()));
                }
            }
        }
        for (ValueBox next : s.getUseAndDefBoxes()) {
            Value val = next.getValue();
            if (val instanceof ArithmeticConstant) {
                out.put(val, getParity(out, val));
            }
        }
        if (Options.v().interactive_mode()) {
            buildAfterFilterMap(s);
            updateAfterFilterMap(s);
        }
    }

    private void buildAfterFilterMap(Unit s) {
        HashMap hashMap = new HashMap();
        for (Local local : this.filter.getLiveLocalsAfter(s)) {
            hashMap.put(local, Parity.BOTTOM);
        }
        this.filterUnitToAfterFlow.put(s, hashMap);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Map<Value, Parity> entryInitialFlow() {
        return newInitialFlow();
    }

    private void updateBeforeFilterMap() {
        for (Unit s : this.filterUnitToBeforeFlow.keySet()) {
            Map<Value, Parity> allData = getFlowBefore(s);
            Map<Value, Parity> filterData = (Map) this.filterUnitToBeforeFlow.get(s);
            this.filterUnitToBeforeFlow.put(s, updateFilter(allData, filterData));
        }
    }

    private void updateAfterFilterMap(Unit s) {
        Map<Value, Parity> allData = getFlowAfter(s);
        Map<Value, Parity> filterData = (Map) this.filterUnitToAfterFlow.get(s);
        this.filterUnitToAfterFlow.put(s, updateFilter(allData, filterData));
    }

    private Map<Value, Parity> updateFilter(Map<Value, Parity> allData, Map<Value, Parity> filterData) {
        if (allData != null) {
            for (Value v : filterData.keySet()) {
                Parity d = allData.get(v);
                if (d == null) {
                    filterData.remove(v);
                } else {
                    filterData.put(v, d);
                }
            }
        }
        return filterData;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Map<Value, Parity> newInitialFlow() {
        Map<Value, Parity> initMap = new HashMap<>();
        for (Local l : this.body.getLocals()) {
            Type t = l.getType();
            if ((t instanceof IntegerType) || (t instanceof LongType)) {
                initMap.put(l, Parity.BOTTOM);
            }
        }
        for (ValueBox vb : this.body.getUseAndDefBoxes()) {
            Value val = vb.getValue();
            if (val instanceof ArithmeticConstant) {
                initMap.put(val, getParity(initMap, val));
            }
        }
        if (Options.v().interactive_mode()) {
            updateBeforeFilterMap();
        }
        return initMap;
    }
}
