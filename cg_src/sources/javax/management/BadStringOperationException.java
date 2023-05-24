package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/BadStringOperationException.class */
public class BadStringOperationException extends Exception {
    private static final long serialVersionUID = 7802201238441662100L;
    private String op;

    public BadStringOperationException(String str) {
        this.op = str;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return new StringBuffer().append("BadStringOperationException: ").append(this.op).toString();
    }
}
