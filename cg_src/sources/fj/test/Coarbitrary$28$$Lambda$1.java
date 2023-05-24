package fj.test;

import fj.F;
import fj.F2;
import fj.data.State;
import fj.test.Coarbitrary;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Coarbitrary$28$$Lambda$1.class */
final /* synthetic */ class Coarbitrary$28$$Lambda$1 implements F {
    private final State arg$1;
    private final F2 arg$2;
    private final Gen arg$3;

    private Coarbitrary$28$$Lambda$1(State state, F2 f2, Gen gen) {
        this.arg$1 = state;
        this.arg$2 = f2;
        this.arg$3 = gen;
    }

    private static F get$Lambda(State state, F2 f2, Gen gen) {
        return new Coarbitrary$28$$Lambda$1(state, f2, gen);
    }

    @Override // fj.F
    public Object f(Object obj) {
        return Coarbitrary.AnonymousClass28.access$lambda$0(this.arg$1, this.arg$2, this.arg$3, obj);
    }

    public static F lambdaFactory$(State state, F2 f2, Gen gen) {
        return new Coarbitrary$28$$Lambda$1(state, f2, gen);
    }
}
