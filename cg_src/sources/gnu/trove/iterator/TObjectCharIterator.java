package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TObjectCharIterator.class */
public interface TObjectCharIterator<K> extends TAdvancingIterator {
    K key();

    char value();

    char setValue(char c);
}
