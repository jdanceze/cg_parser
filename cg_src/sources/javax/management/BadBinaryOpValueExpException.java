package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/BadBinaryOpValueExpException.class */
public class BadBinaryOpValueExpException extends Exception {
    private static final long serialVersionUID = 5068475589449021227L;
    private ValueExp exp;

    public BadBinaryOpValueExpException(ValueExp valueExp) {
        this.exp = valueExp;
    }

    public ValueExp getExp() {
        return this.exp;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return new StringBuffer().append("BadBinaryOpValueExpException: ").append(this.exp).toString();
    }
}
