package soot.baf.toolkits.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.JavaBasicTypes;
import soot.LongType;
import soot.PatchingChain;
import soot.RefLikeType;
import soot.RefType;
import soot.SootMethod;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.baf.AddInst;
import soot.baf.AndInst;
import soot.baf.ArrayLengthInst;
import soot.baf.ArrayReadInst;
import soot.baf.ArrayWriteInst;
import soot.baf.BafBody;
import soot.baf.CmpInst;
import soot.baf.CmpgInst;
import soot.baf.CmplInst;
import soot.baf.DivInst;
import soot.baf.Dup1Inst;
import soot.baf.Dup1_x1Inst;
import soot.baf.Dup1_x2Inst;
import soot.baf.Dup2Inst;
import soot.baf.Dup2_x1Inst;
import soot.baf.Dup2_x2Inst;
import soot.baf.DynamicInvokeInst;
import soot.baf.EnterMonitorInst;
import soot.baf.ExitMonitorInst;
import soot.baf.FieldGetInst;
import soot.baf.FieldPutInst;
import soot.baf.GotoInst;
import soot.baf.IdentityInst;
import soot.baf.IfCmpEqInst;
import soot.baf.IfCmpGeInst;
import soot.baf.IfCmpGtInst;
import soot.baf.IfCmpLeInst;
import soot.baf.IfCmpLtInst;
import soot.baf.IfCmpNeInst;
import soot.baf.IfEqInst;
import soot.baf.IfGeInst;
import soot.baf.IfGtInst;
import soot.baf.IfLeInst;
import soot.baf.IfLtInst;
import soot.baf.IfNeInst;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.baf.IncInst;
import soot.baf.Inst;
import soot.baf.InstSwitch;
import soot.baf.InstanceCastInst;
import soot.baf.InstanceOfInst;
import soot.baf.InterfaceInvokeInst;
import soot.baf.JSRInst;
import soot.baf.LoadInst;
import soot.baf.LookupSwitchInst;
import soot.baf.MethodArgInst;
import soot.baf.MulInst;
import soot.baf.NegInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.NopInst;
import soot.baf.OpTypeArgInst;
import soot.baf.OrInst;
import soot.baf.PopInst;
import soot.baf.PrimitiveCastInst;
import soot.baf.PushInst;
import soot.baf.RemInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.baf.ShlInst;
import soot.baf.ShrInst;
import soot.baf.SpecialInvokeInst;
import soot.baf.StaticGetInst;
import soot.baf.StaticInvokeInst;
import soot.baf.StaticPutInst;
import soot.baf.StoreInst;
import soot.baf.SubInst;
import soot.baf.SwapInst;
import soot.baf.TableSwitchInst;
import soot.baf.TargetArgInst;
import soot.baf.ThrowInst;
import soot.baf.UshrInst;
import soot.baf.VirtualInvokeInst;
import soot.baf.XorInst;
import soot.baf.internal.AbstractOpTypeInst;
import soot.baf.internal.BPopInst;
import soot.coffi.Instruction;
import soot.toolkits.graph.BriefUnitGraph;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/OpStackCalculator.class */
public class OpStackCalculator {
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !OpStackCalculator.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(OpStackCalculator.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/OpStackCalculator$StackEffectSwitch.class */
    public static class StackEffectSwitch implements InstSwitch {
        public boolean shouldThrow;
        public Type[] remove_types;
        public Type[] add_types;

        private StackEffectSwitch() {
            this.shouldThrow = true;
            this.remove_types = null;
            this.add_types = null;
        }

        /* synthetic */ StackEffectSwitch(StackEffectSwitch stackEffectSwitch) {
            this();
        }

        private static RefLikeType arrayRefType() {
            return RefType.v();
        }

        @Override // soot.baf.InstSwitch
        public void caseReturnInst(ReturnInst i) {
            this.remove_types = new Type[]{i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseReturnVoidInst(ReturnVoidInst i) {
            this.remove_types = null;
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseNopInst(NopInst i) {
            this.remove_types = null;
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseGotoInst(GotoInst i) {
            this.remove_types = null;
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseJSRInst(JSRInst i) {
            this.remove_types = null;
            this.add_types = new Type[]{StmtAddressType.v()};
        }

        @Override // soot.baf.InstSwitch
        public void casePushInst(PushInst i) {
            this.remove_types = null;
            this.add_types = new Type[]{i.getConstant().getType()};
        }

        @Override // soot.baf.InstSwitch
        public void casePopInst(PopInst i) {
            this.remove_types = new Type[]{((BPopInst) i).getType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIdentityInst(IdentityInst i) {
            this.remove_types = null;
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseStoreInst(StoreInst i) {
            this.remove_types = new Type[]{((AbstractOpTypeInst) i).getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseLoadInst(LoadInst i) {
            this.remove_types = null;
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseArrayWriteInst(ArrayWriteInst i) {
            this.remove_types = new Type[]{arrayRefType(), IntType.v(), i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseArrayReadInst(ArrayReadInst i) {
            this.remove_types = new Type[]{arrayRefType(), IntType.v()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseIfNullInst(IfNullInst i) {
            this.remove_types = new Type[]{RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfNonNullInst(IfNonNullInst i) {
            this.remove_types = new Type[]{RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfEqInst(IfEqInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfNeInst(IfNeInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfGtInst(IfGtInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfGeInst(IfGeInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfLtInst(IfLtInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfLeInst(IfLeInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpEqInst(IfCmpEqInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpNeInst(IfCmpNeInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpGtInst(IfCmpGtInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpGeInst(IfCmpGeInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpLtInst(IfCmpLtInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpLeInst(IfCmpLeInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseStaticGetInst(StaticGetInst i) {
            this.remove_types = null;
            this.add_types = new Type[]{i.getField().getType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseStaticPutInst(StaticPutInst i) {
            this.remove_types = new Type[]{i.getField().getType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseFieldGetInst(FieldGetInst i) {
            this.remove_types = new Type[]{i.getField().getDeclaringClass().getType()};
            this.add_types = new Type[]{i.getField().getType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseFieldPutInst(FieldPutInst i) {
            this.remove_types = new Type[]{i.getField().getDeclaringClass().getType(), i.getField().getType()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseInstanceCastInst(InstanceCastInst i) {
            this.remove_types = new Type[]{RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)};
            this.add_types = new Type[]{i.getCastType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseInstanceOfInst(InstanceOfInst i) {
            this.remove_types = new Type[]{RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)};
            this.add_types = new Type[]{IntType.v()};
        }

        @Override // soot.baf.InstSwitch
        public void casePrimitiveCastInst(PrimitiveCastInst i) {
            this.remove_types = new Type[]{i.getFromType()};
            this.add_types = new Type[]{i.getToType()};
        }

        private void staticinvoke(MethodArgInst i) {
            SootMethod m = i.getMethod();
            int len = m.getParameterCount();
            this.remove_types = new Type[len];
            System.arraycopy(m.getParameterTypes().toArray(), 0, this.remove_types, 0, len);
            Type retTy = m.getReturnType();
            this.add_types = retTy instanceof VoidType ? null : new Type[]{retTy};
        }

        private void instanceinvoke(MethodArgInst i) {
            SootMethod m = i.getMethod();
            int len = m.getParameterCount();
            this.remove_types = new Type[len + 1];
            this.remove_types[0] = RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT);
            System.arraycopy(m.getParameterTypes().toArray(), 0, this.remove_types, 1, len);
            Type retTy = m.getReturnType();
            this.add_types = retTy instanceof VoidType ? null : new Type[]{retTy};
        }

        @Override // soot.baf.InstSwitch
        public void caseDynamicInvokeInst(DynamicInvokeInst i) {
            staticinvoke(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseStaticInvokeInst(StaticInvokeInst i) {
            staticinvoke(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseVirtualInvokeInst(VirtualInvokeInst i) {
            instanceinvoke(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
            instanceinvoke(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseSpecialInvokeInst(SpecialInvokeInst i) {
            instanceinvoke(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseThrowInst(ThrowInst i) {
            this.remove_types = new Type[]{RefType.v("java.lang.Throwable")};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseAddInst(AddInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{i.getOpType()};
        }

        private void bitOps(OpTypeArgInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseAndInst(AndInst i) {
            bitOps(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseOrInst(OrInst i) {
            bitOps(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseXorInst(XorInst i) {
            bitOps(i);
        }

        @Override // soot.baf.InstSwitch
        public void caseArrayLengthInst(ArrayLengthInst i) {
            this.remove_types = new Type[]{arrayRefType()};
            this.add_types = new Type[]{IntType.v()};
        }

        @Override // soot.baf.InstSwitch
        public void caseCmpInst(CmpInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{IntType.v()};
        }

        @Override // soot.baf.InstSwitch
        public void caseCmpgInst(CmpgInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{IntType.v()};
        }

        @Override // soot.baf.InstSwitch
        public void caseCmplInst(CmplInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{IntType.v()};
        }

        @Override // soot.baf.InstSwitch
        public void caseDivInst(DivInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseIncInst(IncInst i) {
            this.remove_types = null;
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseMulInst(MulInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseRemInst(RemInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseSubInst(SubInst i) {
            this.remove_types = new Type[]{i.getOpType(), i.getOpType()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseShlInst(ShlInst i) {
            this.remove_types = new Type[]{i.getOpType(), IntType.v()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseShrInst(ShrInst i) {
            this.remove_types = new Type[]{i.getOpType(), IntType.v()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseUshrInst(UshrInst i) {
            this.remove_types = new Type[]{i.getOpType(), IntType.v()};
            this.add_types = new Type[]{i.getOpType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseNewInst(NewInst i) {
            this.remove_types = null;
            this.add_types = new Type[]{i.getBaseType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseNegInst(NegInst i) {
            this.remove_types = null;
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseSwapInst(SwapInst i) {
            this.remove_types = new Type[]{i.getFromType(), i.getToType()};
            this.add_types = new Type[]{i.getToType(), i.getFromType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseDup1Inst(Dup1Inst i) {
            this.remove_types = new Type[]{i.getOp1Type()};
            this.add_types = new Type[]{i.getOp1Type(), i.getOp1Type()};
        }

        @Override // soot.baf.InstSwitch
        public void caseDup2Inst(Dup2Inst i) {
            if (!(i.getOp1Type() instanceof DoubleType) && !(i.getOp1Type() instanceof LongType)) {
                this.add_types = new Type[]{i.getOp2Type(), i.getOp1Type()};
                this.remove_types = null;
                return;
            }
            this.add_types = new Type[]{i.getOp1Type()};
            this.remove_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseDup1_x1Inst(Dup1_x1Inst i) {
            this.remove_types = new Type[]{i.getUnder1Type(), i.getOp1Type()};
            this.add_types = new Type[]{i.getOp1Type(), i.getUnder1Type(), i.getOp1Type()};
        }

        @Override // soot.baf.InstSwitch
        public void caseDup1_x2Inst(Dup1_x2Inst i) {
            Type u1 = i.getUnder1Type();
            if ((u1 instanceof DoubleType) || (u1 instanceof LongType)) {
                this.remove_types = new Type[]{u1, i.getOp1Type()};
                this.add_types = new Type[]{i.getOp1Type(), u1, i.getOp1Type()};
                return;
            }
            this.remove_types = new Type[]{i.getUnder2Type(), u1, i.getOp1Type()};
            this.add_types = new Type[]{i.getOp1Type(), i.getUnder2Type(), u1, i.getOp1Type()};
        }

        @Override // soot.baf.InstSwitch
        public void caseDup2_x1Inst(Dup2_x1Inst i) {
            Type ot = i.getOp1Type();
            if ((ot instanceof DoubleType) || (ot instanceof LongType)) {
                this.remove_types = new Type[]{i.getUnder1Type(), ot};
                this.add_types = new Type[]{ot, i.getUnder1Type(), ot};
                return;
            }
            this.remove_types = new Type[]{i.getUnder1Type(), i.getOp2Type(), ot};
            this.add_types = new Type[]{i.getOp2Type(), ot, i.getUnder1Type(), i.getOp2Type(), ot};
        }

        @Override // soot.baf.InstSwitch
        public void caseDup2_x2Inst(Dup2_x2Inst i) {
            Type u1 = i.getUnder1Type();
            Type o1 = i.getOp1Type();
            if ((u1 instanceof DoubleType) || (u1 instanceof LongType)) {
                if ((o1 instanceof DoubleType) || (o1 instanceof LongType)) {
                    this.remove_types = new Type[]{u1, o1};
                    this.add_types = new Type[]{o1, u1, o1};
                    return;
                }
                this.remove_types = new Type[]{u1, i.getOp2Type(), o1};
                this.add_types = new Type[]{i.getOp2Type(), o1, u1, i.getOp2Type(), o1};
            } else if ((o1 instanceof DoubleType) || (o1 instanceof LongType)) {
                this.remove_types = new Type[]{i.getUnder2Type(), u1, o1};
                this.add_types = new Type[]{o1, i.getUnder2Type(), u1, o1};
            } else {
                this.remove_types = new Type[]{i.getUnder2Type(), u1, i.getOp2Type(), o1};
                this.add_types = new Type[]{i.getOp2Type(), o1, i.getUnder2Type(), u1, i.getOp2Type(), o1};
            }
        }

        @Override // soot.baf.InstSwitch
        public void caseNewArrayInst(NewArrayInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = new Type[]{arrayRefType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseNewMultiArrayInst(NewMultiArrayInst i) {
            int size = i.getDimensionCount();
            this.remove_types = new Type[size];
            Arrays.fill(this.remove_types, 0, size, IntType.v());
            this.add_types = new Type[]{arrayRefType()};
        }

        @Override // soot.baf.InstSwitch
        public void caseLookupSwitchInst(LookupSwitchInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseTableSwitchInst(TableSwitchInst i) {
            this.remove_types = new Type[]{IntType.v()};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseEnterMonitorInst(EnterMonitorInst i) {
            this.remove_types = new Type[]{RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)};
            this.add_types = null;
        }

        @Override // soot.baf.InstSwitch
        public void caseExitMonitorInst(ExitMonitorInst i) {
            this.remove_types = new Type[]{RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)};
            this.add_types = null;
        }
    }

    public static Map<Unit, Stack<Type>> calculateStacks(BafBody b) {
        Map<Unit, Stack<Type>> results = new IdentityHashMap<>();
        StackEffectSwitch sw = new StackEffectSwitch(null);
        BriefUnitGraph bug = new BriefUnitGraph(b);
        for (Unit h : bug.getHeads()) {
            RefType handlerExc = isHandlerUnit(b.getTraps(), h);
            Stack<Type> stack = results.get(h);
            if (stack == null) {
                Stack<Type> stack2 = new Stack<>();
                if (handlerExc != null) {
                    stack2.push(handlerExc);
                }
                results.put(h, stack2);
                List<Unit> worklist = new ArrayList<>();
                worklist.add(h);
                while (!worklist.isEmpty()) {
                    Inst inst = (Inst) worklist.remove(0);
                    inst.apply(sw);
                    Stack<Type> stack3 = updateStack(sw, results.get(inst));
                    for (Unit next : bug.getSuccsOf((Unit) inst)) {
                        Stack<Type> nxtStck = results.get(next);
                        if (nxtStck != null) {
                            if (nxtStck.size() != stack3.size()) {
                                printStack(sw, b.getUnits(), results);
                                throw new RuntimeException("Problem with stack height at: " + next + "\n\rHas Stack " + nxtStck + " but is expecting " + stack3);
                            }
                        } else {
                            results.put(next, stack3);
                            worklist.add(next);
                        }
                    }
                }
                continue;
            } else {
                if (stack.size() != (handlerExc != null ? 1 : 0)) {
                    throw new RuntimeException("Problem with stack height - head expects 0 (or 1 if exception handler)");
                }
            }
        }
        return results;
    }

    private static Stack<Type> updateStack(StackEffectSwitch sw, Stack<Type> st) {
        Stack<Type> clone = (Stack) st.clone();
        Type[] remove_types = sw.remove_types;
        if (remove_types != null) {
            if (remove_types.length > clone.size()) {
                StringBuilder exc = new StringBuilder();
                exc.append("Expecting values on stack: ");
                for (Type element : remove_types) {
                    String type = element.toString();
                    if (type.trim().isEmpty()) {
                        type = element instanceof RefLikeType ? "L" : "U";
                    }
                    exc.append(type).append("  ");
                }
                exc.append("\n\tbut only found: ");
                Iterator<Type> it = clone.iterator();
                while (it.hasNext()) {
                    Type element2 = it.next();
                    String type2 = element2.toString();
                    if (type2.trim().isEmpty()) {
                        type2 = element2 instanceof RefLikeType ? "L" : "U";
                    }
                    exc.append(type2).append("  ");
                }
                if (sw.shouldThrow) {
                    throw new RuntimeException(exc.toString());
                }
                logger.debug(exc.toString());
            }
            for (int i = remove_types.length - 1; i >= 0; i--) {
                try {
                    Type t = clone.pop();
                    if (!$assertionsDisabled && !typesAreCompatible(t, remove_types[i])) {
                        throw new AssertionError();
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        }
        if (sw.add_types != null) {
            for (Type element3 : sw.add_types) {
                clone.push(element3);
            }
        }
        return clone;
    }

    private static boolean typesAreCompatible(Type t1, Type t2) {
        if (t1 != t2) {
            if ((t1 instanceof RefLikeType) && (t2 instanceof RefLikeType)) {
                return true;
            }
            if ((t1 instanceof IntegerType) && (t2 instanceof IntegerType)) {
                return true;
            }
            if ((t1 instanceof LongType) && (t2 instanceof LongType)) {
                return true;
            }
            if ((t1 instanceof DoubleType) && (t2 instanceof DoubleType)) {
                return true;
            }
            if ((t1 instanceof FloatType) && (t2 instanceof FloatType)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private static RefType isHandlerUnit(Chain<Trap> traps, Unit h) {
        for (Trap t : traps) {
            if (t.getHandlerUnit() == h) {
                return t.getException().getType();
            }
        }
        return null;
    }

    private static void printStack(StackEffectSwitch sw, PatchingChain<Unit> units, Map<Unit, Stack<Type>> stacks) {
        try {
            sw.shouldThrow = false;
            Map<Unit, Integer> indexes = new HashMap<>();
            int count = 0;
            Iterator<Unit> it = units.snapshotIterator();
            while (it.hasNext()) {
                int i = count;
                count++;
                indexes.put(it.next(), Integer.valueOf(i));
            }
            Iterator<Unit> it2 = units.snapshotIterator();
            while (it2.hasNext()) {
                Unit unit = it2.next();
                StringBuilder s = new StringBuilder();
                try {
                    s.append(indexes.get(unit)).append(' ').append(unit).append("  ");
                } catch (Exception e) {
                    logger.debug("Error in OpStackCalculator trying to find index of unit");
                }
                if (unit instanceof TargetArgInst) {
                    s.append(indexes.get(((TargetArgInst) unit).getTarget()));
                } else if (unit instanceof TableSwitchInst) {
                    TableSwitchInst tswi = (TableSwitchInst) unit;
                    s.append("\r\tdefault: ").append(tswi.getDefaultTarget());
                    s.append("  ").append(indexes.get(tswi.getDefaultTarget()));
                    int index = 0;
                    int e2 = tswi.getHighIndex();
                    for (int x = tswi.getLowIndex(); x <= e2; x++) {
                        s.append("\r\t ").append(x).append(": ").append(tswi.getTarget(index));
                        int i2 = index;
                        index++;
                        s.append("  ").append(indexes.get(tswi.getTarget(i2)));
                    }
                }
                s.append("   [");
                Stack<Type> stack = stacks.get(unit);
                if (stack != null) {
                    unit.apply(sw);
                    Stack<Type> stack2 = updateStack(sw, stack);
                    if (stack2 == null) {
                        printUnits(units, " StackTypeHeightCalc failed");
                        return;
                    }
                    int e3 = stack2.size();
                    for (int i3 = 0; i3 < e3; i3++) {
                        s.append(printType(stack2.get(i3)));
                    }
                } else {
                    s.append("***missing***");
                }
                s.append(']');
                System.out.println(s);
            }
        } finally {
            sw.shouldThrow = true;
        }
    }

    private static String printType(Type t) {
        if (t instanceof IntegerType) {
            return "I";
        }
        if (t instanceof FloatType) {
            return "F";
        }
        if (t instanceof DoubleType) {
            return "D";
        }
        if (t instanceof LongType) {
            return "J";
        }
        if (t instanceof RefLikeType) {
            return "L" + t.toString();
        }
        return "U(" + t.getClass().toString() + ")";
    }

    private static void printUnits(PatchingChain<Unit> u, String msg) {
        System.out.println("\r\r***********  " + msg);
        HashMap<Unit, Integer> numbers = new HashMap<>();
        int i = 0;
        Iterator<Unit> it = u.snapshotIterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            numbers.put(it.next(), Integer.valueOf(i2));
        }
        Iterator<Unit> udit = u.snapshotIterator();
        while (udit.hasNext()) {
            Unit unit = udit.next();
            Integer numb = numbers.get(unit);
            if (unit instanceof TargetArgInst) {
                TargetArgInst ti = (TargetArgInst) unit;
                if (ti.getTarget() == null) {
                    System.out.println(unit + " null null null null null null null null null");
                } else {
                    System.out.println(numb + Instruction.argsep + unit + "   #" + numbers.get(ti.getTarget()));
                }
            } else if (unit instanceof TableSwitchInst) {
                TableSwitchInst tswi = (TableSwitchInst) unit;
                System.out.println(numb + " SWITCH:");
                System.out.println("\tdefault: " + tswi.getDefaultTarget() + "  " + numbers.get(tswi.getDefaultTarget()));
                int idx = 0;
                int e = tswi.getHighIndex();
                for (int x = tswi.getLowIndex(); x <= e; x++) {
                    int i3 = idx;
                    idx++;
                    System.out.println("\t " + x + ": " + tswi.getTarget(idx) + "  " + numbers.get(tswi.getTarget(i3)));
                }
            } else {
                System.out.println(numb + Instruction.argsep + unit);
            }
        }
    }

    private OpStackCalculator() {
    }
}
