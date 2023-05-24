package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/NumericConstant.class */
public abstract class NumericConstant extends Constant {
    private static final long serialVersionUID = -2757437961775194243L;

    public abstract NumericConstant add(NumericConstant numericConstant);

    public abstract NumericConstant subtract(NumericConstant numericConstant);

    public abstract NumericConstant multiply(NumericConstant numericConstant);

    public abstract NumericConstant divide(NumericConstant numericConstant);

    public abstract NumericConstant remainder(NumericConstant numericConstant);

    public abstract NumericConstant equalEqual(NumericConstant numericConstant);

    public abstract NumericConstant notEqual(NumericConstant numericConstant);

    public abstract boolean isLessThan(NumericConstant numericConstant);

    public abstract NumericConstant lessThan(NumericConstant numericConstant);

    public abstract NumericConstant lessThanOrEqual(NumericConstant numericConstant);

    public abstract NumericConstant greaterThan(NumericConstant numericConstant);

    public abstract NumericConstant greaterThanOrEqual(NumericConstant numericConstant);

    public abstract NumericConstant negate();
}
