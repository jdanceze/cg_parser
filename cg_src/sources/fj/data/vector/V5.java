package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P4;
import fj.P5;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/vector/V5.class */
public final class V5<A> implements Iterable<A> {
    private final V4<A> tail;
    private final P1<A> head;

    private V5(P1<A> head, V4<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> V5<A> p(final P5<A, A, A, A, A> p) {
        return new V5<>(new P1<A>() { // from class: fj.data.vector.V5.1
            @Override // fj.P1
            public A _1() {
                return (A) P5.this._1();
            }
        }, V4.p(new P4<A, A, A, A>() { // from class: fj.data.vector.V5.2
            @Override // fj.P4
            public A _1() {
                return (A) P5.this._2();
            }

            @Override // fj.P4
            public A _2() {
                return (A) P5.this._3();
            }

            @Override // fj.P4
            public A _3() {
                return (A) P5.this._4();
            }

            @Override // fj.P4
            public A _4() {
                return (A) P5.this._5();
            }
        }));
    }

    public static <A> V5<A> cons(P1<A> head, V4<A> tail) {
        return new V5<>(head, tail);
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

    public A _4() {
        return this.tail._3();
    }

    public A _5() {
        return this.tail._4();
    }

    public V4<A> tail() {
        return this.tail;
    }

    public P1<A> head() {
        return this.head;
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toStream().iterator();
    }

    public P5<A, A, A, A, A> p() {
        return new P5<A, A, A, A, A>() { // from class: fj.data.vector.V5.3
            @Override // fj.P5
            public A _1() {
                return (A) V5.this._1();
            }

            @Override // fj.P5
            public A _2() {
                return (A) V5.this._2();
            }

            @Override // fj.P5
            public A _3() {
                return (A) V5.this._3();
            }

            @Override // fj.P5
            public A _4() {
                return (A) V5.this._4();
            }

            @Override // fj.P5
            public A _5() {
                return (A) V5.this._5();
            }
        };
    }

    public NonEmptyList<A> toNonEmptyList() {
        return NonEmptyList.nel(_1(), this.tail.toNonEmptyList().toList());
    }

    public Stream<A> toStream() {
        return Stream.cons(this.head._1(), new P1<Stream<A>>() { // from class: fj.data.vector.V5.4
            @Override // fj.P1
            public Stream<A> _1() {
                return V5.this.tail.toStream();
            }
        });
    }

    public Array<A> toArray() {
        return Array.array(_1(), _2(), _3(), _4(), _5());
    }

    public <B> V5<B> map(F<A, B> f) {
        return new V5<>(this.head.map(f), this.tail.map(f));
    }

    public <B> V5<B> apply(V5<F<A, B>> vf) {
        return new V5<>(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> V5<C> zipWith(F<A, F<B, C>> f, V5<B> bs) {
        return (V5<B>) bs.apply(map(f));
    }

    public <B> V5<P2<A, B>> zip(V5<B> bs) {
        return (V5<P2<A, B>>) zipWith(P.p2(), bs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V5<V2<A>> vzip(V5<A> bs) {
        F2<A, A, V2<A>> __2 = V.v2();
        return (V5<V2<A>>) zipWith(Function.curry(__2), bs);
    }

    public static <A> F<V5<A>, Stream<A>> toStream_() {
        return new F<V5<A>, Stream<A>>() { // from class: fj.data.vector.V5.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V5) ((V5) obj));
            }

            public Stream<A> f(V5<A> v) {
                return v.toStream();
            }
        };
    }

    public static <A> F<V5<A>, P5<A, A, A, A, A>> p_() {
        return new F<V5<A>, P5<A, A, A, A, A>>() { // from class: fj.data.vector.V5.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V5) ((V5) obj));
            }

            public P5<A, A, A, A, A> f(V5<A> v) {
                return v.p();
            }
        };
    }

    public static <A> F<V5<A>, A> __1() {
        return new F<V5<A>, A>() { // from class: fj.data.vector.V5.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V5<Object>) obj);
            }

            public A f(V5<A> v) {
                return v._1();
            }
        };
    }

    public static <A> F<V5<A>, A> __2() {
        return new F<V5<A>, A>() { // from class: fj.data.vector.V5.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V5<Object>) obj);
            }

            public A f(V5<A> v) {
                return v._2();
            }
        };
    }

    public static <A> F<V5<A>, A> __3() {
        return new F<V5<A>, A>() { // from class: fj.data.vector.V5.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V5<Object>) obj);
            }

            public A f(V5<A> v) {
                return v._3();
            }
        };
    }

    public static <A> F<V5<A>, A> __4() {
        return new F<V5<A>, A>() { // from class: fj.data.vector.V5.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V5<Object>) obj);
            }

            public A f(V5<A> v) {
                return v._4();
            }
        };
    }

    public static <A> F<V5<A>, A> __5() {
        return new F<V5<A>, A>() { // from class: fj.data.vector.V5.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V5<Object>) obj);
            }

            public A f(V5<A> v) {
                return v._5();
            }
        };
    }
}
