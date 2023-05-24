package heros.solver;

import com.google.common.collect.Maps;
import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;
import heros.solver.JoinHandlingNode;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/JoinHandlingNodesIFDSSolver.class */
public class JoinHandlingNodesIFDSSolver<N, D extends JoinHandlingNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {
    protected final Map<JoinHandlingNodesIFDSSolver<N, D, M, I>.CacheEntry, JoinHandlingNode<D>> cache;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // heros.solver.IDESolver
    public /* bridge */ /* synthetic */ void propagate(Object obj, Object obj2, Object obj3, EdgeFunction<IFDSSolver.BinaryDomain> edgeFunction, Object obj4, boolean z) {
        propagate((Object) ((JoinHandlingNode) obj), (EdgeFunction<IFDSSolver.BinaryDomain>) obj2, (Object) ((JoinHandlingNode) obj3), edgeFunction, (EdgeFunction<IFDSSolver.BinaryDomain>) obj4, z);
    }

    public JoinHandlingNodesIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
        super(ifdsProblem);
        this.cache = Maps.newHashMap();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
        JoinHandlingNodesIFDSSolver<N, D, M, I>.CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal.createJoinKey(), targetVal.createJoinKey());
        boolean propagate = false;
        synchronized (this) {
            if (this.cache.containsKey(currentCacheEntry)) {
                JoinHandlingNode<D> existingTargetVal = this.cache.get(currentCacheEntry);
                if (!existingTargetVal.handleJoin(targetVal)) {
                    propagate = true;
                }
            } else {
                this.cache.put(currentCacheEntry, targetVal);
                propagate = true;
            }
        }
        if (propagate) {
            super.propagate((N) sourceVal, (EdgeFunction<IFDSSolver.BinaryDomain>) target, (N) targetVal, (EdgeFunction) f, (EdgeFunction<IFDSSolver.BinaryDomain>) relatedCallSite, isUnbalancedReturn);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/JoinHandlingNodesIFDSSolver$CacheEntry.class */
    public class CacheEntry {
        private N n;
        private JoinHandlingNode.JoinKey sourceKey;
        private JoinHandlingNode.JoinKey targetKey;

        public CacheEntry(N n, JoinHandlingNode.JoinKey sourceKey, JoinHandlingNode.JoinKey targetKey) {
            this.n = n;
            this.sourceKey = sourceKey;
            this.targetKey = targetKey;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.sourceKey == null ? 0 : this.sourceKey.hashCode());
            return (31 * ((31 * result) + (this.targetKey == null ? 0 : this.targetKey.hashCode()))) + (this.n == null ? 0 : this.n.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            JoinHandlingNodesIFDSSolver<N, D, M, I>.CacheEntry other = (CacheEntry) obj;
            if (this.sourceKey == null) {
                if (other.sourceKey != null) {
                    return false;
                }
            } else if (!this.sourceKey.equals(other.sourceKey)) {
                return false;
            }
            if (this.targetKey == null) {
                if (other.targetKey != null) {
                    return false;
                }
            } else if (!this.targetKey.equals(other.targetKey)) {
                return false;
            }
            if (this.n == null) {
                if (other.n != null) {
                    return false;
                }
                return true;
            } else if (!this.n.equals(other.n)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
