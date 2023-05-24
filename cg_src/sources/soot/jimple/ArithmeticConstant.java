package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/ArithmeticConstant.class */
public abstract class ArithmeticConstant extends NumericConstant {
    public abstract ArithmeticConstant and(ArithmeticConstant arithmeticConstant);

    public abstract ArithmeticConstant or(ArithmeticConstant arithmeticConstant);

    public abstract ArithmeticConstant xor(ArithmeticConstant arithmeticConstant);

    public abstract ArithmeticConstant shiftLeft(ArithmeticConstant arithmeticConstant);

    public abstract ArithmeticConstant shiftRight(ArithmeticConstant arithmeticConstant);

    public abstract ArithmeticConstant unsignedShiftRight(ArithmeticConstant arithmeticConstant);
}
