package fj.data.fingertrees;

import fj.F;
import fj.Function;
import fj.P2;
import fj.data.List;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Deep.class */
public final class Deep<V, A> extends FingerTree<V, A> {
    private final V v;
    private final Digit<V, A> prefix;
    private final FingerTree<V, Node<V, A>> middle;
    private final Digit<V, A> suffix;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Deep(Measured<V, A> m, V v, Digit<V, A> prefix, FingerTree<V, Node<V, A>> middle, Digit<V, A> suffix) {
        super(m);
        this.v = v;
        this.prefix = prefix;
        this.middle = middle;
        this.suffix = suffix;
    }

    public Digit<V, A> prefix() {
        return this.prefix;
    }

    public FingerTree<V, Node<V, A>> middle() {
        return this.middle;
    }

    public Digit<V, A> suffix() {
        return this.suffix;
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B foldRight(F<A, F<B, B>> aff, B z) {
        return (B) this.prefix.foldRight(aff, this.middle.foldRight((F<Node<V, A>, F<F<Node<V, A>, F<B, B>>, F<Node<V, A>, F<B, B>>>>) Function.flip(Node.foldRight_(aff)), (F<Node<V, A>, F<B, B>>) this.suffix.foldRight(aff, z)));
    }

    @Override // fj.data.fingertrees.FingerTree
    public A reduceRight(F<A, F<A, A>> aff) {
        return (A) this.prefix.foldRight(aff, this.middle.foldRight((F<Node<V, A>, F<F<Node<V, A>, F<B, B>>, F<Node<V, A>, F<B, B>>>>) Function.flip(Node.foldRight_(aff)), (F<Node<V, A>, F<B, B>>) this.suffix.reduceRight(aff)));
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B foldLeft(F<B, F<A, B>> bff, B z) {
        return (B) this.suffix.foldLeft(bff, this.middle.foldLeft((F<F<B, F<Node<V, A>, B>>, F<Node<V, A>, F<B, F<Node<V, A>, B>>>>) Node.foldLeft_(bff), (F<B, F<Node<V, A>, B>>) this.prefix.foldLeft(bff, z)));
    }

    @Override // fj.data.fingertrees.FingerTree
    public A reduceLeft(F<A, F<A, A>> aff) {
        return (A) this.suffix.foldLeft(aff, this.middle.foldLeft((F<F<B, F<Node<V, A>, B>>, F<Node<V, A>, F<B, F<Node<V, A>, B>>>>) Node.foldLeft_(aff), (F<B, F<Node<V, A>, B>>) this.prefix.reduceLeft(aff)));
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> FingerTree<V, B> map(F<A, B> abf, Measured<V, B> m) {
        return new Deep(m, this.v, this.prefix.map(abf, m), this.middle.map(Node.liftM(abf, m), m.nodeMeasured()), this.suffix.map(abf, m));
    }

    @Override // fj.data.fingertrees.FingerTree
    public V measure() {
        return this.v;
    }

    @Override // fj.data.fingertrees.FingerTree
    public <B> B match(F<Empty<V, A>, B> empty, F<Single<V, A>, B> single, F<Deep<V, A>, B> deep) {
        return deep.f(this);
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> cons(final A a) {
        final Measured<V, A> m = measured();
        final V measure = m.sum(m.measure(a), this.v);
        final MakeTree<V, A> mk = mkTree(m);
        return (FingerTree) this.prefix.match((F<One<V, A>, FingerTree<V, A>>) new F<One<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(One<V, A> one) {
                return new Deep(m, measure, mk.two(a, one.value()), Deep.this.middle, Deep.this.suffix);
            }
        }, (F<Two<V, A>, FingerTree<V, A>>) new F<Two<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(Two<V, A> two) {
                return new Deep(m, measure, mk.three(a, two.values()._1(), two.values()._2()), Deep.this.middle, Deep.this.suffix);
            }
        }, (F<Three<V, A>, FingerTree<V, A>>) new F<Three<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(Three<V, A> three) {
                return new Deep(m, measure, mk.four(a, three.values()._1(), three.values()._2(), three.values()._3()), Deep.this.middle, Deep.this.suffix);
            }
        }, (F<Four<V, A>, FingerTree<V, A>>) new F<Four<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(Four<V, A> four) {
                return new Deep(m, measure, mk.two(a, four.values()._1()), Deep.this.middle.cons(mk.node3(four.values()._2(), four.values()._3(), four.values()._4())), Deep.this.suffix);
            }
        });
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> snoc(final A a) {
        final Measured<V, A> m = measured();
        final V measure = m.sum(m.measure(a), this.v);
        final MakeTree<V, A> mk = mkTree(m);
        return (FingerTree) this.suffix.match((F<One<V, A>, FingerTree<V, A>>) new F<One<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(One<V, A> one) {
                return new Deep(m, measure, Deep.this.prefix, Deep.this.middle, mk.two(one.value(), a));
            }
        }, (F<Two<V, A>, FingerTree<V, A>>) new F<Two<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(Two<V, A> two) {
                return new Deep(m, measure, Deep.this.prefix, Deep.this.middle, mk.three(two.values()._1(), two.values()._2(), a));
            }
        }, (F<Three<V, A>, FingerTree<V, A>>) new F<Three<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(Three<V, A> three) {
                return new Deep(m, measure, Deep.this.prefix, Deep.this.middle, mk.four(three.values()._1(), three.values()._2(), three.values()._3(), a));
            }
        }, (F<Four<V, A>, FingerTree<V, A>>) new F<Four<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(Four<V, A> four) {
                return new Deep(m, measure, Deep.this.prefix, Deep.this.middle.snoc(mk.node3(four.values()._1(), four.values()._2(), four.values()._3())), mk.two(four.values()._4(), a));
            }
        });
    }

    @Override // fj.data.fingertrees.FingerTree
    public FingerTree<V, A> append(final FingerTree<V, A> t) {
        final Measured<V, A> m = measured();
        return (FingerTree) t.match(Function.constant(t), (F<Single<V, A>, FingerTree<V, A>>) new F<Single<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Single) ((Single) obj));
            }

            public FingerTree<V, A> f(Single<V, A> single) {
                return t.snoc(single.value());
            }
        }, (F<Deep<V, A>, FingerTree<V, A>>) new F<Deep<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Deep.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Deep) ((Deep) obj));
            }

            /* JADX WARN: Multi-variable type inference failed */
            public FingerTree<V, A> f(Deep<V, A> deep) {
                return new Deep(m, m.sum(Deep.this.measure(), deep.measure()), Deep.this.prefix, Deep.addDigits0(m, Deep.this.middle, Deep.this.suffix, ((Deep) deep).prefix, ((Deep) deep).middle), ((Deep) deep).suffix);
            }
        });
    }

    @Override // fj.data.fingertrees.FingerTree
    public P2<Integer, A> lookup(F<V, Integer> o, int i) {
        int spr = o.f(this.prefix.measure()).intValue();
        int spm = o.f(this.middle.measure()).intValue();
        if (i >= spr && i < spm) {
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, A>> addDigits0(final Measured<V, A> m, final FingerTree<V, Node<V, A>> m1, Digit<V, A> s1, final Digit<V, A> p2, final FingerTree<V, Node<V, A>> m2) {
        final MakeTree<V, A> mk = mkTree(m);
        return (FingerTree) s1.match((F<One<V, A>, FingerTree<V, Node<V, A>>>) new F<One<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            public FingerTree<V, Node<V, A>> f(final One<V, A> one1) {
                return (FingerTree) Digit.this.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.11.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(One<V, A> one2) {
                        return Deep.append1(m, m1, mk.node2(one1.value(), one2.value()), m2);
                    }
                }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.11.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Two<V, A> two2) {
                        V2<A> vs = two2.values();
                        return Deep.append1(m, m1, mk.node3(one1.value(), vs._1(), vs._2()), m2);
                    }
                }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.11.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Three<V, A> three) {
                        V3<A> vs = three.values();
                        return Deep.append2(m, m1, mk.node2(one1.value(), vs._1()), mk.node2(vs._2(), vs._3()), m2);
                    }
                }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.11.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Four<V, A> four) {
                        V4<A> vs = four.values();
                        return Deep.append2(m, m1, mk.node3(one1.value(), vs._1(), vs._2()), mk.node2(vs._3(), vs._4()), m2);
                    }
                });
            }
        }, (F<Two<V, A>, FingerTree<V, Node<V, A>>>) new F<Two<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            public FingerTree<V, Node<V, A>> f(Two<V, A> two1) {
                final V2<A> v1 = two1.values();
                return (FingerTree) Digit.this.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.12.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(One<V, A> one) {
                        return Deep.append1(m, m1, mk.node3(v1._1(), v1._2(), one.value()), m2);
                    }
                }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.12.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Two<V, A> two2) {
                        V2<A> v2 = two2.values();
                        return Deep.append2(m, m1, mk.node2(v1._1(), v1._2()), mk.node2(v2._1(), v2._2()), m2);
                    }
                }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.12.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Three<V, A> three) {
                        V3<A> v2 = three.values();
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.12.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Four<V, A> four) {
                        V4<A> v2 = four.values();
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Three<V, A>, FingerTree<V, Node<V, A>>>) new F<Three<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.13
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            public FingerTree<V, Node<V, A>> f(Three<V, A> three1) {
                final V3<A> v1 = three1.values();
                return (FingerTree) Digit.this.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.13.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(One<V, A> one) {
                        return Deep.append2(m, m1, mk.node2(v1._1(), v1._2()), mk.node2(v1._3(), one.value()), m2);
                    }
                }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.13.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Two<V, A> two) {
                        V2<A> v2 = two.values();
                        return Deep.append2(m, m1, mk.node3(v1), mk.node2(v2), m2);
                    }
                }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.13.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Three<V, A> three2) {
                        return Deep.append2(m, m1, mk.node3(v1), mk.node3(three2.values()), m2);
                    }
                }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.13.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Four<V, A> four) {
                        return Deep.append3(m, m1, mk.node3(v1), mk.node2(four.values()._1(), four.values()._2()), mk.node2(four.values()._3(), four.values()._4()), m2);
                    }
                });
            }
        }, (F<Four<V, A>, FingerTree<V, Node<V, A>>>) new F<Four<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            public FingerTree<V, Node<V, A>> f(Four<V, A> four1) {
                final V4<A> v1 = four1.values();
                return (FingerTree) Digit.this.match(new F<One<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.14.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(One<V, A> one) {
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), one.value()), m2);
                    }
                }, new F<Two<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.14.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Two<V, A> two) {
                        V2<A> v2 = two.values();
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), v2._1(), v2._2()), m2);
                    }
                }, new F<Three<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.14.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Three<V, A> three) {
                        V3<A> v2 = three.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, A>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.14.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Four<V, A> four2) {
                        V4<A> v2 = four2.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, A>> append1(final Measured<V, A> m, final FingerTree<V, Node<V, A>> xs, final Node<V, A> a, final FingerTree<V, Node<V, A>> ys) {
        return (FingerTree) xs.match((F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Empty) ((Empty) obj));
            }

            public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                return FingerTree.this.cons(a);
            }
        }, (F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Single) ((Single) obj));
            }

            public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                return FingerTree.this.cons(a).cons(single.value());
            }
        }, (F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.17
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Deep) ((Deep) obj));
            }

            public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep1) {
                return (FingerTree) FingerTree.this.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.17.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Empty) ((Empty) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                        return xs.snoc(a);
                    }
                }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.17.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Single) ((Single) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                        return xs.snoc(a).snoc(single.value());
                    }
                }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.17.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Deep) ((Deep) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                        Measured<V, Node<V, A>> nm = m.nodeMeasured();
                        return new Deep(nm, m.sum(m.sum(deep1.v, nm.measure(a)), ((Deep) deep2).v), deep1.prefix, Deep.addDigits1(nm, deep1.middle, deep1.suffix, a, ((Deep) deep2).prefix, ((Deep) deep2).middle), ((Deep) deep2).suffix);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits1(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> x, final Node<V, A> n, final Digit<V, Node<V, A>> y, final FingerTree<V, Node<V, Node<V, A>>> m2) {
        final MakeTree<V, Node<V, A>> mk = mkTree(m);
        return (FingerTree) x.match((F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.18
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one1) {
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.18.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                        return Deep.append1(m, m1, mk.node3(one1.value(), n, one2.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.18.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append2(m, m1, mk.node2(one1.value(), n), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.18.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append2(m, m1, mk.node3(one1.value(), n, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.18.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append2(m, m1, mk.node3(one1.value(), n, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.19
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two1) {
                final V2<Node<V, A>> v1 = two1.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.19.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append2(m, m1, mk.node2(v1), mk.node2(n, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.19.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.19.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n), mk.node3(three.values()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.19.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.20
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                final V3<Node<V, A>> v1 = three.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.20.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append2(m, m1, mk.node3(v1), mk.node2(n, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.20.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        V2<Node<V, A>> v2 = two.values();
                        return Deep.append2(m, m1, mk.node3(v1), mk.node3(n, v2._1(), v2._2()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.20.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three2) {
                        V3<Node<V, A>> v2 = three2.values();
                        return Deep.append3(m, m1, mk.node3(v1), mk.node2(n, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.20.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(v1), mk.node3(n, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.21
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                final V4<Node<V, A>> v1 = four.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.21.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.21.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), n), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.21.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.21.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four2) {
                        V4<Node<V, A>> v2 = four2.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, A>> append2(final Measured<V, A> m, FingerTree<V, Node<V, A>> t1, final Node<V, A> n1, final Node<V, A> n2, final FingerTree<V, Node<V, A>> t2) {
        return (FingerTree) t1.match((F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.22
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Empty) ((Empty) obj));
            }

            public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                return FingerTree.this.cons(n2).cons(n1);
            }
        }, (F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.23
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Single) ((Single) obj));
            }

            public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                return FingerTree.this.cons(n2).cons(n1).cons(single.value());
            }
        }, (F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.24
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Deep) ((Deep) obj));
            }

            public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep) {
                return (FingerTree) FingerTree.this.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.24.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Empty) ((Empty) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                        return deep.snoc(n1).snoc(n2);
                    }
                }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.24.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Single) ((Single) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                        return deep.snoc(n1).snoc(n2).snoc(single.value());
                    }
                }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.24.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Deep) ((Deep) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                        return new Deep(m.nodeMeasured(), m.sum(m.sum(m.sum(deep.measure(), n1.measure()), n2.measure()), deep2.measure()), deep.prefix, Deep.addDigits2(m.nodeMeasured(), deep.middle, deep.suffix, n1, n2, ((Deep) deep2).prefix, ((Deep) deep2).middle), ((Deep) deep2).suffix);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits2(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> suffix, final Node<V, A> n1, final Node<V, A> n2, final Digit<V, Node<V, A>> prefix, final FingerTree<V, Node<V, Node<V, A>>> m2) {
        final MakeTree<V, Node<V, A>> mk = mkTree(m);
        return (FingerTree) suffix.match((F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.25
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one) {
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.25.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                        return Deep.append2(m, m1, mk.node2(one.value(), n1), mk.node2(n2, one2.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.25.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.25.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node3(three.values()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.25.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.26
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                final V2<Node<V, A>> v1 = two.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.26.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.26.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two2) {
                        V2<Node<V, A>> v2 = two2.values();
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, v2._1(), v2._2()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.26.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.26.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.27
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                final V3<Node<V, A>> v1 = three.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.27.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append2(m, m1, mk.node3(v1), mk.node3(n1, n2, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.27.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append3(m, m1, mk.node3(v1), mk.node2(n1, n2), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.27.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three2) {
                        V3<Node<V, A>> v2 = three2.values();
                        return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.27.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.28
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                final V4<Node<V, A>> v1 = four.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.28.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node2(v1._4(), n1), mk.node2(n2, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.28.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.28.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(three.values()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.28.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four2) {
                        V4<Node<V, A>> v2 = four2.values();
                        return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, A>> append3(Measured<V, A> m, FingerTree<V, Node<V, A>> t1, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final FingerTree<V, Node<V, A>> t2) {
        final Measured<V, Node<V, A>> nm = m.nodeMeasured();
        return (FingerTree) t1.match((F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.29
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Empty) ((Empty) obj));
            }

            public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                return FingerTree.this.cons(n3).cons(n2).cons(n1);
            }
        }, (F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.30
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Single) ((Single) obj));
            }

            public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                return FingerTree.this.cons(n3).cons(n2).cons(n1).cons(single.value());
            }
        }, (F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.31
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Deep) ((Deep) obj));
            }

            public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep) {
                return (FingerTree) FingerTree.this.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.31.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Empty) ((Empty) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                        return deep.snoc(n1).snoc(n2).snoc(n3);
                    }
                }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.31.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Single) ((Single) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                        return deep.snoc(n1).snoc(n2).snoc(n3).snoc(single.value());
                    }
                }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.31.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Deep) ((Deep) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                        return new Deep(nm, nm.monoid().sumLeft(List.list(deep.v, n1.measure(), n2.measure(), n3.measure(), ((Deep) deep2).v)), deep.prefix, Deep.addDigits3(nm, deep.middle, deep.suffix, n1, n2, n3, ((Deep) deep2).prefix, ((Deep) deep2).middle), ((Deep) deep2).suffix);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits3(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> suffix, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final Digit<V, Node<V, A>> prefix, final FingerTree<V, Node<V, Node<V, A>>> m2) {
        final MakeTree<V, Node<V, A>> mk = mkTree(m);
        return (FingerTree) suffix.match((F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.32
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one) {
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.32.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                        return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node2(n3, one2.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.32.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        V2<Node<V, A>> v2 = two.values();
                        return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, v2._1(), v2._2()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.32.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node2(n3, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.32.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.33
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                final V2<Node<V, A>> v1 = two.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.33.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append2(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.33.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two2) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, n3), mk.node2(two2.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.33.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.33.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.34
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(final Three<V, Node<V, A>> three) {
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.34.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append3(m, m1, mk.node3(three.values()), mk.node2(n1, n2), mk.node2(n3, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.34.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append3(m, m1, mk.node3(three.values()), mk.node3(n1, n2, n3), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.34.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three2) {
                        return Deep.append3(m, m1, mk.node3(three.values()), mk.node3(n1, n2, n3), mk.node3(three2.values()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.34.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append4(m, m1, mk.node3(three.values()), mk.node3(n1, n2, n3), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.35
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                final V4<Node<V, A>> v1 = four.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.35.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(n3, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.35.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        V2<Node<V, A>> v2 = two.values();
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, v2._1(), v2._2()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.35.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(n3, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.35.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four2) {
                        V4<Node<V, A>> v2 = four2.values();
                        return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, A>> append4(final Measured<V, A> m, final FingerTree<V, Node<V, A>> t1, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final Node<V, A> n4, final FingerTree<V, Node<V, A>> t2) {
        final Measured<V, Node<V, A>> nm = m.nodeMeasured();
        return (FingerTree) t1.match((F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.36
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Empty) ((Empty) obj));
            }

            public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                return FingerTree.this.cons(n4).cons(n3).cons(n2).cons(n1);
            }
        }, (F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.37
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Single) ((Single) obj));
            }

            public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                return FingerTree.this.cons(n4).cons(n3).cons(n2).cons(n1).cons(single.value());
            }
        }, (F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>) new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.38
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Deep) ((Deep) obj));
            }

            public FingerTree<V, Node<V, A>> f(final Deep<V, Node<V, A>> deep) {
                return (FingerTree) FingerTree.this.match(new F<Empty<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.38.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Empty) ((Empty) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Empty<V, Node<V, A>> empty) {
                        return t1.snoc(n1).snoc(n2).snoc(n3).snoc(n4);
                    }
                }, new F<Single<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.38.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Single) ((Single) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Single<V, Node<V, A>> single) {
                        return t1.snoc(n1).snoc(n2).snoc(n3).snoc(n4).snoc(single.value());
                    }
                }, new F<Deep<V, Node<V, A>>, FingerTree<V, Node<V, A>>>() { // from class: fj.data.fingertrees.Deep.38.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Deep) ((Deep) obj));
                    }

                    public FingerTree<V, Node<V, A>> f(Deep<V, Node<V, A>> deep2) {
                        return new Deep(nm, m.monoid().sumLeft(List.list(deep.v, n1.measure(), n2.measure(), n3.measure(), n4.measure(), ((Deep) deep2).v)), deep.prefix, Deep.addDigits4(nm, deep.middle, deep.suffix, n1, n2, n3, n4, ((Deep) deep2).prefix, ((Deep) deep2).middle), ((Deep) deep2).suffix);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V, A> FingerTree<V, Node<V, Node<V, A>>> addDigits4(final Measured<V, Node<V, A>> m, final FingerTree<V, Node<V, Node<V, A>>> m1, Digit<V, Node<V, A>> suffix, final Node<V, A> n1, final Node<V, A> n2, final Node<V, A> n3, final Node<V, A> n4, final Digit<V, Node<V, A>> prefix, final FingerTree<V, Node<V, Node<V, A>>> m2) {
        final MakeTree<V, Node<V, A>> mk = mkTree(m);
        return (FingerTree) suffix.match((F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.39
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(final One<V, Node<V, A>> one) {
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.39.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one2) {
                        return Deep.append2(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, n4, one2.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.39.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node2(n3, n4), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.39.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.39.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append3(m, m1, mk.node3(one.value(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.40
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                final V2<Node<V, A>> v1 = two.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.40.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node2(n2, n3), mk.node2(n4, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.40.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two2) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, n4), mk.node2(two2.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.40.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, n4), mk.node3(three.values()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.40.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), n1), mk.node3(n2, n3, n4), mk.node2(v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.41
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                final V3<Node<V, A>> v1 = three.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.41.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node2(n4, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.41.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        V2<Node<V, A>> v2 = two.values();
                        return Deep.append3(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node3(n4, v2._1(), v2._2()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.41.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three2) {
                        V3<Node<V, A>> v2 = three2.values();
                        return Deep.append4(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node2(n4, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.41.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                        V4<Node<V, A>> v2 = four.values();
                        return Deep.append4(m, m1, mk.node3(v1), mk.node3(n1, n2, n3), mk.node3(n4, v2._1(), v2._2()), mk.node2(v2._3(), v2._4()), m2);
                    }
                });
            }
        }, (F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>) new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.42
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four) {
                final V4<Node<V, A>> v1 = four.values();
                return (FingerTree) Digit.this.match(new F<One<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.42.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((One) ((One) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(One<V, Node<V, A>> one) {
                        return Deep.append3(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, n4, one.value()), m2);
                    }
                }, new F<Two<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.42.2
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Two) ((Two) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Two<V, Node<V, A>> two) {
                        return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node2(n3, n4), mk.node2(two.values()), m2);
                    }
                }, new F<Three<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.42.3
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Three) ((Three) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Three<V, Node<V, A>> three) {
                        V3<Node<V, A>> v2 = three.values();
                        return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node2(v2._2(), v2._3()), m2);
                    }
                }, new F<Four<V, Node<V, A>>, FingerTree<V, Node<V, Node<V, A>>>>() { // from class: fj.data.fingertrees.Deep.42.4
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Four) ((Four) obj));
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    public FingerTree<V, Node<V, Node<V, A>>> f(Four<V, Node<V, A>> four2) {
                        V4<Node<V, A>> v2 = four2.values();
                        return Deep.append4(m, m1, mk.node3(v1._1(), v1._2(), v1._3()), mk.node3(v1._4(), n1, n2), mk.node3(n3, n4, v2._1()), mk.node3(v2._2(), v2._3(), v2._4()), m2);
                    }
                });
            }
        });
    }
}
