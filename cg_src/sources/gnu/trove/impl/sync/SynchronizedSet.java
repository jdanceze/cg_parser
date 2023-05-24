package gnu.trove.impl.sync;

import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/SynchronizedSet.class */
class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
    private static final long serialVersionUID = 487447009682186044L;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SynchronizedSet(Set<E> s, Object mutex) {
        super(s, mutex);
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.c.equals(o);
        }
        return equals;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.c.hashCode();
        }
        return hashCode;
    }
}
