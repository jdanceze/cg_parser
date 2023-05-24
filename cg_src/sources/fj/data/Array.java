package fj.data;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;
import fj.function.Effect1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Array.class */
public final class Array<A> implements Iterable<A> {
    private final Object[] a;

    private Array(Object[] a) {
        this.a = a;
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toCollection().iterator();
    }

    public A get(int index) {
        return (A) this.a[index];
    }

    public Unit set(int index, A a) {
        this.a[index] = a;
        return Unit.unit();
    }

    public int length() {
        return this.a.length;
    }

    public Array<A>.ImmutableProjection<A> immutable() {
        return new ImmutableProjection<>(this);
    }

    public boolean isEmpty() {
        return this.a.length == 0;
    }

    public boolean isNotEmpty() {
        return this.a.length != 0;
    }

    public A[] array(Class<A[]> c) {
        return (A[]) copyOf(this.a, this.a.length, c);
    }

    public Object[] array() {
        return copyOf(this.a, this.a.length);
    }

    public Option<A> toOption() {
        return this.a.length == 0 ? Option.none() : Option.some(this.a[0]);
    }

    public <X> Either<X, A> toEither(P1<X> x) {
        return this.a.length == 0 ? Either.left(x._1()) : Either.right(this.a[0]);
    }

    public List<A> toList() {
        List<A> x = List.nil();
        for (int i = this.a.length - 1; i >= 0; i--) {
            x = x.cons((List<A>) ((A) this.a[i]));
        }
        return x;
    }

    public Stream<A> toStream() {
        return Stream.unfold(new F<Integer, Option<P2<A, Integer>>>() { // from class: fj.data.Array.1
            @Override // fj.F
            public Option<P2<A, Integer>> f(Integer o) {
                if (Array.this.a.length > o.intValue()) {
                    return Option.some(P.p(Array.this.a[o.intValue()], Integer.valueOf(o.intValue() + 1)));
                }
                return Option.none();
            }
        }, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B> Array<B> map(F<A, B> f) {
        Object[] bs = new Object[this.a.length];
        for (int i = 0; i < this.a.length; i++) {
            bs[i] = f.f(this.a[i]);
        }
        return new Array<>(bs);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Array<A> filter(F<A, Boolean> f) {
        List<A> x = List.nil();
        for (int i = this.a.length - 1; i >= 0; i--) {
            if (((Boolean) f.f(this.a[i])).booleanValue()) {
                x = x.cons((List<A>) this.a[i]);
            }
        }
        return x.toArray();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Unit foreach(F<A, Unit> f) {
        Object[] objArr;
        for (Object x : this.a) {
            f.f(x);
        }
        return Unit.unit();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void foreachDoEffect(Effect1<A> f) {
        Object[] objArr;
        for (Object x : this.a) {
            f.f(x);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B> B foldRight(F<A, F<B, B>> f, B b) {
        B x = b;
        for (int i = this.a.length - 1; i >= 0; i--) {
            x = ((F) f.f(this.a[i])).f(x);
        }
        return x;
    }

    public <B> B foldRight(F2<A, B, B> f, B b) {
        return (B) foldRight((F<A, F<F<A, F<B, B>>, F<A, F<B, B>>>>) Function.curry(f), (F<A, F<B, B>>) b);
    }

    public <B> B foldLeft(F<B, F<A, B>> f, B b) {
        B x = b;
        for (Object obj : this.a) {
            x = f.f(x).f((A) obj);
        }
        return x;
    }

    public <B> B foldLeft(F2<B, A, B> f, B b) {
        return (B) foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) Function.curry(f), (F<B, F<A, B>>) b);
    }

    public <B> Array<B> scanLeft(F<B, F<A, B>> f, B b) {
        Object[] bs = new Object[this.a.length];
        B x = b;
        for (int i = 0; i < this.a.length; i++) {
            x = f.f(x).f((A) this.a[i]);
            bs[i] = x;
        }
        return new Array<>(bs);
    }

    public <B> Array<B> scanLeft(F2<B, A, B> f, B b) {
        return scanLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) Function.curry(f), (F<B, F<A, B>>) b);
    }

    public Array<A> scanLeft1(F<A, F<A, A>> f) {
        Object[] bs = new Object[this.a.length];
        A x = get(0);
        bs[0] = x;
        for (int i = 1; i < this.a.length; i++) {
            x = f.f(x).f((A) this.a[i]);
            bs[i] = x;
        }
        return new Array<>(bs);
    }

    public Array<A> scanLeft1(F2<A, A, A> f) {
        return scanLeft1(Function.curry(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B> Array<B> scanRight(F<A, F<B, B>> f, B b) {
        Object[] bs = new Object[this.a.length];
        B x = b;
        for (int i = this.a.length - 1; i >= 0; i--) {
            x = ((F) f.f(this.a[i])).f(x);
            bs[i] = x;
        }
        return new Array<>(bs);
    }

    public <B> Array<B> scanRight(F2<A, B, B> f, B b) {
        return scanRight((F<A, F<F<A, F<B, B>>, F<A, F<B, B>>>>) Function.curry(f), (F<A, F<B, B>>) b);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Array<A> scanRight1(F<A, F<A, A>> f) {
        Object[] bs = new Object[this.a.length];
        A x = get(length() - 1);
        bs[length() - 1] = x;
        for (int i = this.a.length - 2; i >= 0; i--) {
            x = ((F) f.f(this.a[i])).f(x);
            bs[i] = x;
        }
        return new Array<>(bs);
    }

    public Array<A> scanRight1(F2<A, A, A> f) {
        return scanRight1(Function.curry(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B> Array<B> bind(F<A, Array<B>> f) {
        List<Array<B>> x = List.nil();
        int len = 0;
        for (int i = this.a.length - 1; i >= 0; i--) {
            Array<B> bs = f.f(this.a[i]);
            len += bs.length();
            x = x.cons((List<Array<B>>) bs);
        }
        final Object[] bs2 = new Object[len];
        x.foreach(new F<Array<B>, Unit>() { // from class: fj.data.Array.2
            private int i;

            @Override // fj.F
            public Unit f(Array<B> x2) {
                System.arraycopy(((Array) x2).a, 0, bs2, this.i, ((Array) x2).a.length);
                this.i += ((Array) x2).a.length;
                return Unit.unit();
            }
        });
        return new Array<>(bs2);
    }

    public <B> Array<B> sequence(Array<B> bs) {
        F<A, Array<B>> c = Function.constant(bs);
        return bind(c);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> Array<C> bind(Array<B> sb, F<A, F<B, C>> f) {
        return (Array<B>) sb.apply(map(f));
    }

    public <B, C> Array<C> bind(Array<B> sb, F2<A, B, C> f) {
        return bind(sb, Function.curry(f));
    }

    public <B> Array<B> apply(Array<F<A, B>> lf) {
        return lf.bind(new F<F<A, B>, Array<B>>() { // from class: fj.data.Array.3
            @Override // fj.F
            public Array<B> f(final F<A, B> f) {
                return Array.this.map(new F<A, B>() { // from class: fj.data.Array.3.1
                    /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(A a) {
                        return f.f(a);
                    }
                });
            }
        });
    }

    public Array<A> reverse() {
        Object[] x = new Object[this.a.length];
        for (int i = 0; i < this.a.length; i++) {
            x[(this.a.length - 1) - i] = this.a[i];
        }
        return new Array<>(x);
    }

    public Array<A> append(Array<A> aas) {
        Object[] x = new Object[this.a.length + aas.a.length];
        System.arraycopy(this.a, 0, x, 0, this.a.length);
        System.arraycopy(aas.a, 0, x, this.a.length, aas.a.length);
        return new Array<>(x);
    }

    public static <A> Array<A> empty() {
        return new Array<>(new Object[0]);
    }

    public static <A> Array<A> array(A... a) {
        return new Array<>(a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <A> Array<A> mkArray(Object[] a) {
        return new Array<>(a);
    }

    public static <A> Array<A> single(A a) {
        return new Array<>(new Object[]{a});
    }

    public static <A> F<A[], Array<A>> wrap() {
        return new F<A[], Array<A>>() { // from class: fj.data.Array.4
            @Override // fj.F
            public Array<A> f(A[] as) {
                return Array.array(as);
            }
        };
    }

    public static <A, B> F<F<A, B>, F<Array<A>, Array<B>>> map() {
        return Function.curry(new F2<F<A, B>, Array<A>, Array<B>>() { // from class: fj.data.Array.5
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) obj, (Array) ((Array) obj2));
            }

            public Array<B> f(F<A, B> abf, Array<A> array) {
                return array.map(abf);
            }
        });
    }

    public static <A> Array<A> join(Array<Array<A>> o) {
        return (Array<A>) o.bind(Function.identity());
    }

    public static <A> F<Array<Array<A>>, Array<A>> join() {
        return new F<Array<Array<A>>, Array<A>>() { // from class: fj.data.Array.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Array) ((Array) obj));
            }

            public Array<A> f(Array<Array<A>> as) {
                return Array.join(as);
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean forall(F<A, Boolean> f) {
        Object[] objArr;
        for (Object x : this.a) {
            if (!((Boolean) f.f(x)).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean exists(F<A, Boolean> f) {
        Object[] objArr;
        for (Object x : this.a) {
            if (((Boolean) f.f(x)).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Option<A> find(F<A, Boolean> f) {
        Object[] objArr;
        for (Object x : this.a) {
            if (((Boolean) f.f(x)).booleanValue()) {
                return Option.some(x);
            }
        }
        return Option.none();
    }

    public static Array<Integer> range(int from, int to) {
        if (from >= to) {
            return empty();
        }
        Array<Integer> a = new Array<>(new Integer[to - from]);
        for (int i = from; i < to; i++) {
            a.set(i - from, Integer.valueOf(i));
        }
        return a;
    }

    public <B, C> Array<C> zipWith(Array<B> bs, F<A, F<B, C>> f) {
        int len = Math.min(this.a.length, bs.length());
        Array<C> x = new Array<>(new Object[len]);
        for (int i = 0; i < len; i++) {
            x.set(i, f.f(get(i)).f(bs.get(i)));
        }
        return x;
    }

    public <B, C> Array<C> zipWith(Array<B> bs, F2<A, B, C> f) {
        return zipWith(bs, Function.curry(f));
    }

    public <B> Array<P2<A, B>> zip(Array<B> bs) {
        return (Array<P2<A, B>>) zipWith(bs, P.p2());
    }

    public Array<P2<A, Integer>> zipIndex() {
        return (Array<P2<A, Integer>>) zipWith(range(0, length()), (F<A, F<Integer, P2<A, Integer>>>) new F<A, F<Integer, P2<A, Integer>>>() { // from class: fj.data.Array.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass7) obj);
            }

            @Override // fj.F
            public F<Integer, P2<A, Integer>> f(final A a) {
                return new F<Integer, P2<A, Integer>>() { // from class: fj.data.Array.7.1
                    @Override // fj.F
                    public P2<A, Integer> f(Integer i) {
                        return P.p(a, i);
                    }
                };
            }
        });
    }

    public Collection<A> toCollection() {
        return new AbstractCollection<A>() { // from class: fj.data.Array.8
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<A> iterator() {
                return new Iterator<A>() { // from class: fj.data.Array.8.1
                    private int i;

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.i < Array.this.a.length;
                    }

                    @Override // java.util.Iterator
                    public A next() {
                        if (this.i < Array.this.a.length) {
                            A aa = (A) Array.this.a[this.i];
                            this.i++;
                            return aa;
                        }
                        throw new NoSuchElementException();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return Array.this.a.length;
            }
        };
    }

    public static <A> Array<A> iterableArray(Iterable<A> i) {
        return List.iterableList(i).toArray();
    }

    public static <A, B> P2<Array<A>, Array<B>> unzip(Array<P2<A, B>> xs) {
        int len = xs.length();
        Array<A> aa = new Array<>(new Object[len]);
        Array<B> ab = new Array<>(new Object[len]);
        for (int i = len - 1; i >= 0; i--) {
            P2<A, B> p = xs.get(i);
            aa.set(i, p._1());
            ab.set(i, p._2());
        }
        return P.p(aa, ab);
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Array$ImmutableProjection.class */
    public final class ImmutableProjection<A> implements Iterable<A> {
        private final Array<A> a;

        private ImmutableProjection(Array<A> a) {
            this.a = a;
        }

        @Override // java.lang.Iterable
        public Iterator<A> iterator() {
            return this.a.iterator();
        }

        public A get(int index) {
            return this.a.get(index);
        }

        public int length() {
            return this.a.length();
        }

        public boolean isEmpty() {
            return this.a.isEmpty();
        }

        public boolean isNotEmpty() {
            return this.a.isNotEmpty();
        }

        public Option<A> toOption() {
            return this.a.toOption();
        }

        public <X> Either<X, A> toEither(P1<X> x) {
            return this.a.toEither(x);
        }

        public List<A> toList() {
            return this.a.toList();
        }

        public Stream<A> toStream() {
            return this.a.toStream();
        }

        public <B> Array<B> map(F<A, B> f) {
            return this.a.map(f);
        }

        public Array<A> filter(F<A, Boolean> f) {
            return this.a.filter(f);
        }

        public Unit foreach(F<A, Unit> f) {
            return this.a.foreach(f);
        }

        public <B> B foldRight(F<A, F<B, B>> f, B b) {
            return (B) this.a.foldRight((F<A, F<F<A, F<B, B>>, F<A, F<B, B>>>>) f, (F<A, F<B, B>>) b);
        }

        public <B> B foldLeft(F<B, F<A, B>> f, B b) {
            return (B) this.a.foldLeft((F<F<B, F<A, B>>, F<A, F<B, F<A, B>>>>) f, (F<B, F<A, B>>) b);
        }

        public <B> Array<B> bind(F<A, Array<B>> f) {
            return this.a.bind(f);
        }

        public <B> Array<B> sequence(Array<B> bs) {
            return this.a.sequence(bs);
        }

        public <B> Array<B> apply(Array<F<A, B>> lf) {
            return this.a.apply(lf);
        }

        public Array<A> reverse() {
            return this.a.reverse();
        }

        public Array<A> append(Array<A> aas) {
            return this.a.append(aas);
        }

        public Collection<A> toCollection() {
            return this.a.toCollection();
        }
    }

    public static <T, U> T[] copyOf(U[] a, int len, Class<? extends T[]> newType) {
        T[] copy = (T[]) (newType == Object[].class ? new Object[len] : (Object[]) java.lang.reflect.Array.newInstance(newType.getComponentType(), len));
        System.arraycopy(a, 0, copy, 0, Math.min(a.length, len));
        return copy;
    }

    public static <T> T[] copyOf(T[] a, int len) {
        return (T[]) copyOf(a, len, a.getClass());
    }

    public static char[] copyOfRange(char[] a, int from, int to) {
        int len = to - from;
        if (len < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        char[] copy = new char[len];
        System.arraycopy(a, from, copy, 0, Math.min(a.length - from, len));
        return copy;
    }
}
