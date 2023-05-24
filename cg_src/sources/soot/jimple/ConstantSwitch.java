package soot.jimple;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/ConstantSwitch.class */
public interface ConstantSwitch extends Switch {
    void caseDoubleConstant(DoubleConstant doubleConstant);

    void caseFloatConstant(FloatConstant floatConstant);

    void caseIntConstant(IntConstant intConstant);

    void caseLongConstant(LongConstant longConstant);

    void caseNullConstant(NullConstant nullConstant);

    void caseStringConstant(StringConstant stringConstant);

    void caseClassConstant(ClassConstant classConstant);

    void caseMethodHandle(MethodHandle methodHandle);

    void caseMethodType(MethodType methodType);

    void defaultCase(Object obj);
}
