package soot.jimple.toolkits.pointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/SideEffectTagger.class */
public class SideEffectTagger extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(SideEffectTagger.class);
    public int numRWs = 0;
    public int numWRs = 0;
    public int numRRs = 0;
    public int numWWs = 0;
    public int numNatives = 0;
    public Date startTime = null;
    boolean optionNaive = false;
    private CallGraph cg;

    public SideEffectTagger(Singletons.Global g) {
    }

    public static SideEffectTagger v() {
        return G.v().soot_jimple_toolkits_pointer_SideEffectTagger();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/SideEffectTagger$UniqueRWSets.class */
    protected class UniqueRWSets implements Iterable<RWSet> {
        protected ArrayList<RWSet> l = new ArrayList<>();

        protected UniqueRWSets() {
        }

        RWSet getUnique(RWSet s) {
            if (s == null) {
                return s;
            }
            Iterator<RWSet> it = this.l.iterator();
            while (it.hasNext()) {
                RWSet ret = it.next();
                if (ret.isEquivTo(s)) {
                    return ret;
                }
            }
            this.l.add(s);
            return s;
        }

        @Override // java.lang.Iterable
        public Iterator<RWSet> iterator() {
            return this.l.iterator();
        }

        short indexOf(RWSet s) {
            short i = 0;
            Iterator<RWSet> it = this.l.iterator();
            while (it.hasNext()) {
                RWSet ret = it.next();
                if (ret.isEquivTo(s)) {
                    return i;
                }
                i = (short) (i + 1);
            }
            return (short) -1;
        }
    }

    protected void initializationStuff(String phaseName) {
        G.v().Union_factory = new UnionFactory() { // from class: soot.jimple.toolkits.pointer.SideEffectTagger.1
            @Override // soot.jimple.toolkits.pointer.UnionFactory
            public Union newUnion() {
                return new MemoryEfficientRasUnion();
            }
        };
        if (this.startTime == null) {
            this.startTime = new Date();
        }
        this.cg = Scene.v().getCallGraph();
    }

    protected Object keyFor(Stmt s) {
        if (s.containsInvokeExpr()) {
            if (this.optionNaive) {
                throw new RuntimeException("shouldn't get here");
            }
            Iterator<Edge> it = this.cg.edgesOutOf(s);
            if (!it.hasNext()) {
                return Collections.emptyList();
            }
            ArrayList<Edge> ret = new ArrayList<>();
            while (it.hasNext()) {
                ret.add(it.next());
            }
            return ret;
        }
        return s;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        RWSet inner;
        initializationStuff(phaseName);
        SideEffectAnalysis sea = Scene.v().getSideEffectAnalysis();
        this.optionNaive = PhaseOptions.getBoolean(options, "naive");
        if (!this.optionNaive) {
            sea.findNTRWSets(body.getMethod());
        }
        HashMap<Object, RWSet> stmtToReadSet = new HashMap<>();
        HashMap<Object, RWSet> stmtToWriteSet = new HashMap<>();
        UniqueRWSets sets = new UniqueRWSets();
        boolean justDoTotallyConservativeThing = "<clinit>".equals(body.getMethod().getName());
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit next = it.next();
            Stmt stmt = (Stmt) next;
            if (justDoTotallyConservativeThing || (this.optionNaive && stmt.containsInvokeExpr())) {
                stmtToReadSet.put(stmt, sets.getUnique(new FullRWSet()));
                stmtToWriteSet.put(stmt, sets.getUnique(new FullRWSet()));
            } else {
                Object key = keyFor(stmt);
                if (!stmtToReadSet.containsKey(key)) {
                    stmtToReadSet.put(key, sets.getUnique(sea.readSet(body.getMethod(), stmt)));
                    stmtToWriteSet.put(key, sets.getUnique(sea.writeSet(body.getMethod(), stmt)));
                }
            }
        }
        DependenceGraph graph = new DependenceGraph();
        Iterator<RWSet> it2 = sets.iterator();
        while (it2.hasNext()) {
            RWSet outer = it2.next();
            Iterator<RWSet> it3 = sets.iterator();
            while (it3.hasNext() && (inner = it3.next()) != outer) {
                if (outer.hasNonEmptyIntersection(inner)) {
                    graph.addEdge(sets.indexOf(outer), sets.indexOf(inner));
                }
            }
        }
        body.getMethod().addTag(graph);
        Iterator<Unit> it4 = body.getUnits().iterator();
        while (it4.hasNext()) {
            Unit next2 = it4.next();
            Stmt stmt2 = (Stmt) next2;
            Object key2 = (this.optionNaive && stmt2.containsInvokeExpr()) ? stmt2 : keyFor(stmt2);
            RWSet read = stmtToReadSet.get(key2);
            RWSet write = stmtToWriteSet.get(key2);
            if (read != null || write != null) {
                DependenceTag tag = new DependenceTag();
                if (read != null && read.getCallsNative()) {
                    tag.setCallsNative();
                    this.numNatives++;
                } else if (write != null && write.getCallsNative()) {
                    tag.setCallsNative();
                    this.numNatives++;
                }
                tag.setRead(sets.indexOf(read));
                tag.setWrite(sets.indexOf(write));
                stmt2.addTag(tag);
            }
        }
    }
}
