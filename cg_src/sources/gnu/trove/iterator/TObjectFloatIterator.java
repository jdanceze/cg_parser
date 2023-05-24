package gnu.trove.iterator;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/iterator/TObjectFloatIterator.class */
public interface TObjectFloatIterator<K> extends TAdvancingIterator {
    K key();

    float value();

    float setValue(float f);
}
