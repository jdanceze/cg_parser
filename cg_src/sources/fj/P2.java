package fj;

import fj.data.List;
import fj.data.Stream;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P2.class */
public abstract class P2<A, B> {
    public abstract A _1();

    public abstract B _2();

    public final P2<B, A> swap() {
        return new P2<B, A>() { // from class: fj.P2.1
            {
                P2.this = this;
            }

            @Override // fj.P2
            public B _1() {
                return (B) P2.this._2();
            }

            @Override // fj.P2
            public A _2() {
                return (A) P2.this._1();
            }
        };
    }

    public final <X> P2<X, B> map1(final F<A, X> f) {
        return new P2<X, B>() { // from class: fj.P2.2
            {
                P2.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P2
            public X _1() {
                return f.f(P2.this._1());
            }

            @Override // fj.P2
            public B _2() {
                return (B) P2.this._2();
            }
        };
    }

    public final <X> P2<A, X> map2(final F<B, X> f) {
        return new P2<A, X>() { // from class: fj.P2.3
            {
                P2.this = this;
            }

            @Override // fj.P2
            public A _1() {
                return (A) P2.this._1();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P2
            public X _2() {
                return f.f(P2.this._2());
            }
        };
    }

    public final <C, D> P2<C, D> split(F<A, C> f, F<B, D> g) {
        F<P2<A, D>, P2<C, D>> ff = map1_(f);
        F<P2<A, B>, P2<A, D>> gg = map2_(g);
        return (P2) Function.compose(ff, gg).f(this);
    }

    public final <C> P2<C, B> cobind(final F<P2<A, B>, C> k) {
        return new P2<C, B>() { // from class: fj.P2.4
            {
                P2.this = this;
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.P2
            public C _1() {
                return k.f(P2.this);
            }

            @Override // fj.P2
            public B _2() {
                return (B) P2.this._2();
            }
        };
    }

    public final P2<P2<A, B>, B> duplicate() {
        return (P2<P2<A, B>, B>) cobind(Function.identity());
    }

    public final <C> P2<C, B> inject(C c) {
        F<P2<A, B>, C> co = Function.constant(c);
        return cobind(co);
    }

    public final <C> List<C> sequenceW(List<F<P2<A, B>, C>> fs) {
        List.Buffer<C> cs = List.Buffer.empty();
        Iterator<F<P2<A, B>, C>> it = fs.iterator();
        while (it.hasNext()) {
            F<P2<A, B>, C> f = it.next();
            cs = cs.snoc(f.f(this));
        }
        return cs.toList();
    }

    public final <C> Stream<C> sequenceW(final Stream<F<P2<A, B>, C>> fs) {
        if (fs.isEmpty()) {
            return Stream.nil();
        }
        return Stream.cons(fs.head().f(this), new P1<Stream<C>>() { // from class: fj.P2.5
            {
                P2.this = this;
            }

            @Override // fj.P1
            public Stream<C> _1() {
                return P2.this.sequenceW(fs.tail()._1());
            }
        });
    }

    public final P1<A> _1_() {
        return (P1) F1Functions.lazy(__1()).f(this);
    }

    public final P1<B> _2_() {
        return (P1) F1Functions.lazy(__2()).f(this);
    }

    /* renamed from: fj.P2$6 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P2$6.class */
    class AnonymousClass6 extends P2<A, B> {
        private final P1<A> a;
        private final P1<B> b;
        final /* synthetic */ P2 val$self;

        AnonymousClass6(P2 p2) {
            P2.this = this$0;
            this.val$self = p2;
            this.a = P1.memo(P2$6$$Lambda$1.lambdaFactory$(this.val$self));
            this.b = P1.memo(P2$6$$Lambda$2.lambdaFactory$(this.val$self));
        }

        @Override // fj.P2
        public A _1() {
            return this.a._1();
        }

        @Override // fj.P2
        public B _2() {
            return this.b._1();
        }
    }

    public final P2<A, B> memo() {
        return new AnonymousClass6(this);
    }

    public static <A, B, C, D> F<P2<A, B>, P2<C, D>> split_(final F<A, C> f, final F<B, D> g) {
        return new F<P2<A, B>, P2<C, D>>() { // from class: fj.P2.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2) ((P2) obj));
            }

            public P2<C, D> f(P2<A, B> p) {
                return p.split(f, g);
            }
        };
    }

    public static <A, B, X> F<P2<A, B>, P2<X, B>> map1_(final F<A, X> f) {
        return new F<P2<A, B>, P2<X, B>>() { // from class: fj.P2.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2) ((P2) obj));
            }

            public P2<X, B> f(P2<A, B> p) {
                return p.map1(f);
            }
        };
    }

    public static <A, B, X> F<P2<A, B>, P2<A, X>> map2_(final F<B, X> f) {
        return new F<P2<A, B>, P2<A, X>>() { // from class: fj.P2.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2) ((P2) obj));
            }

            public P2<A, X> f(P2<A, B> p) {
                return p.map2(f);
            }
        };
    }

    public static <B, C, D> P2<C, D> fanout(F<B, C> f, F<B, D> g, B b) {
        return ((P2) Function.join(P.p2()).f(b)).split(f, g);
    }

    public static <A, B> P2<B, B> map(F<A, B> f, P2<A, A> p) {
        return (P2<B, B>) p.split(f, f);
    }

    public static <A, B> F<P2<A, B>, P2<B, A>> swap_() {
        return new F<P2<A, B>, P2<B, A>>() { // from class: fj.P2.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2) ((P2) obj));
            }

            public P2<B, A> f(P2<A, B> p) {
                return p.swap();
            }
        };
    }

    public static <A, B> F<P2<A, B>, A> __1() {
        return new F<P2<A, B>, A>() { // from class: fj.P2.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2<Object, B>) obj);
            }

            public A f(P2<A, B> p) {
                return p._1();
            }
        };
    }

    public static <A, B> F<P2<A, B>, B> __2() {
        return new F<P2<A, B>, B>() { // from class: fj.P2.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2<A, Object>) obj);
            }

            public B f(P2<A, B> p) {
                return p._2();
            }
        };
    }

    public static <A, B, C> F<P2<A, B>, C> tuple(final F<A, F<B, C>> f) {
        return new F<P2<A, B>, C>() { // from class: fj.P2.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P2) ((P2) obj));
            }

            /* JADX WARN: Type inference failed for: r0v4, types: [C, java.lang.Object] */
            public C f(P2<A, B> p) {
                return ((F) f.f(p._1())).f(p._2());
            }
        };
    }

    public static <A, B, C> F<P2<A, B>, C> tuple(F2<A, B, C> f) {
        return tuple(Function.curry(f));
    }

    public static <A, B, C> F2<A, B, C> untuple(final F<P2<A, B>, C> f) {
        return new F2<A, B, C>() { // from class: fj.P2.14
            /* JADX WARN: Type inference failed for: r0v2, types: [C, java.lang.Object] */
            @Override // fj.F2
            public C f(A a, B b) {
                return f.f(P.p(a, b));
            }
        };
    }

    public String toString() {
        return Show.p2Show(Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
