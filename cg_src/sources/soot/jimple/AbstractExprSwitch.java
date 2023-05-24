package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/AbstractExprSwitch.class */
public abstract class AbstractExprSwitch<T> implements ExprSwitch {
    T result;

    @Override // soot.jimple.ExprSwitch
    public void caseAddExpr(AddExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseAndExpr(AndExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmpExpr(CmpExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmpgExpr(CmpgExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmplExpr(CmplExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseDivExpr(DivExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseEqExpr(EqExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNeExpr(NeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseGeExpr(GeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseGtExpr(GtExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLeExpr(LeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLtExpr(LtExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseMulExpr(MulExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseOrExpr(OrExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseRemExpr(RemExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseShlExpr(ShlExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseShrExpr(ShrExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseUshrExpr(UshrExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseSubExpr(SubExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseXorExpr(XorExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseStaticInvokeExpr(StaticInvokeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCastExpr(CastExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseInstanceOfExpr(InstanceOfExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewArrayExpr(NewArrayExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewExpr(NewExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLengthExpr(LengthExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNegExpr(NegExpr v) {
        defaultCase(v);
    }

    @Override // soot.jimple.ExprSwitch, soot.jimple.ConstantSwitch
    public void defaultCase(Object obj) {
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }
}
