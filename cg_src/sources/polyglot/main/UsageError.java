package polyglot.main;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/main/UsageError.class */
public class UsageError extends Exception {
    final int exitCode;

    public UsageError(String s) {
        this(s, 1);
    }

    public UsageError(String s, int exitCode) {
        super(s);
        this.exitCode = exitCode;
    }
}
