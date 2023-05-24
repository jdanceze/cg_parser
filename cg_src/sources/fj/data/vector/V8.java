package fj.data.vector;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P7;
import fj.P8;
import fj.data.Array;
import fj.data.NonEmptyList;
import fj.data.Stream;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/vector/V8.class */
public final class V8<A> implements Iterable<A> {
    private final V7<A> tail;
    private final P1<A> head;

    private V8(P1<A> head, V7<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public static <A> V8<A> p(final P8<A, A, A, A, A, A, A, A> p) {
        return new V8<>(new P1<A>() { // from class: fj.data.vector.V8.1
            @Override // fj.P1
            public A _1() {
                return (A) P8.this._1();
            }
        }, V7.p(new P7<A, A, A, A, A, A, A>() { // from class: fj.data.vector.V8.2
            @Override // fj.P7
            public A _1() {
                return (A) P8.this._2();
            }

            @Override // fj.P7
            public A _2() {
                return (A) P8.this._3();
            }

            @Override // fj.P7
            public A _3() {
                return (A) P8.this._4();
            }

            @Override // fj.P7
            public A _4() {
                return (A) P8.this._5();
            }

            @Override // fj.P7
            public A _5() {
                return (A) P8.this._6();
            }

            @Override // fj.P7
            public A _6() {
                return (A) P8.this._7();
            }

            @Override // fj.P7
            public A _7() {
                return (A) P8.this._8();
            }
        }));
    }

    public static <A> V8<A> cons(P1<A> head, V7<A> tail) {
        return new V8<>(head, tail);
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

    public A _8() {
        return this.tail._7();
    }

    public V7<A> tail() {
        return this.tail;
    }

    public P1<A> head() {
        return this.head;
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toStream().iterator();
    }

    public P8<A, A, A, A, A, A, A, A> p() {
        return new P8<A, A, A, A, A, A, A, A>() { // from class: fj.data.vector.V8.3
            @Override // fj.P8
            public A _1() {
                return (A) V8.this._1();
            }

            @Override // fj.P8
            public A _2() {
                return (A) V8.this._2();
            }

            @Override // fj.P8
            public A _3() {
                return (A) V8.this._3();
            }

            @Override // fj.P8
            public A _4() {
                return (A) V8.this._4();
            }

            @Override // fj.P8
            public A _5() {
                return (A) V8.this._5();
            }

            @Override // fj.P8
            public A _6() {
                return (A) V8.this._6();
            }

            @Override // fj.P8
            public A _7() {
                return (A) V8.this._7();
            }

            @Override // fj.P8
            public A _8() {
                return (A) V8.this._8();
            }
        };
    }

    public NonEmptyList<A> toNonEmptyList() {
        return NonEmptyList.nel(_1(), this.tail.toNonEmptyList().toList());
    }

    public Stream<A> toStream() {
        return Stream.cons(this.head._1(), new P1<Stream<A>>() { // from class: fj.data.vector.V8.4
            @Override // fj.P1
            public Stream<A> _1() {
                return V8.this.tail.toStream();
            }
        });
    }

    public Array<A> toArray() {
        return Array.array(_1(), _2(), _3(), _4(), _5(), _6(), _7(), _8());
    }

    public <B> V8<B> map(F<A, B> f) {
        return new V8<>(this.head.map(f), this.tail.map(f));
    }

    public <B> V8<B> apply(V8<F<A, B>> vf) {
        return new V8<>(this.head.apply(vf.head()), this.tail.apply(vf.tail()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> V8<C> zipWith(F<A, F<B, C>> f, V8<B> bs) {
        return (V8<B>) bs.apply(map(f));
    }

    public <B> V8<P2<A, B>> zip(V8<B> bs) {
        return (V8<P2<A, B>>) zipWith(P.p2(), bs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public V8<V2<A>> vzip(V8<A> bs) {
        F2<A, A, V2<A>> __2 = V.v2();
        return (V8<V2<A>>) zipWith(Function.curry(__2), bs);
    }

    public static <A> F<V8<A>, Stream<A>> toStream_() {
        return new F<V8<A>, Stream<A>>() { // from class: fj.data.vector.V8.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8) ((V8) obj));
            }

            public Stream<A> f(V8<A> v) {
                return v.toStream();
            }
        };
    }

    public static <A> F<V8<A>, P8<A, A, A, A, A, A, A, A>> p_() {
        return new F<V8<A>, P8<A, A, A, A, A, A, A, A>>() { // from class: fj.data.vector.V8.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8) ((V8) obj));
            }

            public P8<A, A, A, A, A, A, A, A> f(V8<A> v) {
                return v.p();
            }
        };
    }

    public static <A> F<V8<A>, A> __1() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._1();
            }
        };
    }

    public static <A> F<V8<A>, A> __2() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._2();
            }
        };
    }

    public static <A> F<V8<A>, A> __3() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._3();
            }
        };
    }

    public static <A> F<V8<A>, A> __4() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._4();
            }
        };
    }

    public static <A> F<V8<A>, A> __5() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._5();
            }
        };
    }

    public static <A> F<V8<A>, A> __6() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._6();
            }
        };
    }

    public static <A> F<V8<A>, A> __7() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._7();
            }
        };
    }

    public static <A> F<V8<A>, A> __8() {
        return new F<V8<A>, A>() { // from class: fj.data.vector.V8.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((V8<Object>) obj);
            }

            public A f(V8<A> v) {
                return v._8();
            }
        };
    }
}
