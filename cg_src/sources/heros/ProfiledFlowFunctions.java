package heros;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/ProfiledFlowFunctions.class */
public class ProfiledFlowFunctions<N, D, M> implements FlowFunctions<N, D, M> {
    protected final FlowFunctions<N, D, M> delegate;
    public long durationNormal;
    public long durationCall;
    public long durationReturn;
    public long durationCallReturn;

    public ProfiledFlowFunctions(FlowFunctions<N, D, M> delegate) {
        this.delegate = delegate;
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getNormalFlowFunction(N curr, N succ) {
        long before = System.currentTimeMillis();
        FlowFunction<D> ret = this.delegate.getNormalFlowFunction(curr, succ);
        long duration = System.currentTimeMillis() - before;
        this.durationNormal += duration;
        return ret;
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getCallFlowFunction(N callStmt, M destinationMethod) {
        long before = System.currentTimeMillis();
        FlowFunction<D> res = this.delegate.getCallFlowFunction(callStmt, destinationMethod);
        long duration = System.currentTimeMillis() - before;
        this.durationCall += duration;
        return res;
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getReturnFlowFunction(N callSite, M calleeMethod, N exitStmt, N returnSite) {
        long before = System.currentTimeMillis();
        FlowFunction<D> res = this.delegate.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite);
        long duration = System.currentTimeMillis() - before;
        this.durationReturn += duration;
        return res;
    }

    @Override // heros.FlowFunctions
    public FlowFunction<D> getCallToReturnFlowFunction(N callSite, N returnSite) {
        long before = System.currentTimeMillis();
        FlowFunction<D> res = this.delegate.getCallToReturnFlowFunction(callSite, returnSite);
        long duration = System.currentTimeMillis() - before;
        this.durationCallReturn += duration;
        return res;
    }
}
