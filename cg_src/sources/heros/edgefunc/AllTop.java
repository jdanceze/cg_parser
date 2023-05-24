package heros.edgefunc;

import heros.EdgeFunction;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/edgefunc/AllTop.class */
public class AllTop<V> implements EdgeFunction<V> {
    private final V topElement;

    public AllTop(V topElement) {
        this.topElement = topElement;
    }

    @Override // heros.EdgeFunction
    public V computeTarget(V source) {
        return this.topElement;
    }

    @Override // heros.EdgeFunction
    public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
        return this;
    }

    @Override // heros.EdgeFunction
    public EdgeFunction<V> meetWith(EdgeFunction<V> otherFunction) {
        return otherFunction;
    }

    @Override // heros.EdgeFunction
    public boolean equalTo(EdgeFunction<V> other) {
        if (other instanceof AllTop) {
            AllTop allTop = (AllTop) other;
            return allTop.topElement.equals(this.topElement);
        }
        return false;
    }

    public String toString() {
        return "alltop";
    }
}
