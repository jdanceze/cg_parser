package fj.data.fingertrees;

import fj.F;
import fj.data.vector.V3;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Three.class */
public final class Three<V, A> extends Digit<V, A> {
    private final V3<A> as;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Three(Measured<V, A> m, V3<A> as) {
        super(m);
        this.as = as;
    }

    @Override // fj.data.fingertrees.Digit
    public <B> B foldRight(F<A, F<B, B>> aff, B z) {
        return aff.f(this.as._1()).f(aff.f(this.as._2()).f(aff.f(this.as._3()).f(z)));
    }

    @Override // fj.data.fingertrees.Digit
    public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
        return (B) this.as.toStream().foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) bff, (F<B, F<A, B>>) z);
    }

    @Override // fj.data.fingertrees.Digit
    public <B> B match(F<One<V, A>, B> one, F<Two<V, A>, B> two, F<Three<V, A>, B> three, F<Four<V, A>, B> four) {
        return three.f(this);
    }

    public V3<A> values() {
        return this.as;
    }
}
