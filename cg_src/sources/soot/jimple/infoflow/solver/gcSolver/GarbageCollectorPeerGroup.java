package soot.jimple.infoflow.solver.gcSolver;

import java.util.Collection;
import java.util.HashSet;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/GarbageCollectorPeerGroup.class */
public class GarbageCollectorPeerGroup implements IGarbageCollectorPeer {
    private final Collection<IGarbageCollectorPeer> peers;

    public GarbageCollectorPeerGroup() {
        this.peers = new HashSet();
    }

    public GarbageCollectorPeerGroup(Collection<IGarbageCollectorPeer> peers) {
        this.peers = peers;
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollectorPeer
    public boolean hasActiveDependencies(SootMethod method) {
        for (IGarbageCollectorPeer peer : this.peers) {
            if (peer.hasActiveDependencies(method)) {
                return true;
            }
        }
        return false;
    }

    public void addGarbageCollector(IGarbageCollectorPeer peer) {
        this.peers.add(peer);
    }
}
