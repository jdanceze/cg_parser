package javax.management;

import org.apache.commons.cli.HelpFormatter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/BinaryOpValueExp.class */
class BinaryOpValueExp extends QueryEval implements ValueExp {
    private static final long serialVersionUID = 1216286847881456786L;
    private int op;
    private ValueExp exp1;
    private ValueExp exp2;

    public BinaryOpValueExp() {
    }

    public BinaryOpValueExp(int i, ValueExp valueExp, ValueExp valueExp2) {
        this.op = i;
        this.exp1 = valueExp;
        this.exp2 = valueExp2;
    }

    public int getOperator() {
        return this.op;
    }

    public ValueExp getLeftValue() {
        return this.exp1;
    }

    public ValueExp getRightValue() {
        return this.exp2;
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        ValueExp apply = this.exp1.apply(objectName);
        ValueExp apply2 = this.exp2.apply(objectName);
        if (apply instanceof NumericValueExp) {
            if (((NumericValueExp) apply).isLong()) {
                long longValue = ((NumericValueExp) apply).longValue();
                long longValue2 = ((NumericValueExp) apply2).longValue();
                switch (this.op) {
                    case 0:
                        return Query.value(longValue + longValue2);
                    case 1:
                        return Query.value(longValue - longValue2);
                    case 2:
                        return Query.value(longValue * longValue2);
                    case 3:
                        return Query.value(longValue / longValue2);
                }
            }
            double doubleValue = ((NumericValueExp) apply).doubleValue();
            double doubleValue2 = ((NumericValueExp) apply2).doubleValue();
            switch (this.op) {
                case 0:
                    return Query.value(doubleValue + doubleValue2);
                case 1:
                    return Query.value(doubleValue - doubleValue2);
                case 2:
                    return Query.value(doubleValue * doubleValue2);
                case 3:
                    return Query.value(doubleValue / doubleValue2);
            }
            throw new BadBinaryOpValueExpException(this);
        }
        String value = ((StringValueExp) apply).getValue();
        String value2 = ((StringValueExp) apply2).getValue();
        switch (this.op) {
            case 0:
                return new StringValueExp(new StringBuffer().append(value).append(value2).toString());
            default:
                throw new BadStringOperationException(opString());
        }
    }

    public String toString() {
        try {
            return new StringBuffer().append(this.exp1).append(Instruction.argsep).append(opString()).append(Instruction.argsep).append(this.exp2).toString();
        } catch (BadBinaryOpValueExpException e) {
            return "invalid expression";
        }
    }

    private String opString() throws BadBinaryOpValueExpException {
        switch (this.op) {
            case 0:
                return "+";
            case 1:
                return HelpFormatter.DEFAULT_OPT_PREFIX;
            case 2:
                return "*";
            case 3:
                return "/";
            default:
                throw new BadBinaryOpValueExpException(this);
        }
    }
}
