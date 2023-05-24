package fj.function;

import fj.F;
import fj.F2;
import fj.F3;
import fj.Function;
import fj.Monoid;
import fj.Semigroup;
import fj.data.List;
import fj.data.Stream;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/Booleans.class */
public final class Booleans {
    public static final F<Boolean, F<Boolean, Boolean>> or = Semigroup.disjunctionSemigroup.sum();
    public static final F<Boolean, F<Boolean, Boolean>> and = Semigroup.conjunctionSemigroup.sum();
    public static final F<Boolean, F<Boolean, Boolean>> xor = Semigroup.exclusiveDisjunctionSemiGroup.sum();
    public static final F<Boolean, Boolean> not = new F<Boolean, Boolean>() { // from class: fj.function.Booleans.1
        @Override // fj.F
        public Boolean f(Boolean p) {
            return Boolean.valueOf(!p.booleanValue());
        }
    };
    public static final F<Boolean, F<Boolean, Boolean>> implies = Function.curry(new F2<Boolean, Boolean, Boolean>() { // from class: fj.function.Booleans.2
        @Override // fj.F2
        public Boolean f(Boolean p, Boolean q) {
            return Boolean.valueOf(!p.booleanValue() || q.booleanValue());
        }
    });
    public static final F<Boolean, F<Boolean, Boolean>> if_ = Function.flip(implies);
    public static final F<Boolean, F<Boolean, Boolean>> iff = Function.compose2(not, xor);
    public static final F<Boolean, F<Boolean, Boolean>> nimp = Function.compose2(not, implies);
    public static final F<Boolean, F<Boolean, Boolean>> nif = Function.compose2(not, if_);
    public static final F<Boolean, F<Boolean, Boolean>> nor = Function.compose2(not, or);

    private Booleans() {
        throw new UnsupportedOperationException();
    }

    public static boolean and(List<Boolean> l) {
        return Monoid.conjunctionMonoid.sumLeft(l).booleanValue();
    }

    public static boolean and(Stream<Boolean> l) {
        return Monoid.conjunctionMonoid.sumLeft(l).booleanValue();
    }

    public static boolean or(List<Boolean> l) {
        return Monoid.disjunctionMonoid.sumLeft(l).booleanValue();
    }

    public static boolean or(Stream<Boolean> l) {
        return Monoid.disjunctionMonoid.sumLeft(l).booleanValue();
    }

    public static <A> F<A, Boolean> not(F<A, Boolean> p) {
        return Function.compose(not, p);
    }

    public static <A> F<Boolean, F<A, F<A, A>>> cond() {
        return Function.curry(new F3<Boolean, A, A, A>() { // from class: fj.function.Booleans.3
            @Override // fj.F3
            public A f(Boolean p, A a1, A a2) {
                return p.booleanValue() ? a1 : a2;
            }
        });
    }
}
