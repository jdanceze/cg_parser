package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TLongObjectIterator.class */
public interface TLongObjectIterator<V> extends TAdvancingIterator {
    long key();

    V value();

    V setValue(V v);
}
