package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/BooleanValueExp.class */
class BooleanValueExp extends QueryEval implements ValueExp {
    private static final long serialVersionUID = 7754922052666594581L;
    private boolean val;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BooleanValueExp(boolean z) {
        this.val = false;
        this.val = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BooleanValueExp(Boolean bool) {
        this.val = false;
        this.val = bool.booleanValue();
    }

    public Boolean getValue() {
        return new Boolean(this.val);
    }

    public String toString() {
        return String.valueOf(this.val);
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        return this;
    }
}
