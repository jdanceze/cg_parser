package soot.jimple.toolkits.scalar;

import java.util.ArrayDeque;
import java.util.Collections;
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
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/UnreachableCodeEliminator.class */
public class UnreachableCodeEliminator extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(UnreachableCodeEliminator.class);
    protected ThrowAnalysis throwAnalysis;

    public static UnreachableCodeEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_UnreachableCodeEliminator();
    }

    public UnreachableCodeEliminator(Singletons.Global g) {
        this.throwAnalysis = null;
    }

    public UnreachableCodeEliminator(ThrowAnalysis ta) {
        this.throwAnalysis = null;
        this.throwAnalysis = ta;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        boolean verbose = Options.v().verbose();
        if (verbose) {
            logger.debug("[" + body.getMethod().getName() + "] Eliminating unreachable code...");
        }
        if (this.throwAnalysis == null) {
            boolean opt = PhaseOptions.getBoolean(options, "remove-unreachable-traps", true);
            this.throwAnalysis = opt ? Scene.v().getDefaultThrowAnalysis() : PedanticThrowAnalysis.v();
        }
        Chain<Unit> units = body.getUnits();
        int origSize = units.size();
        Set<Unit> reachable = origSize == 0 ? Collections.emptySet() : reachable(units.getFirst(), ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, this.throwAnalysis, false));
        Chain<Trap> traps = body.getTraps();
        Iterator<Trap> it = traps.iterator();
        while (it.hasNext()) {
            Trap trap = it.next();
            if (trap.getBeginUnit() == trap.getEndUnit() || !reachable.contains(trap.getHandlerUnit())) {
                it.remove();
            }
        }
        Unit lastUnit = units.getLast();
        Iterator<Trap> it2 = traps.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            Trap t = it2.next();
            if (t.getEndUnit() == lastUnit) {
                reachable.add(lastUnit);
                break;
            }
        }
        Set<Unit> notReachable = null;
        if (verbose) {
            notReachable = new HashSet<>();
            for (Unit u : units) {
                if (!reachable.contains(u)) {
                    notReachable.add(u);
                }
            }
        }
        units.retainAll(reachable);
        if (verbose) {
            String name = body.getMethod().getName();
            logger.debug("[" + name + "]\t Removed " + (origSize - units.size()) + " statements: ");
            Iterator<Unit> it3 = notReachable.iterator();
            while (it3.hasNext()) {
                logger.debug("[" + name + "]\t         " + it3.next());
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> Set<T> reachable(T first, DirectedGraph<T> g) {
        if (first == null || g == 0) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet(g.size());
        Deque<T> q = new ArrayDeque<>();
        q.addFirst(first);
        do {
            T t = q.removeFirst();
            if (hashSet.add(t)) {
                q.addAll(g.getSuccsOf(t));
            }
        } while (!q.isEmpty());
        return hashSet;
    }
}
