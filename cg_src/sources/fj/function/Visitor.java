package fj.function;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Monoid;
import fj.P1;
import fj.P2;
import fj.data.List;
import fj.data.Option;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/function/Visitor.class */
public final class Visitor {
    private Visitor() {
        throw new UnsupportedOperationException();
    }

    public static <X> X findFirst(List<Option<X>> values, P1<X> def) {
        return (X) ((Option) Monoid.firstOptionMonoid().sumLeft(values)).orSome((P1<Object>) def);
    }

    public static <X> X nullablefindFirst(List<X> values, P1<X> def) {
        return (X) findFirst(values.map(Option.fromNull()), def);
    }

    public static <A, B> B visitor(List<F<A, Option<B>>> visitors, P1<B> def, A value) {
        return (B) findFirst(visitors.map(Function.apply(value)), def);
    }

    public static <A, B> B nullableVisitor(List<F<A, B>> visitors, P1<B> def, A value) {
        return (B) visitor(visitors.map(new F<F<A, B>, F<A, Option<B>>>() { // from class: fj.function.Visitor.1
            @Override // fj.F
            public F<A, Option<B>> f(F<A, B> k) {
                return Function.compose(Option.fromNull(), k);
            }
        }), def, value);
    }

    public static <A, B> F<B, F<A, B>> association(final List<P2<A, B>> x, final Equal<A> eq) {
        return Function.curry(new F2<B, A, B>() { // from class: fj.function.Visitor.2
            /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
            @Override // fj.F2
            public B f(B def, A a) {
                return List.lookup(Equal.this, x, a).orSome((Option) def);
            }
        });
    }

    public static <A, B> F<P1<B>, F<A, B>> associationLazy(final List<P2<A, B>> x, final Equal<A> eq) {
        return Function.curry(new F2<P1<B>, A, B>() { // from class: fj.function.Visitor.3
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((P1<Object>) obj, (P1) obj2);
            }

            /* JADX WARN: Type inference failed for: r0v3, types: [B, java.lang.Object] */
            public B f(P1<B> def, A a) {
                return List.lookup(Equal.this, x, a).orSome((P1<??>) def);
            }
        });
    }
}
