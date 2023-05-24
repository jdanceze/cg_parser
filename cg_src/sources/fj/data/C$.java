package fj.data;

import fj.P1;
/* renamed from: fj.data.$  reason: invalid class name */
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/$.class */
public final class C$<A, B> extends P1<B> {
    private final B b;

    private C$(B b) {
        this.b = b;
    }

    @Deprecated
    public static <A, B> C$<A, B> _(B b) {
        return constant(b);
    }

    public static <A, B> C$<A, B> __(B b) {
        return constant(b);
    }

    public static <A, B> C$<A, B> constant(B b) {
        return new C$<>(b);
    }

    @Override // fj.P1
    public B _1() {
        return this.b;
    }
}
