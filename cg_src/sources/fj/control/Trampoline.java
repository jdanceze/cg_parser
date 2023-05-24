package fj.control;

import fj.Bottom;
import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.F2Functions;
import fj.Function;
import fj.P1;
import fj.data.Either;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/Trampoline.class */
public abstract class Trampoline<A> {
    protected abstract <R> R fold(F<Normal<A>, R> f, F<Codense<A>, R> f2);

    public abstract <B> Trampoline<B> bind(F<A, Trampoline<B>> f);

    public abstract Either<P1<Trampoline<A>>, A> resume();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/Trampoline$Normal.class */
    public static abstract class Normal<A> extends Trampoline<A> {
        public abstract <R> R foldNormal(F<A, R> f, F<P1<Trampoline<A>>, R> f2);

        private Normal() {
        }

        @Override // fj.control.Trampoline
        public <B> Trampoline<B> bind(F<A, Trampoline<B>> f) {
            return codense(this, f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/Trampoline$Codense.class */
    public static final class Codense<A> extends Trampoline<A> {
        private final Normal<Object> sub;
        private final F<Object, Trampoline<A>> cont;

        private Codense(Normal<Object> t, F<Object, Trampoline<A>> k) {
            this.sub = t;
            this.cont = k;
        }

        @Override // fj.control.Trampoline
        public <R> R fold(F<Normal<A>, R> n, F<Codense<A>, R> gs) {
            return gs.f(this);
        }

        @Override // fj.control.Trampoline
        public <B> Trampoline<B> bind(final F<A, Trampoline<B>> f) {
            return codense(this.sub, new F<Object, Trampoline<B>>() { // from class: fj.control.Trampoline.Codense.1
                @Override // fj.F
                public Trampoline<B> f(final Object o) {
                    return Trampoline.suspend(new P1<Trampoline<B>>() { // from class: fj.control.Trampoline.Codense.1.1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // fj.P1
                        public Trampoline<B> _1() {
                            return ((Trampoline) Codense.this.cont.f(o)).bind(f);
                        }
                    });
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.control.Trampoline$Codense$2  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/Trampoline$Codense$2.class */
        public class AnonymousClass2 implements F<P1<Trampoline<Object>>, P1<Trampoline<A>>> {
            AnonymousClass2() {
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: fj.control.Trampoline$Codense$2$1  reason: invalid class name */
            /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/Trampoline$Codense$2$1.class */
            public class AnonymousClass1 implements F<Trampoline<Object>, Trampoline<A>> {
                AnonymousClass1() {
                }

                @Override // fj.F
                public Trampoline<A> f(Trampoline<Object> ot) {
                    return (Trampoline) ot.fold((F<Normal<Object>, Trampoline<A>>) new F<Normal<Object>, Trampoline<A>>() { // from class: fj.control.Trampoline.Codense.2.1.1
                        @Override // fj.F
                        public Trampoline<A> f(Normal<Object> o) {
                            return (Trampoline) o.foldNormal((F<Object, Trampoline<A>>) new F<Object, Trampoline<A>>() { // from class: fj.control.Trampoline.Codense.2.1.1.1
                                @Override // fj.F
                                public Trampoline<A> f(Object o2) {
                                    return (Trampoline) Codense.this.cont.f(o2);
                                }
                            }, (F<P1<Trampoline<Object>>, Trampoline<A>>) new F<P1<Trampoline<Object>>, Trampoline<A>>() { // from class: fj.control.Trampoline.Codense.2.1.1.2
                                @Override // fj.F
                                public Trampoline<A> f(P1<Trampoline<Object>> t) {
                                    return (Trampoline<A>) t._1().bind(Codense.this.cont);
                                }
                            });
                        }
                    }, (F<Codense<Object>, Trampoline<A>>) new F<Codense<Object>, Trampoline<A>>() { // from class: fj.control.Trampoline.Codense.2.1.2
                        @Override // fj.F
                        public Trampoline<A> f(final Codense<Object> c) {
                            return Trampoline.codense(((Codense) c).sub, new F<Object, Trampoline<A>>() { // from class: fj.control.Trampoline.Codense.2.1.2.1
                                @Override // fj.F
                                public Trampoline<A> f(Object o) {
                                    return ((Trampoline) c.cont.f(o)).bind(Codense.this.cont);
                                }
                            });
                        }
                    });
                }
            }

            @Override // fj.F
            public P1<Trampoline<A>> f(P1<Trampoline<Object>> p) {
                return (P1<Trampoline<A>>) p.map(new AnonymousClass1());
            }
        }

        @Override // fj.control.Trampoline
        public Either<P1<Trampoline<A>>, A> resume() {
            return Either.left(this.sub.resume().either(new AnonymousClass2(), (F<Object, P1<Trampoline<A>>>) new F<Object, P1<Trampoline<A>>>() { // from class: fj.control.Trampoline.Codense.3
                @Override // fj.F
                public P1<Trampoline<A>> f(final Object o) {
                    return new P1<Trampoline<A>>() { // from class: fj.control.Trampoline.Codense.3.1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // fj.P1
                        public Trampoline<A> _1() {
                            return (Trampoline) Codense.this.cont.f(o);
                        }
                    };
                }
            }));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/Trampoline$Suspend.class */
    public static final class Suspend<A> extends Normal<A> {
        private final P1<Trampoline<A>> suspension;

        private Suspend(P1<Trampoline<A>> s) {
            super();
            this.suspension = s;
        }

        @Override // fj.control.Trampoline.Normal
        public <R> R foldNormal(F<A, R> pure, F<P1<Trampoline<A>>, R> k) {
            return k.f(this.suspension);
        }

        @Override // fj.control.Trampoline
        public <R> R fold(F<Normal<A>, R> n, F<Codense<A>, R> gs) {
            return n.f(this);
        }

        @Override // fj.control.Trampoline
        public Either<P1<Trampoline<A>>, A> resume() {
            return Either.left(this.suspension);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/Trampoline$Pure.class */
    public static final class Pure<A> extends Normal<A> {
        private final A value;

        private Pure(A a) {
            super();
            this.value = a;
        }

        @Override // fj.control.Trampoline.Normal
        public <R> R foldNormal(F<A, R> pure, F<P1<Trampoline<A>>, R> k) {
            return pure.f(this.value);
        }

        @Override // fj.control.Trampoline
        public <R> R fold(F<Normal<A>, R> n, F<Codense<A>, R> gs) {
            return n.f(this);
        }

        @Override // fj.control.Trampoline
        public Either<P1<Trampoline<A>>, A> resume() {
            return Either.right(this.value);
        }
    }

    protected static <A, B> Codense<B> codense(Normal<A> a, F<A, Trampoline<B>> k) {
        return new Codense<>(a, k);
    }

    public static <A> F<A, Trampoline<A>> pure() {
        return new F<A, Trampoline<A>>() { // from class: fj.control.Trampoline.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public Trampoline<A> f(A a) {
                return Trampoline.pure(a);
            }
        };
    }

    public static <A> Trampoline<A> pure(A a) {
        return new Pure(a);
    }

    public static <A> Trampoline<A> suspend(P1<Trampoline<A>> a) {
        return new Suspend(a);
    }

    public static <A> F<P1<Trampoline<A>>, Trampoline<A>> suspend_() {
        return new F<P1<Trampoline<A>>, Trampoline<A>>() { // from class: fj.control.Trampoline.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((P1) ((P1) obj));
            }

            public Trampoline<A> f(P1<Trampoline<A>> trampolineP1) {
                return Trampoline.suspend(trampolineP1);
            }
        };
    }

    public final <B> Trampoline<B> map(F<A, B> f) {
        return bind(F1Functions.o(pure(), f));
    }

    public static <A, B> F<F<A, Trampoline<B>>, F<Trampoline<A>, Trampoline<B>>> bind_() {
        return new F<F<A, Trampoline<B>>, F<Trampoline<A>, Trampoline<B>>>() { // from class: fj.control.Trampoline.3
            @Override // fj.F
            public F<Trampoline<A>, Trampoline<B>> f(final F<A, Trampoline<B>> f) {
                return new F<Trampoline<A>, Trampoline<B>>() { // from class: fj.control.Trampoline.3.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Trampoline) ((Trampoline) obj));
                    }

                    public Trampoline<B> f(Trampoline<A> a) {
                        return a.bind(f);
                    }
                };
            }
        };
    }

    public static <A, B> F<F<A, B>, F<Trampoline<A>, Trampoline<B>>> map_() {
        return new F<F<A, B>, F<Trampoline<A>, Trampoline<B>>>() { // from class: fj.control.Trampoline.4
            @Override // fj.F
            public F<Trampoline<A>, Trampoline<B>> f(final F<A, B> f) {
                return new F<Trampoline<A>, Trampoline<B>>() { // from class: fj.control.Trampoline.4.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Trampoline) ((Trampoline) obj));
                    }

                    public Trampoline<B> f(Trampoline<A> a) {
                        return a.map(f);
                    }
                };
            }
        };
    }

    public static <A> F<Trampoline<A>, Either<P1<Trampoline<A>>, A>> resume_() {
        return new F<Trampoline<A>, Either<P1<Trampoline<A>>, A>>() { // from class: fj.control.Trampoline.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Trampoline) ((Trampoline) obj));
            }

            public Either<P1<Trampoline<A>>, A> f(Trampoline<A> aTrampoline) {
                return aTrampoline.resume();
            }
        };
    }

    public A run() {
        Iterator<A> it;
        Trampoline<A> current = this;
        do {
            Either<P1<Trampoline<A>>, A> x = current.resume();
            Iterator<P1<Trampoline<A>>> it2 = x.left().iterator();
            while (it2.hasNext()) {
                P1<Trampoline<A>> t = it2.next();
                current = t._1();
            }
            it = x.right().iterator();
        } while (!it.hasNext());
        A a = it.next();
        return a;
    }

    public final <B> Trampoline<B> apply(Trampoline<F<A, B>> lf) {
        return lf.bind(new F<F<A, B>, Trampoline<B>>() { // from class: fj.control.Trampoline.6
            @Override // fj.F
            public Trampoline<B> f(F<A, B> f) {
                return Trampoline.this.map(f);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <B, C> Trampoline<C> bind(Trampoline<B> lb, F<A, F<B, C>> f) {
        return (Trampoline<B>) lb.apply(map(f));
    }

    public static <A, B, C> F<Trampoline<A>, F<Trampoline<B>, Trampoline<C>>> liftM2(final F<A, F<B, C>> f) {
        return Function.curry(new F2<Trampoline<A>, Trampoline<B>, Trampoline<C>>() { // from class: fj.control.Trampoline.7
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((Trampoline) ((Trampoline) obj), (Trampoline) obj2);
            }

            public Trampoline<C> f(Trampoline<A> as, Trampoline<B> bs) {
                return as.bind(bs, F.this);
            }
        });
    }

    public <B, C> Trampoline<C> zipWith(Trampoline<B> b, final F2<A, B, C> f) {
        Either<P1<Trampoline<A>>, A> ea = resume();
        Either<P1<Trampoline<B>>, B> eb = b.resume();
        Iterator<P1<Trampoline<A>>> it = ea.left().iterator();
        while (it.hasNext()) {
            P1<Trampoline<A>> x = it.next();
            Iterator<P1<Trampoline<B>>> it2 = eb.left().iterator();
            if (it2.hasNext()) {
                return suspend(x.bind(it2.next(), F2Functions.curry(new F2<Trampoline<A>, Trampoline<B>, Trampoline<C>>() { // from class: fj.control.Trampoline.8
                    @Override // fj.F2
                    public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                        return f((Trampoline) ((Trampoline) obj), (Trampoline) obj2);
                    }

                    public Trampoline<C> f(final Trampoline<A> ta, final Trampoline<B> tb) {
                        return Trampoline.suspend(new P1<Trampoline<C>>() { // from class: fj.control.Trampoline.8.1
                            @Override // fj.P1
                            public Trampoline<C> _1() {
                                return ta.zipWith(tb, f);
                            }
                        });
                    }
                })));
            }
            Iterator<B> it3 = eb.right().iterator();
            if (it3.hasNext()) {
                final B y = it3.next();
                return suspend(x.map((F<Trampoline<A>, Trampoline<C>>) new F<Trampoline<A>, Trampoline<C>>() { // from class: fj.control.Trampoline.9
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Trampoline) ((Trampoline) obj));
                    }

                    public Trampoline<C> f(Trampoline<A> ta) {
                        return ta.map(F2Functions.f(F2Functions.flip(f), y));
                    }
                }));
            }
        }
        Iterator<A> it4 = ea.right().iterator();
        while (it4.hasNext()) {
            final A x2 = it4.next();
            Iterator<B> it5 = eb.right().iterator();
            if (it5.hasNext()) {
                final B y2 = it5.next();
                return suspend(new P1<Trampoline<C>>() { // from class: fj.control.Trampoline.10
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // fj.P1
                    public Trampoline<C> _1() {
                        return Trampoline.pure(f.f(x2, y2));
                    }
                });
            }
            Iterator<P1<Trampoline<B>>> it6 = eb.left().iterator();
            if (it6.hasNext()) {
                P1<Trampoline<B>> y3 = it6.next();
                return suspend(y3.map((F) liftM2(F2Functions.curry(f)).f(pure(x2))));
            }
        }
        throw Bottom.error("Match error: Trampoline is neither done nor suspended.");
    }
}
