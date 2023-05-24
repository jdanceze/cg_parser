package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P7.class */
public abstract class P7<A, B, C, D, E, F, G> {
    public abstract A _1();

    public abstract B _2();

    public abstract C _3();

    public abstract D _4();

    public abstract E _5();

    public abstract F _6();

    public abstract G _7();

    public final <X> P7<X, B, C, D, E, F, G> map1(final F<A, X> f) {
        return new P7<X, B, C, D, E, F, G>() { // from class: fj.P7.1
            {
                P7.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P7
            public X _1() {
                return f.f(P7.this._1());
            }

            @Override // fj.P7
            public B _2() {
                return (B) P7.this._2();
            }

            @Override // fj.P7
            public C _3() {
                return (C) P7.this._3();
            }

            @Override // fj.P7
            public D _4() {
                return (D) P7.this._4();
            }

            @Override // fj.P7
            public E _5() {
                return (E) P7.this._5();
            }

            @Override // fj.P7
            public F _6() {
                return (F) P7.this._6();
            }

            @Override // fj.P7
            public G _7() {
                return (G) P7.this._7();
            }
        };
    }

    public final <X> P7<A, X, C, D, E, F, G> map2(final F<B, X> f) {
        return new P7<A, X, C, D, E, F, G>() { // from class: fj.P7.2
            {
                P7.this = this;
            }

            @Override // fj.P7
            public A _1() {
                return (A) P7.this._1();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P7
            public X _2() {
                return f.f(P7.this._2());
            }

            @Override // fj.P7
            public C _3() {
                return (C) P7.this._3();
            }

            @Override // fj.P7
            public D _4() {
                return (D) P7.this._4();
            }

            @Override // fj.P7
            public E _5() {
                return (E) P7.this._5();
            }

            @Override // fj.P7
            public F _6() {
                return (F) P7.this._6();
            }

            @Override // fj.P7
            public G _7() {
                return (G) P7.this._7();
            }
        };
    }

    public final <X> P7<A, B, X, D, E, F, G> map3(final F<C, X> f) {
        return new P7<A, B, X, D, E, F, G>() { // from class: fj.P7.3
            {
                P7.this = this;
            }

            @Override // fj.P7
            public A _1() {
                return (A) P7.this._1();
            }

            @Override // fj.P7
            public B _2() {
                return (B) P7.this._2();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P7
            public X _3() {
                return f.f(P7.this._3());
            }

            @Override // fj.P7
            public D _4() {
                return (D) P7.this._4();
            }

            @Override // fj.P7
            public E _5() {
                return (E) P7.this._5();
            }

            @Override // fj.P7
            public F _6() {
                return (F) P7.this._6();
            }

            @Override // fj.P7
            public G _7() {
                return (G) P7.this._7();
            }
        };
    }

    public final <X> P7<A, B, C, X, E, F, G> map4(final F<D, X> f) {
        return new P7<A, B, C, X, E, F, G>() { // from class: fj.P7.4
            {
                P7.this = this;
            }

            @Override // fj.P7
            public A _1() {
                return (A) P7.this._1();
            }

            @Override // fj.P7
            public B _2() {
                return (B) P7.this._2();
            }

            @Override // fj.P7
            public C _3() {
                return (C) P7.this._3();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P7
            public X _4() {
                return f.f(P7.this._4());
            }

            @Override // fj.P7
            public E _5() {
                return (E) P7.this._5();
            }

            @Override // fj.P7
            public F _6() {
                return (F) P7.this._6();
            }

            @Override // fj.P7
            public G _7() {
                return (G) P7.this._7();
            }
        };
    }

    public final <X> P7<A, B, C, D, X, F, G> map5(final F<E, X> f) {
        return new P7<A, B, C, D, X, F, G>() { // from class: fj.P7.5
            {
                P7.this = this;
            }

            @Override // fj.P7
            public A _1() {
                return (A) P7.this._1();
            }

            @Override // fj.P7
            public B _2() {
                return (B) P7.this._2();
            }

            @Override // fj.P7
            public C _3() {
                return (C) P7.this._3();
            }

            @Override // fj.P7
            public D _4() {
                return (D) P7.this._4();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P7
            public X _5() {
                return f.f(P7.this._5());
            }

            @Override // fj.P7
            public F _6() {
                return (F) P7.this._6();
            }

            @Override // fj.P7
            public G _7() {
                return (G) P7.this._7();
            }
        };
    }

    public final <X> P7<A, B, C, D, E, X, G> map6(final F<F, X> f) {
        return new P7<A, B, C, D, E, X, G>() { // from class: fj.P7.6
            {
                P7.this = this;
            }

            @Override // fj.P7
            public A _1() {
                return (A) P7.this._1();
            }

            @Override // fj.P7
            public B _2() {
                return (B) P7.this._2();
            }

            @Override // fj.P7
            public C _3() {
                return (C) P7.this._3();
            }

            @Override // fj.P7
            public D _4() {
                return (D) P7.this._4();
            }

            @Override // fj.P7
            public E _5() {
                return (E) P7.this._5();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P7
            public X _6() {
                return f.f(P7.this._6());
            }

            @Override // fj.P7
            public G _7() {
                return (G) P7.this._7();
            }
        };
    }

    public final <X> P7<A, B, C, D, E, F, X> map7(final F<G, X> f) {
        return new P7<A, B, C, D, E, F, X>() { // from class: fj.P7.7
            {
                P7.this = this;
            }

            @Override // fj.P7
            public A _1() {
                return (A) P7.this._1();
            }

            @Override // fj.P7
            public B _2() {
                return (B) P7.this._2();
            }

            @Override // fj.P7
            public C _3() {
                return (C) P7.this._3();
            }

            @Override // fj.P7
            public D _4() {
                return (D) P7.this._4();
            }

            @Override // fj.P7
            public E _5() {
                return (E) P7.this._5();
            }

            @Override // fj.P7
            public F _6() {
                return (F) P7.this._6();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P7
            public X _7() {
                return f.f(P7.this._7());
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

    /* renamed from: fj.P7$8 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P7$8.class */
    class AnonymousClass8 extends P7<A, B, C, D, E, F, G> {
        private final P1<A> a;
        private final P1<B> b;
        private final P1<C> c;
        private final P1<D> d;
        private final P1<E> e;
        private final P1<F> f;
        private final P1<G> g;
        final /* synthetic */ P7 val$self;

        AnonymousClass8(P7 p7) {
            P7.this = this$0;
            this.val$self = p7;
            this.a = P1.memo(P7$8$$Lambda$1.lambdaFactory$(this.val$self));
            this.b = P1.memo(P7$8$$Lambda$2.lambdaFactory$(this.val$self));
            this.c = P1.memo(P7$8$$Lambda$3.lambdaFactory$(this.val$self));
            this.d = P1.memo(P7$8$$Lambda$4.lambdaFactory$(this.val$self));
            this.e = P1.memo(P7$8$$Lambda$5.lambdaFactory$(this.val$self));
            this.f = P1.memo(P7$8$$Lambda$6.lambdaFactory$(this.val$self));
            this.g = P1.memo(P7$8$$Lambda$7.lambdaFactory$(this.val$self));
        }

        @Override // fj.P7
        public A _1() {
            return this.a._1();
        }

        @Override // fj.P7
        public B _2() {
            return this.b._1();
        }

        @Override // fj.P7
        public C _3() {
            return this.c._1();
        }

        @Override // fj.P7
        public D _4() {
            return this.d._1();
        }

        @Override // fj.P7
        public E _5() {
            return this.e._1();
        }

        @Override // fj.P7
        public F _6() {
            return this.f._1();
        }

        @Override // fj.P7
        public G _7() {
            return this.g._1();
        }
    }

    public final P7<A, B, C, D, E, F, G> memo() {
        return new AnonymousClass8(this);
    }

    public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, A> __1() {
        return new F<P7<A, B, C, D, E, F$, G>, A>() { // from class: fj.P7.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P7<Object, B, C, D, E, F$, G>) obj);
            }

            public A f(P7<A, B, C, D, E, F$, G> p) {
                return p._1();
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, B> __2() {
        return new F<P7<A, B, C, D, E, F$, G>, B>() { // from class: fj.P7.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P7<A, Object, C, D, E, F$, G>) obj);
            }

            public B f(P7<A, B, C, D, E, F$, G> p) {
                return p._2();
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, C> __3() {
        return new F<P7<A, B, C, D, E, F$, G>, C>() { // from class: fj.P7.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P7<A, B, Object, D, E, F$, G>) obj);
            }

            public C f(P7<A, B, C, D, E, F$, G> p) {
                return p._3();
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, D> __4() {
        return new F<P7<A, B, C, D, E, F$, G>, D>() { // from class: fj.P7.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P7<A, B, C, Object, E, F$, G>) obj);
            }

            public D f(P7<A, B, C, D, E, F$, G> p) {
                return p._4();
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, E> __5() {
        return new F<P7<A, B, C, D, E, F$, G>, E>() { // from class: fj.P7.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P7<A, B, C, D, Object, F$, G>) obj);
            }

            public E f(P7<A, B, C, D, E, F$, G> p) {
                return p._5();
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, F$> __6() {
        return new F<P7<A, B, C, D, E, F$, G>, F$>() { // from class: fj.P7.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P7<A, B, C, D, E, Object, G>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, F$] */
            public F$ f(P7<A, B, C, D, E, F$, G> p) {
                return p._6();
            }
        };
    }

    public static <A, B, C, D, E, F$, G> F<P7<A, B, C, D, E, F$, G>, G> __7() {
        return new F<P7<A, B, C, D, E, F$, G>, G>() { // from class: fj.P7.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P7<A, B, C, D, E, F$, Object>) obj);
            }

            public G f(P7<A, B, C, D, E, F$, G> p) {
                return p._7();
            }
        };
    }

    public String toString() {
        return Show.p7Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
