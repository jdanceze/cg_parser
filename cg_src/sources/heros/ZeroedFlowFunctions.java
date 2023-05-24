package heros;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/ZeroedFlowFunctions.class */
public class ZeroedFlowFunctions<N, D, M> implements FlowFunctions<N, D, M> {
    protected final FlowFunctions<N, D, M> delegate;
    protected final D zeroValue;

    public ZeroedFlowFunctions(FlowFunctions<N, D, M> delegate, D zeroValue) {
        this.delegate = delegate;
        this.zeroValue = zeroValue;
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getNormalFlowFunction(N curr, N succ) {
        return new ZeroedFlowFunction(this.delegate.getNormalFlowFunction(curr, succ));
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getCallFlowFunction(N callStmt, M destinationMethod) {
        return new ZeroedFlowFunction(this.delegate.getCallFlowFunction(callStmt, destinationMethod));
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getReturnFlowFunction(N callSite, M calleeMethod, N exitStmt, N returnSite) {
        return new ZeroedFlowFunction(this.delegate.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getCallToReturnFlowFunction(N callSite, N returnSite) {
        return new ZeroedFlowFunction(this.delegate.getCallToReturnFlowFunction(callSite, returnSite));
    }

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/ZeroedFlowFunctions$ZeroedFlowFunction.class */
    protected class ZeroedFlowFunction implements FlowFunction<D> {
        protected FlowFunction<D> del;

        private ZeroedFlowFunction(FlowFunction<D> del) {
            this.del = del;
        }

        @Override // heros.FlowFunction
        public Set<D> computeTargets(D source) {
            if (source == ZeroedFlowFunctions.this.zeroValue) {
                HashSet<D> res = new LinkedHashSet<>(this.del.computeTargets(source));
                res.add(ZeroedFlowFunctions.this.zeroValue);
                return res;
            }
            return this.del.computeTargets(source);
        }
    }
}
