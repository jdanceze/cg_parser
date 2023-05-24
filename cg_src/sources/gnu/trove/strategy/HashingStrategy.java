package gnu.trove.strategy;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/strategy/HashingStrategy.class */
public interface HashingStrategy<T> extends Serializable {
    public static final long serialVersionUID = 5674097166776615540L;

    int computeHashCode(T t);

    boolean equals(T t, T t2);
}
