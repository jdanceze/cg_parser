package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.IdentityUnit;
import soot.Local;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLocalDefs.class */
public class SimpleLocalDefs implements LocalDefs {
    private final LocalDefs def;

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLocalDefs$FlowAnalysisMode.class */
    public enum FlowAnalysisMode {
        Automatic,
        OmitSSA,
        FlowInsensitive;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FlowAnalysisMode[] valuesCustom() {
            FlowAnalysisMode[] valuesCustom = values();
            int length = valuesCustom.length;
            FlowAnalysisMode[] flowAnalysisModeArr = new FlowAnalysisMode[length];
            System.arraycopy(valuesCustom, 0, flowAnalysisModeArr, 0, length);
            return flowAnalysisModeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLocalDefs$StaticSingleAssignment.class */
    public static class StaticSingleAssignment implements LocalDefs {
        final Map<Local, List<Unit>> result;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SimpleLocalDefs.class.desiredAssertionStatus();
        }

        public StaticSingleAssignment(Local[] locals, List<Unit>[] listArr) {
            int N = locals.length;
            if (!$assertionsDisabled && N != listArr.length) {
                throw new AssertionError();
            }
            this.result = new HashMap(((N * 3) / 2) + 7);
            for (int i = 0; i < N; i++) {
                List<Unit> curr = listArr[i];
                if (!curr.isEmpty()) {
                    if (!$assertionsDisabled && curr.size() != 1) {
                        throw new AssertionError();
                    }
                    this.result.put(locals[i], curr);
                }
            }
        }

        @Override // soot.toolkits.scalar.LocalDefs
        public List<Unit> getDefsOfAt(Local l, Unit s) {
            List<Unit> lst = this.result.get(l);
            return lst != null ? lst : Collections.emptyList();
        }

        @Override // soot.toolkits.scalar.LocalDefs
        public List<Unit> getDefsOf(Local l) {
            return getDefsOfAt(l, null);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLocalDefs$FlowAssignment.class */
    public class FlowAssignment extends ForwardFlowAnalysis<Unit, FlowBitSet> implements LocalDefs {
        final Map<Local, Integer> locals;
        final List<Unit>[] unitList;
        final int[] localRange;
        final Unit[] universe;
        private Map<Unit, Integer> indexOfUnit;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SimpleLocalDefs.class.desiredAssertionStatus();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLocalDefs$FlowAssignment$FlowBitSet.class */
        public class FlowBitSet extends BitSet {
            private static final long serialVersionUID = -8348696077189400377L;

            FlowBitSet() {
                super(FlowAssignment.this.universe.length);
            }

            List<Unit> asList(int fromIndex, int toIndex) {
                if (fromIndex < 0 || toIndex < fromIndex || FlowAssignment.this.universe.length < toIndex) {
                    throw new IndexOutOfBoundsException();
                }
                if (fromIndex == toIndex) {
                    return Collections.emptyList();
                }
                if (fromIndex == toIndex - 1) {
                    if (get(fromIndex)) {
                        return Collections.singletonList(FlowAssignment.this.universe[fromIndex]);
                    }
                    return Collections.emptyList();
                }
                int i = nextSetBit(fromIndex);
                if (i < 0 || i >= toIndex) {
                    return Collections.emptyList();
                }
                if (i == toIndex - 1) {
                    return Collections.singletonList(FlowAssignment.this.universe[i]);
                }
                List<Unit> elements = new ArrayList<>(toIndex - i);
                do {
                    int endOfRun = Math.min(toIndex, nextClearBit(i + 1));
                    do {
                        int i2 = i;
                        i++;
                        elements.add(FlowAssignment.this.universe[i2]);
                    } while (i < endOfRun);
                    if (i < toIndex) {
                        i = nextSetBit(i + 1);
                        if (i < 0) {
                            break;
                        }
                    } else {
                        break;
                    }
                } while (i < toIndex);
                return elements;
            }
        }

        public FlowAssignment(DirectedGraph<Unit> graph, Local[] locals, List<Unit>[] listArr, int units, boolean omitSSA) {
            super(graph);
            this.unitList = listArr;
            this.universe = new Unit[units];
            this.indexOfUnit = new HashMap(units);
            int N = locals.length;
            this.locals = new HashMap(((N * 3) / 2) + 7);
            this.localRange = new int[N + 1];
            int j = 0;
            int i = 0;
            while (i < N) {
                List<Unit> currUnitList = listArr[i];
                if (currUnitList != null && !currUnitList.isEmpty()) {
                    this.localRange[i + 1] = j;
                    this.locals.put(locals[i], Integer.valueOf(i));
                    if (currUnitList.size() >= 2) {
                        for (Unit u : currUnitList) {
                            this.indexOfUnit.put(u, Integer.valueOf(j));
                            int i2 = j;
                            j++;
                            this.universe[i2] = u;
                        }
                    } else if (omitSSA) {
                        int i3 = j;
                        j++;
                        this.universe[i3] = currUnitList.get(0);
                    }
                }
                i++;
                this.localRange[i] = j;
            }
            if (!$assertionsDisabled && this.localRange[N] != units) {
                throw new AssertionError();
            }
            doAnalysis();
            this.indexOfUnit = null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public boolean omissible(Unit u) {
            List<ValueBox> defs = u.getDefBoxes();
            if (!defs.isEmpty()) {
                for (ValueBox vb : defs) {
                    Value v = vb.getValue();
                    if (v instanceof Local) {
                        Local l = (Local) v;
                        int lno = SimpleLocalDefs.this.getLocalNumber(l);
                        return this.localRange[lno] == this.localRange[lno + 1];
                    }
                }
                return true;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public FlowAnalysis.Flow getFlow(Unit from, Unit to) {
            if ((to instanceof IdentityUnit) && (this.graph instanceof ExceptionalGraph)) {
                ExceptionalGraph<Unit> g = (ExceptionalGraph) this.graph;
                if (!g.getExceptionalPredsOf(to).isEmpty()) {
                    for (ExceptionalGraph.ExceptionDest<Unit> exd : g.getExceptionDests(from)) {
                        Trap trap = exd.getTrap();
                        if (trap != null && trap.getHandlerUnit() == to) {
                            return FlowAnalysis.Flow.IN;
                        }
                    }
                }
            }
            return FlowAnalysis.Flow.OUT;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public void flowThrough(FlowBitSet in, Unit unit, FlowBitSet out) {
            copy(in, out);
            for (ValueBox vb : unit.getDefBoxes()) {
                Value v = vb.getValue();
                if (v instanceof Local) {
                    Local l = (Local) v;
                    int lno = SimpleLocalDefs.this.getLocalNumber(l);
                    int from = this.localRange[lno];
                    int to = this.localRange[1 + lno];
                    if (from == to) {
                        continue;
                    } else if (!$assertionsDisabled && from > to) {
                        throw new AssertionError();
                    } else {
                        if (to - from == 1) {
                            out.set(from);
                        } else {
                            out.clear(from, to);
                            out.set(this.indexOfUnit.get(unit).intValue());
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void copy(FlowBitSet source, FlowBitSet dest) {
            if (dest != source) {
                dest.clear();
                dest.or(source);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public FlowBitSet newInitialFlow() {
            return new FlowBitSet();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void mergeInto(Unit succNode, FlowBitSet inout, FlowBitSet in) {
            inout.or(in);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void merge(FlowBitSet in1, FlowBitSet in2, FlowBitSet out) {
            throw new UnsupportedOperationException("should never be called");
        }

        @Override // soot.toolkits.scalar.LocalDefs
        public List<Unit> getDefsOfAt(Local l, Unit s) {
            Integer lno = this.locals.get(l);
            if (lno == null) {
                return Collections.emptyList();
            }
            int from = this.localRange[lno.intValue()];
            int to = this.localRange[lno.intValue() + 1];
            if ($assertionsDisabled || from <= to) {
                if (from == to) {
                    if ($assertionsDisabled || this.unitList[lno.intValue()].size() == 1) {
                        return this.unitList[lno.intValue()];
                    }
                    throw new AssertionError();
                }
                return getFlowBefore(s).asList(from, to);
            }
            throw new AssertionError();
        }

        @Override // soot.toolkits.scalar.LocalDefs
        public List<Unit> getDefsOf(Local l) {
            List<Unit> defs = new ArrayList<>();
            for (Unit u : this.graph) {
                List<Unit> defsOf = getDefsOfAt(l, u);
                if (defsOf != null) {
                    defs.addAll(defsOf);
                }
            }
            return defs;
        }
    }

    public SimpleLocalDefs(UnitGraph graph) {
        this(graph, FlowAnalysisMode.Automatic);
    }

    public SimpleLocalDefs(UnitGraph graph, FlowAnalysisMode mode) {
        this(graph, graph.getBody().getLocals(), mode);
    }

    SimpleLocalDefs(DirectedGraph<Unit> graph, Collection<Local> locals, FlowAnalysisMode mode) {
        this(graph, (Local[]) locals.toArray(new Local[locals.size()]), mode);
    }

    SimpleLocalDefs(DirectedGraph<Unit> graph, Local[] locals, boolean omitSSA) {
        this(graph, locals, omitSSA ? FlowAnalysisMode.OmitSSA : FlowAnalysisMode.Automatic);
    }

    SimpleLocalDefs(DirectedGraph<Unit> graph, Local[] locals, FlowAnalysisMode mode) {
        boolean time = Options.v().time();
        if (time) {
            Timers.v().defsTimer.start();
        }
        int[] oldNumbers = assignNumbers(locals);
        this.def = init(graph, locals, mode);
        restoreNumbers(locals, oldNumbers);
        if (time) {
            Timers.v().defsTimer.end();
        }
    }

    protected void restoreNumbers(Local[] locals, int[] oldNumbers) {
        for (int i = 0; i < oldNumbers.length; i++) {
            locals[i].setNumber(oldNumbers[i]);
        }
    }

    protected int[] assignNumbers(Local[] locals) {
        int N = locals.length;
        int[] oldNumbers = new int[N];
        for (int i = 0; i < N; i++) {
            oldNumbers[i] = locals[i].getNumber();
            locals[i].setNumber(i);
        }
        return oldNumbers;
    }

    protected LocalDefs init(DirectedGraph<Unit> graph, Local[] locals, FlowAnalysisMode mode) {
        List[] unitList = new List[locals.length];
        Arrays.fill(unitList, Collections.emptyList());
        boolean omitSSA = mode == FlowAnalysisMode.OmitSSA;
        boolean doFlowAnalsis = omitSSA;
        int units = 0;
        for (Unit unit : graph) {
            for (ValueBox box : unit.getDefBoxes()) {
                Value v = box.getValue();
                if (v instanceof Local) {
                    Local l = (Local) v;
                    int lno = getLocalNumber(l);
                    switch (unitList[lno].size()) {
                        case 0:
                            unitList[lno] = Collections.singletonList(unit);
                            if (omitSSA) {
                                units++;
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (!omitSSA) {
                                units++;
                            }
                            unitList[lno] = new ArrayList(unitList[lno]);
                            doFlowAnalsis = true;
                            unitList[lno].add(unit);
                            units++;
                            break;
                        default:
                            unitList[lno].add(unit);
                            units++;
                            break;
                    }
                }
            }
        }
        if (doFlowAnalsis && mode != FlowAnalysisMode.FlowInsensitive) {
            return new FlowAssignment(graph, locals, unitList, units, omitSSA);
        }
        return new StaticSingleAssignment(locals, unitList);
    }

    protected int getLocalNumber(Local l) {
        return l.getNumber();
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOfAt(Local l, Unit s) {
        return this.def.getDefsOfAt(l, s);
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOf(Local l) {
        return this.def.getDefsOf(l);
    }
}
