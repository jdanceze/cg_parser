package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TObjectShortIterator.class */
public interface TObjectShortIterator<K> extends TAdvancingIterator {
    K key();

    short value();

    short setValue(short s);
}
