package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/BadAttributeValueExpException.class */
public class BadAttributeValueExpException extends Exception {
    private static final long serialVersionUID = -3105272988410493376L;
    private Object val;

    public BadAttributeValueExpException(Object obj) {
        this.val = obj;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return new StringBuffer().append("BadAttributeValueException: ").append(this.val).toString();
    }
}
