package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P8.class */
public abstract class P8<A, B, C, D, E, F, G, H> {
    public abstract A _1();

    public abstract B _2();

    public abstract C _3();

    public abstract D _4();

    public abstract E _5();

    public abstract F _6();

    public abstract G _7();

    public abstract H _8();

    public final <X> P8<X, B, C, D, E, F, G, H> map1(final F<A, X> f) {
        return new P8<X, B, C, D, E, F, G, H>() { // from class: fj.P8.1
            {
                P8.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _1() {
                return f.f(P8.this._1());
            }

            @Override // fj.P8
            public B _2() {
                return (B) P8.this._2();
            }

            @Override // fj.P8
            public C _3() {
                return (C) P8.this._3();
            }

            @Override // fj.P8
            public D _4() {
                return (D) P8.this._4();
            }

            @Override // fj.P8
            public E _5() {
                return (E) P8.this._5();
            }

            @Override // fj.P8
            public F _6() {
                return (F) P8.this._6();
            }

            @Override // fj.P8
            public G _7() {
                return (G) P8.this._7();
            }

            @Override // fj.P8
            public H _8() {
                return (H) P8.this._8();
            }
        };
    }

    public final <X> P8<A, X, C, D, E, F, G, H> map2(final F<B, X> f) {
        return new P8<A, X, C, D, E, F, G, H>() { // from class: fj.P8.2
            {
                P8.this = this;
            }

            @Override // fj.P8
            public A _1() {
                return (A) P8.this._1();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _2() {
                return f.f(P8.this._2());
            }

            @Override // fj.P8
            public C _3() {
                return (C) P8.this._3();
            }

            @Override // fj.P8
            public D _4() {
                return (D) P8.this._4();
            }

            @Override // fj.P8
            public E _5() {
                return (E) P8.this._5();
            }

            @Override // fj.P8
            public F _6() {
                return (F) P8.this._6();
            }

            @Override // fj.P8
            public G _7() {
                return (G) P8.this._7();
            }

            @Override // fj.P8
            public H _8() {
                return (H) P8.this._8();
            }
        };
    }

    public final <X> P8<A, B, X, D, E, F, G, H> map3(final F<C, X> f) {
        return new P8<A, B, X, D, E, F, G, H>() { // from class: fj.P8.3
            {
                P8.this = this;
            }

            @Override // fj.P8
            public A _1() {
                return (A) P8.this._1();
            }

            @Override // fj.P8
            public B _2() {
                return (B) P8.this._2();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _3() {
                return f.f(P8.this._3());
            }

            @Override // fj.P8
            public D _4() {
                return (D) P8.this._4();
            }

            @Override // fj.P8
            public E _5() {
                return (E) P8.this._5();
            }

            @Override // fj.P8
            public F _6() {
                return (F) P8.this._6();
            }

            @Override // fj.P8
            public G _7() {
                return (G) P8.this._7();
            }

            @Override // fj.P8
            public H _8() {
                return (H) P8.this._8();
            }
        };
    }

    public final <X> P8<A, B, C, X, E, F, G, H> map4(final F<D, X> f) {
        return new P8<A, B, C, X, E, F, G, H>() { // from class: fj.P8.4
            {
                P8.this = this;
            }

            @Override // fj.P8
            public A _1() {
                return (A) P8.this._1();
            }

            @Override // fj.P8
            public B _2() {
                return (B) P8.this._2();
            }

            @Override // fj.P8
            public C _3() {
                return (C) P8.this._3();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _4() {
                return f.f(P8.this._4());
            }

            @Override // fj.P8
            public E _5() {
                return (E) P8.this._5();
            }

            @Override // fj.P8
            public F _6() {
                return (F) P8.this._6();
            }

            @Override // fj.P8
            public G _7() {
                return (G) P8.this._7();
            }

            @Override // fj.P8
            public H _8() {
                return (H) P8.this._8();
            }
        };
    }

    public final <X> P8<A, B, C, D, X, F, G, H> map5(final F<E, X> f) {
        return new P8<A, B, C, D, X, F, G, H>() { // from class: fj.P8.5
            {
                P8.this = this;
            }

            @Override // fj.P8
            public A _1() {
                return (A) P8.this._1();
            }

            @Override // fj.P8
            public B _2() {
                return (B) P8.this._2();
            }

            @Override // fj.P8
            public C _3() {
                return (C) P8.this._3();
            }

            @Override // fj.P8
            public D _4() {
                return (D) P8.this._4();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _5() {
                return f.f(P8.this._5());
            }

            @Override // fj.P8
            public F _6() {
                return (F) P8.this._6();
            }

            @Override // fj.P8
            public G _7() {
                return (G) P8.this._7();
            }

            @Override // fj.P8
            public H _8() {
                return (H) P8.this._8();
            }
        };
    }

    public final <X> P8<A, B, C, D, E, X, G, H> map6(final F<F, X> f) {
        return new P8<A, B, C, D, E, X, G, H>() { // from class: fj.P8.6
            {
                P8.this = this;
            }

            @Override // fj.P8
            public A _1() {
                return (A) P8.this._1();
            }

            @Override // fj.P8
            public B _2() {
                return (B) P8.this._2();
            }

            @Override // fj.P8
            public C _3() {
                return (C) P8.this._3();
            }

            @Override // fj.P8
            public D _4() {
                return (D) P8.this._4();
            }

            @Override // fj.P8
            public E _5() {
                return (E) P8.this._5();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _6() {
                return f.f(P8.this._6());
            }

            @Override // fj.P8
            public G _7() {
                return (G) P8.this._7();
            }

            @Override // fj.P8
            public H _8() {
                return (H) P8.this._8();
            }
        };
    }

    public final <X> P8<A, B, C, D, E, F, X, H> map7(final F<G, X> f) {
        return new P8<A, B, C, D, E, F, X, H>() { // from class: fj.P8.7
            {
                P8.this = this;
            }

            @Override // fj.P8
            public A _1() {
                return (A) P8.this._1();
            }

            @Override // fj.P8
            public B _2() {
                return (B) P8.this._2();
            }

            @Override // fj.P8
            public C _3() {
                return (C) P8.this._3();
            }

            @Override // fj.P8
            public D _4() {
                return (D) P8.this._4();
            }

            @Override // fj.P8
            public E _5() {
                return (E) P8.this._5();
            }

            @Override // fj.P8
            public F _6() {
                return (F) P8.this._6();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _7() {
                return f.f(P8.this._7());
            }

            @Override // fj.P8
            public H _8() {
                return (H) P8.this._8();
            }
        };
    }

    public final <X> P8<A, B, C, D, E, F, G, X> map8(final F<H, X> f) {
        return new P8<A, B, C, D, E, F, G, X>() { // from class: fj.P8.8
            {
                P8.this = this;
            }

            @Override // fj.P8
            public A _1() {
                return (A) P8.this._1();
            }

            @Override // fj.P8
            public B _2() {
                return (B) P8.this._2();
            }

            @Override // fj.P8
            public C _3() {
                return (C) P8.this._3();
            }

            @Override // fj.P8
            public D _4() {
                return (D) P8.this._4();
            }

            @Override // fj.P8
            public E _5() {
                return (E) P8.this._5();
            }

            @Override // fj.P8
            public F _6() {
                return (F) P8.this._6();
            }

            @Override // fj.P8
            public G _7() {
                return (G) P8.this._7();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P8
            public X _8() {
                return f.f(P8.this._8());
            }
        };
    }

    public final P1<A> _1_() {
        return (P1) F1Functions.lazy(__1()).f(this);
    }

    public final P1<B> _2_() {
        return (P1) F1Functions.lazy(__2()).f(this);
    }

    public final P1<C> _3_() {
        return (P1) F1Functions.lazy(__3()).f(this);
    }

    public final P1<D> _4_() {
        return (P1) F1Functions.lazy(__4()).f(this);
    }

    public final P1<E> _5_() {
        return (P1) F1Functions.lazy(__5()).f(this);
    }

    public final P1<F> _6_() {
        return (P1) F1Functions.lazy(__6()).f(this);
    }

    public final P1<G> _7_() {
        return (P1) F1Functions.lazy(__7()).f(this);
    }

    public final P1<H> _8_() {
        return (P1) F1Functions.lazy(__8()).f(this);
    }

    /* renamed from: fj.P8$9 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P8$9.class */
    class AnonymousClass9 extends P8<A, B, C, D, E, F, G, H> {
        private final P1<A> a;
        private final P1<B> b;
        private final P1<C> c;
        private final P1<D> d;
        private final P1<E> e;
        private final P1<F> f;
        private final P1<G> g;
        private final P1<H> h;
        final /* synthetic */ P8 val$self;

        AnonymousClass9(P8 p8) {
            P8.this = this$0;
            this.val$self = p8;
            this.a = P1.memo(P8$9$$Lambda$1.lambdaFactory$(this.val$self));
            this.b = P1.memo(P8$9$$Lambda$2.lambdaFactory$(this.val$self));
            this.c = P1.memo(P8$9$$Lambda$3.lambdaFactory$(this.val$self));
            this.d = P1.memo(P8$9$$Lambda$4.lambdaFactory$(this.val$self));
            this.e = P1.memo(P8$9$$Lambda$5.lambdaFactory$(this.val$self));
            this.f = P1.memo(P8$9$$Lambda$6.lambdaFactory$(this.val$self));
            this.g = P1.memo(P8$9$$Lambda$7.lambdaFactory$(this.val$self));
            this.h = P1.memo(P8$9$$Lambda$8.lambdaFactory$(this.val$self));
        }

        @Override // fj.P8
        public A _1() {
            return this.a._1();
        }

        @Override // fj.P8
        public B _2() {
            return this.b._1();
        }

        @Override // fj.P8
        public C _3() {
            return this.c._1();
        }

        @Override // fj.P8
        public D _4() {
            return this.d._1();
        }

        @Override // fj.P8
        public E _5() {
            return this.e._1();
        }

        @Override // fj.P8
        public F _6() {
            return this.f._1();
        }

        @Override // fj.P8
        public G _7() {
            return this.g._1();
        }

        @Override // fj.P8
        public H _8() {
            return this.h._1();
        }
    }

    public final P8<A, B, C, D, E, F, G, H> memo() {
        return new AnonymousClass9(this);
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, A> __1() {
        return new F<P8<A, B, C, D, E, F$, G, H>, A>() { // from class: fj.P8.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<Object, B, C, D, E, F$, G, H>) obj);
            }

            public A f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._1();
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, B> __2() {
        return new F<P8<A, B, C, D, E, F$, G, H>, B>() { // from class: fj.P8.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<A, Object, C, D, E, F$, G, H>) obj);
            }

            public B f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._2();
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, C> __3() {
        return new F<P8<A, B, C, D, E, F$, G, H>, C>() { // from class: fj.P8.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<A, B, Object, D, E, F$, G, H>) obj);
            }

            public C f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._3();
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, D> __4() {
        return new F<P8<A, B, C, D, E, F$, G, H>, D>() { // from class: fj.P8.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<A, B, C, Object, E, F$, G, H>) obj);
            }

            public D f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._4();
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, E> __5() {
        return new F<P8<A, B, C, D, E, F$, G, H>, E>() { // from class: fj.P8.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<A, B, C, D, Object, F$, G, H>) obj);
            }

            public E f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._5();
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, F$> __6() {
        return new F<P8<A, B, C, D, E, F$, G, H>, F$>() { // from class: fj.P8.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<A, B, C, D, E, Object, G, H>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, F$] */
            public F$ f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._6();
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, G> __7() {
        return new F<P8<A, B, C, D, E, F$, G, H>, G>() { // from class: fj.P8.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<A, B, C, D, E, F$, Object, H>) obj);
            }

            public G f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._7();
            }
        };
    }

    public static <A, B, C, D, E, F$, G, H> F<P8<A, B, C, D, E, F$, G, H>, H> __8() {
        return new F<P8<A, B, C, D, E, F$, G, H>, H>() { // from class: fj.P8.17
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P8<A, B, C, D, E, F$, G, Object>) obj);
            }

            public H f(P8<A, B, C, D, E, F$, G, H> p) {
                return p._8();
            }
        };
    }

    public String toString() {
        return Show.p8Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
