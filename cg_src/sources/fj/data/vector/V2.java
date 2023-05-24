package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.data.Array;
import fj.data.List;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/vector/V2.class */
public final class V2<A> implements Iterable<A> {
    private final P2<A, A> inner;

    private V2(P2<A, A> inner) {
        this.inner = inner;
    }

    public static <A> V2<A> p(P2<A, A> p) {
        return new V2<>(p);
    }

    public A _1() {
        return this.inner._1();
    }

    public A _2() {
        return this.inner._2();
    }

    public static <A> F<V2<A>, A> __1() {
        return new F<V2<A>, A>() { // from class: fj.data.vector.V2.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V2<Object>) obj);
            }

            public A f(V2<A> v) {
                return v._1();
            }
        };
    }

    public static <A> F<V2<A>, A> __2() {
        return new F<V2<A>, A>() { // from class: fj.data.vector.V2.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V2<Object>) obj);
            }

            public A f(V2<A> v) {
                return v._2();
            }
        };
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toStream().iterator();
    }

    public P2<A, A> p() {
        return this.inner;
    }

    public NonEmptyList<A> toNonEmptyList() {
        return NonEmptyList.nel(_1(), List.single(_2()));
    }

    public Stream<A> toStream() {
        return Stream.cons(_1(), new P1<Stream<A>>() { // from class: fj.data.vector.V2.3
            @Override // fj.P1
            public Stream<A> _1() {
                return Stream.single(V2.this._2());
            }
        });
    }

    public static <A> F<V2<A>, Stream<A>> toStream_() {
        return new F<V2<A>, Stream<A>>() { // from class: fj.data.vector.V2.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V2) ((V2) obj));
            }

            public Stream<A> f(V2<A> v) {
                return v.toStream();
            }
        };
    }

    public static <A> F<V2<A>, P2<A, A>> p_() {
        return new F<V2<A>, P2<A, A>>() { // from class: fj.data.vector.V2.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V2) ((V2) obj));
            }

            public P2<A, A> f(V2<A> v) {
                return v.p();
            }
        };
    }

    public Array<A> toArray() {
        return Array.array(_1(), _2());
    }

    public <B> V2<B> map(F<A, B> f) {
        return p(this.inner.split(f, f));
    }

    public <B> V2<B> apply(V2<F<A, B>> vf) {
        return p(this.inner.split(vf._1(), vf._2()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> V2<C> zipWith(F<A, F<B, C>> f, V2<B> bs) {
        return (V2<B>) bs.apply(map(f));
    }

    public <B> V2<P2<A, B>> zip(V2<B> bs) {
        return (V2<P2<A, B>>) zipWith(P.p2(), bs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V2<V2<A>> vzip(V2<A> bs) {
        F2<A, A, V2<A>> __2 = V.v2();
        return (V2<V2<A>>) zipWith(Function.curry(__2), bs);
    }

    public P1<A> head() {
        return new P1<A>() { // from class: fj.data.vector.V2.6
            @Override // fj.P1
            public A _1() {
                return (A) V2.this._1();
            }
        };
    }
}
