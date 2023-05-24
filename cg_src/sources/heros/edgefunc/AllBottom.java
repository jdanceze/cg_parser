package heros.edgefunc;

import heros.EdgeFunction;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/edgefunc/AllBottom.class */
public class AllBottom<V> implements EdgeFunction<V> {
    private final V bottomElement;

    public AllBottom(V bottomElement) {
        this.bottomElement = bottomElement;
    }

    @Override // heros.EdgeFunction
    public V computeTarget(V source) {
        return this.bottomElement;
    }

    @Override // heros.EdgeFunction
    public EdgeFunction<V> composeWith(EdgeFunction<V> secondFunction) {
        if (secondFunction instanceof EdgeIdentity) {
            return this;
        }
        return secondFunction;
    }

    @Override // heros.EdgeFunction
    public EdgeFunction<V> meetWith(EdgeFunction<V> otherFunction) {
        if (otherFunction == this || otherFunction.equalTo(this)) {
            return this;
        }
        if (otherFunction instanceof AllTop) {
            return this;
        }
        if (otherFunction instanceof EdgeIdentity) {
            return this;
        }
        throw new IllegalStateException("unexpected edge function: " + otherFunction);
    }

    @Override // heros.EdgeFunction
    public boolean equalTo(EdgeFunction<V> other) {
        if (other instanceof AllBottom) {
            AllBottom allBottom = (AllBottom) other;
            return allBottom.bottomElement.equals(this.bottomElement);
        }
        return false;
    }

    public String toString() {
        return "allbottom";
    }
}
