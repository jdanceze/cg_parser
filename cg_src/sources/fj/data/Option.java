package fj.data;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.P3;
import fj.P4;
import fj.P5;
import fj.P6;
import fj.P7;
import fj.P8;
import fj.Show;
import fj.Unit;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Option.class */
public abstract class Option<A> implements Iterable<A> {
    public static final F<String, Option<Byte>> parseByte = new F<String, Option<Byte>>() { // from class: fj.data.Option.19
        @Override // fj.F
        public Option<Byte> f(String s) {
            return Validation.parseByte(s).toOption();
        }
    };
    public static final F<String, Option<Double>> parseDouble = new F<String, Option<Double>>() { // from class: fj.data.Option.20
        @Override // fj.F
        public Option<Double> f(String s) {
            return Validation.parseDouble(s).toOption();
        }
    };
    public static final F<String, Option<Float>> parseFloat = new F<String, Option<Float>>() { // from class: fj.data.Option.21
        @Override // fj.F
        public Option<Float> f(String s) {
            return Validation.parseFloat(s).toOption();
        }
    };
    public static final F<String, Option<Integer>> parseInt = new F<String, Option<Integer>>() { // from class: fj.data.Option.22
        @Override // fj.F
        public Option<Integer> f(String s) {
            return Validation.parseInt(s).toOption();
        }
    };
    public static final F<String, Option<Long>> parseLong = new F<String, Option<Long>>() { // from class: fj.data.Option.23
        @Override // fj.F
        public Option<Long> f(String s) {
            return Validation.parseLong(s).toOption();
        }
    };
    public static final F<String, Option<Short>> parseShort = new F<String, Option<Short>>() { // from class: fj.data.Option.24
        @Override // fj.F
        public Option<Short> f(String s) {
            return Validation.parseShort(s).toOption();
        }
    };

    public abstract A some();

    private Option() {
    }

    public String toString() {
        Show<A> s = Show.anyShow();
        return Show.optionShow(s).showS((Show) this);
    }

    @Override // java.lang.Iterable
    public final Iterator<A> iterator() {
        return toCollection().iterator();
    }

    public final boolean isSome() {
        return this instanceof Some;
    }

    public final boolean isNone() {
        return this instanceof None;
    }

    public static <A> F<Option<A>, Boolean> isSome_() {
        return new F<Option<A>, Boolean>() { // from class: fj.data.Option.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Boolean f(Option<A> a) {
                return Boolean.valueOf(a.isSome());
            }
        };
    }

    public static <A> F<Option<A>, Boolean> isNone_() {
        return new F<Option<A>, Boolean>() { // from class: fj.data.Option.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Boolean f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Boolean f(Option<A> a) {
                return Boolean.valueOf(a.isNone());
            }
        };
    }

    public final <B> B option(B b, F<A, B> f) {
        return isSome() ? f.f(some()) : b;
    }

    public final <B> B option(P1<B> b, F<A, B> f) {
        return isSome() ? f.f(some()) : b._1();
    }

    public final int length() {
        return isSome() ? 1 : 0;
    }

    public final A orSome(P1<A> a) {
        return isSome() ? some() : a._1();
    }

    public final A orSome(A a) {
        return isSome() ? some() : a;
    }

    public final A valueE(P1<String> message) {
        if (isSome()) {
            return some();
        }
        throw Bottom.error(message._1());
    }

    public final A valueE(String message) {
        if (isSome()) {
            return some();
        }
        throw Bottom.error(message);
    }

    public final <B> Option<B> map(F<A, B> f) {
        return isSome() ? some(f.f(some())) : none();
    }

    public static <A, B> F<F<A, B>, F<Option<A>, Option<B>>> map() {
        return Function.curry(new F2<F<A, B>, Option<A>, Option<B>>() { // from class: fj.data.Option.3
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) obj, (Option) ((Option) obj2));
            }

            public Option<B> f(F<A, B> abf, Option<A> option) {
                return option.map(abf);
            }
        });
    }

    public final Unit foreach(F<A, Unit> f) {
        return isSome() ? f.f(some()) : Unit.unit();
    }

    public final void foreachDoEffect(Effect1<A> f) {
        if (isSome()) {
            f.f(some());
        }
    }

    public final Option<A> filter(F<A, Boolean> f) {
        if (isSome() && f.f(some()).booleanValue()) {
            return this;
        }
        return none();
    }

    public final <B> Option<B> bind(F<A, Option<B>> f) {
        return isSome() ? f.f(some()) : none();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C> Option<C> bind(Option<B> ob, F<A, F<B, C>> f) {
        return (Option<B>) ob.apply(map(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D> Option<D> bind(Option<B> ob, Option<C> oc, F<A, F<B, F<C, D>>> f) {
        return (Option<B>) oc.apply(bind(ob, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E> Option<E> bind(Option<B> ob, Option<C> oc, Option<D> od, F<A, F<B, F<C, F<D, E>>>> f) {
        return (Option<B>) od.apply(bind(ob, oc, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$> Option<F$> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, F<A, F<B, F<C, F<D, F<E, F$>>>>> f) {
        return (Option<B>) oe.apply(bind(ob, oc, od, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G> Option<G> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, F<A, F<B, F<C, F<D, F<E, F<F$, G>>>>>> f) {
        return (Option<B>) of.apply(bind(ob, oc, od, oe, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G, H> Option<H> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, H>>>>>>> f) {
        return (Option<B>) og.apply(bind(ob, oc, od, oe, of, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C, D, E, F$, G, H, I> Option<I> bind(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og, Option<H> oh, F<A, F<B, F<C, F<D, F<E, F<F$, F<G, F<H, I>>>>>>>> f) {
        return (Option<B>) oh.apply(bind(ob, oc, od, oe, of, og, f));
    }

    public final <B> Option<P2<A, B>> bindProduct(Option<B> ob) {
        return (Option<P2<A, B>>) bind(ob, P.p2());
    }

    public final <B, C> Option<P3<A, B, C>> bindProduct(Option<B> ob, Option<C> oc) {
        return (Option<P3<A, B, C>>) bind(ob, oc, P.p3());
    }

    public final <B, C, D> Option<P4<A, B, C, D>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od) {
        return (Option<P4<A, B, C, D>>) bind(ob, oc, od, P.p4());
    }

    public final <B, C, D, E> Option<P5<A, B, C, D, E>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe) {
        return (Option<P5<A, B, C, D, E>>) bind(ob, oc, od, oe, P.p5());
    }

    public final <B, C, D, E, F$> Option<P6<A, B, C, D, E, F$>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of) {
        return (Option<P6<A, B, C, D, E, F$>>) bind(ob, oc, od, oe, of, P.p6());
    }

    public final <B, C, D, E, F$, G> Option<P7<A, B, C, D, E, F$, G>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og) {
        return (Option<P7<A, B, C, D, E, F$, G>>) bind(ob, oc, od, oe, of, og, P.p7());
    }

    public final <B, C, D, E, F$, G, H> Option<P8<A, B, C, D, E, F$, G, H>> bindProduct(Option<B> ob, Option<C> oc, Option<D> od, Option<E> oe, Option<F$> of, Option<G> og, Option<H> oh) {
        return (Option<P8<A, B, C, D, E, F$, G, H>>) bind(ob, oc, od, oe, of, og, oh, P.p8());
    }

    public final <B> Option<B> sequence(Option<B> o) {
        F<A, Option<B>> c = Function.constant(o);
        return bind(c);
    }

    public final <B> Option<B> apply(Option<F<A, B>> of) {
        return of.bind(new F<F<A, B>, Option<B>>() { // from class: fj.data.Option.4
            @Override // fj.F
            public Option<B> f(final F<A, B> f) {
                return Option.this.map(new F<A, B>() { // from class: fj.data.Option.4.1
                    /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
                    @Override // fj.F
                    public B f(A a) {
                        return f.f(a);
                    }
                });
            }
        });
    }

    public final Option<A> orElse(P1<Option<A>> o) {
        return isSome() ? this : o._1();
    }

    public final Option<A> orElse(Option<A> o) {
        return isSome() ? this : o;
    }

    public final <X> Either<X, A> toEither(P1<X> x) {
        return isSome() ? Either.right(some()) : Either.left(x._1());
    }

    public final <X> Either<X, A> toEither(X x) {
        return isSome() ? Either.right(some()) : Either.left(x);
    }

    public final <X> Validation<X, A> toValidation(X x) {
        return Validation.validation(toEither((Option<A>) x));
    }

    public static <A, X> F<Option<A>, F<X, Either<X, A>>> toEither() {
        return Function.curry(new F2<Option<A>, X, Either<X, A>>() { // from class: fj.data.Option.5
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Option) ((Option) obj), (Option<A>) obj2);
            }

            public Either<X, A> f(Option<A> a, X x) {
                return a.toEither((Option<A>) x);
            }
        });
    }

    public final List<A> toList() {
        return isSome() ? List.cons(some(), List.nil()) : List.nil();
    }

    public final Stream<A> toStream() {
        return isSome() ? Stream.nil().cons(some()) : Stream.nil();
    }

    public final Array<A> toArray() {
        return isSome() ? Array.array(some()) : Array.empty();
    }

    public final Array<A> toArray(Class<A[]> c) {
        if (isSome()) {
            Object[] objArr = (Object[]) java.lang.reflect.Array.newInstance(c.getComponentType(), 1);
            objArr[0] = some();
            return Array.array(objArr);
        }
        return Array.array((Object[]) java.lang.reflect.Array.newInstance(c.getComponentType(), 0));
    }

    public final A[] array(Class<A[]> c) {
        return toArray(c).array(c);
    }

    public final A toNull() {
        return orSome((Option<A>) null);
    }

    public final boolean forall(F<A, Boolean> f) {
        return isNone() || f.f(some()).booleanValue();
    }

    public final boolean exists(F<A, Boolean> f) {
        return isSome() && f.f(some()).booleanValue();
    }

    public final Collection<A> toCollection() {
        return toList().toCollection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Option$None.class */
    public static final class None<A> extends Option<A> {
        private None() {
            super();
        }

        @Override // fj.data.Option
        public A some() {
            throw Bottom.error("some on None");
        }

        public int hashCode() {
            return 31;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Option$Some.class */
    public static final class Some<A> extends Option<A> {
        private final A a;

        Some(A a) {
            super();
            this.a = a;
        }

        @Override // fj.data.Option
        public A some() {
            return this.a;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.a == null ? 0 : this.a.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Some<?> other = (Some) obj;
            if (this.a == null) {
                if (other.a != null) {
                    return false;
                }
                return true;
            } else if (!this.a.equals(other.a)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static <T> F<T, Option<T>> some_() {
        return new F<T, Option<T>>() { // from class: fj.data.Option.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass6) obj);
            }

            @Override // fj.F
            public Option<T> f(T t) {
                return Option.some(t);
            }
        };
    }

    public static <T> Option<T> some(T t) {
        return new Some(t);
    }

    public static <T> F<T, Option<T>> none_() {
        return new F<T, Option<T>>() { // from class: fj.data.Option.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass7) obj);
            }

            @Override // fj.F
            public Option<T> f(T t) {
                return Option.none();
            }
        };
    }

    public static <T> Option<T> none() {
        return new None();
    }

    public static <T> Option<T> fromNull(T t) {
        return t == null ? none() : some(t);
    }

    public static <T> F<T, Option<T>> fromNull() {
        return new F<T, Option<T>>() { // from class: fj.data.Option.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass8) obj);
            }

            @Override // fj.F
            public Option<T> f(T t) {
                return Option.fromNull(t);
            }
        };
    }

    public static <A> Option<A> join(Option<Option<A>> o) {
        return (Option<A>) o.bind(Function.identity());
    }

    public static <A> Option<List<A>> sequence(final List<Option<A>> a) {
        if (a.isEmpty()) {
            return some(List.nil());
        }
        return (Option<List<A>>) a.head().bind((F<A, Option<List<A>>>) new F<A, Option<List<A>>>() { // from class: fj.data.Option.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass9) obj);
            }

            @Override // fj.F
            public Option<List<A>> f(A aa) {
                return Option.sequence(List.this.tail()).map(List.cons_(aa));
            }
        });
    }

    public static <A> Option<A> iif(F<A, Boolean> f, A a) {
        return f.f(a).booleanValue() ? some(a) : none();
    }

    public static <A> Option<A> iif(boolean p, P1<A> a) {
        return p ? some(a._1()) : none();
    }

    public static <A> Option<A> iif(boolean p, A a) {
        return iif(p, P.p(a));
    }

    public static <A> F2<F<A, Boolean>, A, Option<A>> iif() {
        return new F2<F<A, Boolean>, A, Option<A>>() { // from class: fj.data.Option.10
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F<F<A, Boolean>, Boolean>) obj, (F<A, Boolean>) obj2);
            }

            public Option<A> f(F<A, Boolean> p, A a) {
                return Option.iif(p, a);
            }
        };
    }

    public static <A> List<A> somes(List<Option<A>> as) {
        return (List<A>) as.filter(isSome_()).map((F<Option<A>, A>) new F<Option<A>, A>() { // from class: fj.data.Option.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option<Object>) obj);
            }

            public A f(Option<A> o) {
                return o.some();
            }
        });
    }

    public static <A> Stream<A> somes(Stream<Option<A>> as) {
        return (Stream<A>) as.filter(isSome_()).map((F<Option<A>, A>) new F<Option<A>, A>() { // from class: fj.data.Option.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option<Object>) obj);
            }

            public A f(Option<A> o) {
                return o.some();
            }
        });
    }

    public static Option<String> fromString(String s) {
        return fromNull(s).bind(new F<String, Option<String>>() { // from class: fj.data.Option.13
            @Override // fj.F
            public Option<String> f(String s2) {
                Option<String> none = Option.none();
                return s2.length() == 0 ? none : Option.some(s2);
            }
        });
    }

    public static F<String, Option<String>> fromString() {
        return new F<String, Option<String>>() { // from class: fj.data.Option.14
            @Override // fj.F
            public Option<String> f(String s) {
                return Option.fromString(s);
            }
        };
    }

    public static <A> F<Option<A>, A> fromSome() {
        return new F<Option<A>, A>() { // from class: fj.data.Option.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option<Object>) obj);
            }

            public A f(Option<A> option) {
                return option.some();
            }
        };
    }

    public static <A, B, C> F<Option<A>, F<Option<B>, Option<C>>> liftM2(final F<A, F<B, C>> f) {
        return Function.curry(new F2<Option<A>, Option<B>, Option<C>>() { // from class: fj.data.Option.16
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Option) ((Option) obj), (Option) obj2);
            }

            public Option<C> f(Option<A> a, Option<B> b) {
                return a.bind(b, F.this);
            }
        });
    }

    public static <A, B> F<F<A, Option<B>>, F<Option<A>, Option<B>>> bind() {
        return Function.curry(new F2<F<A, Option<B>>, Option<A>, Option<B>>() { // from class: fj.data.Option.17
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((F) obj, (Option) ((Option) obj2));
            }

            public Option<B> f(F<A, Option<B>> f, Option<A> a) {
                return a.bind(f);
            }
        });
    }

    public static <A> F<Option<Option<A>>, Option<A>> join() {
        return new F<Option<Option<A>>, Option<A>>() { // from class: fj.data.Option.18
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Option) ((Option) obj));
            }

            public Option<A> f(Option<Option<A>> option) {
                return Option.join(option);
            }
        };
    }
}
