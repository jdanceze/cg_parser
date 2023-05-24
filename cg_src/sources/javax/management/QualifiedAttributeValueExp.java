package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/QualifiedAttributeValueExp.class */
class QualifiedAttributeValueExp extends AttributeValueExp {
    private static final long serialVersionUID = 8832517277410933254L;
    private String className;

    public QualifiedAttributeValueExp() {
    }

    public QualifiedAttributeValueExp(String str, String str2) {
        super(str2);
        this.className = str;
    }

    public String getAttrClassName() {
        return this.className;
    }

    @Override // javax.management.AttributeValueExp, javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        try {
            if (QueryEval.getMBeanServer().getObjectInstance(objectName).getClassName().equals(this.className)) {
                return super.apply(objectName);
            }
        } catch (Exception e) {
        }
        throw new InvalidApplicationException("qualified attribute");
    }

    @Override // javax.management.AttributeValueExp
    public String toString() {
        if (this.className != null) {
            return new StringBuffer().append(this.className).append(".").append(super.toString()).toString();
        }
        return super.toString();
    }
}
