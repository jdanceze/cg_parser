package heros;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/EdgeFunctionCache.class */
public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
    protected final EdgeFunctions<N, D, M, V> delegate;
    protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>> normalCache;
    protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.CallKey, EdgeFunction<V>> callCache;
    protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.ReturnKey, EdgeFunction<V>> returnCache;
    protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>> callToReturnCache;
    Logger logger = LoggerFactory.getLogger(getClass());

    public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, CacheBuilder builder) {
        this.delegate = delegate;
        this.normalCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>>() { // from class: heros.EdgeFunctionCache.1
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((NDNDKey) ((NDNDKey) obj));
            }

            public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.NDNDKey key) throws Exception {
                return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
            }
        });
        this.callCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.CallKey, EdgeFunction<V>>() { // from class: heros.EdgeFunctionCache.2
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((CallKey) ((CallKey) obj));
            }

            public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.CallKey key) throws Exception {
                return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
            }
        });
        this.returnCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.ReturnKey, EdgeFunction<V>>() { // from class: heros.EdgeFunctionCache.3
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((ReturnKey) ((ReturnKey) obj));
            }

            public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.ReturnKey key) throws Exception {
                return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
            }
        });
        this.callToReturnCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>>() { // from class: heros.EdgeFunctionCache.4
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((NDNDKey) ((NDNDKey) obj));
            }

            public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.NDNDKey key) throws Exception {
                return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
            }
        });
    }

    @Override // heros.EdgeFunctions
    public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
        return this.normalCache.getUnchecked(new NDNDKey(curr, currNode, succ, succNode));
    }

    @Override // heros.EdgeFunctions
    public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
        return this.callCache.getUnchecked(new CallKey(callStmt, srcNode, destinationMethod, destNode));
    }

    @Override // heros.EdgeFunctions
    public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
        return this.returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
    }

    @Override // heros.EdgeFunctions
    public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
        return this.callToReturnCache.getUnchecked(new NDNDKey(callSite, callNode, returnSite, returnSideNode));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/EdgeFunctionCache$NDNDKey.class */
    public class NDNDKey {
        private final N n1;
        private final N n2;
        private final D d1;
        private final D d2;

        public NDNDKey(N n1, D d1, N n2, D d2) {
            this.n1 = n1;
            this.n2 = n2;
            this.d1 = d1;
            this.d2 = d2;
        }

        public N getN1() {
            return this.n1;
        }

        public D getD1() {
            return this.d1;
        }

        public N getN2() {
            return this.n2;
        }

        public D getD2() {
            return this.d2;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.d1 == null ? 0 : this.d1.hashCode());
            return (31 * ((31 * ((31 * result) + (this.d2 == null ? 0 : this.d2.hashCode()))) + (this.n1 == null ? 0 : this.n1.hashCode()))) + (this.n2 == null ? 0 : this.n2.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            EdgeFunctionCache<N, D, M, V>.NDNDKey other = (NDNDKey) obj;
            if (this.d1 == null) {
                if (other.d1 != null) {
                    return false;
                }
            } else if (!this.d1.equals(other.d1)) {
                return false;
            }
            if (this.d2 == null) {
                if (other.d2 != null) {
                    return false;
                }
            } else if (!this.d2.equals(other.d2)) {
                return false;
            }
            if (this.n1 == null) {
                if (other.n1 != null) {
                    return false;
                }
            } else if (!this.n1.equals(other.n1)) {
                return false;
            }
            if (this.n2 == null) {
                if (other.n2 != null) {
                    return false;
                }
                return true;
            } else if (!this.n2.equals(other.n2)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/EdgeFunctionCache$CallKey.class */
    public class CallKey {
        private final N callSite;
        private final M calleeMethod;
        private final D d1;
        private final D d2;

        public CallKey(N callSite, D d1, M calleeMethod, D d2) {
            this.callSite = callSite;
            this.calleeMethod = calleeMethod;
            this.d1 = d1;
            this.d2 = d2;
        }

        public N getCallSite() {
            return this.callSite;
        }

        public D getD1() {
            return this.d1;
        }

        public M getCalleeMethod() {
            return this.calleeMethod;
        }

        public D getD2() {
            return this.d2;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.d1 == null ? 0 : this.d1.hashCode());
            return (31 * ((31 * ((31 * result) + (this.d2 == null ? 0 : this.d2.hashCode()))) + (this.callSite == null ? 0 : this.callSite.hashCode()))) + (this.calleeMethod == null ? 0 : this.calleeMethod.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            EdgeFunctionCache<N, D, M, V>.CallKey other = (CallKey) obj;
            if (this.d1 == null) {
                if (other.d1 != null) {
                    return false;
                }
            } else if (!this.d1.equals(other.d1)) {
                return false;
            }
            if (this.d2 == null) {
                if (other.d2 != null) {
                    return false;
                }
            } else if (!this.d2.equals(other.d2)) {
                return false;
            }
            if (this.callSite == null) {
                if (other.callSite != null) {
                    return false;
                }
            } else if (!this.callSite.equals(other.callSite)) {
                return false;
            }
            if (this.calleeMethod == null) {
                if (other.calleeMethod != null) {
                    return false;
                }
                return true;
            } else if (!this.calleeMethod.equals(other.calleeMethod)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/EdgeFunctionCache$ReturnKey.class */
    public class ReturnKey extends EdgeFunctionCache<N, D, M, V>.CallKey {
        private final N exitStmt;
        private final N returnSite;

        public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
            super(callSite, exitNode, calleeMethod, retNode);
            this.exitStmt = exitStmt;
            this.returnSite = returnSite;
        }

        public N getExitStmt() {
            return this.exitStmt;
        }

        public N getReturnSite() {
            return this.returnSite;
        }

        @Override // heros.EdgeFunctionCache.CallKey
        public int hashCode() {
            int result = super.hashCode();
            return (31 * ((31 * result) + (this.exitStmt == null ? 0 : this.exitStmt.hashCode()))) + (this.returnSite == null ? 0 : this.returnSite.hashCode());
        }

        @Override // heros.EdgeFunctionCache.CallKey
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            EdgeFunctionCache<N, D, M, V>.ReturnKey other = (ReturnKey) obj;
            if (this.exitStmt == null) {
                if (other.exitStmt != null) {
                    return false;
                }
            } else if (!this.exitStmt.equals(other.exitStmt)) {
                return false;
            }
            if (this.returnSite == null) {
                if (other.returnSite != null) {
                    return false;
                }
                return true;
            } else if (!this.returnSite.equals(other.returnSite)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public void printStats() {
        this.logger.debug("Stats for edge-function cache:\nNormal:         {}\nCall:           {}\nReturn:         {}\nCall-to-return: {}\n", this.normalCache.stats(), this.callCache.stats(), this.returnCache.stats(), this.callToReturnCache.stats());
    }
}
