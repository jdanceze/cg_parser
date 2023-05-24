package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotQueryExp.class */
class NotQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = 5269643775896723397L;
    private QueryExp exp;

    public NotQueryExp() {
    }

    public NotQueryExp(QueryExp queryExp) {
        this.exp = queryExp;
    }

    public QueryExp getNegatedExp() {
        return this.exp;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        return !this.exp.apply(objectName);
    }

    public String toString() {
        return new StringBuffer().append("not (").append(this.exp).append(")").toString();
    }
}
