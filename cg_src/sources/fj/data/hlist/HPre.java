package fj.data.hlist;

import fj.F;
import fj.Show;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre.class */
public final class HPre {
    private static final HTrue hTrue = new HTrue();
    private static final HFalse hFalse = new HFalse();

    private HPre() {
        throw new UnsupportedOperationException();
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HBool.class */
    public static class HBool {
        private HBool() {
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HTrue.class */
    public static class HTrue extends HBool {
        private HTrue() {
            super();
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HFalse.class */
    public static class HFalse extends HBool {
        private HFalse() {
            super();
        }
    }

    public static HTrue hTrue() {
        return hTrue;
    }

    public static HFalse hFalse() {
        return hFalse;
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HAnd.class */
    public static final class HAnd<A extends HBool, B extends HBool, C extends HBool> {
        private final C v;

        private HAnd(C v) {
            this.v = v;
        }

        public C v() {
            return this.v;
        }

        public static HAnd<HFalse, HFalse, HFalse> hAnd(HFalse a, HFalse b) {
            return new HAnd<>(HPre.hFalse());
        }

        public static HAnd<HTrue, HFalse, HFalse> hAnd(HTrue a, HFalse b) {
            return new HAnd<>(HPre.hFalse());
        }

        public static HAnd<HFalse, HTrue, HFalse> hAnd(HFalse a, HTrue b) {
            return new HAnd<>(HPre.hFalse());
        }

        public static HAnd<HTrue, HTrue, HTrue> hAnd(HTrue a, HTrue b) {
            return new HAnd<>(HPre.hTrue());
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HOr.class */
    public static final class HOr<A extends HBool, B extends HBool, C extends HBool> {
        private final C v;

        private HOr(C v) {
            this.v = v;
        }

        public C v() {
            return this.v;
        }

        public static HOr<HFalse, HFalse, HFalse> hOr(HFalse a, HFalse b) {
            return new HOr<>(HPre.hFalse());
        }

        public static HOr<HTrue, HFalse, HTrue> hOr(HTrue a, HFalse b) {
            return new HOr<>(HPre.hTrue());
        }

        public static HOr<HFalse, HTrue, HTrue> hOr(HFalse a, HTrue b) {
            return new HOr<>(HPre.hTrue());
        }

        public static HOr<HTrue, HTrue, HTrue> hOr(HTrue a, HTrue b) {
            return new HOr<>(HPre.hTrue());
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HCond.class */
    public static final class HCond<T, X, Y, Z> {
        private final Z z;

        private HCond(Z z) {
            this.z = z;
        }

        public Z v() {
            return this.z;
        }

        public static <X, Y> HCond<HFalse, X, Y, Y> hCond(HFalse t, X x, Y y) {
            return new HCond<>(y);
        }

        public static <X, Y> HCond<HTrue, X, Y, X> hCond(HTrue t, X x, Y y) {
            return new HCond<>(x);
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HNat.class */
    public static abstract class HNat<A extends HNat<A>> {
        public abstract Show<A> show();

        public abstract Integer toInteger();

        public static HZero hZero() {
            return new HZero();
        }

        public static <N extends HNat<N>> HSucc<N> hSucc(N n) {
            return new HSucc<>(n);
        }

        public static <N extends HNat<N>> N hPred(HSucc<N> n) {
            return (N) ((HSucc) n).pred;
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HZero.class */
    public static final class HZero extends HNat<HZero> {
        private HZero() {
        }

        @Override // fj.data.hlist.HPre.HNat
        public Show<HZero> show() {
            return Show.showS((F) new F<HZero, String>() { // from class: fj.data.hlist.HPre.HZero.1
                @Override // fj.F
                public String f(HZero hZero) {
                    return "HZero";
                }
            });
        }

        @Override // fj.data.hlist.HPre.HNat
        public Integer toInteger() {
            return 0;
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HSucc.class */
    public static final class HSucc<N extends HNat<N>> extends HNat<HSucc<N>> {
        private final N pred;

        private HSucc(N n) {
            this.pred = n;
        }

        @Override // fj.data.hlist.HPre.HNat
        public Show<HSucc<N>> show() {
            return Show.showS((F) new F<HSucc<N>, String>() { // from class: fj.data.hlist.HPre.HSucc.1
                @Override // fj.F
                public /* bridge */ /* synthetic */ String f(Object obj) {
                    return f((HSucc) ((HSucc) obj));
                }

                public String f(HSucc<N> s) {
                    return "HSucc (" + s.show().showS((Show<HSucc<N>>) s) + ')';
                }
            });
        }

        @Override // fj.data.hlist.HPre.HNat
        public Integer toInteger() {
            return Integer.valueOf(1 + this.pred.toInteger().intValue());
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HEq.class */
    public static final class HEq<X, Y, B extends HBool> {
        private final B v;

        private HEq(B v) {
            this.v = v;
        }

        public B v() {
            return this.v;
        }

        public static HEq<HZero, HZero, HTrue> eq(HZero a, HZero b) {
            return new HEq<>(HPre.hTrue());
        }

        public static <N extends HNat<N>> HEq<HZero, HSucc<N>, HFalse> eq(HZero a, HSucc<N> b) {
            return new HEq<>(HPre.hFalse());
        }

        public static <N extends HNat<N>> HEq<HSucc<N>, HZero, HFalse> eq(HSucc<N> a, HZero b) {
            return new HEq<>(HPre.hFalse());
        }

        public static <N extends HNat<N>, NN extends HNat<NN>, B extends HBool, E extends HEq<N, NN, B>> HEq<HSucc<N>, HSucc<NN>, B> eq(HSucc<N> a, HSucc<NN> b, E e) {
            return new HEq<>(e.v());
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/hlist/HPre$HAdd.class */
    public static final class HAdd<A extends HNat<A>, B extends HNat<B>, C extends HNat<C>> {
        private final C sum;

        private HAdd(C sum) {
            this.sum = sum;
        }

        public C sum() {
            return this.sum;
        }

        public static <N extends HNat<N>> HAdd<HZero, HSucc<N>, HSucc<N>> add(HZero a, HSucc<N> b) {
            return new HAdd<>(b);
        }

        public static <N extends HNat<N>> HAdd<HSucc<N>, HZero, HSucc<N>> add(HSucc<N> a, HZero b) {
            return new HAdd<>(a);
        }

        public static <N extends HNat<N>, M extends HNat<M>, R extends HNat<R>, H extends HAdd<N, HSucc<M>, R>> HAdd<HSucc<N>, HSucc<M>, HSucc<R>> add(HSucc<N> a, HSucc<M> b, H h) {
            return new HAdd<>(HNat.hSucc(h.sum()));
        }
    }
}
