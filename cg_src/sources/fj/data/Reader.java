package fj.data;

import fj.F;
import fj.F1Functions;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Reader.class */
public class Reader<A, B> {
    private F<A, B> function;

    public Reader(F<A, B> f) {
        this.function = f;
    }

    public F<A, B> getFunction() {
        return this.function;
    }

    public static <A, B> Reader<A, B> unit(F<A, B> f) {
        return new Reader<>(f);
    }

    public static <A, B> Reader<A, B> constant(B b) {
        return unit(Reader$$Lambda$1.lambdaFactory$(b));
    }

    public static /* synthetic */ Object lambda$constant$154(Object obj, Object a) {
        return obj;
    }

    public B f(A a) {
        return this.function.f(a);
    }

    public <C> Reader<A, C> map(F<B, C> f) {
        return unit(F1Functions.andThen(this.function, f));
    }

    public <C> Reader<A, C> andThen(F<B, C> f) {
        return map(f);
    }

    public <C> Reader<A, C> flatMap(F<B, Reader<A, C>> f) {
        return unit(Reader$$Lambda$2.lambdaFactory$(this, f));
    }

    public /* synthetic */ Object lambda$flatMap$155(F f, Object a) {
        return ((Reader) f.f(this.function.f(a))).f(a);
    }

    public <C> Reader<A, C> bind(F<B, Reader<A, C>> f) {
        return flatMap(f);
    }
}
