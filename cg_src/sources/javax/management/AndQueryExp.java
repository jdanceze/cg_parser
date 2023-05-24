package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/AndQueryExp.class */
class AndQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -1081892073854801359L;
    private QueryExp exp1;
    private QueryExp exp2;

    public AndQueryExp() {
    }

    public AndQueryExp(QueryExp queryExp, QueryExp queryExp2) {
        this.exp1 = queryExp;
        this.exp2 = queryExp2;
    }

    public QueryExp getLeftExp() {
        return this.exp1;
    }

    public QueryExp getRightExp() {
        return this.exp2;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        return this.exp1.apply(objectName) && this.exp2.apply(objectName);
    }

    public String toString() {
        return new StringBuffer().append("(").append(this.exp1).append(") and (").append(this.exp2).append(")").toString();
    }
}
