package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/AbstractConstantSwitch.class */
public abstract class AbstractConstantSwitch<T> implements ConstantSwitch {
    T result;

    @Override // soot.jimple.ConstantSwitch
    public void caseDoubleConstant(DoubleConstant v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseFloatConstant(FloatConstant v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseIntConstant(IntConstant v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseLongConstant(LongConstant v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseNullConstant(NullConstant v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseStringConstant(StringConstant v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseClassConstant(ClassConstant v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseMethodHandle(MethodHandle v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void caseMethodType(MethodType v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ConstantSwitch
    public void defaultCase(Object v) {
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }
}
