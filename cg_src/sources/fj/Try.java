package fj;

import fj.data.IO;
import fj.data.IOFunctions;
import fj.data.Validation;
import fj.function.Try0;
import fj.function.Try1;
import fj.function.Try2;
import fj.function.Try3;
import fj.function.Try4;
import fj.function.Try5;
import fj.function.Try6;
import fj.function.Try7;
import fj.function.Try8;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Try.class */
public class Try {
    public static <A, E extends Exception> P1<Validation<E, A>> f(Try0<A, E> t) {
        return P.lazy(Try$$Lambda$1.lambdaFactory$(t));
    }

    public static /* synthetic */ Validation lambda$f$70(Try0 try0, Unit u) {
        try {
            return Validation.success(try0.f());
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, E extends Exception> F<A, Validation<E, B>> f(Try1<A, B, E> t) {
        return Try$$Lambda$2.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$71(Try1 try1, Object a) {
        try {
            return Validation.success(try1.f(a));
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, C, E extends Exception> F2<A, B, Validation<E, C>> f(Try2<A, B, C, E> t) {
        return Try$$Lambda$3.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$72(Try2 try2, Object a, Object b) {
        try {
            return Validation.success(try2.f(a, b));
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, C, D, E extends Exception> F3<A, B, C, Validation<E, D>> f(Try3<A, B, C, D, E> t) {
        return Try$$Lambda$4.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$73(Try3 try3, Object a, Object b, Object c) {
        try {
            return Validation.success(try3.f(a, b, c));
        } catch (Exception e) {
            return Validation.fail(e);
        }
    }

    public static <A, B, C, D, E, Z extends Exception> F4<A, B, C, D, Validation<Z, E>> f(Try4<A, B, C, D, E, Z> t) {
        return Try$$Lambda$5.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$74(Try4 try4, Object a, Object b, Object c, Object d) {
        try {
            return Validation.success(try4.f(a, b, c, d));
        } catch (Exception ex) {
            return Validation.fail(ex);
        }
    }

    public static <A, B, C, D, E, F, Z extends Exception> F5<A, B, C, D, E, Validation<Z, F>> f(Try5<A, B, C, D, E, F, Z> t) {
        return Try$$Lambda$6.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$75(Try5 try5, Object a, Object b, Object c, Object d, Object e) {
        try {
            return Validation.success(try5.f(a, b, c, d, e));
        } catch (Exception ex) {
            return Validation.fail(ex);
        }
    }

    public static <A, B, C, D, E, F, G, Z extends Exception> F6<A, B, C, D, E, F, Validation<Z, G>> f(Try6<A, B, C, D, E, F, G, Z> t) {
        return Try$$Lambda$7.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$76(Try6 try6, Object a, Object b, Object c, Object d, Object e, Object f) {
        try {
            return Validation.success(try6.f(a, b, c, d, e, f));
        } catch (Exception ex) {
            return Validation.fail(ex);
        }
    }

    public static <A, B, C, D, E, F, G, H, Z extends Exception> F7<A, B, C, D, E, F, G, Validation<Z, H>> f(Try7<A, B, C, D, E, F, G, H, Z> t) {
        return Try$$Lambda$8.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$77(Try7 try7, Object a, Object b, Object c, Object d, Object e, Object f, Object g) {
        try {
            return Validation.success(try7.f(a, b, c, d, e, f, g));
        } catch (Exception ex) {
            return Validation.fail(ex);
        }
    }

    public static <A, B, C, D, E, F, G, H, I, Z extends Exception> F8<A, B, C, D, E, F, G, H, Validation<Z, I>> f(Try8<A, B, C, D, E, F, G, H, I, Z> t) {
        return Try$$Lambda$9.lambdaFactory$(t);
    }

    public static /* synthetic */ Validation lambda$f$78(Try8 try8, Object a, Object b, Object c, Object d, Object e, Object f, Object g, Object h) {
        try {
            return Validation.success(try8.f(a, b, c, d, e, f, g, h));
        } catch (Exception ex) {
            return Validation.fail(ex);
        }
    }

    public static <A> IO<A> io(Try0<A, ? extends IOException> t) {
        return IOFunctions.io(t);
    }
}
