package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/BetweenQueryExp.class */
class BetweenQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -2933597532866307444L;
    private ValueExp exp1;
    private ValueExp exp2;
    private ValueExp exp3;

    public BetweenQueryExp() {
    }

    public BetweenQueryExp(ValueExp valueExp, ValueExp valueExp2, ValueExp valueExp3) {
        this.exp1 = valueExp;
        this.exp2 = valueExp2;
        this.exp3 = valueExp3;
    }

    public ValueExp getCheckedValue() {
        return this.exp1;
    }

    public ValueExp getLowerBound() {
        return this.exp2;
    }

    public ValueExp getUpperBound() {
        return this.exp3;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        ValueExp apply = this.exp1.apply(objectName);
        ValueExp apply2 = this.exp2.apply(objectName);
        ValueExp apply3 = this.exp3.apply(objectName);
        if (apply instanceof NumericValueExp) {
            if (((NumericValueExp) apply).isLong()) {
                long longValue = ((NumericValueExp) apply).longValue();
                return ((NumericValueExp) apply2).longValue() <= longValue && longValue <= ((NumericValueExp) apply3).longValue();
            }
            double doubleValue = ((NumericValueExp) apply).doubleValue();
            return ((NumericValueExp) apply2).doubleValue() <= doubleValue && doubleValue <= ((NumericValueExp) apply3).doubleValue();
        }
        String stringValueExp = ((StringValueExp) apply).toString();
        return ((StringValueExp) apply2).toString().compareTo(stringValueExp) <= 0 && stringValueExp.compareTo(((StringValueExp) apply3).toString()) <= 0;
    }

    public String toString() {
        return new StringBuffer().append("(").append(this.exp1).append(") between (").append(this.exp2).append(") and (").append(this.exp3).append(")").toString();
    }
}
