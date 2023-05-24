package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TFloatObjectIterator.class */
public interface TFloatObjectIterator<V> extends TAdvancingIterator {
    float key();

    V value();

    V setValue(V v);
}
