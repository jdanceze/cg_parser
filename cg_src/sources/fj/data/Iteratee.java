package fj.data;

import fj.F;
import fj.F1Functions;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Unit;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee.class */
public final class Iteratee {

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee$Input.class */
    public static abstract class Input<E> {
        public abstract <Z> Z apply(P1<Z> p1, P1<F<E, Z>> p12, P1<Z> p13);

        Input() {
        }

        public static final <E> Input<E> empty() {
            return new Input<E>() { // from class: fj.data.Iteratee.Input.1
                @Override // fj.data.Iteratee.Input
                public <Z> Z apply(P1<Z> empty, P1<F<E, Z>> el, P1<Z> eof) {
                    return empty._1();
                }
            };
        }

        public static final <E> Input<E> eof() {
            return new Input<E>() { // from class: fj.data.Iteratee.Input.2
                @Override // fj.data.Iteratee.Input
                public <Z> Z apply(P1<Z> empty, P1<F<E, Z>> el, P1<Z> eof) {
                    return eof._1();
                }
            };
        }

        public static final <E> Input<E> el(final E element) {
            return new Input<E>() { // from class: fj.data.Iteratee.Input.3
                @Override // fj.data.Iteratee.Input
                public <Z> Z apply(P1<Z> empty, P1<F<E, Z>> el, P1<Z> eof) {
                    return el._1().f((E) element);
                }
            };
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee$IterV.class */
    public static abstract class IterV<E, A> {
        public abstract <Z> Z fold(F<P2<A, Input<E>>, Z> f, F<F<Input<E>, IterV<E, A>>, Z> f2);

        IterV() {
        }

        public static <E, A> IterV<E, A> cont(final F<Input<E>, IterV<E, A>> f) {
            return new IterV<E, A>() { // from class: fj.data.Iteratee.IterV.1
                @Override // fj.data.Iteratee.IterV
                public <Z> Z fold(F<P2<A, Input<E>>, Z> done, F<F<Input<E>, IterV<E, A>>, Z> cont) {
                    return cont.f(F.this);
                }
            };
        }

        public static <E, A> IterV<E, A> done(A a, Input<E> i) {
            final P2<A, Input<E>> p = P.p(a, i);
            return new IterV<E, A>() { // from class: fj.data.Iteratee.IterV.2
                @Override // fj.data.Iteratee.IterV
                public <Z> Z fold(F<P2<A, Input<E>>, Z> done, F<F<Input<E>, IterV<E, A>>, Z> cont) {
                    return done.f(P2.this);
                }
            };
        }

        public final A run() {
            final F<IterV<E, A>, Option<A>> runCont = new F<IterV<E, A>, Option<A>>() { // from class: fj.data.Iteratee.IterV.3
                final F<P2<A, Input<E>>, Option<A>> done = F1Functions.andThen(P2.__1(), Option.some_());
                final F<F<Input<E>, IterV<E, A>>, Option<A>> cont = Function.constant(Option.none());

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((IterV) ((IterV) obj));
                }

                public Option<A> f(IterV<E, A> i) {
                    return (Option) i.fold((F<P2<A, Input<E>>, Option<A>>) this.done, (F<F<Input<E>, IterV<E, A>>, Option<A>>) this.cont);
                }
            };
            return (A) fold(P2.__1(), (F<F<Input<E>, IterV<E, A>>, A>) new F<F<Input<E>, IterV<E, A>>, A>() { // from class: fj.data.Iteratee.IterV.4
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((F<Input<E>, IterV<E, Object>>) obj);
                }

                public A f(F<Input<E>, IterV<E, A>> k) {
                    return (A) ((Option) runCont.f(k.f(Input.eof()))).valueE("diverging iteratee");
                }
            });
        }

        public final <B> IterV<E, B> bind(final F<A, IterV<E, B>> f) {
            return (IterV) fold((F<P2<A, Input<E>>, IterV<E, B>>) new F<P2<A, Input<E>>, IterV<E, B>>() { // from class: fj.data.Iteratee.IterV.5
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((P2) ((P2) obj));
                }

                public IterV<E, B> f(P2<A, Input<E>> xe) {
                    final Input<E> e = xe._2();
                    Object obj = new F<P2<B, Input<E>>, IterV<E, B>>() { // from class: fj.data.Iteratee.IterV.5.1
                        @Override // fj.F
                        public IterV<E, B> f(P2<B, Input<E>> y_) {
                            return IterV.done(y_._1(), e);
                        }
                    };
                    Object obj2 = new F<F<Input<E>, IterV<E, B>>, IterV<E, B>>() { // from class: fj.data.Iteratee.IterV.5.2
                        @Override // fj.F
                        public IterV<E, B> f(F<Input<E>, IterV<E, B>> k) {
                            return (IterV) k.f(e);
                        }
                    };
                    A x = xe._1();
                    return (IterV) ((IterV) f.f(x)).fold(obj, obj2);
                }
            }, (F<F<Input<E>, IterV<E, A>>, IterV<E, B>>) new F<F<Input<E>, IterV<E, A>>, IterV<E, B>>() { // from class: fj.data.Iteratee.IterV.6
                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((F) ((F) obj));
                }

                public IterV<E, B> f(final F<Input<E>, IterV<E, A>> k) {
                    return IterV.cont(new F<Input<E>, IterV<E, B>>() { // from class: fj.data.Iteratee.IterV.6.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((Input) ((Input) obj));
                        }

                        public IterV<E, B> f(Input<E> e) {
                            return ((IterV) k.f(e)).bind(f);
                        }
                    });
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.data.Iteratee$IterV$7  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee$IterV$7.class */
        public static class AnonymousClass7 implements F<Integer, F<Input<E>, IterV<E, Integer>>> {
            final F<Integer, F<Input<E>, IterV<E, Integer>>> step = this;

            AnonymousClass7() {
            }

            @Override // fj.F
            public F<Input<E>, IterV<E, Integer>> f(final Integer acc) {
                final P1<IterV<E, Integer>> empty = new P1<IterV<E, Integer>>() { // from class: fj.data.Iteratee.IterV.7.1
                    @Override // fj.P1
                    public IterV<E, Integer> _1() {
                        return IterV.cont(AnonymousClass7.this.step.f(acc));
                    }
                };
                final P1<F<E, IterV<E, Integer>>> el = new P1<F<E, IterV<E, Integer>>>() { // from class: fj.data.Iteratee.IterV.7.2
                    @Override // fj.P1
                    public F<E, IterV<E, Integer>> _1() {
                        return P.p(IterV.cont(AnonymousClass7.this.step.f(Integer.valueOf(acc.intValue() + 1)))).constant();
                    }
                };
                final P1<IterV<E, Integer>> eof = new P1<IterV<E, Integer>>() { // from class: fj.data.Iteratee.IterV.7.3
                    @Override // fj.P1
                    public IterV<E, Integer> _1() {
                        return IterV.done(acc, Input.eof());
                    }
                };
                return new F<Input<E>, IterV<E, Integer>>() { // from class: fj.data.Iteratee.IterV.7.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Input) ((Input) obj));
                    }

                    public IterV<E, Integer> f(Input<E> s) {
                        return (IterV) s.apply(empty, el, eof);
                    }
                };
            }
        }

        public static final <E> IterV<E, Integer> length() {
            F<Integer, F<Input<E>, IterV<E, Integer>>> step = new AnonymousClass7();
            return cont(step.f(0));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.data.Iteratee$IterV$8  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee$IterV$8.class */
        public static class AnonymousClass8 implements F<Input<E>, IterV<E, Unit>> {
            final F<Input<E>, IterV<E, Unit>> step = this;
            final P1<IterV<E, Unit>> empty = new P1<IterV<E, Unit>>() { // from class: fj.data.Iteratee.IterV.8.1
                @Override // fj.P1
                public IterV<E, Unit> _1() {
                    return IterV.cont(AnonymousClass8.this.step);
                }
            };
            final P1<F<E, IterV<E, Unit>>> el = new P1<F<E, IterV<E, Unit>>>() { // from class: fj.data.Iteratee.IterV.8.2
                @Override // fj.P1
                public F<E, IterV<E, Unit>> _1() {
                    return P.p(IterV.drop(AnonymousClass8.this.val$n - 1)).constant();
                }
            };
            final P1<IterV<E, Unit>> eof = new P1<IterV<E, Unit>>() { // from class: fj.data.Iteratee.IterV.8.3
                @Override // fj.P1
                public IterV<E, Unit> _1() {
                    return IterV.done(Unit.unit(), Input.eof());
                }
            };
            final /* synthetic */ int val$n;

            AnonymousClass8(int i) {
                this.val$n = i;
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Input) ((Input) obj));
            }

            public IterV<E, Unit> f(Input<E> s) {
                return (IterV) s.apply((P1<IterV<E, Unit>>) this.empty, (P1<F<E, IterV<E, Unit>>>) this.el, (P1<IterV<E, Unit>>) this.eof);
            }
        }

        public static final <E> IterV<E, Unit> drop(int n) {
            F<Input<E>, IterV<E, Unit>> step = new AnonymousClass8(n);
            if (n == 0) {
                return done(Unit.unit(), Input.empty());
            }
            return cont(step);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.data.Iteratee$IterV$9  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee$IterV$9.class */
        public static class AnonymousClass9 implements F<Input<E>, IterV<E, Option<E>>> {
            final F<Input<E>, IterV<E, Option<E>>> step = this;
            final P1<IterV<E, Option<E>>> empty = new P1<IterV<E, Option<E>>>() { // from class: fj.data.Iteratee.IterV.9.1
                @Override // fj.P1
                public IterV<E, Option<E>> _1() {
                    return IterV.cont(AnonymousClass9.this.step);
                }
            };
            final P1<F<E, IterV<E, Option<E>>>> el = new P1<F<E, IterV<E, Option<E>>>>() { // from class: fj.data.Iteratee.IterV.9.2
                @Override // fj.P1
                public F<E, IterV<E, Option<E>>> _1() {
                    return new F<E, IterV<E, Option<E>>>() { // from class: fj.data.Iteratee.IterV.9.2.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((AnonymousClass1) obj);
                        }

                        @Override // fj.F
                        public IterV<E, Option<E>> f(E e) {
                            return IterV.done(Option.some(e), Input.empty());
                        }
                    };
                }
            };
            final P1<IterV<E, Option<E>>> eof = new P1<IterV<E, Option<E>>>() { // from class: fj.data.Iteratee.IterV.9.3
                @Override // fj.P1
                public IterV<E, Option<E>> _1() {
                    return IterV.done(Option.none(), Input.eof());
                }
            };

            AnonymousClass9() {
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Input) ((Input) obj));
            }

            public IterV<E, Option<E>> f(Input<E> s) {
                return (IterV) s.apply((P1<IterV<E, Option<E>>>) this.empty, (P1<F<E, IterV<E, Option<E>>>>) this.el, (P1<IterV<E, Option<E>>>) this.eof);
            }
        }

        public static final <E> IterV<E, Option<E>> head() {
            F<Input<E>, IterV<E, Option<E>>> step = new AnonymousClass9();
            return cont(step);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.data.Iteratee$IterV$10  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee$IterV$10.class */
        public static class AnonymousClass10 implements F<Input<E>, IterV<E, Option<E>>> {
            final F<Input<E>, IterV<E, Option<E>>> step = this;
            final P1<IterV<E, Option<E>>> empty = new P1<IterV<E, Option<E>>>() { // from class: fj.data.Iteratee.IterV.10.1
                @Override // fj.P1
                public IterV<E, Option<E>> _1() {
                    return IterV.cont(AnonymousClass10.this.step);
                }
            };
            final P1<F<E, IterV<E, Option<E>>>> el = new P1<F<E, IterV<E, Option<E>>>>() { // from class: fj.data.Iteratee.IterV.10.2
                @Override // fj.P1
                public F<E, IterV<E, Option<E>>> _1() {
                    return new F<E, IterV<E, Option<E>>>() { // from class: fj.data.Iteratee.IterV.10.2.1
                        @Override // fj.F
                        public /* bridge */ /* synthetic */ Object f(Object obj) {
                            return f((AnonymousClass1) obj);
                        }

                        @Override // fj.F
                        public IterV<E, Option<E>> f(E e) {
                            return IterV.done(Option.some(e), Input.el(e));
                        }
                    };
                }
            };
            final P1<IterV<E, Option<E>>> eof = new P1<IterV<E, Option<E>>>() { // from class: fj.data.Iteratee.IterV.10.3
                @Override // fj.P1
                public IterV<E, Option<E>> _1() {
                    return IterV.done(Option.none(), Input.eof());
                }
            };

            AnonymousClass10() {
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Input) ((Input) obj));
            }

            public IterV<E, Option<E>> f(Input<E> s) {
                return (IterV) s.apply((P1<IterV<E, Option<E>>>) this.empty, (P1<F<E, IterV<E, Option<E>>>>) this.el, (P1<IterV<E, Option<E>>>) this.eof);
            }
        }

        public static final <E> IterV<E, Option<E>> peek() {
            F<Input<E>, IterV<E, Option<E>>> step = new AnonymousClass10();
            return cont(step);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: fj.data.Iteratee$IterV$11  reason: invalid class name */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Iteratee$IterV$11.class */
        public static class AnonymousClass11 implements F<List<E>, F<Input<E>, IterV<E, List<E>>>> {
            final F<List<E>, F<Input<E>, IterV<E, List<E>>>> step = this;

            AnonymousClass11() {
            }

            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public F<Input<E>, IterV<E, List<E>>> f(final List<E> acc) {
                final P1<IterV<E, List<E>>> empty = new P1<IterV<E, List<E>>>() { // from class: fj.data.Iteratee.IterV.11.1
                    @Override // fj.P1
                    public IterV<E, List<E>> _1() {
                        return IterV.cont(AnonymousClass11.this.step.f(acc));
                    }
                };
                final P1<F<E, IterV<E, List<E>>>> el = new P1<F<E, IterV<E, List<E>>>>() { // from class: fj.data.Iteratee.IterV.11.2
                    @Override // fj.P1
                    public F<E, IterV<E, List<E>>> _1() {
                        return new F<E, IterV<E, List<E>>>() { // from class: fj.data.Iteratee.IterV.11.2.1
                            @Override // fj.F
                            public /* bridge */ /* synthetic */ Object f(Object obj) {
                                return f((AnonymousClass1) obj);
                            }

                            @Override // fj.F
                            public IterV<E, List<E>> f(E e) {
                                return IterV.cont(AnonymousClass11.this.step.f(acc.cons((List) e)));
                            }
                        };
                    }
                };
                final P1<IterV<E, List<E>>> eof = new P1<IterV<E, List<E>>>() { // from class: fj.data.Iteratee.IterV.11.3
                    @Override // fj.P1
                    public IterV<E, List<E>> _1() {
                        return IterV.done(acc, Input.eof());
                    }
                };
                return new F<Input<E>, IterV<E, List<E>>>() { // from class: fj.data.Iteratee.IterV.11.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Input) ((Input) obj));
                    }

                    public IterV<E, List<E>> f(Input<E> s) {
                        return (IterV) s.apply(empty, el, eof);
                    }
                };
            }
        }

        public static final <E> IterV<E, List<E>> list() {
            F<List<E>, F<Input<E>, IterV<E, List<E>>>> step = new AnonymousClass11();
            return cont(step.f(List.nil()));
        }
    }

    private Iteratee() {
        throw new UnsupportedOperationException();
    }
}
