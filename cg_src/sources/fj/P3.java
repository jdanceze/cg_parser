package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P3.class */
public abstract class P3<A, B, C> {
    public abstract A _1();

    public abstract B _2();

    public abstract C _3();

    public final <X> P3<X, B, C> map1(final F<A, X> f) {
        return new P3<X, B, C>() { // from class: fj.P3.1
            {
                P3.this = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P3
            public X _1() {
                return f.f(P3.this._1());
            }

            @Override // fj.P3
            public B _2() {
                return (B) P3.this._2();
            }

            @Override // fj.P3
            public C _3() {
                return (C) P3.this._3();
            }
        };
    }

    public final <X> P3<A, X, C> map2(final F<B, X> f) {
        return new P3<A, X, C>() { // from class: fj.P3.2
            {
                P3.this = this;
            }

            @Override // fj.P3
            public A _1() {
                return (A) P3.this._1();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P3
            public X _2() {
                return f.f(P3.this._2());
            }

            @Override // fj.P3
            public C _3() {
                return (C) P3.this._3();
            }
        };
    }

    public final <X> P3<A, B, X> map3(final F<C, X> f) {
        return new P3<A, B, X>() { // from class: fj.P3.3
            {
                P3.this = this;
            }

            @Override // fj.P3
            public A _1() {
                return (A) P3.this._1();
            }

            @Override // fj.P3
            public B _2() {
                return (B) P3.this._2();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P3
            public X _3() {
                return f.f(P3.this._3());
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

    /* renamed from: fj.P3$4 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P3$4.class */
    class AnonymousClass4 extends P3<A, B, C> {
        private final P1<A> a;
        private final P1<B> b;
        private final P1<C> c;
        final /* synthetic */ P3 val$self;

        AnonymousClass4(P3 p3) {
            P3.this = this$0;
            this.val$self = p3;
            this.a = P1.memo(P3$4$$Lambda$1.lambdaFactory$(this.val$self));
            this.b = P1.memo(P3$4$$Lambda$2.lambdaFactory$(this.val$self));
            this.c = P1.memo(P3$4$$Lambda$3.lambdaFactory$(this.val$self));
        }

        @Override // fj.P3
        public A _1() {
            return this.a._1();
        }

        @Override // fj.P3
        public B _2() {
            return this.b._1();
        }

        @Override // fj.P3
        public C _3() {
            return this.c._1();
        }
    }

    public final P3<A, B, C> memo() {
        return new AnonymousClass4(this);
    }

    public static <A, B, C> F<P3<A, B, C>, A> __1() {
        return new F<P3<A, B, C>, A>() { // from class: fj.P3.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P3<Object, B, C>) obj);
            }

            public A f(P3<A, B, C> p) {
                return p._1();
            }
        };
    }

    public static <A, B, C> F<P3<A, B, C>, B> __2() {
        return new F<P3<A, B, C>, B>() { // from class: fj.P3.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P3<A, Object, C>) obj);
            }

            public B f(P3<A, B, C> p) {
                return p._2();
            }
        };
    }

    public static <A, B, C> F<P3<A, B, C>, C> __3() {
        return new F<P3<A, B, C>, C>() { // from class: fj.P3.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P3<A, B, Object>) obj);
            }

            public C f(P3<A, B, C> p) {
                return p._3();
            }
        };
    }

    public String toString() {
        return Show.p3Show(Show.anyShow(), Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
