package soot.jimple.infoflow.solver.gcSolver;

import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/IGarbageCollectorPeer.class */
public interface IGarbageCollectorPeer {
    boolean hasActiveDependencies(SootMethod sootMethod);
}
