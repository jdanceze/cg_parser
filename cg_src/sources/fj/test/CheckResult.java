package fj.test;

import fj.Bottom;
import fj.F;
import fj.Show;
import fj.data.List;
import fj.data.Option;
import java.io.PrintWriter;
import java.io.StringWriter;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/CheckResult.class */
public final class CheckResult {
    private final R r;
    private final Option<List<Arg<?>>> args;
    private final Option<Throwable> ex;
    private final int succeeded;
    private final int discarded;
    public static final Show<CheckResult> summary = summary(Arg.argShow);
    public static final Show<CheckResult> summaryEx = summaryEx(Arg.argShow);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/CheckResult$R.class */
    public enum R {
        Passed,
        Proven,
        Falsified,
        Exhausted,
        PropException,
        GenException
    }

    private CheckResult(R r, Option<List<Arg<?>>> args, Option<Throwable> ex, int succeeded, int discarded) {
        this.r = r;
        this.args = args;
        this.ex = ex;
        this.succeeded = succeeded;
        this.discarded = discarded;
    }

    public static CheckResult passed(int succeeded, int discarded) {
        return new CheckResult(R.Passed, Option.none(), Option.none(), succeeded, discarded);
    }

    public static CheckResult proven(List<Arg<?>> args, int succeeded, int discarded) {
        return new CheckResult(R.Proven, Option.some(args), Option.none(), succeeded, discarded);
    }

    public static CheckResult falsified(List<Arg<?>> args, int succeeded, int discarded) {
        return new CheckResult(R.Falsified, Option.some(args), Option.none(), succeeded, discarded);
    }

    public static CheckResult exhausted(int succeeded, int discarded) {
        return new CheckResult(R.Exhausted, Option.none(), Option.none(), succeeded, discarded);
    }

    public static CheckResult propException(List<Arg<?>> args, Throwable ex, int succeeded, int discarded) {
        return new CheckResult(R.PropException, Option.some(args), Option.some(ex), succeeded, discarded);
    }

    public static CheckResult genException(Throwable ex, int succeeded, int discarded) {
        return new CheckResult(R.GenException, Option.none(), Option.some(ex), succeeded, discarded);
    }

    public boolean isPassed() {
        return this.r == R.Passed;
    }

    public boolean isProven() {
        return this.r == R.Proven;
    }

    public boolean isFalsified() {
        return this.r == R.Falsified;
    }

    public boolean isExhausted() {
        return this.r == R.Exhausted;
    }

    public boolean isPropException() {
        return this.r == R.PropException;
    }

    public boolean isGenException() {
        return this.r == R.GenException;
    }

    public Option<List<Arg<?>>> args() {
        return this.args;
    }

    public Option<Throwable> exception() {
        return this.ex;
    }

    public int succeeded() {
        return this.succeeded;
    }

    public int discarded() {
        return this.discarded;
    }

    public static Show<CheckResult> summary(final Show<Arg<?>> sa) {
        return Show.showS((F) new F<CheckResult, String>() { // from class: fj.test.CheckResult.1
            private String test(CheckResult r) {
                return r.succeeded() == 1 ? "test" : "tests";
            }

            private String arguments(CheckResult r) {
                List<Arg<?>> args = r.args().some();
                return args.length() == 1 ? "argument: " + Show.this.showS((Show) args.head()) : "arguments: " + Show.listShow(Show.this).showS((Show) args);
            }

            @Override // fj.F
            public String f(CheckResult r) {
                if (r.isProven()) {
                    return "OK, property proven with " + arguments(r);
                }
                if (r.isPassed()) {
                    return "OK, passed " + r.succeeded() + ' ' + test(r) + (r.discarded() > 0 ? " (" + r.discarded() + " discarded)" : "") + '.';
                } else if (r.isFalsified()) {
                    return "Falsified after " + r.succeeded() + " passed " + test(r) + " with " + arguments(r);
                } else {
                    if (r.isExhausted()) {
                        return "Gave up after " + r.succeeded() + " passed " + test(r) + " and " + r.discarded() + " discarded tests.";
                    }
                    if (r.isPropException()) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        r.exception().some().printStackTrace(pw);
                        return "Exception on property evaluation with " + arguments(r) + System.getProperty("line.separator") + sw;
                    } else if (r.isGenException()) {
                        StringWriter sw2 = new StringWriter();
                        PrintWriter pw2 = new PrintWriter(sw2);
                        r.exception().some().printStackTrace(pw2);
                        return "Exception on argument generation " + System.getProperty("line.separator") + sw2;
                    } else {
                        throw Bottom.decons(r.getClass());
                    }
                }
            }
        });
    }

    public static Show<CheckResult> summaryEx(final Show<Arg<?>> sa) {
        return Show.showS((F) new F<CheckResult, String>() { // from class: fj.test.CheckResult.2
            @Override // fj.F
            public String f(CheckResult r) {
                String s = CheckResult.summary(Show.this).show((Show<CheckResult>) r).toString();
                if (r.isProven() || r.isPassed() || r.isExhausted()) {
                    return s;
                }
                if (r.isFalsified() || r.isPropException() || r.isGenException()) {
                    throw new Error(s);
                }
                throw Bottom.decons(r.getClass());
            }
        });
    }
}
