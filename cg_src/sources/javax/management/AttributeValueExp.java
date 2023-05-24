package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/AttributeValueExp.class */
public class AttributeValueExp implements ValueExp {
    private static final long serialVersionUID = -7768025046539163385L;
    private String attr;

    public AttributeValueExp() {
    }

    public AttributeValueExp(String str) {
        this.attr = str;
    }

    public String getAttributeName() {
        return this.attr;
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        Object attribute = getAttribute(objectName);
        if (attribute instanceof Number) {
            return new NumericValueExp((Number) attribute);
        }
        if (attribute instanceof String) {
            return new StringValueExp((String) attribute);
        }
        if (attribute instanceof Boolean) {
            return new BooleanValueExp((Boolean) attribute);
        }
        throw new BadAttributeValueExpException(attribute);
    }

    public String toString() {
        return this.attr;
    }

    @Override // javax.management.ValueExp
    public void setMBeanServer(MBeanServer mBeanServer) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object getAttribute(ObjectName objectName) {
        Object obj = null;
        try {
            obj = QueryEval.getMBeanServer().getAttribute(objectName, this.attr);
        } catch (Exception e) {
        }
        return obj;
    }
}
