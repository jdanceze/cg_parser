package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/vector/V3.class */
public final class V3<A> implements Iterable<A> {
    private final V2<A> tail;
    private final P1<A> head;

    private V3(P1<A> head, V2<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> V3<A> p(final P3<A, A, A> p) {
        return new V3<>(new P1<A>() { // from class: fj.data.vector.V3.1
            @Override // fj.P1
            public A _1() {
                return (A) P3.this._1();
            }
        }, V2.p(new P2<A, A>() { // from class: fj.data.vector.V3.2
            @Override // fj.P2
            public A _1() {
                return (A) P3.this._2();
            }

            @Override // fj.P2
            public A _2() {
                return (A) P3.this._3();
            }
        }));
    }

    public static <A> V3<A> cons(P1<A> head, V2<A> tail) {
        return new V3<>(head, tail);
    }

    public A _1() {
        return this.head._1();
    }

    public A _2() {
        return this.tail._1();
    }

    public A _3() {
        return this.tail._2();
    }

    public V2<A> tail() {
        return this.tail;
    }

    public P1<A> head() {
        return this.head;
    }

    public P3<A, A, A> p() {
        return new P3<A, A, A>() { // from class: fj.data.vector.V3.3
            @Override // fj.P3
            public A _1() {
                return (A) V3.this._1();
            }

            @Override // fj.P3
            public A _2() {
                return (A) V3.this._2();
            }

            @Override // fj.P3
            public A _3() {
                return (A) V3.this._3();
            }
        };
    }

    public Array<A> toArray() {
        return Array.array(_1(), _2(), _3());
    }

    public <B> V3<B> apply(V3<F<A, B>> vf) {
        return new V3<>(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> V3<C> zipWith(F<A, F<B, C>> f, V3<B> bs) {
        return (V3<B>) bs.apply(map(f));
    }

    public <B> V3<P2<A, B>> zip(V3<B> bs) {
        return (V3<P2<A, B>>) zipWith(P.p2(), bs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V3<V2<A>> vzip(V3<A> bs) {
        F2<A, A, V2<A>> __2 = V.v2();
        return (V3<V2<A>>) zipWith(Function.curry(__2), bs);
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toStream().iterator();
    }

    public NonEmptyList<A> toNonEmptyList() {
        return NonEmptyList.nel(head()._1(), tail().toNonEmptyList().toList());
    }

    public Stream<A> toStream() {
        return Stream.cons(head()._1(), new P1<Stream<A>>() { // from class: fj.data.vector.V3.4
            @Override // fj.P1
            public Stream<A> _1() {
                return V3.this.tail().toStream();
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B> V3<B> map(F<A, B> f) {
        return new V3<>(head().map(f), tail().map(f));
    }

    public static <A> F<V3<A>, Stream<A>> toStream_() {
        return new F<V3<A>, Stream<A>>() { // from class: fj.data.vector.V3.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V3) ((V3) obj));
            }

            public Stream<A> f(V3<A> v) {
                return v.toStream();
            }
        };
    }

    public static <A> F<V3<A>, P3<A, A, A>> p_() {
        return new F<V3<A>, P3<A, A, A>>() { // from class: fj.data.vector.V3.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V3) ((V3) obj));
            }

            public P3<A, A, A> f(V3<A> v) {
                return v.p();
            }
        };
    }

    public static <A> F<V3<A>, A> __1() {
        return new F<V3<A>, A>() { // from class: fj.data.vector.V3.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V3<Object>) obj);
            }

            public A f(V3<A> v) {
                return v._1();
            }
        };
    }

    public static <A> F<V3<A>, A> __2() {
        return new F<V3<A>, A>() { // from class: fj.data.vector.V3.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V3<Object>) obj);
            }

            public A f(V3<A> v) {
                return v._2();
            }
        };
    }

    public static <A> F<V3<A>, A> __3() {
        return new F<V3<A>, A>() { // from class: fj.data.vector.V3.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V3<Object>) obj);
            }

            public A f(V3<A> v) {
                return v._3();
            }
        };
    }
}
