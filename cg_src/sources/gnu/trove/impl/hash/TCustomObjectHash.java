package gnu.trove.impl.hash;

import gnu.trove.strategy.HashingStrategy;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/hash/TCustomObjectHash.class */
public abstract class TCustomObjectHash<T> extends TObjectHash<T> {
    static final long serialVersionUID = 8766048185963756400L;
    protected HashingStrategy<? super T> strategy;

    public TCustomObjectHash() {
    }

    public TCustomObjectHash(HashingStrategy<? super T> strategy) {
        this.strategy = strategy;
    }

    public TCustomObjectHash(HashingStrategy<? super T> strategy, int initialCapacity) {
        super(initialCapacity);
        this.strategy = strategy;
    }

    public TCustomObjectHash(HashingStrategy<? super T> strategy, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.strategy = strategy;
    }

    @Override // gnu.trove.impl.hash.TObjectHash
    protected int hash(Object obj) {
        return this.strategy.computeHashCode(obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // gnu.trove.impl.hash.TObjectHash
    public boolean equals(Object one, Object two) {
        return two != REMOVED && this.strategy.equals(one, two);
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        super.writeExternal(out);
        out.writeObject(this.strategy);
    }

    @Override // gnu.trove.impl.hash.TObjectHash, gnu.trove.impl.hash.THash, java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        super.readExternal(in);
        this.strategy = (HashingStrategy) in.readObject();
    }
}
