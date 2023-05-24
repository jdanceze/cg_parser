package gnu.trove.strategy;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/strategy/IdentityHashingStrategy.class */
public class IdentityHashingStrategy<K> implements HashingStrategy<K> {
    static final long serialVersionUID = -5188534454583764904L;
    public static final IdentityHashingStrategy<Object> INSTANCE = new IdentityHashingStrategy<>();

    @Override // gnu.trove.strategy.HashingStrategy
    public int computeHashCode(K object) {
        return System.identityHashCode(object);
    }

    @Override // gnu.trove.strategy.HashingStrategy
    public boolean equals(K o1, K o2) {
        return o1 == o2;
    }
}
