package soot.jimple.toolkits.annotation.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/logic/LoopFinder.class */
public class LoopFinder extends BodyTransformer {
    private Set<Loop> loops = null;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LoopFinder.class.desiredAssertionStatus();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        getLoops(b);
    }

    public Set<Loop> getLoops(Body b) {
        if (this.loops != null) {
            return this.loops;
        }
        return getLoops(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b));
    }

    public Set<Loop> getLoops(UnitGraph g) {
        if (this.loops != null) {
            return this.loops;
        }
        MHGDominatorsFinder<Unit> a = new MHGDominatorsFinder<>(g);
        Map<Stmt, List<Stmt>> loops = new HashMap<>();
        Iterator<Unit> it = g.getBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            List<Unit> succs = g.getSuccsOf(u);
            List<Unit> dominaters = a.getDominators(u);
            List<Stmt> headers = new ArrayList<>();
            for (Unit succ : succs) {
                if (dominaters.contains(succ)) {
                    headers.add((Stmt) succ);
                }
            }
            for (Stmt header : headers) {
                List<Stmt> loopBody = getLoopBodyFor(header, u, g);
                if (loops.containsKey(header)) {
                    List<Stmt> lb1 = loops.get(header);
                    loops.put(header, union(lb1, loopBody));
                } else {
                    loops.put(header, loopBody);
                }
            }
        }
        Set<Loop> ret = new HashSet<>();
        for (Map.Entry<Stmt, List<Stmt>> entry : loops.entrySet()) {
            ret.add(new Loop(entry.getKey(), entry.getValue(), g));
        }
        this.loops = ret;
        return ret;
    }

    private List<Stmt> getLoopBodyFor(Unit header, Unit node, UnitGraph g) {
        List<Stmt> loopBody = new ArrayList<>();
        Deque<Unit> stack = new ArrayDeque<>();
        loopBody.add((Stmt) header);
        stack.push(node);
        while (!stack.isEmpty()) {
            Stmt next = (Stmt) stack.pop();
            if (!loopBody.contains(next)) {
                loopBody.add(0, next);
                for (Unit u : g.getPredsOf((Unit) next)) {
                    stack.push(u);
                }
            }
        }
        if ($assertionsDisabled || ((node == header && loopBody.size() == 1) || loopBody.get(loopBody.size() - 2) == node)) {
            if ($assertionsDisabled || loopBody.get(loopBody.size() - 1) == header) {
                return loopBody;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    private List<Stmt> union(List<Stmt> l1, List<Stmt> l2) {
        for (Stmt next : l2) {
            if (!l1.contains(next)) {
                l1.add(next);
            }
        }
        return l1;
    }
}
