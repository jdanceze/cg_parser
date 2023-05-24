package fj.function;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.Semigroup;
import fj.data.List;
import fj.data.Option;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/Doubles.class */
public final class Doubles {
    public static final F<Double, F<Double, Double>> add = Semigroup.doubleAdditionSemigroup.sum();
    public static final F<Double, F<Double, Double>> multiply = Semigroup.doubleMultiplicationSemigroup.sum();
    public static final F<Double, F<Double, Double>> subtract = Function.curry(new F2<Double, Double, Double>() { // from class: fj.function.Doubles.1
        @Override // fj.F2
        public Double f(Double x, Double y) {
            return Double.valueOf(x.doubleValue() - y.doubleValue());
        }
    });
    public static final F<Double, Double> negate = new F<Double, Double>() { // from class: fj.function.Doubles.2
        @Override // fj.F
        public Double f(Double x) {
            return Double.valueOf(x.doubleValue() * (-1.0d));
        }
    };
    public static final F<Double, Double> abs = new F<Double, Double>() { // from class: fj.function.Doubles.3
        @Override // fj.F
        public Double f(Double x) {
            return Double.valueOf(Math.abs(x.doubleValue()));
        }
    };
    public static final F<Double, F<Double, Double>> remainder = Function.curry(new F2<Double, Double, Double>() { // from class: fj.function.Doubles.4
        @Override // fj.F2
        public Double f(Double a, Double b) {
            return Double.valueOf(a.doubleValue() % b.doubleValue());
        }
    });
    public static final F<Double, F<Double, Double>> power = Function.curry(new F2<Double, Double, Double>() { // from class: fj.function.Doubles.5
        @Override // fj.F2
        public Double f(Double a, Double b) {
            return Double.valueOf(StrictMath.pow(a.doubleValue(), b.doubleValue()));
        }
    });
    public static final F<Double, Boolean> even = new F<Double, Boolean>() { // from class: fj.function.Doubles.6
        @Override // fj.F
        public Boolean f(Double i) {
            return Boolean.valueOf(i.doubleValue() % 2.0d == Const.default_value_double);
        }
    };
    public static final F<Double, Boolean> gtZero = new F<Double, Boolean>() { // from class: fj.function.Doubles.8
        @Override // fj.F
        public Boolean f(Double i) {
            return Boolean.valueOf(Double.compare(i.doubleValue(), Const.default_value_double) > 0);
        }
    };
    public static final F<Double, Boolean> gteZero = new F<Double, Boolean>() { // from class: fj.function.Doubles.9
        @Override // fj.F
        public Boolean f(Double i) {
            return Boolean.valueOf(Double.compare(i.doubleValue(), Const.default_value_double) >= 0);
        }
    };
    public static final F<Double, Boolean> ltZero = new F<Double, Boolean>() { // from class: fj.function.Doubles.10
        @Override // fj.F
        public Boolean f(Double i) {
            return Boolean.valueOf(Double.compare(i.doubleValue(), Const.default_value_double) < 0);
        }
    };
    public static final F<Double, Boolean> lteZero = new F<Double, Boolean>() { // from class: fj.function.Doubles.11
        @Override // fj.F
        public Boolean f(Double i) {
            return Boolean.valueOf(Double.compare(i.doubleValue(), Const.default_value_double) <= 0);
        }
    };

    private Doubles() {
        throw new UnsupportedOperationException();
    }

    public static double sum(List<Double> doubles) {
        return Monoid.doubleAdditionMonoid.sumLeft(doubles).doubleValue();
    }

    public static double product(List<Double> doubles) {
        return Monoid.doubleMultiplicationMonoid.sumLeft(doubles).doubleValue();
    }

    public static F<String, Option<Double>> fromString() {
        return new F<String, Option<Double>>() { // from class: fj.function.Doubles.7
            @Override // fj.F
            public Option<Double> f(String s) {
                try {
                    return Option.some(Double.valueOf(s));
                } catch (NumberFormatException e) {
                    return Option.none();
                }
            }
        };
    }
}
