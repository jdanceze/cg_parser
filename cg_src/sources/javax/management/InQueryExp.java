package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/InQueryExp.class */
class InQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -5801329450358952434L;
    private ValueExp val;
    private ValueExp[] valueList;

    public InQueryExp() {
    }

    public InQueryExp(ValueExp valueExp, ValueExp[] valueExpArr) {
        this.val = valueExp;
        this.valueList = valueExpArr;
    }

    public ValueExp getCheckedValue() {
        return this.val;
    }

    public ValueExp[] getExplicitValues() {
        return this.valueList;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        if (this.valueList != null) {
            ValueExp apply = this.val.apply(objectName);
            boolean z = apply instanceof NumericValueExp;
            for (int i = 0; i < this.valueList.length; i++) {
                if (z) {
                    if (((NumericValueExp) this.valueList[i]).doubleValue() == ((NumericValueExp) apply).doubleValue()) {
                        return true;
                    }
                } else if (((StringValueExp) this.valueList[i]).getValue().equals(((StringValueExp) apply).getValue())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public String toString() {
        return new StringBuffer().append(this.val).append(" in (").append(generateValueList()).append(")").toString();
    }

    private String generateValueList() {
        if (this.valueList == null || this.valueList.length == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(this.valueList[0].toString());
        for (int i = 1; i < this.valueList.length; i++) {
            stringBuffer.append(", ");
            stringBuffer.append(this.valueList[i]);
        }
        return stringBuffer.toString();
    }
}
