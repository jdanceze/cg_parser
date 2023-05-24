package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P5;
import fj.P6;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/vector/V6.class */
public final class V6<A> implements Iterable<A> {
    private final V5<A> tail;
    private final P1<A> head;

    private V6(P1<A> head, V5<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> V6<A> p(final P6<A, A, A, A, A, A> p) {
        return new V6<>(new P1<A>() { // from class: fj.data.vector.V6.1
            @Override // fj.P1
            public A _1() {
                return (A) P6.this._1();
            }
        }, V5.p(new P5<A, A, A, A, A>() { // from class: fj.data.vector.V6.2
            @Override // fj.P5
            public A _1() {
                return (A) P6.this._2();
            }

            @Override // fj.P5
            public A _2() {
                return (A) P6.this._3();
            }

            @Override // fj.P5
            public A _3() {
                return (A) P6.this._4();
            }

            @Override // fj.P5
            public A _4() {
                return (A) P6.this._5();
            }

            @Override // fj.P5
            public A _5() {
                return (A) P6.this._6();
            }
        }));
    }

    public static <A> V6<A> cons(P1<A> head, V5<A> tail) {
        return new V6<>(head, tail);
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

    public A _6() {
        return this.tail._5();
    }

    public V5<A> tail() {
        return this.tail;
    }

    public P1<A> head() {
        return this.head;
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toStream().iterator();
    }

    public P6<A, A, A, A, A, A> p() {
        return new P6<A, A, A, A, A, A>() { // from class: fj.data.vector.V6.3
            @Override // fj.P6
            public A _1() {
                return (A) V6.this._1();
            }

            @Override // fj.P6
            public A _2() {
                return (A) V6.this._2();
            }

            @Override // fj.P6
            public A _3() {
                return (A) V6.this._3();
            }

            @Override // fj.P6
            public A _4() {
                return (A) V6.this._4();
            }

            @Override // fj.P6
            public A _5() {
                return (A) V6.this._5();
            }

            @Override // fj.P6
            public A _6() {
                return (A) V6.this._6();
            }
        };
    }

    public NonEmptyList<A> toNonEmptyList() {
        return NonEmptyList.nel(_1(), this.tail.toNonEmptyList().toList());
    }

    public Stream<A> toStream() {
        return Stream.cons(this.head._1(), new P1<Stream<A>>() { // from class: fj.data.vector.V6.4
            @Override // fj.P1
            public Stream<A> _1() {
                return V6.this.tail.toStream();
            }
        });
    }

    public Array<A> toArray() {
        return Array.array(_1(), _2(), _3(), _4(), _5(), _6());
    }

    public <B> V6<B> map(F<A, B> f) {
        return new V6<>(this.head.map(f), this.tail.map(f));
    }

    public <B> V6<B> apply(V6<F<A, B>> vf) {
        return new V6<>(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> V6<C> zipWith(F<A, F<B, C>> f, V6<B> bs) {
        return (V6<B>) bs.apply(map(f));
    }

    public <B> V6<P2<A, B>> zip(V6<B> bs) {
        return (V6<P2<A, B>>) zipWith(P.p2(), bs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V6<V2<A>> vzip(V6<A> bs) {
        F2<A, A, V2<A>> __2 = V.v2();
        return (V6<V2<A>>) zipWith(Function.curry(__2), bs);
    }

    public static <A> F<V6<A>, Stream<A>> toStream_() {
        return new F<V6<A>, Stream<A>>() { // from class: fj.data.vector.V6.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6) ((V6) obj));
            }

            public Stream<A> f(V6<A> v) {
                return v.toStream();
            }
        };
    }

    public static <A> F<V6<A>, P6<A, A, A, A, A, A>> p_() {
        return new F<V6<A>, P6<A, A, A, A, A, A>>() { // from class: fj.data.vector.V6.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6) ((V6) obj));
            }

            public P6<A, A, A, A, A, A> f(V6<A> v) {
                return v.p();
            }
        };
    }

    public static <A> F<V6<A>, A> __1() {
        return new F<V6<A>, A>() { // from class: fj.data.vector.V6.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6<Object>) obj);
            }

            public A f(V6<A> v) {
                return v._1();
            }
        };
    }

    public static <A> F<V6<A>, A> __2() {
        return new F<V6<A>, A>() { // from class: fj.data.vector.V6.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6<Object>) obj);
            }

            public A f(V6<A> v) {
                return v._2();
            }
        };
    }

    public static <A> F<V6<A>, A> __3() {
        return new F<V6<A>, A>() { // from class: fj.data.vector.V6.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6<Object>) obj);
            }

            public A f(V6<A> v) {
                return v._3();
            }
        };
    }

    public static <A> F<V6<A>, A> __4() {
        return new F<V6<A>, A>() { // from class: fj.data.vector.V6.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6<Object>) obj);
            }

            public A f(V6<A> v) {
                return v._4();
            }
        };
    }

    public static <A> F<V6<A>, A> __5() {
        return new F<V6<A>, A>() { // from class: fj.data.vector.V6.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6<Object>) obj);
            }

            public A f(V6<A> v) {
                return v._5();
            }
        };
    }

    public static <A> F<V6<A>, A> __6() {
        return new F<V6<A>, A>() { // from class: fj.data.vector.V6.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V6<Object>) obj);
            }

            public A f(V6<A> v) {
                return v._6();
            }
        };
    }
}
