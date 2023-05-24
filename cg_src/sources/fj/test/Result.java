package fj.test;

import fj.F;
import fj.P1;
import fj.data.List;
import fj.data.Option;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Result.class */
public final class Result {
    private final Option<List<Arg<?>>> args;
    private final R r;
    private final Option<Throwable> t;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Result$R.class */
    public enum R {
        Unfalsified,
        Falsified,
        Proven,
        Exception,
        NoResult
    }

    private Result(Option<List<Arg<?>>> args, R r, Option<Throwable> t) {
        this.args = args;
        this.r = r;
        this.t = t;
    }

    public Option<List<Arg<?>>> args() {
        return this.args;
    }

    public Option<Throwable> exception() {
        return this.t;
    }

    public boolean isUnfalsified() {
        return this.r == R.Unfalsified;
    }

    public boolean isFalsified() {
        return this.r == R.Falsified;
    }

    public boolean isProven() {
        return this.r == R.Proven;
    }

    public boolean isException() {
        return this.r == R.Exception;
    }

    public boolean isNoResult() {
        return this.r == R.NoResult;
    }

    public boolean failed() {
        return isFalsified() || isException();
    }

    public boolean passed() {
        return isUnfalsified() || isProven();
    }

    public Result provenAsUnfalsified() {
        return isProven() ? unfalsified(this.args.some()) : this;
    }

    public Result addArg(Arg<?> a) {
        F<Arg<?>, F<List<Arg<?>>, List<Arg<?>>>> cons = List.cons();
        return new Result(this.args.map(cons.f(a)), this.r, this.t);
    }

    public Option<Result> toOption() {
        if (isNoResult()) {
            return Option.none();
        }
        return Option.some(this);
    }

    public static Result noResult(Option<Result> r) {
        return r.orSome(new P1<Result>() { // from class: fj.test.Result.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.P1
            public Result _1() {
                return Result.noResult();
            }
        });
    }

    public static Result noResult() {
        return new Result(Option.none(), R.NoResult, Option.none());
    }

    public static Result unfalsified(List<Arg<?>> args) {
        return new Result(Option.some(args), R.Unfalsified, Option.none());
    }

    public static Result falsified(List<Arg<?>> args) {
        return new Result(Option.some(args), R.Falsified, Option.none());
    }

    public static Result proven(List<Arg<?>> args) {
        return new Result(Option.some(args), R.Proven, Option.none());
    }

    public static Result exception(List<Arg<?>> args, Throwable t) {
        return new Result(Option.some(args), R.Exception, Option.some(t));
    }
}
