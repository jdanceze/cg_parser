package heros.edgefunc;

import heros.EdgeFunction;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/edgefunc/EdgeIdentity.class */
public class EdgeIdentity<V> implements EdgeFunction<V> {
    private static final EdgeIdentity instance = new EdgeIdentity();

    private EdgeIdentity() {
    }

    @Override // heros.EdgeFunction
    public V computeTarget(V source) {
        return source;
    }

    @Override // heros.EdgeFunction
    public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
        return secondFunction;
    }

    @Override // heros.EdgeFunction
    public EdgeFunction<V> meetWith(EdgeFunction<V> otherFunction) {
        if (otherFunction == this || otherFunction.equalTo(this)) {
            return this;
        }
        if (otherFunction instanceof AllBottom) {
            return otherFunction;
        }
        if (otherFunction instanceof AllTop) {
            return this;
        }
        return otherFunction.meetWith(this);
    }

    @Override // heros.EdgeFunction
    public boolean equalTo(EdgeFunction<V> other) {
        return other == this;
    }

    public static <A> EdgeIdentity<A> v() {
        return instance;
    }

    public String toString() {
        return "id";
    }
}
