package fj.data.fingertrees;

import fj.F;
import fj.P2;
import fj.data.vector.V3;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Node3.class */
public final class Node3<V, A> extends Node<V, A> {
    private final V3<A> as;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Node3(Measured<V, A> m, V3<A> as) {
        super(m, m.sum(m.measure(as._1()), m.sum(m.measure(as._2()), m.measure(as._3()))));
        this.as = as;
    }

    @Override // fj.data.fingertrees.Node
    public <B> B foldRight(F<A, F<B, B>> aff, B z) {
        return aff.f(this.as._1()).f(aff.f(this.as._2()).f(aff.f(this.as._3()).f(z)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // fj.data.fingertrees.Node
    public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
        return (B) ((F) bff.f(((F) bff.f(((F) bff.f(z)).f(this.as._1()))).f(this.as._2()))).f(this.as._3());
    }

    @Override // fj.data.fingertrees.Node
    public <B> B match(F<Node2<V, A>, B> n2, F<Node3<V, A>, B> n3) {
        return n3.f(this);
    }

    @Override // fj.data.fingertrees.Node
    public Digit<V, A> toDigit() {
        return new Three(measured(), this.as);
    }

    @Override // fj.data.fingertrees.Node
    public P2<Integer, A> lookup(F<V, Integer> o, int i) {
        return null;
    }

    public V3<A> toVector() {
        return this.as;
    }
}
