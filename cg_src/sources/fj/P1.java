package fj;

import fj.data.Array;
import fj.data.List;
import fj.data.Stream;
import java.lang.ref.SoftReference;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/P1.class */
public abstract class P1<A> {
    public abstract A _1();

    public static <A> F<P1<A>, A> __1() {
        return new F<P1<A>, A>() { // from class: fj.P1.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1<Object>) obj);
            }

            public A f(P1<A> p) {
                return p._1();
            }
        };
    }

    public static <A, B> F<P1<A>, P1<B>> fmap(final F<A, B> f) {
        return new F<P1<A>, P1<B>>() { // from class: fj.P1.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public P1<B> f(P1<A> a) {
                return a.map(F.this);
            }
        };
    }

    public <B> P1<B> bind(final F<A, P1<B>> f) {
        return new P1<B>() { // from class: fj.P1.3
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v4, types: [B, java.lang.Object] */
            @Override // fj.P1
            public B _1() {
                return ((P1) f.f(this._1()))._1();
            }
        };
    }

    public static <A, B> F<A, P1<B>> curry(final F<A, B> f) {
        return new F<A, P1<B>>() { // from class: fj.P1.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass4) obj);
            }

            @Override // fj.F
            public P1<B> f(final A a) {
                return new P1<B>() { // from class: fj.P1.4.1
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
                    @Override // fj.P1
                    public B _1() {
                        return F.this.f(a);
                    }
                };
            }
        };
    }

    public <B> P1<B> apply(P1<F<A, B>> cf) {
        return cf.bind(new F<F<A, B>, P1<B>>() { // from class: fj.P1.5
            @Override // fj.F
            public P1<B> f(F<A, B> f) {
                return (P1) P1.fmap(f).f(this);
            }
        });
    }

    public <B, C> P1<C> bind(P1<B> cb, F<A, F<B, C>> f) {
        return (P1<B>) cb.apply((P1) fmap(f).f(this));
    }

    public static <A> P1<A> join(P1<P1<A>> a) {
        return (P1<A>) a.bind(Function.identity());
    }

    public static <A, B, C> F<P1<A>, F<P1<B>, P1<C>>> liftM2(final F<A, F<B, C>> f) {
        return Function.curry(new F2<P1<A>, P1<B>, P1<C>>() { // from class: fj.P1.6
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((P1) ((P1) obj), (P1) obj2);
            }

            public P1<C> f(P1<A> pa, P1<B> pb) {
                return pa.bind(pb, F.this);
            }
        });
    }

    public static <A> P1<List<A>> sequence(List<P1<A>> as) {
        return (P1) as.foldRight((F<P1<A>, F<F<P1<A>, F<B, B>>, F<P1<A>, F<B, B>>>>) liftM2(List.cons()), (F<P1<A>, F<B, B>>) P.p(List.nil()));
    }

    public static <A> F<List<P1<A>>, P1<List<A>>> sequenceList() {
        return new F<List<P1<A>>, P1<List<A>>>() { // from class: fj.P1.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public P1<List<A>> f(List<P1<A>> as) {
                return P1.sequence(as);
            }
        };
    }

    public static <A> P1<Stream<A>> sequence(Stream<P1<A>> as) {
        return (P1) as.foldRight((F<P1<A>, F<P1<F<P1<A>, F<P1<B>, B>>>, F<P1<A>, F<P1<B>, B>>>>) liftM2(Stream.cons()), (F<P1<A>, F<P1<B>, B>>) P.p(Stream.nil()));
    }

    public static <A> P1<Array<A>> sequence(final Array<P1<A>> as) {
        return new P1<Array<A>>() { // from class: fj.P1.8
            @Override // fj.P1
            public Array<A> _1() {
                return Array.this.map(P1.__1());
            }
        };
    }

    public <X> P1<X> map(final F<A, X> f) {
        return new P1<X>() { // from class: fj.P1.9
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [X, java.lang.Object] */
            @Override // fj.P1
            public X _1() {
                return f.f(this._1());
            }
        };
    }

    public P1<A> memo() {
        return new P1<A>() { // from class: fj.P1.10
            private final Object latch = new Object();
            private volatile SoftReference<A> v;

            @Override // fj.P1
            public A _1() {
                Object obj = this.v != null ? this.v.get() : null;
                if (obj == null) {
                    synchronized (this.latch) {
                        if (this.v == null || this.v.get() == null) {
                            obj = this._1();
                        }
                        this.v = new SoftReference<>(obj);
                    }
                }
                return (A) obj;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <A> P1<A> memo(F<Unit, A> f) {
        return P.lazy(f).memo();
    }

    public <B> F<B, A> constant() {
        return new F<B, A>() { // from class: fj.P1.11
            @Override // fj.F
            public A f(B b) {
                return (A) P1.this._1();
            }
        };
    }

    public String toString() {
        return Show.p1Show(Show.anyShow()).showS((Show) this);
    }
}
