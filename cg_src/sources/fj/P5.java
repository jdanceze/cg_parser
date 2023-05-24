package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P5.class */
public abstract class P5<A, B, C, D, E> {
    public abstract A _1();

    public abstract B _2();

    public abstract C _3();

    public abstract D _4();

    public abstract E _5();

    public final <X> P5<X, B, C, D, E> map1(final F<A, X> f) {
        return new P5<X, B, C, D, E>() { // from class: fj.P5.1
            {
                P5.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P5
            public X _1() {
                return f.f(P5.this._1());
            }

            @Override // fj.P5
            public B _2() {
                return (B) P5.this._2();
            }

            @Override // fj.P5
            public C _3() {
                return (C) P5.this._3();
            }

            @Override // fj.P5
            public D _4() {
                return (D) P5.this._4();
            }

            @Override // fj.P5
            public E _5() {
                return (E) P5.this._5();
            }
        };
    }

    public final <X> P5<A, X, C, D, E> map2(final F<B, X> f) {
        return new P5<A, X, C, D, E>() { // from class: fj.P5.2
            {
                P5.this = this;
            }

            @Override // fj.P5
            public A _1() {
                return (A) P5.this._1();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P5
            public X _2() {
                return f.f(P5.this._2());
            }

            @Override // fj.P5
            public C _3() {
                return (C) P5.this._3();
            }

            @Override // fj.P5
            public D _4() {
                return (D) P5.this._4();
            }

            @Override // fj.P5
            public E _5() {
                return (E) P5.this._5();
            }
        };
    }

    public final <X> P5<A, B, X, D, E> map3(final F<C, X> f) {
        return new P5<A, B, X, D, E>() { // from class: fj.P5.3
            {
                P5.this = this;
            }

            @Override // fj.P5
            public A _1() {
                return (A) P5.this._1();
            }

            @Override // fj.P5
            public B _2() {
                return (B) P5.this._2();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P5
            public X _3() {
                return f.f(P5.this._3());
            }

            @Override // fj.P5
            public D _4() {
                return (D) P5.this._4();
            }

            @Override // fj.P5
            public E _5() {
                return (E) P5.this._5();
            }
        };
    }

    public final <X> P5<A, B, C, X, E> map4(final F<D, X> f) {
        return new P5<A, B, C, X, E>() { // from class: fj.P5.4
            {
                P5.this = this;
            }

            @Override // fj.P5
            public A _1() {
                return (A) P5.this._1();
            }

            @Override // fj.P5
            public B _2() {
                return (B) P5.this._2();
            }

            @Override // fj.P5
            public C _3() {
                return (C) P5.this._3();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P5
            public X _4() {
                return f.f(P5.this._4());
            }

            @Override // fj.P5
            public E _5() {
                return (E) P5.this._5();
            }
        };
    }

    public final <X> P5<A, B, C, D, X> map5(final F<E, X> f) {
        return new P5<A, B, C, D, X>() { // from class: fj.P5.5
            {
                P5.this = this;
            }

            @Override // fj.P5
            public A _1() {
                return (A) P5.this._1();
            }

            @Override // fj.P5
            public B _2() {
                return (B) P5.this._2();
            }

            @Override // fj.P5
            public C _3() {
                return (C) P5.this._3();
            }

            @Override // fj.P5
            public D _4() {
                return (D) P5.this._4();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P5
            public X _5() {
                return f.f(P5.this._5());
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

    /* renamed from: fj.P5$6 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P5$6.class */
    class AnonymousClass6 extends P5<A, B, C, D, E> {
        private final P1<A> a;
        private final P1<B> b;
        private final P1<C> c;
        private final P1<D> d;
        private final P1<E> e;
        final /* synthetic */ P5 val$self;

        AnonymousClass6(P5 p5) {
            P5.this = this$0;
            this.val$self = p5;
            this.a = P1.memo(P5$6$$Lambda$1.lambdaFactory$(this.val$self));
            this.b = P1.memo(P5$6$$Lambda$2.lambdaFactory$(this.val$self));
            this.c = P1.memo(P5$6$$Lambda$3.lambdaFactory$(this.val$self));
            this.d = P1.memo(P5$6$$Lambda$4.lambdaFactory$(this.val$self));
            this.e = P1.memo(P5$6$$Lambda$5.lambdaFactory$(this.val$self));
        }

        @Override // fj.P5
        public A _1() {
            return this.a._1();
        }

        @Override // fj.P5
        public B _2() {
            return this.b._1();
        }

        @Override // fj.P5
        public C _3() {
            return this.c._1();
        }

        @Override // fj.P5
        public D _4() {
            return this.d._1();
        }

        @Override // fj.P5
        public E _5() {
            return this.e._1();
        }
    }

    public final P5<A, B, C, D, E> memo() {
        return new AnonymousClass6(this);
    }

    public static <A, B, C, D, E> F<P5<A, B, C, D, E>, A> __1() {
        return new F<P5<A, B, C, D, E>, A>() { // from class: fj.P5.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P5<Object, B, C, D, E>) obj);
            }

            public A f(P5<A, B, C, D, E> p) {
                return p._1();
            }
        };
    }

    public static <A, B, C, D, E> F<P5<A, B, C, D, E>, B> __2() {
        return new F<P5<A, B, C, D, E>, B>() { // from class: fj.P5.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P5<A, Object, C, D, E>) obj);
            }

            public B f(P5<A, B, C, D, E> p) {
                return p._2();
            }
        };
    }

    public static <A, B, C, D, E> F<P5<A, B, C, D, E>, C> __3() {
        return new F<P5<A, B, C, D, E>, C>() { // from class: fj.P5.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P5<A, B, Object, D, E>) obj);
            }

            public C f(P5<A, B, C, D, E> p) {
                return p._3();
            }
        };
    }

    public static <A, B, C, D, E> F<P5<A, B, C, D, E>, D> __4() {
        return new F<P5<A, B, C, D, E>, D>() { // from class: fj.P5.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P5<A, B, C, Object, E>) obj);
            }

            public D f(P5<A, B, C, D, E> p) {
                return p._4();
            }
        };
    }

    public static <A, B, C, D, E> F<P5<A, B, C, D, E>, E> __5() {
        return new F<P5<A, B, C, D, E>, E>() { // from class: fj.P5.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P5<A, B, C, D, Object>) obj);
            }

            public E f(P5<A, B, C, D, E> p) {
                return p._5();
            }
        };
    }

    public String toString() {
        return Show.p5Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
