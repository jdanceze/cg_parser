package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/Query.class */
public class Query {
    public static final int GT = 0;
    public static final int LT = 1;
    public static final int GE = 2;
    public static final int LE = 3;
    public static final int EQ = 4;
    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int TIMES = 2;
    public static final int DIV = 3;

    public static QueryExp and(QueryExp queryExp, QueryExp queryExp2) {
        return new AndQueryExp(queryExp, queryExp2);
    }

    public static QueryExp or(QueryExp queryExp, QueryExp queryExp2) {
        return new OrQueryExp(queryExp, queryExp2);
    }

    public static QueryExp gt(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryRelQueryExp(0, valueExp, valueExp2);
    }

    public static QueryExp geq(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryRelQueryExp(2, valueExp, valueExp2);
    }

    public static QueryExp leq(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryRelQueryExp(3, valueExp, valueExp2);
    }

    public static QueryExp lt(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryRelQueryExp(1, valueExp, valueExp2);
    }

    public static QueryExp eq(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryRelQueryExp(4, valueExp, valueExp2);
    }

    public static QueryExp between(ValueExp valueExp, ValueExp valueExp2, ValueExp valueExp3) {
        return new BetweenQueryExp(valueExp, valueExp2, valueExp3);
    }

    public static QueryExp match(AttributeValueExp attributeValueExp, StringValueExp stringValueExp) {
        return new MatchQueryExp(attributeValueExp, stringValueExp);
    }

    public static AttributeValueExp attr(String str) {
        return new AttributeValueExp(str);
    }

    public static AttributeValueExp attr(String str, String str2) {
        return new QualifiedAttributeValueExp(str, str2);
    }

    public static AttributeValueExp classattr() {
        return new ClassAttributeValueExp();
    }

    public static QueryExp not(QueryExp queryExp) {
        return new NotQueryExp(queryExp);
    }

    public static QueryExp in(ValueExp valueExp, ValueExp[] valueExpArr) {
        return new InQueryExp(valueExp, valueExpArr);
    }

    public static StringValueExp value(String str) {
        return new StringValueExp(str);
    }

    public static ValueExp value(Number number) {
        return new NumericValueExp(number);
    }

    public static ValueExp value(int i) {
        return new NumericValueExp(new Long(i));
    }

    public static ValueExp value(long j) {
        return new NumericValueExp(new Long(j));
    }

    public static ValueExp value(float f) {
        return new NumericValueExp(new Double(f));
    }

    public static ValueExp value(double d) {
        return new NumericValueExp(new Double(d));
    }

    public static ValueExp value(boolean z) {
        return new BooleanValueExp(z);
    }

    public static ValueExp plus(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryOpValueExp(0, valueExp, valueExp2);
    }

    public static ValueExp times(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryOpValueExp(2, valueExp, valueExp2);
    }

    public static ValueExp minus(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryOpValueExp(1, valueExp, valueExp2);
    }

    public static ValueExp div(ValueExp valueExp, ValueExp valueExp2) {
        return new BinaryOpValueExp(3, valueExp, valueExp2);
    }

    public static QueryExp initialSubString(AttributeValueExp attributeValueExp, StringValueExp stringValueExp) {
        return new MatchQueryExp(attributeValueExp, new StringValueExp(new StringBuffer().append(stringValueExp.getValue()).append("*").toString()));
    }

    public static QueryExp anySubString(AttributeValueExp attributeValueExp, StringValueExp stringValueExp) {
        return new MatchQueryExp(attributeValueExp, new StringValueExp(new StringBuffer().append("*").append(stringValueExp.getValue()).append("*").toString()));
    }

    public static QueryExp finalSubString(AttributeValueExp attributeValueExp, StringValueExp stringValueExp) {
        return new MatchQueryExp(attributeValueExp, new StringValueExp(new StringBuffer().append("*").append(stringValueExp.getValue()).toString()));
    }
}
