package fj;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import fj.data.Array;
import fj.data.List;
import fj.data.Natural;
import fj.data.Option;
import fj.data.Set;
import fj.data.Stream;
import java.math.BigDecimal;
import java.math.BigInteger;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Monoid.class */
public final class Monoid<A> {
    private final F<A, F<A, A>> sum;
    private final A zero;
    public static final Monoid<Integer> intAdditionMonoid = monoid((Semigroup<int>) Semigroup.intAdditionSemigroup, 0);
    public static final Monoid<Integer> intMultiplicationMonoid = monoid((Semigroup<int>) Semigroup.intMultiplicationSemigroup, 1);
    public static final Monoid<Double> doubleAdditionMonoid = monoid(Semigroup.doubleAdditionSemigroup, Double.valueOf((double) Const.default_value_double));
    public static final Monoid<Double> doubleMultiplicationMonoid = monoid(Semigroup.doubleMultiplicationSemigroup, Double.valueOf(1.0d));
    public static final Monoid<BigInteger> bigintAdditionMonoid = monoid(Semigroup.bigintAdditionSemigroup, BigInteger.ZERO);
    public static final Monoid<BigInteger> bigintMultiplicationMonoid = monoid(Semigroup.bigintMultiplicationSemigroup, BigInteger.ONE);
    public static final Monoid<BigDecimal> bigdecimalAdditionMonoid = monoid(Semigroup.bigdecimalAdditionSemigroup, BigDecimal.ZERO);
    public static final Monoid<BigDecimal> bigdecimalMultiplicationMonoid = monoid(Semigroup.bigdecimalMultiplicationSemigroup, BigDecimal.ONE);
    public static final Monoid<Natural> naturalAdditionMonoid = monoid(Semigroup.naturalAdditionSemigroup, Natural.ZERO);
    public static final Monoid<Natural> naturalMultiplicationMonoid = monoid(Semigroup.naturalMultiplicationSemigroup, Natural.ONE);
    public static final Monoid<Long> longAdditionMonoid = monoid((Semigroup<long>) Semigroup.longAdditionSemigroup, 0L);
    public static final Monoid<Long> longMultiplicationMonoid = monoid((Semigroup<long>) Semigroup.longMultiplicationSemigroup, 1L);
    public static final Monoid<Boolean> disjunctionMonoid = monoid((Semigroup<boolean>) Semigroup.disjunctionSemigroup, false);
    public static final Monoid<Boolean> exclusiveDisjunctionMonoid = monoid((Semigroup<boolean>) Semigroup.exclusiveDisjunctionSemiGroup, false);
    public static final Monoid<Boolean> conjunctionMonoid = monoid((Semigroup<boolean>) Semigroup.conjunctionSemigroup, true);
    public static final Monoid<String> stringMonoid = monoid(Semigroup.stringSemigroup, "");
    public static final Monoid<StringBuffer> stringBufferMonoid = monoid(Semigroup.stringBufferSemigroup, new StringBuffer());
    public static final Monoid<StringBuilder> stringBuilderMonoid = monoid(Semigroup.stringBuilderSemigroup, new StringBuilder());

    private Monoid(F<A, F<A, A>> sum, A zero) {
        this.sum = sum;
        this.zero = zero;
    }

    public Semigroup<A> semigroup() {
        return Semigroup.semigroup(this.sum);
    }

    public A sum(A a1, A a2) {
        return this.sum.f(a1).f(a2);
    }

    public F<A, A> sum(A a1) {
        return this.sum.f(a1);
    }

    public F<A, F<A, A>> sum() {
        return this.sum;
    }

    public A zero() {
        return this.zero;
    }

    public A sumRight(List<A> as) {
        return (A) as.foldRight((F<A, F<F, F>>) ((F<A, F<A, A>>) this.sum), (F) this.zero);
    }

    public A sumRight(Stream<A> as) {
        return (A) as.foldRight((F2<A, P1<F2>, F2>) ((F2<A, P1<A>, A>) new F2<A, P1<A>, A>() { // from class: fj.Monoid.1
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass1) obj, (P1<AnonymousClass1>) obj2);
            }

            public A f(A a, P1<A> ap1) {
                return (A) Monoid.this.sum(a, ap1._1());
            }
        }), (F2) this.zero);
    }

    public A sumLeft(List<A> as) {
        return (A) as.foldLeft((F<F, F<A, F>>) ((F<A, F<A, A>>) this.sum), (F) this.zero);
    }

    public A sumLeft(Stream<A> as) {
        return (A) as.foldLeft((F<F, F<A, F>>) ((F<A, F<A, A>>) this.sum), (F) this.zero);
    }

    public F<List<A>, A> sumLeft() {
        return new F<List<A>, A>() { // from class: fj.Monoid.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List<Object>) obj);
            }

            public A f(List<A> as) {
                return (A) Monoid.this.sumLeft(as);
            }
        };
    }

    public F<List<A>, A> sumRight() {
        return new F<List<A>, A>() { // from class: fj.Monoid.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List<Object>) obj);
            }

            public A f(List<A> as) {
                return (A) Monoid.this.sumRight(as);
            }
        };
    }

    public F<Stream<A>, A> sumLeftS() {
        return new F<Stream<A>, A>() { // from class: fj.Monoid.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Stream<Object>) obj);
            }

            public A f(Stream<A> as) {
                return (A) Monoid.this.sumLeft(as);
            }
        };
    }

    public A join(Iterable<A> as, A a) {
        Stream<A> s = Stream.iterableStream(as);
        return s.isEmpty() ? this.zero : s.foldLeft1(Function.compose(this.sum, (F) Function.flip(this.sum).f(a)));
    }

    public static <A> Monoid<A> monoid(F<A, F<A, A>> sum, A zero) {
        return new Monoid<>(sum, zero);
    }

    public static <A> Monoid<A> monoid(F2<A, A, A> sum, A zero) {
        return new Monoid<>(Function.curry(sum), zero);
    }

    public static <A> Monoid<A> monoid(Semigroup<A> s, A zero) {
        return new Monoid<>(s.sum(), zero);
    }

    public static <A, B> Monoid<F<A, B>> functionMonoid(Monoid<B> mb) {
        return monoid(Semigroup.functionSemigroup(mb.semigroup()), Function.constant(((Monoid) mb).zero));
    }

    public static <A> Monoid<List<A>> listMonoid() {
        return monoid(Semigroup.listSemigroup(), List.nil());
    }

    public static <A> Monoid<Option<A>> optionMonoid() {
        return monoid(Semigroup.optionSemigroup(), Option.none());
    }

    public static <A> Monoid<Option<A>> firstOptionMonoid() {
        return monoid(Semigroup.firstOptionSemigroup(), Option.none());
    }

    public static <A> Monoid<Option<A>> lastOptionMonoid() {
        return monoid(Semigroup.lastOptionSemigroup(), Option.none());
    }

    public static <A> Monoid<Stream<A>> streamMonoid() {
        return monoid(Semigroup.streamSemigroup(), Stream.nil());
    }

    public static <A> Monoid<Array<A>> arrayMonoid() {
        return monoid(Semigroup.arraySemigroup(), Array.empty());
    }

    public static <A> Monoid<Set<A>> setMonoid(Ord<A> o) {
        return monoid(Semigroup.setSemigroup(), Set.empty(o));
    }
}
