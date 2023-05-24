package fj;

import fj.data.Validation;
import fj.function.TryEffect0;
import fj.function.TryEffect1;
import fj.function.TryEffect2;
import fj.function.TryEffect3;
import fj.function.TryEffect4;
import fj.function.TryEffect5;
import fj.function.TryEffect6;
import fj.function.TryEffect7;
import fj.function.TryEffect8;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/TryEffect.class */
public class TryEffect {
    private TryEffect() {
    }

    public static <A, Z extends Exception> P1<Validation<Z, Unit>> f(TryEffect0<Z> t) {
        return P.lazy(TryEffect$$Lambda$1.lambdaFactory$(t));
    }

    public static /* synthetic */ Validation lambda$f$79(TryEffect0 tryEffect0, Unit u) {
        try {
            tryEffect0.f();
            return Validation.success(Unit.unit());
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, Z extends Exception> F<A, Validation<Z, Unit>> f(TryEffect1<A, Z> t) {
        return TryEffect$$Lambda$2.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$80(TryEffect1 tryEffect1, Object a) {
        try {
            tryEffect1.f(a);
            return Validation.success(Unit.unit());
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, Z extends Exception> F2<A, B, Validation<Z, Unit>> f(TryEffect2<A, B, Z> t) {
        return TryEffect$$Lambda$3.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$81(TryEffect2 tryEffect2, Object a, Object b) {
        try {
            tryEffect2.f(a, b);
            return Validation.success(Unit.unit());
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, C, Z extends Exception> F3<A, B, C, Validation<Z, Unit>> f(TryEffect3<A, B, C, Z> t) {
        return TryEffect$$Lambda$4.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$82(TryEffect3 tryEffect3, Object a, Object b, Object c) {
        try {
            tryEffect3.f(a, b, c);
            return Validation.success(Unit.unit());
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, C, D, Z extends Exception> F4<A, B, C, D, Validation<Z, Unit>> f(TryEffect4<A, B, C, D, Z> t) {
        return TryEffect$$Lambda$5.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$83(TryEffect4 tryEffect4, Object a, Object b, Object c, Object d) {
        try {
            tryEffect4.f(a, b, c, d);
            return Validation.success(Unit.unit());
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, C, D, E, Z extends Exception> F5<A, B, C, D, E, Validation<Z, Unit>> f(TryEffect5<A, B, C, D, E, Z> t) {
        return TryEffect$$Lambda$6.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$84(TryEffect5 tryEffect5, Object a, Object b, Object c, Object d, Object e) {
        try {
            tryEffect5.f(a, b, c, d, e);
            return Validation.success(Unit.unit());
        } catch (Exception z) {
            return Validation.fail(z);
        }
    }

    public static <A, B, C, D, E, $F, Z extends Exception> F6<A, B, C, D, E, $F, Validation<Z, Unit>> f(TryEffect6<A, B, C, D, E, $F, Z> t) {
        return TryEffect$$Lambda$7.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$85(TryEffect6 tryEffect6, Object a, Object b, Object c, Object d, Object e, Object f) {
        try {
            tryEffect6.f(a, b, c, d, e, f);
            return Validation.success(Unit.unit());
        } catch (Exception z) {
            return Validation.fail(z);
        }
    }

    public static <A, B, C, D, E, $F, G, Z extends Exception> F7<A, B, C, D, E, $F, G, Validation<Z, Unit>> f(TryEffect7<A, B, C, D, E, $F, G, Z> t) {
        return TryEffect$$Lambda$8.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$86(TryEffect7 tryEffect7, Object a, Object b, Object c, Object d, Object e, Object f, Object g) {
        try {
            tryEffect7.f(a, b, c, d, e, f, g);
            return Validation.success(Unit.unit());
        } catch (Exception z) {
            return Validation.fail(z);
        }
    }

    public static <A, B, C, D, E, $F, G, H, Z extends Exception> F8<A, B, C, D, E, $F, G, H, Validation<Z, Unit>> f(TryEffect8<A, B, C, D, E, $F, G, H, Z> t) {
        return TryEffect$$Lambda$9.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$87(TryEffect8 tryEffect8, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h) {
        try {
            tryEffect8.f(a, b, c, d, e, f, g, h);
            return Validation.success(Unit.unit());
        } catch (Exception z) {
            return Validation.fail(z);
        }
    }
}
