package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/StringValueExp.class */
public class StringValueExp implements ValueExp {
    private static final long serialVersionUID = -3256390509806284044L;
    private String val;

    public StringValueExp() {
    }

    public StringValueExp(String str) {
        this.val = str;
    }

    public String getValue() {
        return this.val;
    }

    public String toString() {
        return new StringBuffer().append("'").append(this.val).append("'").toString();
    }

    @Override // javax.management.ValueExp
    public void setMBeanServer(MBeanServer mBeanServer) {
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        return this;
    }
}
