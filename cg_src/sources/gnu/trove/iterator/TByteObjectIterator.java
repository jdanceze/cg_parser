package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TByteObjectIterator.class */
public interface TByteObjectIterator<V> extends TAdvancingIterator {
    byte key();

    V value();

    V setValue(V v);
}
