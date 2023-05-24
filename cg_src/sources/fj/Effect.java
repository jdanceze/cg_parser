package fj;

import fj.function.Effect0;
import fj.function.Effect1;
import fj.function.Effect2;
import fj.function.Effect3;
import fj.function.Effect4;
import fj.function.Effect5;
import fj.function.Effect6;
import fj.function.Effect7;
import fj.function.Effect8;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Effect.class */
public class Effect {
    private Effect() {
    }

    public static P1<Unit> f(Effect0 e) {
        F f;
        f = Effect$$Lambda$1.instance;
        return P.lazy(f);
    }

    public static final <A> F<A, Unit> f(Effect1<A> e1) {
        return Effect$$Lambda$2.lambdaFactory$(e1);
    }

    public static /* synthetic */ Unit lambda$f$53(Effect1 effect1, Object a) {
        effect1.f(a);
        return Unit.unit();
    }

    public static <A, B> F2<A, B, Unit> f(Effect2<A, B> e) {
        return Effect$$Lambda$3.lambdaFactory$(e);
    }

    public static /* synthetic */ Unit lambda$f$54(Effect2 effect2, Object a, Object b) {
        effect2.f(a, b);
        return Unit.unit();
    }

    public static <A, B, C> F3<A, B, C, Unit> f(Effect3<A, B, C> e) {
        return Effect$$Lambda$4.lambdaFactory$(e);
    }

    public static /* synthetic */ Unit lambda$f$55(Effect3 effect3, Object a, Object b, Object c) {
        effect3.f(a, b, c);
        return Unit.unit();
    }

    public static <A, B, C, D> F4<A, B, C, D, Unit> f(Effect4<A, B, C, D> e) {
        return Effect$$Lambda$5.lambdaFactory$(e);
    }

    public static /* synthetic */ Unit lambda$f$56(Effect4 effect4, Object a, Object b, Object c, Object d) {
        effect4.f(a, b, c, d);
        return Unit.unit();
    }

    public static <A, B, C, D, E> F5<A, B, C, D, E, Unit> f(Effect5<A, B, C, D, E> z) {
        return Effect$$Lambda$6.lambdaFactory$(z);
    }

    public static /* synthetic */ Unit lambda$f$57(Effect5 effect5, Object a, Object b, Object c, Object d, Object e) {
        effect5.f(a, b, c, d, e);
        return Unit.unit();
    }

    public static <A, B, C, D, E, $F> F6<A, B, C, D, E, $F, Unit> f(Effect6<A, B, C, D, E, $F> z) {
        return Effect$$Lambda$7.lambdaFactory$(z);
    }

    public static /* synthetic */ Unit lambda$f$58(Effect6 effect6, Object a, Object b, Object c, Object d, Object e, Object f) {
        effect6.f(a, b, c, d, e, f);
        return Unit.unit();
    }

    public static <A, B, C, D, E, $F, G> F7<A, B, C, D, E, $F, G, Unit> f(Effect7<A, B, C, D, E, $F, G> z) {
        return Effect$$Lambda$8.lambdaFactory$(z);
    }

    public static /* synthetic */ Unit lambda$f$59(Effect7 effect7, Object a, Object b, Object c, Object d, Object e, Object f, Object g) {
        effect7.f(a, b, c, d, e, f, g);
        return Unit.unit();
    }

    public static <A, B, C, D, E, $F, G, H> F8<A, B, C, D, E, $F, G, H, Unit> f(Effect8<A, B, C, D, E, $F, G, H> z) {
        return Effect$$Lambda$9.lambdaFactory$(z);
    }

    public static /* synthetic */ Unit lambda$f$60(Effect8 effect8, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h) {
        effect8.f(a, b, c, d, e, f, g, h);
        return Unit.unit();
    }

    public final <A, B> Effect1<B> comap(final Effect1<A> e1, final F<B, A> f) {
        return new Effect1<B>() { // from class: fj.Effect.1
            {
                Effect.this = this;
            }

            @Override // fj.function.Effect1
            public void f(B b) {
                e1.f(f.f(b));
            }
        };
    }

    public static <A> Effect1<A> lazy(final F<A, Unit> f) {
        return new Effect1<A>() { // from class: fj.Effect.2
            @Override // fj.function.Effect1
            public void f(A a) {
                f.f(a);
            }
        };
    }
}
