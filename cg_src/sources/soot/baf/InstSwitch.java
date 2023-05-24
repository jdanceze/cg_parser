package soot.baf;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/InstSwitch.class */
public interface InstSwitch extends Switch {
    void caseReturnVoidInst(ReturnVoidInst returnVoidInst);

    void caseReturnInst(ReturnInst returnInst);

    void caseNopInst(NopInst nopInst);

    void caseGotoInst(GotoInst gotoInst);

    void caseJSRInst(JSRInst jSRInst);

    void casePushInst(PushInst pushInst);

    void casePopInst(PopInst popInst);

    void caseIdentityInst(IdentityInst identityInst);

    void caseStoreInst(StoreInst storeInst);

    void caseLoadInst(LoadInst loadInst);

    void caseArrayWriteInst(ArrayWriteInst arrayWriteInst);

    void caseArrayReadInst(ArrayReadInst arrayReadInst);

    void caseIfNullInst(IfNullInst ifNullInst);

    void caseIfNonNullInst(IfNonNullInst ifNonNullInst);

    void caseIfEqInst(IfEqInst ifEqInst);

    void caseIfNeInst(IfNeInst ifNeInst);

    void caseIfGtInst(IfGtInst ifGtInst);

    void caseIfGeInst(IfGeInst ifGeInst);

    void caseIfLtInst(IfLtInst ifLtInst);

    void caseIfLeInst(IfLeInst ifLeInst);

    void caseIfCmpEqInst(IfCmpEqInst ifCmpEqInst);

    void caseIfCmpNeInst(IfCmpNeInst ifCmpNeInst);

    void caseIfCmpGtInst(IfCmpGtInst ifCmpGtInst);

    void caseIfCmpGeInst(IfCmpGeInst ifCmpGeInst);

    void caseIfCmpLtInst(IfCmpLtInst ifCmpLtInst);

    void caseIfCmpLeInst(IfCmpLeInst ifCmpLeInst);

    void caseStaticGetInst(StaticGetInst staticGetInst);

    void caseStaticPutInst(StaticPutInst staticPutInst);

    void caseFieldGetInst(FieldGetInst fieldGetInst);

    void caseFieldPutInst(FieldPutInst fieldPutInst);

    void caseInstanceCastInst(InstanceCastInst instanceCastInst);

    void caseInstanceOfInst(InstanceOfInst instanceOfInst);

    void casePrimitiveCastInst(PrimitiveCastInst primitiveCastInst);

    void caseDynamicInvokeInst(DynamicInvokeInst dynamicInvokeInst);

    void caseStaticInvokeInst(StaticInvokeInst staticInvokeInst);

    void caseVirtualInvokeInst(VirtualInvokeInst virtualInvokeInst);

    void caseInterfaceInvokeInst(InterfaceInvokeInst interfaceInvokeInst);

    void caseSpecialInvokeInst(SpecialInvokeInst specialInvokeInst);

    void caseThrowInst(ThrowInst throwInst);

    void caseAddInst(AddInst addInst);

    void caseAndInst(AndInst andInst);

    void caseOrInst(OrInst orInst);

    void caseXorInst(XorInst xorInst);

    void caseArrayLengthInst(ArrayLengthInst arrayLengthInst);

    void caseCmpInst(CmpInst cmpInst);

    void caseCmpgInst(CmpgInst cmpgInst);

    void caseCmplInst(CmplInst cmplInst);

    void caseDivInst(DivInst divInst);

    void caseIncInst(IncInst incInst);

    void caseMulInst(MulInst mulInst);

    void caseRemInst(RemInst remInst);

    void caseSubInst(SubInst subInst);

    void caseShlInst(ShlInst shlInst);

    void caseShrInst(ShrInst shrInst);

    void caseUshrInst(UshrInst ushrInst);

    void caseNewInst(NewInst newInst);

    void caseNegInst(NegInst negInst);

    void caseSwapInst(SwapInst swapInst);

    void caseDup1Inst(Dup1Inst dup1Inst);

    void caseDup2Inst(Dup2Inst dup2Inst);

    void caseDup1_x1Inst(Dup1_x1Inst dup1_x1Inst);

    void caseDup1_x2Inst(Dup1_x2Inst dup1_x2Inst);

    void caseDup2_x1Inst(Dup2_x1Inst dup2_x1Inst);

    void caseDup2_x2Inst(Dup2_x2Inst dup2_x2Inst);

    void caseNewArrayInst(NewArrayInst newArrayInst);

    void caseNewMultiArrayInst(NewMultiArrayInst newMultiArrayInst);

    void caseLookupSwitchInst(LookupSwitchInst lookupSwitchInst);

    void caseTableSwitchInst(TableSwitchInst tableSwitchInst);

    void caseEnterMonitorInst(EnterMonitorInst enterMonitorInst);

    void caseExitMonitorInst(ExitMonitorInst exitMonitorInst);
}
