package fj.data;

import fj.F;
import fj.Monoid;
import fj.P;
import fj.P2;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Writer.class */
public class Writer<W, A> {
    private A val;
    private W logValue;
    private Monoid<W> monoid;

    private Writer(A a, W w, Monoid<W> m) {
        this.val = a;
        this.logValue = w;
        this.monoid = m;
    }

    public P2<W, A> run() {
        return P.p(this.logValue, this.val);
    }

    public A value() {
        return this.val;
    }

    public W log() {
        return this.logValue;
    }

    public Monoid<W> monoid() {
        return this.monoid;
    }

    public static <W, A> Writer<W, A> unit(A a, W w, Monoid<W> m) {
        return new Writer<>(a, w, m);
    }

    public static <W, A> Writer<W, A> unit(A a, Monoid<W> m) {
        return new Writer<>(a, m.zero(), m);
    }

    public Writer<W, A> tell(W w) {
        return unit(this.val, this.monoid.sum(this.logValue, w), this.monoid);
    }

    public <B> Writer<W, B> map(F<A, B> f) {
        return unit(f.f(this.val), this.logValue, this.monoid);
    }

    public <B> Writer<W, B> flatMap(F<A, Writer<W, B>> f) {
        Writer<W, B> writer = f.f(this.val);
        return unit(writer.val, writer.monoid.sum(this.logValue, writer.logValue), writer.monoid);
    }

    public static <B> Writer<String, B> unit(B b) {
        return unit(b, Monoid.stringMonoid);
    }

    public static <A> F<A, Writer<String, A>> stringLogger() {
        F<A, Writer<String, A>> f;
        f = Writer$$Lambda$1.instance;
        return f;
    }

    public static /* synthetic */ Writer lambda$stringLogger$51(Object a) {
        return unit(a, Monoid.stringMonoid);
    }
}
