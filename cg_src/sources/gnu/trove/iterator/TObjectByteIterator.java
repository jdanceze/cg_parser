package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TObjectByteIterator.class */
public interface TObjectByteIterator<K> extends TAdvancingIterator {
    K key();

    byte value();

    byte setValue(byte b);
}
