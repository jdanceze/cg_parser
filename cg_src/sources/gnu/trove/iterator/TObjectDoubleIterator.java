package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TObjectDoubleIterator.class */
public interface TObjectDoubleIterator<K> extends TAdvancingIterator {
    K key();

    double value();

    double setValue(double d);
}
