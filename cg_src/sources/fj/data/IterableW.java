package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.P1;
import fj.P2;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IterableW.class */
public final class IterableW<A> implements Iterable<A> {
    private final Iterable<A> i;

    private IterableW(Iterable<A> i) {
        this.i = i;
    }

    public static <A> IterableW<A> wrap(Iterable<A> a) {
        return new IterableW<>(a);
    }

    public static <A, T extends Iterable<A>> F<T, IterableW<A>> wrap() {
        return (F<T, IterableW<A>>) new F<T, IterableW<A>>() { // from class: fj.data.IterableW.1
            /* JADX WARN: Incorrect types in method signature: (TT;)Lfj/data/IterableW<TA;>; */
            @Override // fj.F
            public IterableW f(Iterable iterable) {
                return IterableW.wrap(iterable);
            }
        };
    }

    public static <A> IterableW<A> iterable(A a) {
        return wrap(Option.some(a));
    }

    public static <A, B> F<A, IterableW<B>> iterable(final F<A, B> f) {
        return new F<A, IterableW<B>>() { // from class: fj.data.IterableW.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            @Override // fj.F
            public IterableW<B> f(A a) {
                return IterableW.iterable(F.this.f(a));
            }
        };
    }

    public static <A, B> F<F<A, B>, F<A, IterableW<B>>> arrow() {
        return new F<F<A, B>, F<A, IterableW<B>>>() { // from class: fj.data.IterableW.3
            @Override // fj.F
            public F<A, IterableW<B>> f(F<A, B> f) {
                return IterableW.iterable((F) f);
            }
        };
    }

    public <B, T extends Iterable<B>> IterableW<B> bind(final F<A, T> f) {
        return wrap(Stream.iterableStream(this).bind(new F<A, Stream<B>>() { // from class: fj.data.IterableW.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass4) obj);
            }

            @Override // fj.F
            public Stream<B> f(A a) {
                return Stream.iterableStream((Iterable) f.f(a));
            }
        }));
    }

    public <B> IterableW<B> apply(Iterable<F<A, B>> f) {
        return wrap(f).bind(new F<F<A, B>, Iterable<B>>() { // from class: fj.data.IterableW.5
            @Override // fj.F
            public Iterable<B> f(F<A, B> f2) {
                return IterableW.this.map(f2);
            }
        });
    }

    public static <A, B, C> IterableW<C> bind(Iterable<A> a, Iterable<B> b, F<A, F<B, C>> f) {
        return wrap(b).apply(wrap(a).map(f));
    }

    public static <A, B, C> F<Iterable<A>, F<Iterable<B>, IterableW<C>>> liftM2(final F<A, F<B, C>> f) {
        return Function.curry(new F2<Iterable<A>, Iterable<B>, IterableW<C>>() { // from class: fj.data.IterableW.6
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Iterable) ((Iterable) obj), (Iterable) obj2);
            }

            public IterableW<C> f(Iterable<A> ca, Iterable<B> cb) {
                return IterableW.bind(ca, cb, F.this);
            }
        });
    }

    public static <A, T extends Iterable<A>> IterableW<IterableW<A>> sequence(Iterable<T> as) {
        final Stream<T> ts = Stream.iterableStream(as);
        return ts.isEmpty() ? iterable(wrap(Option.none())) : wrap(ts.head()).bind(new F<A, Iterable<IterableW<A>>>() { // from class: fj.data.IterableW.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass7) obj);
            }

            @Override // fj.F
            public Iterable<IterableW<A>> f(final A a) {
                return IterableW.sequence((Iterable) Stream.this.tail().map(IterableW.wrap())._1()).bind(new F<IterableW<A>, Iterable<IterableW<A>>>() { // from class: fj.data.IterableW.7.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((IterableW) ((IterableW) obj));
                    }

                    public Iterable<IterableW<A>> f(final IterableW<A> as2) {
                        return IterableW.iterable(IterableW.wrap(Stream.cons(a, new P1<Stream<A>>() { // from class: fj.data.IterableW.7.1.1
                            @Override // fj.P1
                            public Stream<A> _1() {
                                return Stream.iterableStream(as2);
                            }
                        })));
                    }
                });
            }
        });
    }

    public static <A, B, T extends Iterable<B>> F<IterableW<A>, F<F<A, T>, IterableW<B>>> bind() {
        return (F<IterableW<A>, F<F<A, T>, IterableW<B>>>) new F<IterableW<A>, F<F<A, T>, IterableW<B>>>() { // from class: fj.data.IterableW.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((IterableW) ((IterableW) obj));
            }

            public F<F<A, T>, IterableW<B>> f(final IterableW<A> a) {
                return new F<F<A, T>, IterableW<B>>() { // from class: fj.data.IterableW.8.1
                    @Override // fj.F
                    public IterableW<B> f(F<A, T> f) {
                        return a.bind(f);
                    }
                };
            }
        };
    }

    public static <A, T extends Iterable<A>> IterableW<A> join(Iterable<T> as) {
        F<T, T> id = Function.identity();
        return wrap(as).bind(id);
    }

    public static <A, T extends Iterable<A>> F<Iterable<T>, IterableW<A>> join() {
        return (F<Iterable<T>, IterableW<A>>) new F<Iterable<T>, IterableW<A>>() { // from class: fj.data.IterableW.9
            @Override // fj.F
            public IterableW<A> f(Iterable<T> a) {
                return IterableW.join(a);
            }
        };
    }

    public <B> IterableW<B> map(F<A, B> f) {
        return bind(iterable((F) f));
    }

    public static <A, B> F<F<A, B>, F<IterableW<A>, IterableW<B>>> map() {
        return new F<F<A, B>, F<IterableW<A>, IterableW<B>>>() { // from class: fj.data.IterableW.10
            @Override // fj.F
            public F<IterableW<A>, IterableW<B>> f(final F<A, B> f) {
                return new F<IterableW<A>, IterableW<B>>() { // from class: fj.data.IterableW.10.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((IterableW) ((IterableW) obj));
                    }

                    public IterableW<B> f(IterableW<A> a) {
                        return a.map(f);
                    }
                };
            }
        };
    }

    public <B> B foldLeft(F<B, F<A, B>> f, B z) {
        B p = z;
        Iterator<A> it = iterator();
        while (it.hasNext()) {
            A x = it.next();
            p = f.f(p).f(x);
        }
        return p;
    }

    public A foldLeft1(F2<A, A, A> f) {
        return foldLeft1(Function.curry(f));
    }

    public A foldLeft1(F<A, F<A, A>> f) {
        return (A) Stream.iterableStream(this).foldLeft1(f);
    }

    public <B> B foldRight(final F2<A, B, B> f, B z) {
        F<B, B> id = Function.identity();
        return (B) ((F) foldLeft(Function.curry(new F3<F<B, B>, A, B, B>() { // from class: fj.data.IterableW.11
            @Override // fj.F3
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2, Object obj3) {
                return f((F<Object, Object>) obj, (F) obj2, obj3);
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [B, java.lang.Object] */
            public B f(F<B, B> k, A a, B b) {
                return k.f(f.f(a, b));
            }
        }), id)).f(z);
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return this.i.iterator();
    }

    public <B> IterableW<B> zapp(Iterable<F<A, B>> fs) {
        return wrap(Stream.iterableStream(this).zapp(Stream.iterableStream(fs)));
    }

    public <B, C> Iterable<C> zipWith(Iterable<B> bs, F<A, F<B, C>> f) {
        return wrap(Stream.iterableStream(this).zipWith(Stream.iterableStream(bs), f));
    }

    public <B, C> Iterable<C> zipWith(Iterable<B> bs, F2<A, B, C> f) {
        return zipWith(bs, Function.curry(f));
    }

    public <B> Iterable<P2<A, B>> zip(Iterable<B> bs) {
        return wrap(Stream.iterableStream(this).zip(Stream.iterableStream(bs)));
    }

    public Iterable<P2<A, Integer>> zipIndex() {
        return wrap(Stream.iterableStream(this).zipIndex());
    }

    public java.util.List<A> toStandardList() {
        return new java.util.List<A>() { // from class: fj.data.IterableW.12
            @Override // java.util.List, java.util.Collection
            public int size() {
                return Stream.iterableStream(IterableW.this).length();
            }

            @Override // java.util.List, java.util.Collection
            public boolean isEmpty() {
                return Stream.iterableStream(IterableW.this).isEmpty();
            }

            @Override // java.util.List, java.util.Collection
            public boolean contains(Object o) {
                return Stream.iterableStream(IterableW.this).exists(Equal.anyEqual().eq(o));
            }

            @Override // java.util.List, java.util.Collection, java.lang.Iterable
            public Iterator<A> iterator() {
                return IterableW.this.iterator();
            }

            @Override // java.util.List, java.util.Collection
            public Object[] toArray() {
                return Array.iterableArray(Stream.iterableStream(IterableW.this)).array();
            }

            @Override // java.util.List, java.util.Collection
            public <T> T[] toArray(T[] a) {
                return (T[]) Stream.iterableStream(IterableW.this).toCollection().toArray(a);
            }

            @Override // java.util.List, java.util.Collection
            public boolean add(A a) {
                return false;
            }

            @Override // java.util.List, java.util.Collection
            public boolean remove(Object o) {
                return false;
            }

            @Override // java.util.List, java.util.Collection
            public boolean containsAll(Collection<?> c) {
                return Stream.iterableStream(IterableW.this).toCollection().containsAll(c);
            }

            @Override // java.util.List, java.util.Collection
            public boolean addAll(Collection<? extends A> c) {
                return false;
            }

            @Override // java.util.List
            public boolean addAll(int index, Collection<? extends A> c) {
                return false;
            }

            @Override // java.util.List, java.util.Collection
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override // java.util.List, java.util.Collection
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override // java.util.List, java.util.Collection
            public void clear() {
                throw new UnsupportedOperationException("Modifying an immutable List.");
            }

            @Override // java.util.List
            public A get(int index) {
                return (A) Stream.iterableStream(IterableW.this).index(index);
            }

            @Override // java.util.List
            public A set(int index, A element) {
                throw new UnsupportedOperationException("Modifying an immutable List.");
            }

            @Override // java.util.List
            public void add(int index, A element) {
                throw new UnsupportedOperationException("Modifying an immutable List.");
            }

            @Override // java.util.List
            public A remove(int index) {
                throw new UnsupportedOperationException("Modifying an immutable List.");
            }

            @Override // java.util.List
            public int indexOf(Object o) {
                int i = -1;
                Iterator<A> it = IterableW.this.iterator();
                while (it.hasNext()) {
                    A a = it.next();
                    i++;
                    if (a.equals(o)) {
                        return i;
                    }
                }
                return i;
            }

            @Override // java.util.List
            public int lastIndexOf(Object o) {
                int i = -1;
                int last = -1;
                Iterator<A> it = IterableW.this.iterator();
                while (it.hasNext()) {
                    A a = it.next();
                    i++;
                    if (a.equals(o)) {
                        last = i;
                    }
                }
                return last;
            }

            @Override // java.util.List
            public ListIterator<A> listIterator() {
                return toListIterator(IterableW.this.toZipper());
            }

            @Override // java.util.List
            public ListIterator<A> listIterator(int index) {
                return toListIterator(IterableW.this.toZipper().bind((F) Zipper.move().f(Integer.valueOf(index))));
            }

            @Override // java.util.List
            public java.util.List<A> subList(int fromIndex, int toIndex) {
                return IterableW.wrap(Stream.iterableStream(IterableW.this).drop(fromIndex).take(toIndex - fromIndex)).toStandardList();
            }

            private ListIterator<A> toListIterator(final Option<Zipper<A>> z) {
                return new ListIterator<A>() { // from class: fj.data.IterableW.12.1
                    private Option<Zipper<A>> pz;

                    {
                        this.pz = z;
                    }

                    @Override // java.util.ListIterator, java.util.Iterator
                    public boolean hasNext() {
                        return this.pz.isSome() && !this.pz.some().atEnd();
                    }

                    @Override // java.util.ListIterator, java.util.Iterator
                    public A next() {
                        if (this.pz.isSome()) {
                            this.pz = this.pz.some().next();
                            if (this.pz.isSome()) {
                                return this.pz.some().focus();
                            }
                            throw new NoSuchElementException();
                        }
                        throw new NoSuchElementException();
                    }

                    @Override // java.util.ListIterator
                    public boolean hasPrevious() {
                        return this.pz.isSome() && !this.pz.some().atStart();
                    }

                    @Override // java.util.ListIterator
                    public A previous() {
                        this.pz = this.pz.some().previous();
                        return this.pz.some().focus();
                    }

                    @Override // java.util.ListIterator
                    public int nextIndex() {
                        return this.pz.some().index() + (this.pz.some().atEnd() ? 0 : 1);
                    }

                    @Override // java.util.ListIterator
                    public int previousIndex() {
                        return this.pz.some().index() - (this.pz.some().atStart() ? 0 : 1);
                    }

                    @Override // java.util.ListIterator, java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("Remove on immutable ListIterator");
                    }

                    @Override // java.util.ListIterator
                    public void set(A a) {
                        throw new UnsupportedOperationException("Set on immutable ListIterator");
                    }

                    @Override // java.util.ListIterator
                    public void add(A a) {
                        throw new UnsupportedOperationException("Add on immutable ListIterator");
                    }
                };
            }
        };
    }

    public Option<Zipper<A>> toZipper() {
        return Zipper.fromStream(Stream.iterableStream(this));
    }
}
