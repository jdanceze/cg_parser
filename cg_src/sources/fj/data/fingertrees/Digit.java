package fj.data.fingertrees;

import fj.F;
import fj.F2;
import fj.Function;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/fingertrees/Digit.class */
public abstract class Digit<V, A> {
    private final Measured<V, A> m;

    public abstract <B> B foldRight(F<A, F<B, B>> f, B b);

    public abstract <B> B foldLeft(F<B, F<A, B>> f, B b);

    public abstract <B> B match(F<One<V, A>, B> f, F<Two<V, A>, B> f2, F<Three<V, A>, B> f3, F<Four<V, A>, B> f4);

    public final A reduceRight(final F<A, F<A, A>> f) {
        return (A) match((F<One<V, A>, A>) new F<One<V, A>, A>() { // from class: fj.data.fingertrees.Digit.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One<V, Object>) obj);
            }

            public A f(One<V, A> one) {
                return one.value();
            }
        }, (F<Two<V, A>, A>) new F<Two<V, A>, A>() { // from class: fj.data.fingertrees.Digit.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two<V, Object>) obj);
            }

            public A f(Two<V, A> two) {
                V2<A> v = two.values();
                return (A) ((F) f.f(v._1())).f(v._2());
            }
        }, (F<Three<V, A>, A>) new F<Three<V, A>, A>() { // from class: fj.data.fingertrees.Digit.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three<V, Object>) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public A f(Three<V, A> three) {
                V3<A> v = three.values();
                return (A) ((F) f.f(v._1())).f(((F) f.f(v._2())).f(v._3()));
            }
        }, (F<Four<V, A>, A>) new F<Four<V, A>, A>() { // from class: fj.data.fingertrees.Digit.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four<V, Object>) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public A f(Four<V, A> four) {
                V4<A> v = four.values();
                return (A) ((F) f.f(v._1())).f(((F) f.f(v._2())).f(((F) f.f(v._3())).f(v._4())));
            }
        });
    }

    public final A reduceLeft(final F<A, F<A, A>> f) {
        return (A) match((F<One<V, A>, A>) new F<One<V, A>, A>() { // from class: fj.data.fingertrees.Digit.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One<V, Object>) obj);
            }

            public A f(One<V, A> one) {
                return one.value();
            }
        }, (F<Two<V, A>, A>) new F<Two<V, A>, A>() { // from class: fj.data.fingertrees.Digit.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two<V, Object>) obj);
            }

            public A f(Two<V, A> two) {
                V2<A> v = two.values();
                return (A) ((F) f.f(v._1())).f(v._2());
            }
        }, (F<Three<V, A>, A>) new F<Three<V, A>, A>() { // from class: fj.data.fingertrees.Digit.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three<V, Object>) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public A f(Three<V, A> three) {
                V3<A> v = three.values();
                return (A) ((F) f.f(((F) f.f(v._1())).f(v._2()))).f(v._3());
            }
        }, (F<Four<V, A>, A>) new F<Four<V, A>, A>() { // from class: fj.data.fingertrees.Digit.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four<V, Object>) obj);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public A f(Four<V, A> four) {
                V4<A> v = four.values();
                return (A) ((F) f.f(((F) f.f(((F) f.f(v._1())).f(v._2()))).f(v._3()))).f(v._4());
            }
        });
    }

    public final <B> Digit<V, B> map(final F<A, B> f, final Measured<V, B> m) {
        return (Digit) match(new F<One<V, A>, Digit<V, B>>() { // from class: fj.data.fingertrees.Digit.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            public Digit<V, B> f(One<V, A> one) {
                return new One(m, f.f(one.value()));
            }
        }, new F<Two<V, A>, Digit<V, B>>() { // from class: fj.data.fingertrees.Digit.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            public Digit<V, B> f(Two<V, A> two) {
                return new Two(m, two.values().map(f));
            }
        }, new F<Three<V, A>, Digit<V, B>>() { // from class: fj.data.fingertrees.Digit.11
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            public Digit<V, B> f(Three<V, A> three) {
                return new Three(m, three.values().map(f));
            }
        }, new F<Four<V, A>, Digit<V, B>>() { // from class: fj.data.fingertrees.Digit.12
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            public Digit<V, B> f(Four<V, A> four) {
                return new Four(m, four.values().map(f));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Digit(Measured<V, A> m) {
        this.m = m;
    }

    public final V measure() {
        return (V) foldLeft(Function.curry(new F2<V, A, V>() { // from class: fj.data.fingertrees.Digit.13
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F2
            public V f(V v, A a) {
                return (V) Digit.this.m.sum(v, Digit.this.m.measure(a));
            }
        }), this.m.zero());
    }

    public final FingerTree<V, A> toTree() {
        final MakeTree<V, A> mk = FingerTree.mkTree(this.m);
        return (FingerTree) match((F<One<V, A>, FingerTree<V, A>>) new F<One<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Digit.14
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((One) ((One) obj));
            }

            public FingerTree<V, A> f(One<V, A> one) {
                return mk.single(one.value());
            }
        }, (F<Two<V, A>, FingerTree<V, A>>) new F<Two<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Digit.15
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Two) ((Two) obj));
            }

            public FingerTree<V, A> f(Two<V, A> two) {
                return mk.deep(mk.one(two.values()._1()), new Empty(Digit.this.m.nodeMeasured()), mk.one(two.values()._2()));
            }
        }, (F<Three<V, A>, FingerTree<V, A>>) new F<Three<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Digit.16
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Three) ((Three) obj));
            }

            public FingerTree<V, A> f(Three<V, A> three) {
                return mk.deep(mk.two(three.values()._1(), three.values()._2()), new Empty(Digit.this.m.nodeMeasured()), mk.one(three.values()._3()));
            }
        }, (F<Four<V, A>, FingerTree<V, A>>) new F<Four<V, A>, FingerTree<V, A>>() { // from class: fj.data.fingertrees.Digit.17
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Four) ((Four) obj));
            }

            public FingerTree<V, A> f(Four<V, A> four) {
                return mk.deep(mk.two(four.values()._1(), four.values()._2()), new Empty(Digit.this.m.nodeMeasured()), mk.two(four.values()._3(), four.values()._4()));
            }
        });
    }
}
