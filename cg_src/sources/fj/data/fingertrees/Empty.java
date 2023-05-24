package fj.data.fingertrees;

import fj.Bottom;
import fj.F;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Empty.class */
public final class Empty<V, A> extends FingerTree<V, A> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Empty(Measured<V, A> m) {
        super(m);
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> cons(A a) {
        return new Single(measured(), a);
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> snoc(A a) {
        return cons(a);
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> append(FingerTree<V, A> t) {
        return t;
    }

    @Override // fj.data.fingertrees.FingerTree
    public P2<Integer, A> lookup(F<V, Integer> o, int i) {
        throw Bottom.error("Lookup of empty tree.");
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B foldRight(F<A, F<B, B>> aff, B z) {
        return z;
    }

    @Override // fj.data.fingertrees.FingerTree
    public A reduceRight(F<A, F<A, A>> aff) {
        throw Bottom.error("Reduction of empty tree");
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
        return z;
    }

    @Override // fj.data.fingertrees.FingerTree
    public A reduceLeft(F<A, F<A, A>> aff) {
        throw Bottom.error("Reduction of empty tree");
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> FingerTree<V, B> map(F<A, B> abf, Measured<V, B> m) {
        return new Empty(m);
    }

    @Override // fj.data.fingertrees.FingerTree
    public V measure() {
        return measured().zero();
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B match(F<Empty<V, A>, B> empty, F<Single<V, A>, B> single, F<Deep<V, A>, B> deep) {
        return empty.f(this);
    }
}
