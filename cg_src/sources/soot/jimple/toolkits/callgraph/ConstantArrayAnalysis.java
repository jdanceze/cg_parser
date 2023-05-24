package soot.jimple.toolkits.callgraph;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.NullType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.shimple.PhiExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ConstantArrayAnalysis.class */
public class ConstantArrayAnalysis extends ForwardFlowAnalysis<Unit, ArrayState> {
    private final Map<Local, Integer> localToInt;
    private final Map<Type, Integer> typeToInt;
    private final Map<Integer, Integer> sizeToInt;
    private final Map<Integer, Type> rvTypeToInt;
    private final Map<Integer, Integer> rvSizeToInt;
    private int size;
    private int typeSize;
    private int szSize;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ConstantArrayAnalysis.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ConstantArrayAnalysis$ArrayTypesInternal.class */
    public class ArrayTypesInternal implements Cloneable {
        BitSet mustAssign;
        BitSet[] typeState;
        BitSet sizeState;

        private ArrayTypesInternal() {
            this.sizeState = new BitSet(ConstantArrayAnalysis.this.szSize);
        }

        /* synthetic */ ArrayTypesInternal(ConstantArrayAnalysis constantArrayAnalysis, ArrayTypesInternal arrayTypesInternal) {
            this();
        }

        public Object clone() {
            try {
                ArrayTypesInternal s = (ArrayTypesInternal) super.clone();
                s.sizeState = (BitSet) s.sizeState.clone();
                s.typeState = (BitSet[]) s.typeState.clone();
                s.mustAssign = (BitSet) s.mustAssign.clone();
                return s;
            } catch (CloneNotSupportedException e) {
                throw new InternalError();
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ArrayTypesInternal)) {
                return false;
            }
            ArrayTypesInternal otherTypes = (ArrayTypesInternal) obj;
            return this.sizeState.equals(otherTypes.sizeState) && Arrays.equals(this.typeState, otherTypes.typeState) && this.mustAssign.equals(otherTypes.mustAssign);
        }

        public int hashCode() {
            int hash = (59 * 5) + Objects.hashCode(this.mustAssign);
            return (59 * ((59 * hash) + Arrays.deepHashCode(this.typeState))) + Objects.hashCode(this.sizeState);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ConstantArrayAnalysis$ArrayTypes.class */
    public static class ArrayTypes {
        public Set<Integer> possibleSizes;
        public Set<Type>[] possibleTypes;

        public String toString() {
            return "ArrayTypes [possibleSizes=" + this.possibleSizes + ", possibleTypes=" + Arrays.toString(this.possibleTypes) + "]";
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/ConstantArrayAnalysis$ArrayState.class */
    public class ArrayState {
        ArrayTypesInternal[] state;
        BitSet active;

        public ArrayState() {
            this.state = new ArrayTypesInternal[ConstantArrayAnalysis.this.size];
            this.active = new BitSet(ConstantArrayAnalysis.this.size);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ArrayState)) {
                return false;
            }
            ArrayState otherState = (ArrayState) obj;
            return this.active.equals(otherState.active) && Arrays.equals(this.state, otherState.state);
        }

        public int hashCode() {
            int hash = (73 * 3) + Arrays.deepHashCode(this.state);
            return (73 * hash) + Objects.hashCode(this.active);
        }

        public void deepCloneLocalValueSlot(int localRef, int index) {
            this.state[localRef] = (ArrayTypesInternal) this.state[localRef].clone();
            this.state[localRef].typeState[index] = (BitSet) this.state[localRef].typeState[index].clone();
        }
    }

    public ConstantArrayAnalysis(DirectedGraph<Unit> graph, Body b) {
        super(graph);
        this.localToInt = new HashMap();
        this.typeToInt = new HashMap();
        this.sizeToInt = new HashMap();
        this.rvTypeToInt = new HashMap();
        this.rvSizeToInt = new HashMap();
        for (Local l : b.getLocals()) {
            Map<Local, Integer> map = this.localToInt;
            int i = this.size;
            this.size = i + 1;
            map.put(l, Integer.valueOf(i));
        }
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof DefinitionStmt) {
                Value rhs = ((DefinitionStmt) u).getRightOp();
                Type ty = rhs.getType();
                if (!this.typeToInt.containsKey(ty)) {
                    int key = this.typeSize;
                    this.typeSize = key + 1;
                    this.typeToInt.put(ty, Integer.valueOf(key));
                    this.rvTypeToInt.put(Integer.valueOf(key), ty);
                }
                if (rhs instanceof NewArrayExpr) {
                    NewArrayExpr nae = (NewArrayExpr) rhs;
                    if (nae.getSize() instanceof IntConstant) {
                        int sz = ((IntConstant) nae.getSize()).value;
                        if (!this.sizeToInt.containsKey(Integer.valueOf(sz))) {
                            int key2 = this.szSize;
                            this.szSize = key2 + 1;
                            this.sizeToInt.put(Integer.valueOf(sz), Integer.valueOf(key2));
                            this.rvSizeToInt.put(Integer.valueOf(key2), Integer.valueOf(sz));
                        }
                    }
                }
            }
        }
        doAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(ArrayState in, Unit d, ArrayState out) {
        Integer localRef;
        Integer localRef2;
        Integer localRef3;
        out.active.clear();
        out.active.or(in.active);
        out.state = (ArrayTypesInternal[]) Arrays.copyOf(in.state, in.state.length);
        if (d instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt) d;
            Value rhs = ds.getRightOp();
            Value lhs = ds.getLeftOp();
            if (rhs instanceof NewArrayExpr) {
                Local l = (Local) lhs;
                int varRef = this.localToInt.get(l).intValue();
                out.active.set(varRef);
                Value naeSize = ((NewArrayExpr) rhs).getSize();
                if (naeSize instanceof IntConstant) {
                    int arraySize = ((IntConstant) naeSize).value;
                    out.state[varRef] = new ArrayTypesInternal(this, null);
                    out.state[varRef].sizeState.set(this.sizeToInt.get(Integer.valueOf(arraySize)).intValue());
                    out.state[varRef].typeState = new BitSet[arraySize];
                    out.state[varRef].mustAssign = new BitSet(arraySize);
                    for (int i = 0; i < arraySize; i++) {
                        out.state[varRef].typeState[i] = new BitSet(this.typeSize);
                    }
                } else {
                    out.state[varRef] = null;
                }
            } else if (lhs instanceof ArrayRef) {
                ArrayRef ar = (ArrayRef) lhs;
                int localRef4 = this.localToInt.get((Local) ar.getBase()).intValue();
                Value indexVal = ar.getIndex();
                if (!(indexVal instanceof IntConstant)) {
                    out.state[localRef4] = null;
                    out.active.set(localRef4);
                } else if (out.state[localRef4] != null) {
                    int index = ((IntConstant) indexVal).value;
                    if (!$assertionsDisabled && index >= out.state[localRef4].typeState.length) {
                        throw new AssertionError();
                    }
                    out.deepCloneLocalValueSlot(localRef4, index);
                    if (!$assertionsDisabled && out.state[localRef4].typeState[index] == null) {
                        throw new AssertionError(d);
                    }
                    out.state[localRef4].typeState[index].set(this.typeToInt.get(rhs.getType()).intValue());
                    out.state[localRef4].mustAssign.set(index);
                }
            } else if (lhs instanceof Local) {
                if ((rhs instanceof NullConstant) && (lhs.getType() instanceof ArrayType)) {
                    int varRef2 = this.localToInt.get((Local) lhs).intValue();
                    out.active.clear(varRef2);
                    out.state[varRef2] = null;
                } else if ((rhs instanceof Local) && in.state[this.localToInt.get((Local) rhs).intValue()] != null && in.active.get(this.localToInt.get((Local) rhs).intValue())) {
                    int lhsRef = this.localToInt.get((Local) lhs).intValue();
                    int rhsRef = this.localToInt.get((Local) rhs).intValue();
                    out.active.set(lhsRef);
                    out.state[lhsRef] = in.state[rhsRef];
                    out.state[rhsRef] = null;
                } else if (rhs instanceof PhiExpr) {
                    PhiExpr rPhi = (PhiExpr) rhs;
                    int lhsRef2 = this.localToInt.get((Local) lhs).intValue();
                    out.state[lhsRef2] = null;
                    int i2 = 0;
                    List<Value> phiValues = rPhi.getValues();
                    while (true) {
                        if (i2 >= phiValues.size()) {
                            break;
                        }
                        int argRef = this.localToInt.get((Local) phiValues.get(i2)).intValue();
                        if (in.active.get(argRef)) {
                            out.active.set(lhsRef2);
                            if (in.state[argRef] == null) {
                                out.state[lhsRef2] = null;
                                break;
                            }
                            if (out.state[lhsRef2] == null) {
                                out.state[lhsRef2] = in.state[argRef];
                            } else {
                                out.state[lhsRef2] = mergeTypeStates(in.state[argRef], out.state[lhsRef2]);
                            }
                            out.state[argRef] = null;
                        }
                        i2++;
                    }
                    while (i2 < phiValues.size()) {
                        out.state[this.localToInt.get((Local) phiValues.get(i2)).intValue()] = null;
                        i2++;
                    }
                } else {
                    int varRef3 = this.localToInt.get((Local) lhs).intValue();
                    out.active.set(varRef3);
                    out.state[varRef3] = null;
                }
            }
            for (ValueBox b : rhs.getUseBoxes()) {
                Value v = b.getValue();
                if ((v instanceof Local) && (localRef3 = this.localToInt.get((Local) v)) != null) {
                    int iLocalRef = localRef3.intValue();
                    out.state[iLocalRef] = null;
                    out.active.set(iLocalRef);
                }
            }
            if ((rhs instanceof Local) && (localRef2 = this.localToInt.get((Local) rhs)) != null) {
                int iLocalRef2 = localRef2.intValue();
                out.state[iLocalRef2] = null;
                out.active.set(iLocalRef2);
                return;
            }
            return;
        }
        for (ValueBox b2 : d.getUseBoxes()) {
            Value v2 = b2.getValue();
            if ((v2 instanceof Local) && (localRef = this.localToInt.get((Local) v2)) != null) {
                int iLocalRef3 = localRef.intValue();
                out.state[iLocalRef3] = null;
                out.active.set(iLocalRef3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public ArrayState newInitialFlow() {
        return new ArrayState();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(ArrayState in1, ArrayState in2, ArrayState out) {
        out.active.clear();
        out.active.or(in1.active);
        out.active.or(in2.active);
        BitSet in2_excl = (BitSet) in2.active.clone();
        in2_excl.andNot(in1.active);
        int nextSetBit = in1.active.nextSetBit(0);
        while (true) {
            int i = nextSetBit;
            if (i < 0) {
                break;
            }
            if (in1.state[i] == null) {
                out.state[i] = null;
            } else if (in2.active.get(i)) {
                if (in2.state[i] == null) {
                    out.state[i] = null;
                } else {
                    out.state[i] = mergeTypeStates(in1.state[i], in2.state[i]);
                }
            } else {
                out.state[i] = in1.state[i];
            }
            nextSetBit = in1.active.nextSetBit(i + 1);
        }
        int nextSetBit2 = in2_excl.nextSetBit(0);
        while (true) {
            int i2 = nextSetBit2;
            if (i2 >= 0) {
                out.state[i2] = in2.state[i2];
                nextSetBit2 = in2_excl.nextSetBit(i2 + 1);
            } else {
                return;
            }
        }
    }

    private ArrayTypesInternal mergeTypeStates(ArrayTypesInternal a1, ArrayTypesInternal a2) {
        if ($assertionsDisabled || !(a1 == null || a2 == null)) {
            ArrayTypesInternal toRet = new ArrayTypesInternal(this, null);
            toRet.sizeState.or(a1.sizeState);
            toRet.sizeState.or(a2.sizeState);
            int maxSize = Math.max(a1.typeState.length, a2.typeState.length);
            int commonSize = Math.min(a1.typeState.length, a2.typeState.length);
            toRet.mustAssign = new BitSet(maxSize);
            toRet.typeState = new BitSet[maxSize];
            for (int i = 0; i < commonSize; i++) {
                toRet.typeState[i] = new BitSet(this.typeSize);
                toRet.typeState[i].or(a1.typeState[i]);
                toRet.typeState[i].or(a2.typeState[i]);
                toRet.mustAssign.set(i, a1.mustAssign.get(i) && a2.mustAssign.get(i));
            }
            for (int i2 = commonSize; i2 < maxSize; i2++) {
                if (a1.typeState.length > i2) {
                    toRet.typeState[i2] = (BitSet) a1.typeState[i2].clone();
                    toRet.mustAssign.set(i2, a1.mustAssign.get(i2));
                } else {
                    toRet.mustAssign.set(i2, a2.mustAssign.get(i2));
                    toRet.typeState[i2] = (BitSet) a2.typeState[i2].clone();
                }
            }
            return toRet;
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(ArrayState source, ArrayState dest) {
        dest.active = source.active;
        dest.state = source.state;
    }

    public boolean isConstantBefore(Stmt s, Local arrayLocal) {
        ArrayState flowResults = getFlowBefore(s);
        int varRef = this.localToInt.get(arrayLocal).intValue();
        return flowResults.active.get(varRef) && flowResults.state[varRef] != null;
    }

    public ArrayTypes getArrayTypesBefore(Stmt s, Local arrayLocal) {
        if (!isConstantBefore(s, arrayLocal)) {
            return null;
        }
        ArrayTypes toRet = new ArrayTypes();
        int varRef = this.localToInt.get(arrayLocal).intValue();
        ArrayTypesInternal ati = getFlowBefore(s).state[varRef];
        toRet.possibleSizes = new HashSet();
        toRet.possibleTypes = new Set[ati.typeState.length];
        int nextSetBit = ati.sizeState.nextSetBit(0);
        while (true) {
            int i = nextSetBit;
            if (i < 0) {
                break;
            }
            toRet.possibleSizes.add(this.rvSizeToInt.get(Integer.valueOf(i)));
            nextSetBit = ati.sizeState.nextSetBit(i + 1);
        }
        for (int i2 = 0; i2 < toRet.possibleTypes.length; i2++) {
            toRet.possibleTypes[i2] = new HashSet();
            int nextSetBit2 = ati.typeState[i2].nextSetBit(0);
            while (true) {
                int j = nextSetBit2;
                if (j < 0) {
                    break;
                }
                toRet.possibleTypes[i2].add(this.rvTypeToInt.get(Integer.valueOf(j)));
                nextSetBit2 = ati.typeState[i2].nextSetBit(j + 1);
            }
            if (!ati.mustAssign.get(i2)) {
                toRet.possibleTypes[i2].add(NullType.v());
            }
        }
        return toRet;
    }
}
