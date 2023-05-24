package soot.baf.toolkits.base;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import soot.Body;
import soot.DoubleType;
import soot.ErroneousType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefLikeType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
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
import soot.baf.MulInst;
import soot.baf.NegInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.NopInst;
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
import soot.baf.ThrowInst;
import soot.baf.UshrInst;
import soot.baf.VirtualInvokeInst;
import soot.baf.XorInst;
import soot.jimple.IdentityRef;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/StackTypesValidator.class */
public enum StackTypesValidator implements BodyValidator {
    INSTANCE;
    
    static final /* synthetic */ boolean $assertionsDisabled;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static StackTypesValidator[] valuesCustom() {
        StackTypesValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        StackTypesValidator[] stackTypesValidatorArr = new StackTypesValidator[length];
        System.arraycopy(valuesCustom, 0, stackTypesValidatorArr, 0, length);
        return stackTypesValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    static {
        $assertionsDisabled = !StackTypesValidator.class.desiredAssertionStatus();
    }

    public static StackTypesValidator v() {
        return INSTANCE;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        if (!$assertionsDisabled && !(body instanceof BafBody)) {
            throw new AssertionError();
        }
        VMStateAnalysis a = new VMStateAnalysis((BafBody) body, exceptions);
        InstSwitch verif = a.createVerifier();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            u.apply(verif);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/StackTypesValidator$BitArray.class */
    public static final class BitArray implements Cloneable {
        public static final int BITS_PER_VAL = 4;
        public static final int VALS_PER_IDX = 8;
        public static final int VAL_MASK = 15;
        private final int[] data;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !StackTypesValidator.class.desiredAssertionStatus();
        }

        public BitArray(int numValues) {
            if (!$assertionsDisabled && numValues < 0) {
                throw new AssertionError();
            }
            this.data = new int[(numValues / 8) + (numValues % 8 == 0 ? 0 : 1)];
        }

        private BitArray(int[] otherData) {
            int count = otherData.length;
            int[] temp = new int[count];
            System.arraycopy(otherData, 0, temp, 0, count);
            this.data = temp;
        }

        public int get(int index) {
            int arrIdx = index / 8;
            int bitShift = (index % 8) * 4;
            return (this.data[arrIdx] >>> bitShift) & 15;
        }

        public void set(int index, int value) {
            if ((value & 15) != value) {
                throw new IllegalArgumentException(String.valueOf(value) + " does not fit in 4 bits!");
            }
            int arrIdx = index / 8;
            int bitShift = (index % 8) * 4;
            this.data[arrIdx] = (this.data[arrIdx] & Integer.rotateLeft(-16, bitShift)) | (value << bitShift);
        }

        public int hashCode() {
            return Arrays.hashCode(this.data);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || BitArray.class != obj.getClass()) {
                return false;
            }
            BitArray other = (BitArray) obj;
            return Arrays.equals(this.data, other.data);
        }

        /* renamed from: clone */
        public BitArray m2510clone() {
            return new BitArray(this.data);
        }

        public void copyTo(BitArray dest) {
            if (this != dest) {
                if (!$assertionsDisabled && this.data.length != dest.data.length) {
                    throw new AssertionError();
                }
                System.arraycopy(this.data, 0, dest.data, 0, this.data.length);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/baf/toolkits/base/StackTypesValidator$VMStateAnalysis.class */
    public static final class VMStateAnalysis extends ForwardFlowAnalysis<Unit, BitArray> {
        protected static final Type TYPE_UNK;
        protected static final Type TYPE_REF;
        protected static final Type TYPE_INT;
        protected static final Type TYPE_DUB;
        protected static final Type TYPE_FLT;
        protected static final Type TYPE_LNG;
        protected static final Type TYPE_ERR;
        protected static final int TYPE_UNK_BITS = 0;
        protected static final int TYPE_ERR_BITS = 7;
        protected final List<ValidationException> exceptions;
        protected final Map<Unit, Stack<Type>> opStacks;
        protected final Map<Local, Integer> varToIdx;
        protected final BitArray initFlow;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !StackTypesValidator.class.desiredAssertionStatus();
            TYPE_UNK = UnknownType.v();
            TYPE_REF = RefType.v();
            TYPE_INT = IntType.v();
            TYPE_DUB = DoubleType.v();
            TYPE_FLT = FloatType.v();
            TYPE_LNG = LongType.v();
            TYPE_ERR = ErroneousType.v();
        }

        protected static Type canonicalize(Type t) {
            return ((t instanceof RefLikeType) || (t instanceof NullType)) ? TYPE_REF : t instanceof IntegerType ? TYPE_INT : t;
        }

        protected static int typeToBits(Type type) {
            if (type == TYPE_UNK) {
                return 0;
            }
            if (type == TYPE_INT || (type instanceof IntegerType)) {
                return 2;
            }
            if (type == TYPE_REF || (type instanceof RefLikeType) || (type instanceof NullType)) {
                return 1;
            }
            if (type == TYPE_DUB) {
                return 3;
            }
            if (type == TYPE_FLT) {
                return 4;
            }
            if (type == TYPE_LNG) {
                return 5;
            }
            if (type == TYPE_ERR) {
                return 7;
            }
            throw new IllegalArgumentException(Objects.toString(type));
        }

        protected static Type bitsToType(int bits) {
            switch (bits) {
                case 0:
                    return TYPE_UNK;
                case 1:
                    return TYPE_REF;
                case 2:
                    return TYPE_INT;
                case 3:
                    return TYPE_DUB;
                case 4:
                    return TYPE_FLT;
                case 5:
                    return TYPE_LNG;
                case 6:
                default:
                    throw new IllegalArgumentException(Integer.toString(bits));
                case 7:
                    return TYPE_ERR;
            }
        }

        public VMStateAnalysis(BafBody body, List<ValidationException> exceptions) {
            super(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, PedanticThrowAnalysis.v(), false));
            this.exceptions = exceptions;
            this.opStacks = OpStackCalculator.calculateStacks(body);
            if (!$assertionsDisabled && !this.opStacks.keySet().equals(new HashSet(body.getUnits()))) {
                throw new AssertionError();
            }
            HashMap<Local, Integer> varToIdx = new HashMap<>();
            int varNum = 0;
            for (Local l : body.getLocals()) {
                int i = varNum;
                varNum++;
                varToIdx.put(l, Integer.valueOf(i));
            }
            this.varToIdx = varToIdx;
            this.initFlow = new BitArray(varNum);
            doAnalysis();
        }

        protected static String toString(Type t) {
            return t == TYPE_REF ? "RefType" : t.toString();
        }

        protected int indexOf(Local loc) {
            Integer idx = this.varToIdx.get(loc);
            if ($assertionsDisabled || idx != null) {
                return idx.intValue();
            }
            throw new AssertionError("Unrecognized Local: " + loc);
        }

        protected Type peekStackAt(Unit u) {
            try {
                return this.opStacks.get(u).peek();
            } catch (EmptyStackException e) {
                this.exceptions.add(new ValidationException(u, "Stack is empty!"));
                return ErroneousType.v();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public void flowThrough(BitArray in, Unit u, BitArray out) {
            if (!$assertionsDisabled && !(u instanceof Inst)) {
                throw new AssertionError();
            }
            copy(in, out);
            if (!(u instanceof IdentityInst)) {
                if (u instanceof StoreInst) {
                    int x = indexOf(((StoreInst) u).getLocal());
                    out.set(x, merge(out.get(x), typeToBits(peekStackAt(u)), u));
                    return;
                }
                return;
            }
            IdentityInst i = (IdentityInst) u;
            if (!$assertionsDisabled && !(i.getLeftOp() instanceof Local)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !(i.getRightOp() instanceof IdentityRef)) {
                throw new AssertionError();
            }
            int x2 = indexOf((Local) i.getLeftOp());
            out.set(x2, merge(out.get(x2), typeToBits(i.getRightOp().getType()), u));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public BitArray newInitialFlow() {
            return this.initFlow.m2510clone();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public boolean omissible(Unit u) {
            return ((u instanceof IdentityInst) || (u instanceof StoreInst)) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void copy(BitArray in, BitArray out) {
            in.copyTo(out);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void merge(BitArray in1, BitArray in2, BitArray out) {
            merge((Unit) null, in1, in2, out);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void merge(Unit successor, BitArray in1, BitArray in2, BitArray out) {
            if (in1.equals(in2)) {
                copy(in1, out);
                return;
            }
            int e = this.varToIdx.size();
            for (int i = 0; i < e; i++) {
                out.set(i, merge(in1.get(i), in2.get(i), successor));
            }
        }

        private int merge(int in1, int in2, Unit u) {
            if (in1 == in2) {
                return in1;
            }
            if (in1 == 0) {
                return in2;
            }
            if (in2 == 0) {
                return in1;
            }
            if (in1 == 7 || in2 == 7) {
                return 7;
            }
            this.exceptions.add(new ValidationException(u, "Ambiguous type: '" + toString(bitsToType(in1)) + "' vs '" + toString(bitsToType(in2)) + "'"));
            return 7;
        }

        public InstSwitch createVerifier() {
            return new InstSwitch() { // from class: soot.baf.toolkits.base.StackTypesValidator.VMStateAnalysis.1
                private void checkType(Inst i, Type expect, Type actual) {
                    Type canonExpect = VMStateAnalysis.canonicalize(expect);
                    Type canonActual = VMStateAnalysis.canonicalize(actual);
                    if (!Objects.equals(canonExpect, canonActual)) {
                        VMStateAnalysis.this.exceptions.add(new ValidationException(i, "Expected " + VMStateAnalysis.toString(canonExpect) + " but found " + VMStateAnalysis.toString(canonActual)));
                    }
                }

                private void checkStack1(Inst i, Type expect) {
                    checkType(i, expect, VMStateAnalysis.this.peekStackAt(i));
                }

                private void checkStack2(Inst i, Type expect) {
                    checkStackN(i, expect, 2);
                }

                private void checkStack2(Inst i, Type expect1, Type expect2) {
                    Stack<Type> stk = VMStateAnalysis.this.opStacks.get(i);
                    int idx = stk.size() - 1;
                    checkType(i, expect1, stk.elementAt(idx));
                    checkType(i, expect2, stk.elementAt(idx - 1));
                }

                private void checkStack3(Inst i, Type expect1, Type expect2, Type expect3) {
                    Stack<Type> stk = VMStateAnalysis.this.opStacks.get(i);
                    int idx = stk.size() - 1;
                    checkType(i, expect1, stk.elementAt(idx));
                    int idx2 = idx - 1;
                    checkType(i, expect2, stk.elementAt(idx2));
                    checkType(i, expect3, stk.elementAt(idx2 - 1));
                }

                private void checkStackN(Inst i, Type expect, int count) {
                    Stack<Type> stk = VMStateAnalysis.this.opStacks.get(i);
                    int idx = stk.size();
                    for (int j = 0; j < count; j++) {
                        idx--;
                        checkType(i, expect, stk.elementAt(idx));
                    }
                }

                @Override // soot.baf.InstSwitch
                public void caseIdentityInst(IdentityInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseNopInst(NopInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseGotoInst(GotoInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseJSRInst(JSRInst i) {
                    throw new UnsupportedOperationException("deprecated bytecode");
                }

                @Override // soot.baf.InstSwitch
                public void caseReturnVoidInst(ReturnVoidInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseReturnInst(ReturnInst i) {
                    checkStack1(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void casePushInst(PushInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void casePopInst(PopInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseStoreInst(StoreInst i) {
                    checkStack1(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseLoadInst(LoadInst i) {
                    checkType(i, i.getOpType(), VMStateAnalysis.bitsToType(VMStateAnalysis.this.getFlowBefore(i).get(VMStateAnalysis.this.indexOf(i.getLocal()))));
                }

                @Override // soot.baf.InstSwitch
                public void caseArrayWriteInst(ArrayWriteInst i) {
                    checkStack3(i, i.getOpType(), VMStateAnalysis.TYPE_INT, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseArrayReadInst(ArrayReadInst i) {
                    checkStack2(i, VMStateAnalysis.TYPE_INT, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseArrayLengthInst(ArrayLengthInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseNewArrayInst(NewArrayInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseNewMultiArrayInst(NewMultiArrayInst i) {
                    checkStackN(i, VMStateAnalysis.TYPE_INT, i.getDimensionCount());
                }

                @Override // soot.baf.InstSwitch
                public void caseIfNullInst(IfNullInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfNonNullInst(IfNonNullInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfEqInst(IfEqInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfNeInst(IfNeInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfGtInst(IfGtInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfGeInst(IfGeInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfLtInst(IfLtInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfLeInst(IfLeInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseIfCmpEqInst(IfCmpEqInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseIfCmpNeInst(IfCmpNeInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseIfCmpGtInst(IfCmpGtInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseIfCmpGeInst(IfCmpGeInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseIfCmpLtInst(IfCmpLtInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseIfCmpLeInst(IfCmpLeInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseCmpInst(CmpInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseCmpgInst(CmpgInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseCmplInst(CmplInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseStaticGetInst(StaticGetInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseStaticPutInst(StaticPutInst i) {
                    checkStack1(i, i.getFieldRef().type());
                }

                @Override // soot.baf.InstSwitch
                public void caseFieldGetInst(FieldGetInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseFieldPutInst(FieldPutInst i) {
                    checkStack2(i, i.getFieldRef().type(), VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseInstanceCastInst(InstanceCastInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseInstanceOfInst(InstanceOfInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void casePrimitiveCastInst(PrimitiveCastInst i) {
                    checkStack1(i, i.getFromType());
                }

                private void checkStackForParams(Inst i, List<Type> pTypes, Type baseType) {
                    Stack<Type> stk = VMStateAnalysis.this.opStacks.get(i);
                    int idx = stk.size();
                    int numParams = pTypes.size();
                    if (numParams > 0) {
                        ListIterator<Type> it = pTypes.listIterator(numParams);
                        while (it.hasPrevious()) {
                            Type t = it.previous();
                            idx--;
                            checkType(i, t, stk.elementAt(idx));
                        }
                    }
                    if (baseType != null) {
                        checkType(i, baseType, stk.elementAt(idx - 1));
                    }
                }

                @Override // soot.baf.InstSwitch
                public void caseDynamicInvokeInst(DynamicInvokeInst i) {
                    checkStackForParams(i, i.getMethodRef().getParameterTypes(), null);
                }

                @Override // soot.baf.InstSwitch
                public void caseStaticInvokeInst(StaticInvokeInst i) {
                    checkStackForParams(i, i.getMethodRef().getParameterTypes(), null);
                }

                @Override // soot.baf.InstSwitch
                public void caseVirtualInvokeInst(VirtualInvokeInst i) {
                    checkStackForParams(i, i.getMethodRef().getParameterTypes(), VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
                    checkStackForParams(i, i.getMethodRef().getParameterTypes(), VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseSpecialInvokeInst(SpecialInvokeInst i) {
                    checkStackForParams(i, i.getMethodRef().getParameterTypes(), VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseThrowInst(ThrowInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseAndInst(AndInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseOrInst(OrInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseXorInst(XorInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseAddInst(AddInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseSubInst(SubInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseMulInst(MulInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseDivInst(DivInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseRemInst(RemInst i) {
                    checkStack2(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseShlInst(ShlInst i) {
                    checkStack2(i, VMStateAnalysis.TYPE_INT, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseShrInst(ShrInst i) {
                    checkStack2(i, VMStateAnalysis.TYPE_INT, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseUshrInst(UshrInst i) {
                    checkStack2(i, VMStateAnalysis.TYPE_INT, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseNegInst(NegInst i) {
                    checkStack1(i, i.getOpType());
                }

                @Override // soot.baf.InstSwitch
                public void caseIncInst(IncInst i) {
                    checkType(i, VMStateAnalysis.TYPE_INT, VMStateAnalysis.bitsToType(VMStateAnalysis.this.getFlowBefore(i).get(VMStateAnalysis.this.indexOf(i.getLocal()))));
                }

                @Override // soot.baf.InstSwitch
                public void caseNewInst(NewInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseSwapInst(SwapInst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseDup1Inst(Dup1Inst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseDup2Inst(Dup2Inst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseDup1_x1Inst(Dup1_x1Inst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseDup1_x2Inst(Dup1_x2Inst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseDup2_x1Inst(Dup2_x1Inst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseDup2_x2Inst(Dup2_x2Inst i) {
                }

                @Override // soot.baf.InstSwitch
                public void caseLookupSwitchInst(LookupSwitchInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseTableSwitchInst(TableSwitchInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_INT);
                }

                @Override // soot.baf.InstSwitch
                public void caseEnterMonitorInst(EnterMonitorInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }

                @Override // soot.baf.InstSwitch
                public void caseExitMonitorInst(ExitMonitorInst i) {
                    checkStack1(i, VMStateAnalysis.TYPE_REF);
                }
            };
        }
    }
}
