package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TDoubleObjectIterator.class */
public interface TDoubleObjectIterator<V> extends TAdvancingIterator {
    double key();

    V value();

    V setValue(V v);
}
