package heros;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/EdgeFunctions.class */
public interface EdgeFunctions<N, D, M, V> {
    EdgeFunction<V> getNormalEdgeFunction(N n, D d, N n2, D d2);

    EdgeFunction<V> getCallEdgeFunction(N n, D d, M m, D d2);

    EdgeFunction<V> getReturnEdgeFunction(N n, M m, N n2, D d, N n3, D d2);

    EdgeFunction<V> getCallToReturnEdgeFunction(N n, D d, N n2, D d2);
}
