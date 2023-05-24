package fj.data.fingertrees;

import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.Monoid;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/FingerTree.class */
public abstract class FingerTree<V, A> {
    private final Measured<V, A> m;

    public abstract <B> B foldRight(F<A, F<B, B>> f, B b);

    public abstract A reduceRight(F<A, F<A, A>> f);

    public abstract <B> B foldLeft(F<B, F<A, B>> f, B b);

    public abstract A reduceLeft(F<A, F<A, A>> f);

    public abstract <B> FingerTree<V, B> map(F<A, B> f, Measured<V, B> measured);

    public abstract V measure();

    public abstract <B> B match(F<Empty<V, A>, B> f, F<Single<V, A>, B> f2, F<Deep<V, A>, B> f3);

    public abstract FingerTree<V, A> cons(A a);

    public abstract FingerTree<V, A> snoc(A a);

    public abstract FingerTree<V, A> append(FingerTree<V, A> fingerTree);

    public abstract P2<Integer, A> lookup(F<V, Integer> f, int i);

    public <B> B foldRight(F2<A, B, B> f, B z) {
        return (B) foldRight((F<A, F<F<A, F<B, B>>, F<A, F<B, B>>>>) F2Functions.curry(f), (F<A, F<B, B>>) z);
    }

    public <B> B foldLeft(F2<B, A, B> f, B z) {
        return (B) foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) F2Functions.curry(f), (F<B, F<A, B>>) z);
    }

    public <B> FingerTree<V, A> filter(F<A, Boolean> f) {
        return (FingerTree) foldLeft((F2<F2<B, A, B>, A, F2<B, A, B>>) FingerTree$$Lambda$1.lambdaFactory$(f), (F2<B, A, B>) new Empty(this.m));
    }

    public static /* synthetic */ FingerTree lambda$filter$156(F f, FingerTree acc, Object a) {
        return ((Boolean) f.f(a)).booleanValue() ? acc.snoc(a) : acc;
    }

    public final boolean isEmpty() {
        return this instanceof Empty;
    }

    public Measured<V, A> measured() {
        return this.m;
    }

    public FingerTree(Measured<V, A> m) {
        this.m = m;
    }

    public static <V, A> Measured<V, A> measured(Monoid<V> monoid, F<A, V> measure) {
        return Measured.measured(monoid, measure);
    }

    public static <V, A> MakeTree<V, A> mkTree(Measured<V, A> m) {
        return new MakeTree<>(m);
    }
}
