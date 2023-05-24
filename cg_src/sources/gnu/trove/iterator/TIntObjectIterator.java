package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TIntObjectIterator.class */
public interface TIntObjectIterator<V> extends TAdvancingIterator {
    int key();

    V value();

    V setValue(V v);
}
