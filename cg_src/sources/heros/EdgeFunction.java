package heros;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/EdgeFunction.class */
public interface EdgeFunction<V> {
    V computeTarget(V v);

    EdgeFunction<V> composeWith(EdgeFunction<V> edgeFunction);

    EdgeFunction<V> meetWith(EdgeFunction<V> edgeFunction);

    boolean equalTo(EdgeFunction<V> edgeFunction);
}
