package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P4.class */
public abstract class P4<A, B, C, D> {
    public abstract A _1();

    public abstract B _2();

    public abstract C _3();

    public abstract D _4();

    public final <X> P4<X, B, C, D> map1(final F<A, X> f) {
        return new P4<X, B, C, D>() { // from class: fj.P4.1
            {
                P4.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P4
            public X _1() {
                return f.f(P4.this._1());
            }

            @Override // fj.P4
            public B _2() {
                return (B) P4.this._2();
            }

            @Override // fj.P4
            public C _3() {
                return (C) P4.this._3();
            }

            @Override // fj.P4
            public D _4() {
                return (D) P4.this._4();
            }
        };
    }

    public final <X> P4<A, X, C, D> map2(final F<B, X> f) {
        return new P4<A, X, C, D>() { // from class: fj.P4.2
            {
                P4.this = this;
            }

            @Override // fj.P4
            public A _1() {
                return (A) P4.this._1();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P4
            public X _2() {
                return f.f(P4.this._2());
            }

            @Override // fj.P4
            public C _3() {
                return (C) P4.this._3();
            }

            @Override // fj.P4
            public D _4() {
                return (D) P4.this._4();
            }
        };
    }

    public final <X> P4<A, B, X, D> map3(final F<C, X> f) {
        return new P4<A, B, X, D>() { // from class: fj.P4.3
            {
                P4.this = this;
            }

            @Override // fj.P4
            public A _1() {
                return (A) P4.this._1();
            }

            @Override // fj.P4
            public B _2() {
                return (B) P4.this._2();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P4
            public X _3() {
                return f.f(P4.this._3());
            }

            @Override // fj.P4
            public D _4() {
                return (D) P4.this._4();
            }
        };
    }

    public final <X> P4<A, B, C, X> map4(final F<D, X> f) {
        return new P4<A, B, C, X>() { // from class: fj.P4.4
            {
                P4.this = this;
            }

            @Override // fj.P4
            public A _1() {
                return (A) P4.this._1();
            }

            @Override // fj.P4
            public B _2() {
                return (B) P4.this._2();
            }

            @Override // fj.P4
            public C _3() {
                return (C) P4.this._3();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P4
            public X _4() {
                return f.f(P4.this._4());
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

    /* renamed from: fj.P4$5 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P4$5.class */
    class AnonymousClass5 extends P4<A, B, C, D> {
        private final P1<A> a;
        private final P1<B> b;
        private final P1<C> c;
        private final P1<D> d;
        final /* synthetic */ P4 val$self;

        AnonymousClass5(P4 p4) {
            P4.this = this$0;
            this.val$self = p4;
            this.a = P1.memo(P4$5$$Lambda$1.lambdaFactory$(this.val$self));
            this.b = P1.memo(P4$5$$Lambda$2.lambdaFactory$(this.val$self));
            this.c = P1.memo(P4$5$$Lambda$3.lambdaFactory$(this.val$self));
            this.d = P1.memo(P4$5$$Lambda$4.lambdaFactory$(this.val$self));
        }

        public static /* synthetic */ Object lambda$$6(P4 p4, Unit u) {
            return p4._2();
        }

        public static /* synthetic */ Object lambda$$7(P4 p4, Unit u) {
            return p4._3();
        }

        @Override // fj.P4
        public A _1() {
            return this.a._1();
        }

        @Override // fj.P4
        public B _2() {
            return this.b._1();
        }

        @Override // fj.P4
        public C _3() {
            return this.c._1();
        }

        @Override // fj.P4
        public D _4() {
            return this.d._1();
        }
    }

    public final P4<A, B, C, D> memo() {
        return new AnonymousClass5(this);
    }

    public static <A, B, C, D> F<P4<A, B, C, D>, A> __1() {
        return new F<P4<A, B, C, D>, A>() { // from class: fj.P4.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P4<Object, B, C, D>) obj);
            }

            public A f(P4<A, B, C, D> p) {
                return p._1();
            }
        };
    }

    public static <A, B, C, D> F<P4<A, B, C, D>, B> __2() {
        return new F<P4<A, B, C, D>, B>() { // from class: fj.P4.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P4<A, Object, C, D>) obj);
            }

            public B f(P4<A, B, C, D> p) {
                return p._2();
            }
        };
    }

    public static <A, B, C, D> F<P4<A, B, C, D>, C> __3() {
        return new F<P4<A, B, C, D>, C>() { // from class: fj.P4.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P4<A, B, Object, D>) obj);
            }

            public C f(P4<A, B, C, D> p) {
                return p._3();
            }
        };
    }

    public static <A, B, C, D> F<P4<A, B, C, D>, D> __4() {
        return new F<P4<A, B, C, D>, D>() { // from class: fj.P4.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P4<A, B, C, Object>) obj);
            }

            public D f(P4<A, B, C, D> p) {
                return p._4();
            }
        };
    }

    public String toString() {
        return Show.p4Show(Show.anyShow(), Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
