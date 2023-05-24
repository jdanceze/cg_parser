package fj.data.fingertrees;

import fj.F;
import fj.P2;
import fj.data.vector.V2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Node2.class */
public final class Node2<V, A> extends Node<V, A> {
    private final V2<A> as;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Node2(Measured<V, A> m, V2<A> as) {
        super(m, m.sum(m.measure(as._1()), m.measure(as._2())));
        this.as = as;
    }

    @Override // fj.data.fingertrees.Node
    public <B> B foldRight(F<A, F<B, B>> aff, B z) {
        return aff.f(this.as._1()).f(aff.f(this.as._2()).f(z));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // fj.data.fingertrees.Node
    public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
        return (B) ((F) bff.f(((F) bff.f(z)).f(this.as._1()))).f(this.as._2());
    }

    @Override // fj.data.fingertrees.Node
    public Digit<V, A> toDigit() {
        return new Two(measured(), this.as);
    }

    @Override // fj.data.fingertrees.Node
    public P2<Integer, A> lookup(F<V, Integer> o, int i) {
        return null;
    }

    @Override // fj.data.fingertrees.Node
    public <B> B match(F<Node2<V, A>, B> n2, F<Node3<V, A>, B> n3) {
        return n2.f(this);
    }

    public V2<A> toVector() {
        return this.as;
    }
}
