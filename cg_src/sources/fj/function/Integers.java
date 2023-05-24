package fj.function;

import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Semigroup;
import fj.data.List;
import fj.data.Option;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/Integers.class */
public final class Integers {
    public static final F<Integer, F<Integer, Integer>> add = Semigroup.intAdditionSemigroup.sum();
    public static final F<Integer, F<Integer, Integer>> multiply = Semigroup.intMultiplicationSemigroup.sum();
    public static final F<Integer, F<Integer, Integer>> subtract = Function.curry(new F2<Integer, Integer, Integer>() { // from class: fj.function.Integers.1
        @Override // fj.F2
        public Integer f(Integer x, Integer y) {
            return Integer.valueOf(x.intValue() - y.intValue());
        }
    });
    public static final F<Integer, Integer> negate = new F<Integer, Integer>() { // from class: fj.function.Integers.2
        @Override // fj.F
        public Integer f(Integer x) {
            return Integer.valueOf(x.intValue() * (-1));
        }
    };
    public static final F<Integer, Integer> abs = new F<Integer, Integer>() { // from class: fj.function.Integers.3
        @Override // fj.F
        public Integer f(Integer x) {
            return Integer.valueOf(Math.abs(x.intValue()));
        }
    };
    public static final F<Integer, F<Integer, Integer>> remainder = Function.curry(new F2<Integer, Integer, Integer>() { // from class: fj.function.Integers.4
        @Override // fj.F2
        public Integer f(Integer a, Integer b) {
            return Integer.valueOf(a.intValue() % b.intValue());
        }
    });
    public static final F<Integer, F<Integer, Integer>> power = Function.curry(new F2<Integer, Integer, Integer>() { // from class: fj.function.Integers.5
        @Override // fj.F2
        public Integer f(Integer a, Integer b) {
            return Integer.valueOf((int) StrictMath.pow(a.intValue(), b.intValue()));
        }
    });
    public static final F<Integer, Boolean> even = new F<Integer, Boolean>() { // from class: fj.function.Integers.6
        @Override // fj.F
        public Boolean f(Integer i) {
            return Boolean.valueOf(i.intValue() % 2 == 0);
        }
    };
    public static final F<Integer, Boolean> gtZero = new F<Integer, Boolean>() { // from class: fj.function.Integers.8
        @Override // fj.F
        public Boolean f(Integer i) {
            return Boolean.valueOf(i.intValue() > 0);
        }
    };
    public static final F<Integer, Boolean> gteZero = new F<Integer, Boolean>() { // from class: fj.function.Integers.9
        @Override // fj.F
        public Boolean f(Integer i) {
            return Boolean.valueOf(i.intValue() >= 0);
        }
    };
    public static final F<Integer, Boolean> ltZero = new F<Integer, Boolean>() { // from class: fj.function.Integers.10
        @Override // fj.F
        public Boolean f(Integer i) {
            return Boolean.valueOf(i.intValue() < 0);
        }
    };
    public static final F<Integer, Boolean> lteZero = new F<Integer, Boolean>() { // from class: fj.function.Integers.11
        @Override // fj.F
        public Boolean f(Integer i) {
            return Boolean.valueOf(i.intValue() <= 0);
        }
    };

    private Integers() {
        throw new UnsupportedOperationException();
    }

    public static int sum(List<Integer> ints) {
        return Monoid.intAdditionMonoid.sumLeft(ints).intValue();
    }

    public static int product(List<Integer> ints) {
        return Monoid.intMultiplicationMonoid.sumLeft(ints).intValue();
    }

    public static F<String, Option<Integer>> fromString() {
        return new F<String, Option<Integer>>() { // from class: fj.function.Integers.7
            @Override // fj.F
            public Option<Integer> f(String s) {
                try {
                    return Option.some(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                    return Option.none();
                }
            }
        };
    }
}
