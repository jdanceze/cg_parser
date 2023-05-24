package heros.solver;

import com.google.common.collect.Maps;
import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;
import heros.solver.LinkedNode;
import java.util.Map;
@Deprecated
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/PathTrackingIFDSSolver.class */
public class PathTrackingIFDSSolver<N, D extends LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {
    protected final Map<PathTrackingIFDSSolver<N, D, M, I>.CacheEntry, LinkedNode<D>> cache;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // heros.solver.IDESolver
    public /* bridge */ /* synthetic */ void propagate(Object obj, Object obj2, Object obj3, EdgeFunction<IFDSSolver.BinaryDomain> edgeFunction, Object obj4, boolean z) {
        propagate((Object) ((LinkedNode) obj), (EdgeFunction<IFDSSolver.BinaryDomain>) obj2, (Object) ((LinkedNode) obj3), edgeFunction, (EdgeFunction<IFDSSolver.BinaryDomain>) obj4, z);
    }

    public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
        super(ifdsProblem);
        this.cache = Maps.newHashMap();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
        PathTrackingIFDSSolver<N, D, M, I>.CacheEntry currentCacheEntry = new CacheEntry(target, sourceVal, targetVal);
        boolean propagate = false;
        synchronized (this) {
            if (this.cache.containsKey(currentCacheEntry)) {
                LinkedNode<D> existingTargetVal = this.cache.get(currentCacheEntry);
                if (existingTargetVal != targetVal) {
                    existingTargetVal.addNeighbor(targetVal);
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
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/PathTrackingIFDSSolver$CacheEntry.class */
    public class CacheEntry {
        private N n;
        private D sourceVal;
        private D targetVal;

        public CacheEntry(N n, D sourceVal, D targetVal) {
            this.n = n;
            this.sourceVal = sourceVal;
            this.targetVal = targetVal;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.sourceVal == null ? 0 : this.sourceVal.hashCode());
            return (31 * ((31 * result) + (this.targetVal == null ? 0 : this.targetVal.hashCode()))) + (this.n == null ? 0 : this.n.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PathTrackingIFDSSolver<N, D, M, I>.CacheEntry other = (CacheEntry) obj;
            if (this.sourceVal == null) {
                if (other.sourceVal != null) {
                    return false;
                }
            } else if (!this.sourceVal.equals(other.sourceVal)) {
                return false;
            }
            if (this.targetVal == null) {
                if (other.targetVal != null) {
                    return false;
                }
            } else if (!this.targetVal.equals(other.targetVal)) {
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
