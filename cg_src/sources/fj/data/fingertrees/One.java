package fj.data.fingertrees;

import fj.F;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/One.class */
public final class One<V, A> extends Digit<V, A> {
    private final A a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public One(Measured<V, A> m, A a) {
        super(m);
        this.a = a;
    }

    @Override // fj.data.fingertrees.Digit
    public <B> B foldRight(F<A, F<B, B>> aff, B z) {
        return aff.f(this.a).f(z);
    }

    @Override // fj.data.fingertrees.Digit
    public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
        return bff.f(z).f(this.a);
    }

    @Override // fj.data.fingertrees.Digit
    public <B> B match(F<One<V, A>, B> one, F<Two<V, A>, B> two, F<Three<V, A>, B> three, F<Four<V, A>, B> four) {
        return one.f(this);
    }

    public A value() {
        return this.a;
    }
}
