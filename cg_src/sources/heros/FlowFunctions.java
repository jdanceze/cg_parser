package heros;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/FlowFunctions.class */
public interface FlowFunctions<N, D, M> {
    FlowFunction<D> getNormalFlowFunction(N n, N n2);

    FlowFunction<D> getCallFlowFunction(N n, M m);

    FlowFunction<D> getReturnFlowFunction(N n, M m, N n2, N n3);

    FlowFunction<D> getCallToReturnFlowFunction(N n, N n2);
}
