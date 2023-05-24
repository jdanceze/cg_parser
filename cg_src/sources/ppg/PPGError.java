package ppg;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/PPGError.class */
public class PPGError extends Throwable {
    private String filename;
    private String error;
    private int line;

    public PPGError(String file, int lineNum, String errorMsg) {
        this.filename = file;
        this.line = lineNum;
        this.error = errorMsg;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return new StringBuffer().append(this.filename).append(":").append(this.line).append(": syntax error: ").append(this.error).toString();
    }
}
