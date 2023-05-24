package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/AbstractRefSwitch.class */
public abstract class AbstractRefSwitch<T> implements RefSwitch {
    T result;

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

    @Override // soot.jimple.RefSwitch
    public void defaultCase(Object obj) {
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }
}
