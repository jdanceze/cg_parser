package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TShortObjectIterator.class */
public interface TShortObjectIterator<V> extends TAdvancingIterator {
    short key();

    V value();

    V setValue(V v);
}
