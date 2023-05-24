package fj.control.parallel;

import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
import fj.function.Effect1;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/parallel/Promise.class */
public final class Promise<A> {
    private final Actor<P2<Either<P1<A>, Actor<A>>, Promise<A>>> actor;
    private final Strategy<Unit> s;
    private final CountDownLatch l = new CountDownLatch(1);
    private volatile Option<A> v = Option.none();
    private final Queue<Actor<A>> waiting = new LinkedList();

    private Promise(Strategy<Unit> s, Actor<P2<Either<P1<A>, Actor<A>>, Promise<A>>> qa) {
        this.s = s;
        this.actor = qa;
    }

    private static <A> Promise<A> mkPromise(Strategy<Unit> s) {
        Actor<P2<Either<P1<A>, Actor<A>>, Promise<A>>> q = Actor.queueActor(s, new Effect1<P2<Either<P1<A>, Actor<A>>, Promise<A>>>() { // from class: fj.control.parallel.Promise.1
            @Override // fj.function.Effect1
            public /* bridge */ /* synthetic */ void f(Object obj) {
                f((P2) ((P2) obj));
            }

            public void f(P2<Either<P1<A>, Actor<A>>, Promise<A>> p) {
                Promise<A> snd = p._2();
                Queue<Actor<A>> as = ((Promise) snd).waiting;
                if (!p._1().isLeft()) {
                    if (!((Promise) snd).v.isNone()) {
                        p._1().right().value().act((A) ((Promise) snd).v.some());
                        return;
                    } else {
                        as.add(p._1().right().value());
                        return;
                    }
                }
                A a = p._1().left().value()._1();
                ((Promise) snd).v = Option.some(a);
                ((Promise) snd).l.countDown();
                while (!as.isEmpty()) {
                    as.remove().act(a);
                }
            }
        });
        return new Promise<>(s, q);
    }

    public static <A> Promise<A> promise(Strategy<Unit> s, P1<A> a) {
        Promise<A> p = mkPromise(s);
        ((Promise) p).actor.act(P.p(Either.left(a), p));
        return p;
    }

    public static <A> F<P1<A>, Promise<A>> promise(final Strategy<Unit> s) {
        return new F<P1<A>, Promise<A>>() { // from class: fj.control.parallel.Promise.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public Promise<A> f(P1<A> a) {
                return Promise.promise(Strategy.this, a);
            }
        };
    }

    public static <A> Promise<Callable<A>> promise(Strategy<Unit> s, final Callable<A> a) {
        return promise(s, new P1<Callable<A>>() { // from class: fj.control.parallel.Promise.3
            @Override // fj.P1
            public Callable<A> _1() {
                return Callables.normalise(a);
            }
        });
    }

    public static <A, B> F<A, Promise<B>> promise(final Strategy<Unit> s, final F<A, B> f) {
        return new F<A, Promise<B>>() { // from class: fj.control.parallel.Promise.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass4) obj);
            }

            @Override // fj.F
            public Promise<B> f(A a) {
                return Promise.promise(Strategy.this, (P1) P1.curry(f).f(a));
            }
        };
    }

    public void to(Actor<A> a) {
        this.actor.act(P.p(Either.right(a), this));
    }

    public <B> Promise<B> fmap(F<A, B> f) {
        return bind(promise(this.s, f));
    }

    public static <A, B> F<Promise<A>, Promise<B>> fmap_(final F<A, B> f) {
        return new F<Promise<A>, Promise<B>>() { // from class: fj.control.parallel.Promise.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Promise) ((Promise) obj));
            }

            public Promise<B> f(Promise<A> a) {
                return a.fmap(F.this);
            }
        };
    }

    public static <A> Promise<A> join(Promise<Promise<A>> p) {
        return (Promise<A>) p.bind(Function.identity());
    }

    public static <A> Promise<A> join(Strategy<Unit> s, P1<Promise<A>> p) {
        return join(promise(s, p));
    }

    public <B> Promise<B> bind(F<A, Promise<B>> f) {
        final Promise<B> r = mkPromise(this.s);
        Actor<B> ab = Actor.actor(this.s, new Effect1<B>() { // from class: fj.control.parallel.Promise.6
            @Override // fj.function.Effect1
            public void f(B b) {
                r.actor.act(P.p(Either.left(P.p(b)), r));
            }
        });
        to(ab.promise().comap(f));
        return r;
    }

    public <B> Promise<B> apply(Promise<F<A, B>> pf) {
        return pf.bind(new F<F<A, B>, Promise<B>>() { // from class: fj.control.parallel.Promise.7
            @Override // fj.F
            public Promise<B> f(F<A, B> f) {
                return Promise.this.fmap(f);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> Promise<C> bind(Promise<B> pb, F<A, F<B, C>> f) {
        return (Promise<B>) pb.apply(fmap(f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <B, C> Promise<C> bind(P1<Promise<B>> p, F<A, F<B, C>> f) {
        return join(this.s, p).apply(fmap(f));
    }

    public static <A, B, C> F<Promise<A>, F<Promise<B>, Promise<C>>> liftM2(final F<A, F<B, C>> f) {
        return Function.curry(new F2<Promise<A>, Promise<B>, Promise<C>>() { // from class: fj.control.parallel.Promise.8
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Promise) ((Promise) obj), (Promise) obj2);
            }

            public Promise<C> f(Promise<A> ca, Promise<B> cb) {
                return ca.bind(cb, F.this);
            }
        });
    }

    public static <A> Promise<List<A>> sequence(Strategy<Unit> s, List<Promise<A>> as) {
        return join((Promise) foldRight(s, liftM2(List.cons()), promise(s, P.p(List.nil()))).f(as));
    }

    public static <A> F<List<Promise<A>>, Promise<List<A>>> sequence(final Strategy<Unit> s) {
        return new F<List<Promise<A>>, Promise<List<A>>>() { // from class: fj.control.parallel.Promise.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Promise<List<A>> f(List<Promise<A>> as) {
                return Promise.sequence(Strategy.this, as);
            }
        };
    }

    public static <A> Promise<Stream<A>> sequence(Strategy<Unit> s, Stream<Promise<A>> as) {
        return join((Promise) foldRightS(s, Function.curry(new F2<Promise<A>, P1<Promise<Stream<A>>>, Promise<Stream<A>>>() { // from class: fj.control.parallel.Promise.10
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Promise) ((Promise) obj), (P1) ((P1) obj2));
            }

            public Promise<Stream<A>> f(Promise<A> o, final P1<Promise<Stream<A>>> p) {
                return (Promise<Stream<A>>) o.bind((F<A, Promise<Stream<A>>>) new F<A, Promise<Stream<A>>>() { // from class: fj.control.parallel.Promise.10.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((AnonymousClass1) obj);
                    }

                    @Override // fj.F
                    public Promise<Stream<A>> f(A a) {
                        return ((Promise) p._1()).fmap((F) Stream.cons_().f(a));
                    }
                });
            }
        }), promise(s, P.p(Stream.nil()))).f(as));
    }

    public static <A> F<List<Promise<A>>, Promise<List<A>>> sequenceS(final Strategy<Unit> s) {
        return new F<List<Promise<A>>, Promise<List<A>>>() { // from class: fj.control.parallel.Promise.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Promise<List<A>> f(List<Promise<A>> as) {
                return Promise.sequence(Strategy.this, as);
            }
        };
    }

    public static <A> Promise<P1<A>> sequence(Strategy<Unit> s, P1<Promise<A>> p) {
        return join(promise(s, p)).fmap(P.p1());
    }

    public static <A, B> F<List<A>, Promise<B>> foldRight(final Strategy<Unit> s, final F<A, F<B, B>> f, final B b) {
        return new F<List<A>, Promise<B>>() { // from class: fj.control.parallel.Promise.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Promise<B> f(List<A> as) {
                return as.isEmpty() ? Promise.promise(Strategy.this, P.p(b)) : (Promise) ((F) Promise.liftM2(f).f(Promise.promise(Strategy.this, P.p(as.head())))).f(Promise.join(Strategy.this, (P1) P1.curry(this).f(as.tail())));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.control.parallel.Promise$13  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/parallel/Promise$13.class */
    public static class AnonymousClass13 implements F<Stream<A>, Promise<B>> {
        final /* synthetic */ Strategy val$s;
        final /* synthetic */ Object val$b;
        final /* synthetic */ F val$f;

        AnonymousClass13(Strategy strategy, Object obj, F f) {
            this.val$s = strategy;
            this.val$b = obj;
            this.val$f = f;
        }

        @Override // fj.F
        public /* bridge */ /* synthetic */ Object f(Object obj) {
            return f((Stream) ((Stream) obj));
        }

        public Promise<B> f(final Stream<A> as) {
            return as.isEmpty() ? Promise.promise(this.val$s, P.p(this.val$b)) : (Promise) ((F) Promise.liftM2(this.val$f).f(Promise.promise(this.val$s, P.p(as.head())))).f(Promise.join(this.val$s, new P1<Promise<P1<B>>>() { // from class: fj.control.parallel.Promise.13.1
                @Override // fj.P1
                public Promise<P1<B>> _1() {
                    return AnonymousClass13.this.f((Stream) as.tail()._1()).fmap(P.p1());
                }
            }));
        }
    }

    public static <A, B> F<Stream<A>, Promise<B>> foldRightS(Strategy<Unit> s, F<A, F<P1<B>, B>> f, B b) {
        return new AnonymousClass13(s, b, f);
    }

    public A claim() {
        try {
            this.l.await();
            return this.v.some();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    public Option<A> claim(long timeout, TimeUnit unit) {
        try {
            if (this.l.await(timeout, unit)) {
                return this.v;
            }
            return Option.none();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    public boolean isFulfilled() {
        return this.v.isSome();
    }

    public <B> Promise<B> cobind(final F<Promise<A>, B> f) {
        return promise(this.s, new P1<B>() { // from class: fj.control.parallel.Promise.14
            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.P1
            public B _1() {
                return f.f(Promise.this);
            }
        });
    }

    public Promise<Promise<A>> cojoin() {
        return (Promise<Promise<A>>) cobind(Function.identity());
    }

    public <B> Stream<B> sequenceW(final Stream<F<Promise<A>, B>> fs) {
        if (fs.isEmpty()) {
            return Stream.nil();
        }
        return Stream.cons(fs.head().f(this), new P1<Stream<B>>() { // from class: fj.control.parallel.Promise.15
            @Override // fj.P1
            public Stream<B> _1() {
                return Promise.this.sequenceW(fs.tail()._1());
            }
        });
    }
}
