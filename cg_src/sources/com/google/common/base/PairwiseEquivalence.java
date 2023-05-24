package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible(serializable = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/PairwiseEquivalence.class */
final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>> implements Serializable {
    final Equivalence<? super T> elementEquivalence;
    private static final long serialVersionUID = 1;

    @Override // com.google.common.base.Equivalence
    protected /* bridge */ /* synthetic */ int doHash(Object obj) {
        return doHash((Iterable) ((Iterable) obj));
    }

    @Override // com.google.common.base.Equivalence
    protected /* bridge */ /* synthetic */ boolean doEquivalent(Object obj, Object obj2) {
        return doEquivalent((Iterable) ((Iterable) obj), (Iterable) ((Iterable) obj2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PairwiseEquivalence(Equivalence<? super T> elementEquivalence) {
        this.elementEquivalence = (Equivalence) Preconditions.checkNotNull(elementEquivalence);
    }

    protected boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
        Iterator<T> iteratorA = iterableA.iterator();
        Iterator<T> iteratorB = iterableB.iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            if (!this.elementEquivalence.equivalent(iteratorA.next(), iteratorB.next())) {
                return false;
            }
        }
        return (iteratorA.hasNext() || iteratorB.hasNext()) ? false : true;
    }

    protected int doHash(Iterable<T> iterable) {
        int hash = 78721;
        for (T element : iterable) {
            hash = (hash * 24943) + this.elementEquivalence.hash(element);
        }
        return hash;
    }

    public boolean equals(@NullableDecl Object object) {
        if (object instanceof PairwiseEquivalence) {
            PairwiseEquivalence<?> that = (PairwiseEquivalence) object;
            return this.elementEquivalence.equals(that.elementEquivalence);
        }
        return false;
    }

    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 1185147655;
    }

    public String toString() {
        return this.elementEquivalence + ".pairwise()";
    }
}
