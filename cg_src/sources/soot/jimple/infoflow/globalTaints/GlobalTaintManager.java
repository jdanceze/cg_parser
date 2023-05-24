package soot.jimple.infoflow.globalTaints;

import heros.solver.PathEdge;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/globalTaints/GlobalTaintManager.class */
public class GlobalTaintManager {
    private final Set<Abstraction> globalTaintState = new HashSet();
    private final Set<IInfoflowSolver> solvers;

    public GlobalTaintManager(Set<IInfoflowSolver> solvers) {
        this.solvers = solvers;
    }

    public boolean addToGlobalTaintState(Abstraction abs) {
        MethodOrMethodContext mmoc;
        if (this.globalTaintState.add(abs) && this.solvers != null && !this.solvers.isEmpty()) {
            Set<Stmt> injectionPoints = new HashSet<>();
            QueueReader<MethodOrMethodContext> methodListener = Scene.v().getReachableMethods().listener();
            while (methodListener.hasNext() && (mmoc = methodListener.next()) != null) {
                SootMethod sm = mmoc.method();
                if (sm != null && sm.isConcrete()) {
                    Iterator<Unit> it = sm.getActiveBody().getUnits().iterator();
                    while (it.hasNext()) {
                        Unit u = it.next();
                        if (u instanceof Stmt) {
                            Stmt stmt = (Stmt) u;
                            for (ValueBox vb : stmt.getUseBoxes()) {
                                if (vb.getValue() instanceof StaticFieldRef) {
                                    StaticFieldRef fieldRef = (StaticFieldRef) vb.getValue();
                                    if (abs.getAccessPath().firstFieldMatches(fieldRef.getField())) {
                                        injectionPoints.add(stmt);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!injectionPoints.isEmpty()) {
                for (IInfoflowSolver solver : this.solvers) {
                    for (Stmt stmt2 : injectionPoints) {
                        solver.processEdge(new PathEdge<>(solver.getTabulationProblem().zeroValue(), stmt2, abs));
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }
}
