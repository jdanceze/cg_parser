package fj.data;

import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.P;
import fj.P2;
import fj.Unit;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/State.class */
public class State<S, A> {
    private F<S, P2<S, A>> run;

    private State(F<S, P2<S, A>> f) {
        this.run = f;
    }

    public P2<S, A> run(S s) {
        return this.run.f(s);
    }

    public static <S, A> State<S, A> unit(F<S, P2<S, A>> f) {
        return new State<>(f);
    }

    public static <S> State<S, S> units(F<S, S> f) {
        return unit(State$$Lambda$1.lambdaFactory$(f));
    }

    public static /* synthetic */ P2 lambda$units$136(F f, Object s) {
        Object f2 = f.f(s);
        return P.p(f2, f2);
    }

    public static <S, A> State<S, A> constant(A a) {
        return unit(State$$Lambda$2.lambdaFactory$(a));
    }

    public <B> State<S, B> map(F<A, B> f) {
        return unit(State$$Lambda$3.lambdaFactory$(this, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ P2 lambda$map$138(F f, Object s) {
        P2<S, A> p2 = run(s);
        return P.p(p2._1(), f.f(p2._2()));
    }

    public static <S> State<S, Unit> modify(F<S, S> f) {
        return init().flatMap(State$$Lambda$4.lambdaFactory$(f));
    }

    public static /* synthetic */ State lambda$modify$140(F f, Object s) {
        return unit(State$$Lambda$17.lambdaFactory$(f, s));
    }

    public static /* synthetic */ P2 lambda$null$139(F f, Object obj, Object s2) {
        return P.p(f.f(obj), Unit.unit());
    }

    public <B> State<S, B> mapState(F<P2<S, A>, P2<S, B>> f) {
        return unit(State$$Lambda$5.lambdaFactory$(this, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ P2 lambda$mapState$141(F f, Object s) {
        return (P2) f.f(run(s));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <S, B, C> State<S, C> flatMap(State<S, B> mb, F<B, State<S, C>> f) {
        return (State<S, B>) mb.flatMap(f);
    }

    public <B> State<S, B> flatMap(F<A, State<S, B>> f) {
        return unit(State$$Lambda$6.lambdaFactory$(this, f));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ P2 lambda$flatMap$142(F f, Object s) {
        P2<S, A> p = run(s);
        A a = p._2();
        S s2 = p._1();
        return ((State) f.f(a)).run(s2);
    }

    public static <S> State<S, S> init() {
        F f;
        f = State$$Lambda$7.instance;
        return unit(f);
    }

    public State<S, S> gets() {
        return unit(State$$Lambda$8.lambdaFactory$(this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ P2 lambda$gets$144(Object s) {
        P2<S, A> p = run(s);
        S s2 = p._1();
        return P.p(s2, s2);
    }

    public static <S> State<S, Unit> put(S s) {
        return unit(State$$Lambda$9.lambdaFactory$(s));
    }

    public static /* synthetic */ P2 lambda$put$145(Object obj, Object z) {
        return P.p(obj, Unit.unit());
    }

    public A eval(S s) {
        return run(s)._2();
    }

    public S exec(S s) {
        return run(s)._1();
    }

    public State<S, A> withs(F<S, S> f) {
        return unit(F1Functions.andThen(f, this.run));
    }

    public static <S, A> State<S, A> gets(F<S, A> f) {
        return init().map(State$$Lambda$10.lambdaFactory$(f));
    }

    public static <S, A> State<S, List<A>> sequence(List<State<S, A>> list) {
        F2<B, State<S, A>, B> f2;
        f2 = State$$Lambda$11.instance;
        return (State) list.foldLeft((F2<F2<B, State<S, A>, B>, State<S, A>, F2<B, State<S, A>, B>>) f2, (F2<B, State<S, A>, B>) constant(List.nil()));
    }

    public static /* synthetic */ State lambda$sequence$149(State acc, State ma) {
        return acc.flatMap(State$$Lambda$15.lambdaFactory$(ma));
    }

    public static /* synthetic */ State lambda$null$148(State state, List xs) {
        return state.map(State$$Lambda$16.lambdaFactory$(xs));
    }

    public static <S, A, B> State<S, List<B>> traverse(List<A> list, F<A, State<S, B>> f) {
        return (State) list.foldLeft((F2<F2<B, A, B>, A, F2<B, A, B>>) State$$Lambda$12.lambdaFactory$(f), (F2<B, A, B>) constant(List.nil()));
    }

    public static /* synthetic */ State lambda$traverse$152(F f, State acc, Object a) {
        return acc.flatMap(State$$Lambda$13.lambdaFactory$(f, a));
    }

    public static /* synthetic */ State lambda$null$151(F f, Object obj, List bs) {
        return ((State) f.f(obj)).map(State$$Lambda$14.lambdaFactory$(bs));
    }
}
