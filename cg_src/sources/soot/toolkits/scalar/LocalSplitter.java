package soot.toolkits.scalar;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.Singletons;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalUses;
import soot.util.LocalBitSetPacker;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalSplitter.class */
public class LocalSplitter extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(LocalSplitter.class);
    protected ThrowAnalysis throwAnalysis;
    protected boolean omitExceptingUnitEdges;

    public LocalSplitter(Singletons.Global g) {
    }

    public LocalSplitter(ThrowAnalysis ta) {
        this(ta, false);
    }

    public LocalSplitter(ThrowAnalysis ta, boolean omitExceptingUnitEdges) {
        this.throwAnalysis = ta;
        this.omitExceptingUnitEdges = omitExceptingUnitEdges;
    }

    public static LocalSplitter v() {
        return G.v().soot_toolkits_scalar_LocalSplitter();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Splitting locals...");
        }
        if (Options.v().time()) {
            Timers.v().splitTimer.start();
            Timers.v().splitPhase1Timer.start();
        }
        if (this.throwAnalysis == null) {
            this.throwAnalysis = Scene.v().getDefaultThrowAnalysis();
        }
        if (!this.omitExceptingUnitEdges) {
            this.omitExceptingUnitEdges = Options.v().omit_excepting_unit_edges();
        }
        LocalBitSetPacker localPacker = new LocalBitSetPacker(body);
        localPacker.pack();
        ExceptionalUnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, this.throwAnalysis, this.omitExceptingUnitEdges);
        LocalDefs defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs((UnitGraph) graph, true);
        LocalUses uses = LocalUses.Factory.newLocalUses(graph, defs);
        if (Options.v().time()) {
            Timers.v().splitPhase1Timer.end();
            Timers.v().splitPhase2Timer.start();
        }
        int localCount = localPacker.getLocalCount();
        BitSet localsVisited = new BitSet(localCount);
        BitSet localsToSplit = new BitSet(localCount);
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Iterator<ValueBox> defsInUnitItr = it.next().getDefBoxes().iterator();
            if (defsInUnitItr.hasNext()) {
                Value value = defsInUnitItr.next().getValue();
                if (value instanceof Local) {
                    int localNumber = ((Local) value).getNumber();
                    if (localsVisited.get(localNumber)) {
                        localsToSplit.set(localNumber);
                    } else {
                        localsVisited.set(localNumber);
                    }
                }
            }
        }
        int w = 0;
        Set<Unit> visited = new HashSet<>();
        Iterator<Unit> it2 = body.getUnits().iterator();
        while (it2.hasNext()) {
            Unit s = it2.next();
            Iterator<ValueBox> defsInUnitItr2 = s.getDefBoxes().iterator();
            if (defsInUnitItr2.hasNext()) {
                Value singleDef = defsInUnitItr2.next().getValue();
                if (defsInUnitItr2.hasNext()) {
                    throw new RuntimeException("stmt with more than 1 defbox!");
                }
                if ((singleDef instanceof Local) && !visited.remove(s)) {
                    Local oldLocal = (Local) singleDef;
                    if (localsToSplit.get(oldLocal.getNumber())) {
                        Local newLocal = (Local) oldLocal.clone();
                        String name = newLocal.getName();
                        if (name != null) {
                            w++;
                            newLocal.setName(String.valueOf(name) + '#' + w);
                        }
                        body.getLocals().add(newLocal);
                        Deque<Unit> queue = new ArrayDeque<>();
                        queue.addFirst(s);
                        do {
                            Unit head = queue.removeFirst();
                            if (visited.add(head)) {
                                for (UnitValueBoxPair use : uses.getUsesOf(head)) {
                                    ValueBox vb = use.valueBox;
                                    Value v = vb.getValue();
                                    if (v != newLocal && (v instanceof Local)) {
                                        queue.addAll(defs.getDefsOfAt((Local) v, use.unit));
                                        vb.setValue(newLocal);
                                    }
                                }
                                for (ValueBox vb2 : head.getDefBoxes()) {
                                    if (vb2.getValue() instanceof Local) {
                                        vb2.setValue(newLocal);
                                    }
                                }
                            }
                        } while (!queue.isEmpty());
                        visited.remove(s);
                    }
                }
            }
        }
        localPacker.unpack();
        if (Options.v().time()) {
            Timers.v().splitPhase2Timer.end();
            Timers.v().splitTimer.end();
        }
    }
}
