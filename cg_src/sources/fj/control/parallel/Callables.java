package fj.control.parallel;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P1;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import java.util.concurrent.Callable;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/parallel/Callables.class */
public final class Callables {
    private Callables() {
    }

    public static <A> Callable<A> callable(final A a) {
        return new Callable<A>() { // from class: fj.control.parallel.Callables.1
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public A call() throws Exception {
                return a;
            }
        };
    }

    public static <A> Callable<A> callable(final Exception e) {
        return new Callable<A>() { // from class: fj.control.parallel.Callables.2
            @Override // java.util.concurrent.Callable
            public A call() throws Exception {
                throw e;
            }
        };
    }

    public static <A> F<A, Callable<A>> callable() {
        return new F<A, Callable<A>>() { // from class: fj.control.parallel.Callables.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            @Override // fj.F
            public Callable<A> f(A a) {
                return Callables.callable(a);
            }
        };
    }

    public static <A, B> F<A, Callable<B>> callable(final F<A, B> f) {
        return new F<A, Callable<B>>() { // from class: fj.control.parallel.Callables.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass4) obj);
            }

            @Override // fj.F
            public Callable<B> f(final A a) {
                return new Callable<B>() { // from class: fj.control.parallel.Callables.4.1
                    /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
                    @Override // java.util.concurrent.Callable
                    public B call() {
                        return F.this.f(a);
                    }
                };
            }
        };
    }

    public static <A, B> F<F<A, B>, F<A, Callable<B>>> arrow() {
        return new F<F<A, B>, F<A, Callable<B>>>() { // from class: fj.control.parallel.Callables.5
            @Override // fj.F
            public F<A, Callable<B>> f(F<A, B> f) {
                return Callables.callable((F) f);
            }
        };
    }

    public static <A, B> Callable<B> bind(final Callable<A> a, final F<A, Callable<B>> f) {
        return new Callable<B>() { // from class: fj.control.parallel.Callables.6
            /* JADX WARN: Type inference failed for: r0v4, types: [B, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public B call() throws Exception {
                return ((Callable) F.this.f(a.call())).call();
            }
        };
    }

    public static <A, B> F<Callable<A>, Callable<B>> fmap(final F<A, B> f) {
        return new F<Callable<A>, Callable<B>>() { // from class: fj.control.parallel.Callables.7
            @Override // fj.F
            public Callable<B> f(Callable<A> a) {
                return Callables.bind(a, Callables.callable(F.this));
            }
        };
    }

    public static <A, B> Callable<B> apply(final Callable<A> ca, Callable<F<A, B>> cf) {
        return bind(cf, new F<F<A, B>, Callable<B>>() { // from class: fj.control.parallel.Callables.8
            @Override // fj.F
            public Callable<B> f(F<A, B> f) {
                return (Callable) Callables.fmap(f).f(ca);
            }
        });
    }

    public static <A, B, C> Callable<C> bind(Callable<A> ca, Callable<B> cb, F<A, F<B, C>> f) {
        return apply(cb, (Callable) fmap(f).f(ca));
    }

    public static <A> Callable<A> join(Callable<Callable<A>> a) {
        return bind(a, Function.identity());
    }

    public static <A, B, C> F<Callable<A>, F<Callable<B>, Callable<C>>> liftM2(final F<A, F<B, C>> f) {
        return Function.curry(new F2<Callable<A>, Callable<B>, Callable<C>>() { // from class: fj.control.parallel.Callables.9
            @Override // fj.F2
            public Callable<C> f(Callable<A> ca, Callable<B> cb) {
                return Callables.bind(ca, cb, F.this);
            }
        });
    }

    public static <A> Callable<List<A>> sequence(List<Callable<A>> as) {
        return (Callable) as.foldRight((F<Callable<A>, F<F<Callable<A>, F<B, B>>, F<Callable<A>, F<B, B>>>>) liftM2(List.cons()), (F<Callable<A>, F<B, B>>) callable(List.nil()));
    }

    public static <A> F<List<Callable<A>>, Callable<List<A>>> sequence_() {
        return new F<List<Callable<A>>, Callable<List<A>>>() { // from class: fj.control.parallel.Callables.10
            @Override // fj.F
            public Callable<List<A>> f(List<Callable<A>> as) {
                return Callables.sequence(as);
            }
        };
    }

    public static <A> P1<Option<A>> option(final Callable<A> a) {
        return new P1<Option<A>>() { // from class: fj.control.parallel.Callables.11
            @Override // fj.P1
            public Option<A> _1() {
                try {
                    return Option.some(a.call());
                } catch (Exception e) {
                    return Option.none();
                }
            }
        };
    }

    public static <A> F<Callable<A>, P1<Option<A>>> option() {
        return new F<Callable<A>, P1<Option<A>>>() { // from class: fj.control.parallel.Callables.12
            @Override // fj.F
            public P1<Option<A>> f(Callable<A> a) {
                return Callables.option(a);
            }
        };
    }

    public static <A> P1<Either<Exception, A>> either(final Callable<A> a) {
        return new P1<Either<Exception, A>>() { // from class: fj.control.parallel.Callables.13
            @Override // fj.P1
            public Either<Exception, A> _1() {
                try {
                    return Either.right(a.call());
                } catch (Exception e) {
                    return Either.left(e);
                }
            }
        };
    }

    public static <A> F<Callable<A>, P1<Either<Exception, A>>> either() {
        return new F<Callable<A>, P1<Either<Exception, A>>>() { // from class: fj.control.parallel.Callables.14
            @Override // fj.F
            public P1<Either<Exception, A>> f(Callable<A> a) {
                return Callables.either(a);
            }
        };
    }

    public static <A> Callable<A> fromEither(final P1<Either<Exception, A>> e) {
        return new Callable<A>() { // from class: fj.control.parallel.Callables.15
            /* JADX WARN: Type inference failed for: r0v8, types: [A, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public A call() throws Exception {
                Either either = (Either) P1.this._1();
                if (either.isLeft()) {
                    throw ((Exception) either.left().value());
                }
                return either.right().value();
            }
        };
    }

    public static <A> F<P1<Either<Exception, A>>, Callable<A>> fromEither() {
        return new F<P1<Either<Exception, A>>, Callable<A>>() { // from class: fj.control.parallel.Callables.16
            @Override // fj.F
            public Callable<A> f(P1<Either<Exception, A>> e) {
                return Callables.fromEither(e);
            }
        };
    }

    public static <A> Callable<A> fromOption(final P1<Option<A>> o) {
        return new Callable<A>() { // from class: fj.control.parallel.Callables.17
            /* JADX WARN: Type inference failed for: r0v8, types: [A, java.lang.Object] */
            @Override // java.util.concurrent.Callable
            public A call() throws Exception {
                Option option = (Option) P1.this._1();
                if (option.isSome()) {
                    return option.some();
                }
                throw new Exception("No value.");
            }
        };
    }

    public static <A> F<P1<Option<A>>, Callable<A>> fromOption() {
        return new F<P1<Option<A>>, Callable<A>>() { // from class: fj.control.parallel.Callables.18
            @Override // fj.F
            public Callable<A> f(P1<Option<A>> o) {
                return Callables.fromOption(o);
            }
        };
    }

    public static <A> Callable<A> normalise(Callable<A> a) {
        try {
            return callable(a.call());
        } catch (Exception e) {
            return callable(e);
        }
    }

    public static <A> F<Callable<A>, Callable<A>> normalise() {
        return new F<Callable<A>, Callable<A>>() { // from class: fj.control.parallel.Callables.19
            @Override // fj.F
            public Callable<A> f(Callable<A> a) {
                return Callables.normalise(a);
            }
        };
    }
}
