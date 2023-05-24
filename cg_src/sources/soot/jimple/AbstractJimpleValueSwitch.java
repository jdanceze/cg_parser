package soot.jimple;

import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/jimple/AbstractJimpleValueSwitch.class */
public abstract class AbstractJimpleValueSwitch<T> extends AbstractExprSwitch<T> implements JimpleValueSwitch {
    @Override // soot.jimple.ImmediateSwitch
    public void caseLocal(Local v) {
        defaultCase(v);
    }

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

    @Override // soot.jimple.RefSwitch
    public void caseArrayRef(ArrayRef v) {
        defaultCase(v);
    }

    @Override // soot.jimple.RefSwitch
    public void caseStaticFieldRef(StaticFieldRef v) {
        defaultCase(v);
    }

    @Override // soot.jimple.RefSwitch
    public void caseInstanceFieldRef(InstanceFieldRef v) {
        defaultCase(v);
    }

    @Override // soot.jimple.RefSwitch
    public void caseParameterRef(ParameterRef v) {
        defaultCase(v);
    }

    @Override // soot.jimple.RefSwitch
    public void caseCaughtExceptionRef(CaughtExceptionRef v) {
        defaultCase(v);
    }

    @Override // soot.jimple.RefSwitch
    public void caseThisRef(ThisRef v) {
        defaultCase(v);
    }
}
