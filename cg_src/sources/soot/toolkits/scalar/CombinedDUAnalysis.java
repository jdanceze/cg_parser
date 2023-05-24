package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;
import soot.util.Cons;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/CombinedDUAnalysis.class */
public class CombinedDUAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<ValueBox>> implements CombinedAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(CombinedDUAnalysis.class);
    private final Map<Cons<Local, Unit>, List<Unit>> defsOfAt;
    private final Map<Unit, List<UnitValueBoxPair>> usesOf;
    private final Map<Unit, List<Local>> liveLocalsBefore;
    private final Map<Unit, List<Local>> liveLocalsAfter;
    private final Map<ValueBox, Unit> useBoxToUnit;
    private final Map<Unit, Local> unitToLocalDefed;
    private final Map<Unit, ArraySparseSet<ValueBox>> unitToLocalUseBoxes;
    private final MultiMap<Value, ValueBox> localToUseBoxes;

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOfAt(Local l, Unit s) {
        Cons<Local, Unit> cons = new Cons<>(l, s);
        List<Unit> ret = this.defsOfAt.get(cons);
        if (ret == null) {
            Map<Cons<Local, Unit>, List<Unit>> map = this.defsOfAt;
            List<Unit> arrayList = new ArrayList<>();
            ret = arrayList;
            map.put(cons, arrayList);
        }
        return ret;
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOf(Local l) {
        throw new RuntimeException("Not implemented");
    }

    @Override // soot.toolkits.scalar.LocalUses
    public List<UnitValueBoxPair> getUsesOf(Unit u) {
        List<UnitValueBoxPair> ret = this.usesOf.get(u);
        if (ret == null) {
            Local def = this.unitToLocalDefed.get(u);
            if (def == null) {
                Map<Unit, List<UnitValueBoxPair>> map = this.usesOf;
                List<UnitValueBoxPair> emptyList = Collections.emptyList();
                ret = emptyList;
                map.put(u, emptyList);
            } else {
                Map<Unit, List<UnitValueBoxPair>> map2 = this.usesOf;
                List<UnitValueBoxPair> arrayList = new ArrayList<>();
                ret = arrayList;
                map2.put(u, arrayList);
                for (ValueBox vb : getFlowAfter(u)) {
                    if (vb.getValue() == def) {
                        ret.add(new UnitValueBoxPair(this.useBoxToUnit.get(vb), vb));
                    }
                }
            }
        }
        return ret;
    }

    @Override // soot.toolkits.scalar.LiveLocals
    public List<Local> getLiveLocalsBefore(Unit u) {
        List<Local> ret = this.liveLocalsBefore.get(u);
        if (ret == null) {
            HashSet<Local> hs = new HashSet<>();
            for (ValueBox vb : getFlowBefore(u)) {
                hs.add((Local) vb.getValue());
            }
            Map<Unit, List<Local>> map = this.liveLocalsBefore;
            List<Local> arrayList = new ArrayList<>(hs);
            ret = arrayList;
            map.put(u, arrayList);
        }
        return ret;
    }

    @Override // soot.toolkits.scalar.LiveLocals
    public List<Local> getLiveLocalsAfter(Unit u) {
        List<Local> ret = this.liveLocalsAfter.get(u);
        if (ret == null) {
            HashSet<Local> hs = new HashSet<>();
            for (ValueBox vb : getFlowAfter(u)) {
                hs.add((Local) vb.getValue());
            }
            Map<Unit, List<Local>> map = this.liveLocalsAfter;
            List<Local> arrayList = new ArrayList<>(hs);
            ret = arrayList;
            map.put(u, arrayList);
        }
        return ret;
    }

    public CombinedDUAnalysis(UnitGraph graph) {
        super(graph);
        this.defsOfAt = new HashMap();
        this.usesOf = new HashMap();
        this.liveLocalsBefore = new HashMap();
        this.liveLocalsAfter = new HashMap();
        this.useBoxToUnit = new HashMap();
        this.unitToLocalDefed = new HashMap();
        this.unitToLocalUseBoxes = new HashMap();
        this.localToUseBoxes = new HashMultiMap();
        if (Options.v().verbose()) {
            logger.debug("[" + graph.getBody().getMethod().getName() + "]     Constructing CombinedDUAnalysis...");
        }
        Iterator<Unit> it = graph.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            List<Value> defs = localsInBoxes(u.getDefBoxes());
            switch (defs.size()) {
                case 0:
                    break;
                default:
                    throw new RuntimeException("Locals defed in " + u + ": " + defs.size());
                case 1:
                    this.unitToLocalDefed.put(u, (Local) defs.get(0));
                    break;
            }
            ArraySparseSet<ValueBox> localUseBoxes = new ArraySparseSet<>();
            for (ValueBox vb : u.getUseBoxes()) {
                Value v = vb.getValue();
                if (v instanceof Local) {
                    localUseBoxes.add(vb);
                    if (this.useBoxToUnit.containsKey(vb)) {
                        throw new RuntimeException("Aliased ValueBox " + vb + " in Unit " + u);
                    }
                    this.useBoxToUnit.put(vb, u);
                    this.localToUseBoxes.put(v, vb);
                }
            }
            this.unitToLocalUseBoxes.put(u, localUseBoxes);
        }
        doAnalysis();
        Iterator<Unit> it2 = graph.iterator();
        while (it2.hasNext()) {
            Unit defUnit = it2.next();
            Local localDefed = this.unitToLocalDefed.get(defUnit);
            if (localDefed != null) {
                for (ValueBox vb2 : getFlowAfter(defUnit)) {
                    if (vb2.getValue() == localDefed) {
                        Unit useUnit = this.useBoxToUnit.get(vb2);
                        getDefsOfAt(localDefed, useUnit).add(defUnit);
                    }
                }
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[" + graph.getBody().getMethod().getName() + "]     Finished CombinedDUAnalysis...");
        }
    }

    private List<Value> localsInBoxes(List<ValueBox> boxes) {
        List<Value> ret = new ArrayList<>();
        for (ValueBox vb : boxes) {
            Value v = vb.getValue();
            if (v instanceof Local) {
                ret.add(v);
            }
        }
        return ret;
    }

    protected void merge(FlowSet<ValueBox> inout, FlowSet<ValueBox> in) {
        inout.union(in);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<ValueBox> in1, FlowSet<ValueBox> in2, FlowSet<ValueBox> out) {
        in1.union(in2, out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<ValueBox> out, Unit u, FlowSet<ValueBox> in) {
        Local def = this.unitToLocalDefed.get(u);
        out.copy(in);
        if (def != null) {
            Collection<ValueBox> boxesDefed = this.localToUseBoxes.get(def);
            for (ValueBox vb : in) {
                if (boxesDefed.contains(vb)) {
                    in.remove(vb);
                }
            }
        }
        in.union(this.unitToLocalUseBoxes.get(u));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<ValueBox> entryInitialFlow() {
        return new ArraySparseSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<ValueBox> newInitialFlow() {
        return new ArraySparseSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<ValueBox> source, FlowSet<ValueBox> dest) {
        source.copy(dest);
    }
}
