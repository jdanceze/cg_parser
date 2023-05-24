package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TObjectLongIterator.class */
public interface TObjectLongIterator<K> extends TAdvancingIterator {
    K key();

    long value();

    long setValue(long j);
}
