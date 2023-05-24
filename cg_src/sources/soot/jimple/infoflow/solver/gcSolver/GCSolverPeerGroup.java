package soot.jimple.infoflow.solver.gcSolver;

import soot.jimple.infoflow.solver.SolverPeerGroup;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/GCSolverPeerGroup.class */
public class GCSolverPeerGroup extends SolverPeerGroup {
    private GarbageCollectorPeerGroup gcPeerGroup = null;

    public GarbageCollectorPeerGroup getGCPeerGroup() {
        if (this.gcPeerGroup == null) {
            this.gcPeerGroup = new GarbageCollectorPeerGroup();
        }
        return this.gcPeerGroup;
    }
}
