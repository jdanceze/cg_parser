package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/BinaryRelQueryExp.class */
class BinaryRelQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -5690656271650491000L;
    private int relOp;
    private ValueExp exp1;
    private ValueExp exp2;

    public BinaryRelQueryExp() {
    }

    public BinaryRelQueryExp(int i, ValueExp valueExp, ValueExp valueExp2) {
        this.relOp = i;
        this.exp1 = valueExp;
        this.exp2 = valueExp2;
    }

    public int getOperator() {
        return this.relOp;
    }

    public ValueExp getLeftValue() {
        return this.exp1;
    }

    public ValueExp getRightValue() {
        return this.exp2;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        ValueExp apply = this.exp1.apply(objectName);
        ValueExp apply2 = this.exp2.apply(objectName);
        boolean z = apply instanceof NumericValueExp;
        boolean z2 = apply instanceof BooleanValueExp;
        if (z) {
            if (((NumericValueExp) apply).isLong()) {
                long longValue = ((NumericValueExp) apply).longValue();
                long longValue2 = ((NumericValueExp) apply2).longValue();
                switch (this.relOp) {
                    case 0:
                        return longValue > longValue2;
                    case 1:
                        return longValue < longValue2;
                    case 2:
                        return longValue >= longValue2;
                    case 3:
                        return longValue <= longValue2;
                    case 4:
                        return longValue == longValue2;
                    default:
                        return false;
                }
            }
            double doubleValue = ((NumericValueExp) apply).doubleValue();
            double doubleValue2 = ((NumericValueExp) apply2).doubleValue();
            switch (this.relOp) {
                case 0:
                    return doubleValue > doubleValue2;
                case 1:
                    return doubleValue < doubleValue2;
                case 2:
                    return doubleValue >= doubleValue2;
                case 3:
                    return doubleValue <= doubleValue2;
                case 4:
                    return doubleValue == doubleValue2;
                default:
                    return false;
            }
        } else if (z2) {
            boolean booleanValue = ((BooleanValueExp) apply).getValue().booleanValue();
            boolean booleanValue2 = ((BooleanValueExp) apply2).getValue().booleanValue();
            switch (this.relOp) {
                case 0:
                    return booleanValue && !booleanValue2;
                case 1:
                    return !booleanValue && booleanValue2;
                case 2:
                    return booleanValue || !booleanValue2;
                case 3:
                    return !booleanValue || booleanValue2;
                case 4:
                    return booleanValue == booleanValue2;
                default:
                    return false;
            }
        } else {
            String value = ((StringValueExp) apply).getValue();
            String value2 = ((StringValueExp) apply2).getValue();
            switch (this.relOp) {
                case 0:
                    return value.compareTo(value2) > 0;
                case 1:
                    return value.compareTo(value2) < 0;
                case 2:
                    return value.compareTo(value2) >= 0;
                case 3:
                    return value.compareTo(value2) <= 0;
                case 4:
                    return value.compareTo(value2) == 0;
                default:
                    return false;
            }
        }
    }

    public String toString() {
        return new StringBuffer().append("(").append(this.exp1).append(") ").append(relOpString()).append(" (").append(this.exp2).append(")").toString();
    }

    private String relOpString() {
        switch (this.relOp) {
            case 0:
                return ">";
            case 1:
                return "<";
            case 2:
                return ">=";
            case 3:
                return "<=";
            case 4:
                return "=";
            default:
                return "=";
        }
    }
}
