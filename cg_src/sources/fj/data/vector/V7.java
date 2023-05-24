package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P6;
import fj.P7;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/vector/V7.class */
public final class V7<A> implements Iterable<A> {
    private final V6<A> tail;
    private final P1<A> head;

    private V7(P1<A> head, V6<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> V7<A> p(final P7<A, A, A, A, A, A, A> p) {
        return new V7<>(new P1<A>() { // from class: fj.data.vector.V7.1
            @Override // fj.P1
            public A _1() {
                return (A) P7.this._1();
            }
        }, V6.p(new P6<A, A, A, A, A, A>() { // from class: fj.data.vector.V7.2
            @Override // fj.P6
            public A _1() {
                return (A) P7.this._2();
            }

            @Override // fj.P6
            public A _2() {
                return (A) P7.this._3();
            }

            @Override // fj.P6
            public A _3() {
                return (A) P7.this._4();
            }

            @Override // fj.P6
            public A _4() {
                return (A) P7.this._5();
            }

            @Override // fj.P6
            public A _5() {
                return (A) P7.this._6();
            }

            @Override // fj.P6
            public A _6() {
                return (A) P7.this._7();
            }
        }));
    }

    public static <A> V7<A> cons(P1<A> head, V6<A> tail) {
        return new V7<>(head, tail);
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

    public A _7() {
        return this.tail._6();
    }

    public V6<A> tail() {
        return this.tail;
    }

    public P1<A> head() {
        return this.head;
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toStream().iterator();
    }

    public P7<A, A, A, A, A, A, A> p() {
        return new P7<A, A, A, A, A, A, A>() { // from class: fj.data.vector.V7.3
            @Override // fj.P7
            public A _1() {
                return (A) V7.this._1();
            }

            @Override // fj.P7
            public A _2() {
                return (A) V7.this._2();
            }

            @Override // fj.P7
            public A _3() {
                return (A) V7.this._3();
            }

            @Override // fj.P7
            public A _4() {
                return (A) V7.this._4();
            }

            @Override // fj.P7
            public A _5() {
                return (A) V7.this._5();
            }

            @Override // fj.P7
            public A _6() {
                return (A) V7.this._6();
            }

            @Override // fj.P7
            public A _7() {
                return (A) V7.this._7();
            }
        };
    }

    public NonEmptyList<A> toNonEmptyList() {
        return NonEmptyList.nel(_1(), this.tail.toNonEmptyList().toList());
    }

    public Stream<A> toStream() {
        return Stream.cons(this.head._1(), new P1<Stream<A>>() { // from class: fj.data.vector.V7.4
            @Override // fj.P1
            public Stream<A> _1() {
                return V7.this.tail.toStream();
            }
        });
    }

    public Array<A> toArray() {
        return Array.array(_1(), _2(), _3(), _4(), _5(), _6(), _7());
    }

    public <B> V7<B> map(F<A, B> f) {
        return new V7<>(this.head.map(f), this.tail.map(f));
    }

    public <B> V7<B> apply(V7<F<A, B>> vf) {
        return new V7<>(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> V7<C> zipWith(F<A, F<B, C>> f, V7<B> bs) {
        return (V7<B>) bs.apply(map(f));
    }

    public <B> V7<P2<A, B>> zip(V7<B> bs) {
        return (V7<P2<A, B>>) zipWith(P.p2(), bs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V7<V2<A>> vzip(V7<A> bs) {
        F2<A, A, V2<A>> __2 = V.v2();
        return (V7<V2<A>>) zipWith(Function.curry(__2), bs);
    }

    public static <A> F<V7<A>, Stream<A>> toStream_() {
        return new F<V7<A>, Stream<A>>() { // from class: fj.data.vector.V7.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7) ((V7) obj));
            }

            public Stream<A> f(V7<A> v) {
                return v.toStream();
            }
        };
    }

    public static <A> F<V7<A>, P7<A, A, A, A, A, A, A>> p_() {
        return new F<V7<A>, P7<A, A, A, A, A, A, A>>() { // from class: fj.data.vector.V7.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7) ((V7) obj));
            }

            public P7<A, A, A, A, A, A, A> f(V7<A> v) {
                return v.p();
            }
        };
    }

    public static <A> F<V7<A>, A> __1() {
        return new F<V7<A>, A>() { // from class: fj.data.vector.V7.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7<Object>) obj);
            }

            public A f(V7<A> v) {
                return v._1();
            }
        };
    }

    public static <A> F<V7<A>, A> __2() {
        return new F<V7<A>, A>() { // from class: fj.data.vector.V7.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7<Object>) obj);
            }

            public A f(V7<A> v) {
                return v._2();
            }
        };
    }

    public static <A> F<V7<A>, A> __3() {
        return new F<V7<A>, A>() { // from class: fj.data.vector.V7.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7<Object>) obj);
            }

            public A f(V7<A> v) {
                return v._3();
            }
        };
    }

    public static <A> F<V7<A>, A> __4() {
        return new F<V7<A>, A>() { // from class: fj.data.vector.V7.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7<Object>) obj);
            }

            public A f(V7<A> v) {
                return v._4();
            }
        };
    }

    public static <A> F<V7<A>, A> __5() {
        return new F<V7<A>, A>() { // from class: fj.data.vector.V7.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7<Object>) obj);
            }

            public A f(V7<A> v) {
                return v._5();
            }
        };
    }

    public static <A> F<V7<A>, A> __6() {
        return new F<V7<A>, A>() { // from class: fj.data.vector.V7.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7<Object>) obj);
            }

            public A f(V7<A> v) {
                return v._6();
            }
        };
    }

    public static <A> F<V7<A>, A> __7() {
        return new F<V7<A>, A>() { // from class: fj.data.vector.V7.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V7<Object>) obj);
            }

            public A f(V7<A> v) {
                return v._7();
            }
        };
    }
}
