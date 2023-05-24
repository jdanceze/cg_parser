package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P6.class */
public abstract class P6<A, B, C, D, E, F> {
    public abstract A _1();

    public abstract B _2();

    public abstract C _3();

    public abstract D _4();

    public abstract E _5();

    public abstract F _6();

    public final <X> P6<X, B, C, D, E, F> map1(final F<A, X> f) {
        return new P6<X, B, C, D, E, F>() { // from class: fj.P6.1
            {
                P6.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P6
            public X _1() {
                return f.f(P6.this._1());
            }

            @Override // fj.P6
            public B _2() {
                return (B) P6.this._2();
            }

            @Override // fj.P6
            public C _3() {
                return (C) P6.this._3();
            }

            @Override // fj.P6
            public D _4() {
                return (D) P6.this._4();
            }

            @Override // fj.P6
            public E _5() {
                return (E) P6.this._5();
            }

            @Override // fj.P6
            public F _6() {
                return (F) P6.this._6();
            }
        };
    }

    public final <X> P6<A, X, C, D, E, F> map2(final F<B, X> f) {
        return new P6<A, X, C, D, E, F>() { // from class: fj.P6.2
            {
                P6.this = this;
            }

            @Override // fj.P6
            public A _1() {
                return (A) P6.this._1();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P6
            public X _2() {
                return f.f(P6.this._2());
            }

            @Override // fj.P6
            public C _3() {
                return (C) P6.this._3();
            }

            @Override // fj.P6
            public D _4() {
                return (D) P6.this._4();
            }

            @Override // fj.P6
            public E _5() {
                return (E) P6.this._5();
            }

            @Override // fj.P6
            public F _6() {
                return (F) P6.this._6();
            }
        };
    }

    public final <X> P6<A, B, X, D, E, F> map3(final F<C, X> f) {
        return new P6<A, B, X, D, E, F>() { // from class: fj.P6.3
            {
                P6.this = this;
            }

            @Override // fj.P6
            public A _1() {
                return (A) P6.this._1();
            }

            @Override // fj.P6
            public B _2() {
                return (B) P6.this._2();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P6
            public X _3() {
                return f.f(P6.this._3());
            }

            @Override // fj.P6
            public D _4() {
                return (D) P6.this._4();
            }

            @Override // fj.P6
            public E _5() {
                return (E) P6.this._5();
            }

            @Override // fj.P6
            public F _6() {
                return (F) P6.this._6();
            }
        };
    }

    public final <X> P6<A, B, C, X, E, F> map4(final F<D, X> f) {
        return new P6<A, B, C, X, E, F>() { // from class: fj.P6.4
            {
                P6.this = this;
            }

            @Override // fj.P6
            public A _1() {
                return (A) P6.this._1();
            }

            @Override // fj.P6
            public B _2() {
                return (B) P6.this._2();
            }

            @Override // fj.P6
            public C _3() {
                return (C) P6.this._3();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P6
            public X _4() {
                return f.f(P6.this._4());
            }

            @Override // fj.P6
            public E _5() {
                return (E) P6.this._5();
            }

            @Override // fj.P6
            public F _6() {
                return (F) P6.this._6();
            }
        };
    }

    public final <X> P6<A, B, C, D, X, F> map5(final F<E, X> f) {
        return new P6<A, B, C, D, X, F>() { // from class: fj.P6.5
            {
                P6.this = this;
            }

            @Override // fj.P6
            public A _1() {
                return (A) P6.this._1();
            }

            @Override // fj.P6
            public B _2() {
                return (B) P6.this._2();
            }

            @Override // fj.P6
            public C _3() {
                return (C) P6.this._3();
            }

            @Override // fj.P6
            public D _4() {
                return (D) P6.this._4();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P6
            public X _5() {
                return f.f(P6.this._5());
            }

            @Override // fj.P6
            public F _6() {
                return (F) P6.this._6();
            }
        };
    }

    public final <X> P6<A, B, C, D, E, X> map6(final F<F, X> f) {
        return new P6<A, B, C, D, E, X>() { // from class: fj.P6.6
            {
                P6.this = this;
            }

            @Override // fj.P6
            public A _1() {
                return (A) P6.this._1();
            }

            @Override // fj.P6
            public B _2() {
                return (B) P6.this._2();
            }

            @Override // fj.P6
            public C _3() {
                return (C) P6.this._3();
            }

            @Override // fj.P6
            public D _4() {
                return (D) P6.this._4();
            }

            @Override // fj.P6
            public E _5() {
                return (E) P6.this._5();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P6
            public X _6() {
                return f.f(P6.this._6());
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

    /* renamed from: fj.P6$7 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P6$7.class */
    class AnonymousClass7 extends P6<A, B, C, D, E, F> {
        private final P1<A> a;
        private final P1<B> b;
        private final P1<C> c;
        private final P1<D> d;
        private final P1<E> e;
        private final P1<F> f;
        final /* synthetic */ P6 val$self;

        AnonymousClass7(P6 p6) {
            P6.this = this$0;
            this.val$self = p6;
            this.a = P1.memo(P6$7$$Lambda$1.lambdaFactory$(this.val$self));
            this.b = P1.memo(P6$7$$Lambda$2.lambdaFactory$(this.val$self));
            this.c = P1.memo(P6$7$$Lambda$3.lambdaFactory$(this.val$self));
            this.d = P1.memo(P6$7$$Lambda$4.lambdaFactory$(this.val$self));
            this.e = P1.memo(P6$7$$Lambda$5.lambdaFactory$(this.val$self));
            this.f = P1.memo(P6$7$$Lambda$6.lambdaFactory$(this.val$self));
        }

        @Override // fj.P6
        public A _1() {
            return this.a._1();
        }

        @Override // fj.P6
        public B _2() {
            return this.b._1();
        }

        @Override // fj.P6
        public C _3() {
            return this.c._1();
        }

        @Override // fj.P6
        public D _4() {
            return this.d._1();
        }

        @Override // fj.P6
        public E _5() {
            return this.e._1();
        }

        @Override // fj.P6
        public F _6() {
            return this.f._1();
        }
    }

    public final P6<A, B, C, D, E, F> memo() {
        return new AnonymousClass7(this);
    }

    public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, A> __1() {
        return new F<P6<A, B, C, D, E, F$>, A>() { // from class: fj.P6.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P6<Object, B, C, D, E, F$>) obj);
            }

            public A f(P6<A, B, C, D, E, F$> p) {
                return p._1();
            }
        };
    }

    public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, B> __2() {
        return new F<P6<A, B, C, D, E, F$>, B>() { // from class: fj.P6.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P6<A, Object, C, D, E, F$>) obj);
            }

            public B f(P6<A, B, C, D, E, F$> p) {
                return p._2();
            }
        };
    }

    public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, C> __3() {
        return new F<P6<A, B, C, D, E, F$>, C>() { // from class: fj.P6.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P6<A, B, Object, D, E, F$>) obj);
            }

            public C f(P6<A, B, C, D, E, F$> p) {
                return p._3();
            }
        };
    }

    public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, D> __4() {
        return new F<P6<A, B, C, D, E, F$>, D>() { // from class: fj.P6.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P6<A, B, C, Object, E, F$>) obj);
            }

            public D f(P6<A, B, C, D, E, F$> p) {
                return p._4();
            }
        };
    }

    public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, E> __5() {
        return new F<P6<A, B, C, D, E, F$>, E>() { // from class: fj.P6.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P6<A, B, C, D, Object, F$>) obj);
            }

            public E f(P6<A, B, C, D, E, F$> p) {
                return p._5();
            }
        };
    }

    public static <A, B, C, D, E, F$> F<P6<A, B, C, D, E, F$>, F$> __6() {
        return new F<P6<A, B, C, D, E, F$>, F$>() { // from class: fj.P6.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P6<A, B, C, D, E, Object>) obj);
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, F$] */
            public F$ f(P6<A, B, C, D, E, F$> p) {
                return p._6();
            }
        };
    }

    public String toString() {
        return Show.p6Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
