package soot.jimple.toolkits.pointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/FieldRWTagger.class */
public class FieldRWTagger extends BodyTransformer {
    public int numRWs = 0;
    public int numWRs = 0;
    public int numRRs = 0;
    public int numWWs = 0;
    public int numNatives = 0;
    public Date startTime = null;
    boolean optionDontTag = false;
    boolean optionNaive = false;
    private CallGraph cg;

    public FieldRWTagger(Singletons.Global g) {
    }

    public static FieldRWTagger v() {
        return G.v().soot_jimple_toolkits_pointer_FieldRWTagger();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/FieldRWTagger$UniqueRWSets.class */
    protected class UniqueRWSets implements Iterable<RWSet> {
        protected final ArrayList<RWSet> l = new ArrayList<>();

        protected UniqueRWSets() {
        }

        RWSet getUnique(RWSet s) {
            if (s != null) {
                Iterator<RWSet> it = this.l.iterator();
                while (it.hasNext()) {
                    RWSet ret = it.next();
                    if (ret.isEquivTo(s)) {
                        return ret;
                    }
                }
                this.l.add(s);
            }
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
        if (G.v().Union_factory == null) {
            G.v().Union_factory = new UnionFactory() { // from class: soot.jimple.toolkits.pointer.FieldRWTagger.1
                @Override // soot.jimple.toolkits.pointer.UnionFactory
                public Union newUnion() {
                    return FullObjectSet.v();
                }
            };
        }
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
        initializationStuff(phaseName);
        SideEffectAnalysis sea = new SideEffectAnalysis(DumbPointerAnalysis.v(), Scene.v().getCallGraph());
        sea.findNTRWSets(body.getMethod());
        HashMap<Object, RWSet> stmtToReadSet = new HashMap<>();
        HashMap<Object, RWSet> stmtToWriteSet = new HashMap<>();
        UniqueRWSets sets = new UniqueRWSets();
        this.optionDontTag = PhaseOptions.getBoolean(options, "dont-tag");
        boolean justDoTotallyConservativeThing = "<clinit>".equals(body.getMethod().getName());
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                if (justDoTotallyConservativeThing) {
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
        }
    }
}
