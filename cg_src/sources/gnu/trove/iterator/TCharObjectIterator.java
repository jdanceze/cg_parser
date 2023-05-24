package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TCharObjectIterator.class */
public interface TCharObjectIterator<V> extends TAdvancingIterator {
    char key();

    V value();

    V setValue(V v);
}
