package fj.data.fingertrees;

import fj.F;
import fj.P;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Single.class */
public final class Single<V, A> extends FingerTree<V, A> {
    private final A a;
    private final V v;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Single(Measured<V, A> m, A a) {
        super(m);
        this.a = a;
        this.v = m.measure(a);
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B foldRight(F<A, F<B, B>> aff, B z) {
        return aff.f(this.a).f(z);
    }

    @Override // fj.data.fingertrees.FingerTree
    public A reduceRight(F<A, F<A, A>> aff) {
        return this.a;
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
        return bff.f(z).f(this.a);
    }

    @Override // fj.data.fingertrees.FingerTree
    public A reduceLeft(F<A, F<A, A>> aff) {
        return this.a;
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> FingerTree<V, B> map(F<A, B> abf, Measured<V, B> m) {
        return new Single(m, abf.f(this.a));
    }

    @Override // fj.data.fingertrees.FingerTree
    public V measure() {
        return this.v;
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B match(F<Empty<V, A>, B> empty, F<Single<V, A>, B> single, F<Deep<V, A>, B> deep) {
        return single.f(this);
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> cons(A b) {
        MakeTree<V, A> mk = mkTree(measured());
        return mk.deep(mk.one(b), new Empty<>(measured().nodeMeasured()), mk.one(this.a));
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> snoc(A b) {
        MakeTree<V, A> mk = mkTree(measured());
        return mk.deep(mk.one(this.a), new Empty<>(measured().nodeMeasured()), mk.one(b));
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> append(FingerTree<V, A> t) {
        return t.cons(this.a);
    }

    @Override // fj.data.fingertrees.FingerTree
    public P2<Integer, A> lookup(F<V, Integer> o, int i) {
        return P.p(Integer.valueOf(i), this.a);
    }

    public A value() {
        return this.a;
    }
}
