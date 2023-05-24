package soot;
/* loaded from: gencallgraphv3.jar:soot/CompilationDeathException.class */
public class CompilationDeathException extends RuntimeException {
    public static final int COMPILATION_ABORTED = 0;
    public static final int COMPILATION_SUCCEEDED = 1;
    private final int mStatus;

    public CompilationDeathException(String msg, Throwable t) {
        super(msg, t);
        this.mStatus = 0;
    }

    public CompilationDeathException(String msg) {
        super(msg);
        this.mStatus = 0;
    }

    public CompilationDeathException(int status, String msg) {
        super(msg);
        this.mStatus = status;
    }

    public CompilationDeathException(int status) {
        this.mStatus = status;
    }

    public int getStatus() {
        return this.mStatus;
    }
}
