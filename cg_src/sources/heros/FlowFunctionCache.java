package heros;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/FlowFunctionCache.class */
public class FlowFunctionCache<N, D, M> implements FlowFunctions<N, D, M> {
    protected final FlowFunctions<N, D, M> delegate;
    protected final LoadingCache<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>> normalCache;
    protected final LoadingCache<FlowFunctionCache<N, D, M>.CallKey, FlowFunction<D>> callCache;
    protected final LoadingCache<FlowFunctionCache<N, D, M>.ReturnKey, FlowFunction<D>> returnCache;
    protected final LoadingCache<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>> callToReturnCache;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public FlowFunctionCache(final FlowFunctions<N, D, M> delegate, CacheBuilder builder) {
        this.delegate = delegate;
        this.normalCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>>() { // from class: heros.FlowFunctionCache.1
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((NNKey) ((NNKey) obj));
            }

            public FlowFunction<D> load(FlowFunctionCache<N, D, M>.NNKey key) throws Exception {
                return delegate.getNormalFlowFunction(key.getCurr(), key.getSucc());
            }
        });
        this.callCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.CallKey, FlowFunction<D>>() { // from class: heros.FlowFunctionCache.2
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((CallKey) ((CallKey) obj));
            }

            public FlowFunction<D> load(FlowFunctionCache<N, D, M>.CallKey key) throws Exception {
                return delegate.getCallFlowFunction(key.getCallStmt(), key.getDestinationMethod());
            }
        });
        this.returnCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.ReturnKey, FlowFunction<D>>() { // from class: heros.FlowFunctionCache.3
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((ReturnKey) ((ReturnKey) obj));
            }

            public FlowFunction<D> load(FlowFunctionCache<N, D, M>.ReturnKey key) throws Exception {
                return delegate.getReturnFlowFunction(key.getCallStmt(), key.getDestinationMethod(), key.getExitStmt(), key.getReturnSite());
            }
        });
        this.callToReturnCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>>() { // from class: heros.FlowFunctionCache.4
            @Override // com.google.common.cache.CacheLoader
            public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                return load((NNKey) ((NNKey) obj));
            }

            public FlowFunction<D> load(FlowFunctionCache<N, D, M>.NNKey key) throws Exception {
                return delegate.getCallToReturnFlowFunction(key.getCurr(), key.getSucc());
            }
        });
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getNormalFlowFunction(N curr, N succ) {
        return this.normalCache.getUnchecked(new NNKey(curr, succ));
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getCallFlowFunction(N callStmt, M destinationMethod) {
        return this.callCache.getUnchecked(new CallKey(callStmt, destinationMethod));
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getReturnFlowFunction(N callSite, M calleeMethod, N exitStmt, N returnSite) {
        return this.returnCache.getUnchecked(new ReturnKey(callSite, calleeMethod, exitStmt, returnSite));
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getCallToReturnFlowFunction(N callSite, N returnSite) {
        return this.callToReturnCache.getUnchecked(new NNKey(callSite, returnSite));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/FlowFunctionCache$NNKey.class */
    public class NNKey {
        private final N curr;
        private final N succ;

        private NNKey(N curr, N succ) {
            this.curr = curr;
            this.succ = succ;
        }

        public N getCurr() {
            return this.curr;
        }

        public N getSucc() {
            return this.succ;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.curr == null ? 0 : this.curr.hashCode());
            return (31 * result) + (this.succ == null ? 0 : this.succ.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            FlowFunctionCache<N, D, M>.NNKey other = (NNKey) obj;
            if (this.curr == null) {
                if (other.curr != null) {
                    return false;
                }
            } else if (!this.curr.equals(other.curr)) {
                return false;
            }
            if (this.succ == null) {
                if (other.succ != null) {
                    return false;
                }
                return true;
            } else if (!this.succ.equals(other.succ)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/FlowFunctionCache$CallKey.class */
    public class CallKey {
        private final N callStmt;
        private final M destinationMethod;

        private CallKey(N callStmt, M destinationMethod) {
            this.callStmt = callStmt;
            this.destinationMethod = destinationMethod;
        }

        public N getCallStmt() {
            return this.callStmt;
        }

        public M getDestinationMethod() {
            return this.destinationMethod;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.callStmt == null ? 0 : this.callStmt.hashCode());
            return (31 * result) + (this.destinationMethod == null ? 0 : this.destinationMethod.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            FlowFunctionCache<N, D, M>.CallKey other = (CallKey) obj;
            if (this.callStmt == null) {
                if (other.callStmt != null) {
                    return false;
                }
            } else if (!this.callStmt.equals(other.callStmt)) {
                return false;
            }
            if (this.destinationMethod == null) {
                if (other.destinationMethod != null) {
                    return false;
                }
                return true;
            } else if (!this.destinationMethod.equals(other.destinationMethod)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/FlowFunctionCache$ReturnKey.class */
    public class ReturnKey extends FlowFunctionCache<N, D, M>.CallKey {
        private final N exitStmt;
        private final N returnSite;

        private ReturnKey(N callStmt, M destinationMethod, N exitStmt, N returnSite) {
            super(callStmt, destinationMethod);
            this.exitStmt = exitStmt;
            this.returnSite = returnSite;
        }

        public N getExitStmt() {
            return this.exitStmt;
        }

        public N getReturnSite() {
            return this.returnSite;
        }

        @Override // heros.FlowFunctionCache.CallKey
        public int hashCode() {
            int result = super.hashCode();
            return (31 * ((31 * result) + (this.exitStmt == null ? 0 : this.exitStmt.hashCode()))) + (this.returnSite == null ? 0 : this.returnSite.hashCode());
        }

        @Override // heros.FlowFunctionCache.CallKey
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            FlowFunctionCache<N, D, M>.ReturnKey other = (ReturnKey) obj;
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
        this.logger.debug("Stats for flow-function cache:\nNormal:         {}\nCall:           {}\nReturn:         {}\nCall-to-return: {}\n", this.normalCache.stats(), this.callCache.stats(), this.returnCache.stats(), this.callToReturnCache.stats());
    }

    public void invalidate() {
        this.callCache.invalidateAll();
        this.callToReturnCache.invalidateAll();
        this.normalCache.invalidateAll();
        this.returnCache.invalidateAll();
    }
}
