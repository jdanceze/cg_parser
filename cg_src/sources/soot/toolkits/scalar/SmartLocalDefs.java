package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Cons;
import soot.util.LocalBitSetPacker;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SmartLocalDefs.class */
public class SmartLocalDefs implements LocalDefs {
    private static final Logger logger = LoggerFactory.getLogger(SmartLocalDefs.class);
    private final UnitGraph graph;
    private Map<Local, Set<Unit>> localToDefs;
    private Map<Unit, BitSet> liveLocalsAfter;
    private final Map<Cons<Unit, Local>, List<Unit>> answer = new HashMap();

    public SmartLocalDefs(UnitGraph g, LiveLocals live) {
        Local l;
        Set<Unit> s2;
        this.graph = g;
        this.localToDefs = new HashMap((2 * g.getBody().getLocalCount()) + 1);
        this.liveLocalsAfter = new HashMap((2 * g.getBody().getUnits().size()) + 1);
        Options op = Options.v();
        if (op.verbose()) {
            logger.debug("[" + g.getBody().getMethod().getName() + "]     Constructing SmartLocalDefs...");
        }
        if (op.time()) {
            Timers.v().defsTimer.start();
        }
        LocalBitSetPacker localPacker = new LocalBitSetPacker(g.getBody());
        localPacker.pack();
        Iterator<Unit> it = g.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            BitSet set = new BitSet(localPacker.getLocalCount());
            for (Local l2 : live.getLiveLocalsAfter(u)) {
                set.set(l2.getNumber());
            }
            this.liveLocalsAfter.put(u, set);
            Local l3 = localDef(u);
            if (l3 != null) {
                addDefOf(l3, u);
            }
        }
        if (op.verbose()) {
            logger.debug("[" + g.getBody().getMethod().getName() + "]        done localToDefs map...");
        }
        LocalDefsAnalysis analysis = new LocalDefsAnalysis(g);
        this.liveLocalsAfter = null;
        Iterator<Unit> it2 = g.iterator();
        while (it2.hasNext()) {
            Unit u2 = it2.next();
            Set<Unit> s1 = analysis.getFlowBefore(u2);
            if (s1 != null && !s1.isEmpty()) {
                for (ValueBox vb : u2.getUseBoxes()) {
                    Value v = vb.getValue();
                    if ((v instanceof Local) && (s2 = defsOf((l = (Local) v))) != null && !s2.isEmpty()) {
                        List<Unit> lst = intersectionAsList(s1, s2);
                        if (!lst.isEmpty()) {
                            this.answer.putIfAbsent(new Cons<>(u2, l), lst);
                        }
                    }
                }
            }
        }
        this.localToDefs = null;
        localPacker.unpack();
        if (op.time()) {
            Timers.v().defsTimer.end();
        }
        if (op.verbose()) {
            logger.debug("[" + g.getBody().getMethod().getName() + "]     SmartLocalDefs finished.");
        }
    }

    private static <T> List<T> intersectionAsList(Set<T> a, Set<T> b) {
        if (a == null || b == null || a.isEmpty() || b.isEmpty()) {
            return Collections.emptyList();
        }
        if (a.size() < b.size()) {
            List<T> c = new ArrayList<>(a);
            c.retainAll(b);
            return c;
        }
        List<T> c2 = new ArrayList<>(b);
        c2.retainAll(a);
        return c2;
    }

    public void printAnswer() {
        System.out.println(this.answer.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Local localDef(Unit u) {
        List<ValueBox> defBoxes = u.getDefBoxes();
        switch (defBoxes.size()) {
            case 0:
                return null;
            case 1:
                Value v = defBoxes.get(0).getValue();
                if (v instanceof Local) {
                    return (Local) v;
                }
                return null;
            default:
                throw new RuntimeException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<Unit> defsOf(Local l) {
        Set<Unit> s = this.localToDefs.get(l);
        return s == null ? Collections.emptySet() : s;
    }

    private void addDefOf(Local l, Unit u) {
        Set<Unit> s = this.localToDefs.get(l);
        if (s == null) {
            Map<Local, Set<Unit>> map = this.localToDefs;
            Set<Unit> hashSet = new HashSet<>();
            s = hashSet;
            map.put(l, hashSet);
        }
        s.add(u);
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SmartLocalDefs$LocalDefsAnalysis.class */
    class LocalDefsAnalysis extends ForwardFlowAnalysisExtended<Unit, Set<Unit>> {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SmartLocalDefs.class.desiredAssertionStatus();
        }

        LocalDefsAnalysis(UnitGraph g) {
            super(g);
            doAnalysis();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.ForwardFlowAnalysisExtended
        public void mergeInto(Unit succNode, Set<Unit> inout, Set<Unit> in) {
            inout.addAll(in);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.ForwardFlowAnalysisExtended
        public void merge(Set<Unit> in1, Set<Unit> in2, Set<Unit> out) {
            throw new RuntimeException("should never be called");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.ForwardFlowAnalysisExtended
        public void flowThrough(Set<Unit> in, Unit u, Unit succ, Set<Unit> out) {
            out.clear();
            BitSet liveLocals = (BitSet) SmartLocalDefs.this.liveLocalsAfter.get(u);
            Local l = SmartLocalDefs.this.localDef(u);
            if (l != null) {
                Set<Unit> allDefUnits = SmartLocalDefs.this.defsOf(l);
                boolean isExceptionalTarget = false;
                if (this.graph instanceof ExceptionalUnitGraph) {
                    for (ExceptionalUnitGraph.ExceptionDest ed : ((ExceptionalUnitGraph) this.graph).getExceptionDests(u)) {
                        if (ed.getTrap() != null && ed.getTrap().getHandlerUnit() == succ) {
                            isExceptionalTarget = true;
                        }
                    }
                }
                for (Unit inU : in) {
                    if (liveLocals.get(SmartLocalDefs.this.localDef(inU).getNumber()) && (isExceptionalTarget || !allDefUnits.contains(inU))) {
                        out.add(inU);
                    }
                }
                if (!$assertionsDisabled && !isExceptionalTarget && out.removeAll(allDefUnits)) {
                    throw new AssertionError();
                }
                if (liveLocals.get(l.getNumber()) && !isExceptionalTarget) {
                    out.add(u);
                    return;
                }
                return;
            }
            for (Unit inU2 : in) {
                if (liveLocals.get(SmartLocalDefs.this.localDef(inU2).getNumber())) {
                    out.add(inU2);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.ForwardFlowAnalysisExtended
        public void copy(Set<Unit> sourceSet, Set<Unit> destSet) {
            destSet.clear();
            destSet.addAll(sourceSet);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // soot.toolkits.scalar.ForwardFlowAnalysisExtended
        public Set<Unit> newInitialFlow() {
            return new HashSet();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // soot.toolkits.scalar.ForwardFlowAnalysisExtended
        public Set<Unit> entryInitialFlow() {
            return new HashSet();
        }
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOfAt(Local l, Unit s) {
        List<Unit> lst = this.answer.get(new Cons(s, l));
        return lst != null ? lst : Collections.emptyList();
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOf(Local l) {
        List<Unit> result = new ArrayList<>();
        for (Cons<Unit, Local> cons : this.answer.keySet()) {
            if (cons.cdr() == l) {
                result.addAll(this.answer.get(cons));
            }
        }
        return result;
    }

    public UnitGraph getGraph() {
        return this.graph;
    }
}
