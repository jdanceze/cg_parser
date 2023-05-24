package soot.jimple;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/ExprSwitch.class */
public interface ExprSwitch extends Switch {
    void caseAddExpr(AddExpr addExpr);

    void caseAndExpr(AndExpr andExpr);

    void caseCmpExpr(CmpExpr cmpExpr);

    void caseCmpgExpr(CmpgExpr cmpgExpr);

    void caseCmplExpr(CmplExpr cmplExpr);

    void caseDivExpr(DivExpr divExpr);

    void caseEqExpr(EqExpr eqExpr);

    void caseNeExpr(NeExpr neExpr);

    void caseGeExpr(GeExpr geExpr);

    void caseGtExpr(GtExpr gtExpr);

    void caseLeExpr(LeExpr leExpr);

    void caseLtExpr(LtExpr ltExpr);

    void caseMulExpr(MulExpr mulExpr);

    void caseOrExpr(OrExpr orExpr);

    void caseRemExpr(RemExpr remExpr);

    void caseShlExpr(ShlExpr shlExpr);

    void caseShrExpr(ShrExpr shrExpr);

    void caseUshrExpr(UshrExpr ushrExpr);

    void caseSubExpr(SubExpr subExpr);

    void caseXorExpr(XorExpr xorExpr);

    void caseInterfaceInvokeExpr(InterfaceInvokeExpr interfaceInvokeExpr);

    void caseSpecialInvokeExpr(SpecialInvokeExpr specialInvokeExpr);

    void caseStaticInvokeExpr(StaticInvokeExpr staticInvokeExpr);

    void caseVirtualInvokeExpr(VirtualInvokeExpr virtualInvokeExpr);

    void caseDynamicInvokeExpr(DynamicInvokeExpr dynamicInvokeExpr);

    void caseCastExpr(CastExpr castExpr);

    void caseInstanceOfExpr(InstanceOfExpr instanceOfExpr);

    void caseNewArrayExpr(NewArrayExpr newArrayExpr);

    void caseNewMultiArrayExpr(NewMultiArrayExpr newMultiArrayExpr);

    void caseNewExpr(NewExpr newExpr);

    void caseLengthExpr(LengthExpr lengthExpr);

    void caseNegExpr(NegExpr negExpr);

    void defaultCase(Object obj);
}
