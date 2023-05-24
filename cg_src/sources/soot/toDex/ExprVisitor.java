package soot.toDex;

import java.util.ArrayList;
import java.util.List;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.ArrayType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.CastExpr;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.ExprSwitch;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn10x;
import soot.toDex.instructions.Insn11x;
import soot.toDex.instructions.Insn12x;
import soot.toDex.instructions.Insn21c;
import soot.toDex.instructions.Insn21t;
import soot.toDex.instructions.Insn22b;
import soot.toDex.instructions.Insn22c;
import soot.toDex.instructions.Insn22s;
import soot.toDex.instructions.Insn22t;
import soot.toDex.instructions.Insn23x;
import soot.toDex.instructions.Insn35c;
import soot.toDex.instructions.Insn3rc;
import soot.toDex.instructions.InsnWithOffset;
/* loaded from: gencallgraphv3.jar:soot/toDex/ExprVisitor.class */
public class ExprVisitor implements ExprSwitch {
    protected StmtVisitor stmtV;
    protected ConstantVisitor constantV;
    protected RegisterAllocator regAlloc;
    protected Register destinationReg;
    protected Stmt targetForOffset;
    protected Stmt origStmt;
    private int lastInvokeInstructionPosition;

    public ExprVisitor(StmtVisitor stmtV, ConstantVisitor constantV, RegisterAllocator regAlloc) {
        this.stmtV = stmtV;
        this.constantV = constantV;
        this.regAlloc = regAlloc;
    }

    public void setDestinationReg(Register destinationReg) {
        this.destinationReg = destinationReg;
    }

    public void setOrigStmt(Stmt stmt) {
        this.origStmt = stmt;
    }

    public void setTargetForOffset(Stmt targetForOffset) {
        this.targetForOffset = targetForOffset;
    }

    @Override // soot.jimple.ExprSwitch, soot.jimple.ConstantSwitch
    public void defaultCase(Object o) {
        throw new Error("unknown Object (" + o.getClass() + ") as Expression: " + o);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
        throw new Error("DynamicInvokeExpr not supported: " + v);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseSpecialInvokeExpr(SpecialInvokeExpr sie) {
        MethodReference method = DexPrinter.toMethodReference(sie.getMethodRef());
        List<Register> arguments = getInstanceInvokeArgumentRegs(sie);
        this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
        if (isCallToConstructor(sie) || isCallToPrivate(sie)) {
            this.stmtV.addInsn(buildInvokeInsn("INVOKE_DIRECT", method, arguments), this.origStmt);
        } else if (isCallToSuper(sie)) {
            this.stmtV.addInsn(buildInvokeInsn("INVOKE_SUPER", method, arguments), this.origStmt);
        } else {
            this.stmtV.addInsn(buildInvokeInsn("INVOKE_VIRTUAL", method, arguments), this.origStmt);
        }
    }

    private Insn buildInvokeInsn(String invokeOpcode, MethodReference method, List<Register> argumentRegs) {
        Insn invokeInsn;
        int regCountForArguments = SootToDexUtils.getRealRegCount(argumentRegs);
        if (regCountForArguments <= 5) {
            Register[] paddedArray = pad35cRegs(argumentRegs);
            Opcode opc = Opcode.valueOf(invokeOpcode);
            invokeInsn = new Insn35c(opc, regCountForArguments, paddedArray[0], paddedArray[1], paddedArray[2], paddedArray[3], paddedArray[4], method);
        } else if (regCountForArguments <= 255) {
            Opcode opc2 = Opcode.valueOf(String.valueOf(invokeOpcode) + "_RANGE");
            invokeInsn = new Insn3rc(opc2, argumentRegs, (short) regCountForArguments, method);
        } else {
            throw new Error("too many parameter registers for invoke-* (> 255): " + regCountForArguments + " or registers too big (> 4 bits)");
        }
        this.stmtV.setLastReturnTypeDescriptor(method.getReturnType());
        return invokeInsn;
    }

    private boolean isCallToPrivate(SpecialInvokeExpr sie) {
        return sie.getMethod().isPrivate();
    }

    private boolean isCallToConstructor(SpecialInvokeExpr sie) {
        return sie.getMethodRef().name().equals("<init>");
    }

    private boolean isCallToSuper(SpecialInvokeExpr sie) {
        SootClass classWithInvokation = sie.getMethod().getDeclaringClass();
        SootClass currentClass = this.stmtV.getBelongingClass();
        while (currentClass != null) {
            currentClass = currentClass.getSuperclassUnsafe();
            if (currentClass != null) {
                if (currentClass != classWithInvokation) {
                    if (currentClass.isPhantom() && !currentClass.getName().equals(JavaBasicTypes.JAVA_LANG_OBJECT)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseVirtualInvokeExpr(VirtualInvokeExpr vie) {
        MethodReference method = DexPrinter.toMethodReference(vie.getMethodRef());
        List<Register> argumentRegs = getInstanceInvokeArgumentRegs(vie);
        this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
        this.stmtV.addInsn(buildInvokeInsn("INVOKE_VIRTUAL", method, argumentRegs), this.origStmt);
    }

    private List<Register> getInvokeArgumentRegs(InvokeExpr ie) {
        this.constantV.setOrigStmt(this.origStmt);
        List<Register> argumentRegs = new ArrayList<>();
        for (Value arg : ie.getArgs()) {
            Register currentReg = this.regAlloc.asImmediate(arg, this.constantV);
            argumentRegs.add(currentReg);
        }
        return argumentRegs;
    }

    private List<Register> getInstanceInvokeArgumentRegs(InstanceInvokeExpr iie) {
        this.constantV.setOrigStmt(this.origStmt);
        List<Register> argumentRegs = getInvokeArgumentRegs(iie);
        Local callee = (Local) iie.getBase();
        Register calleeRegister = this.regAlloc.asLocal(callee);
        argumentRegs.add(0, calleeRegister);
        return argumentRegs;
    }

    private Register[] pad35cRegs(List<Register> realRegs) {
        Register[] paddedArray = new Register[5];
        int nextReg = 0;
        for (Register realReg : realRegs) {
            paddedArray[nextReg] = realReg;
            if (realReg.isWide()) {
                nextReg++;
                paddedArray[nextReg] = Register.EMPTY_REGISTER;
            }
            nextReg++;
        }
        while (nextReg < 5) {
            paddedArray[nextReg] = Register.EMPTY_REGISTER;
            nextReg++;
        }
        return paddedArray;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseInterfaceInvokeExpr(InterfaceInvokeExpr iie) {
        MethodReference method = DexPrinter.toMethodReference(iie.getMethodRef());
        List<Register> arguments = getInstanceInvokeArgumentRegs(iie);
        this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
        this.stmtV.addInsn(buildInvokeInsn("INVOKE_INTERFACE", method, arguments), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseStaticInvokeExpr(StaticInvokeExpr sie) {
        MethodReference method = DexPrinter.toMethodReference(sie.getMethodRef());
        List<Register> arguments = getInvokeArgumentRegs(sie);
        this.lastInvokeInstructionPosition = this.stmtV.getInstructionCount();
        this.stmtV.addInsn(buildInvokeInsn("INVOKE_STATIC", method, arguments), this.origStmt);
    }

    private void buildCalculatingBinaryInsn(String binaryOperation, Value firstOperand, Value secondOperand, Expr originalExpr) {
        this.constantV.setOrigStmt(this.origStmt);
        Register firstOpReg = this.regAlloc.asImmediate(firstOperand, this.constantV);
        if ((this.destinationReg.getType() instanceof IntType) && (secondOperand instanceof IntConstant) && !binaryOperation.equals("SUB")) {
            int secondOpConstant = ((IntConstant) secondOperand).value;
            if (SootToDexUtils.fitsSigned8(secondOpConstant)) {
                this.stmtV.addInsn(buildLit8BinaryInsn(binaryOperation, firstOpReg, (byte) secondOpConstant), this.origStmt);
                return;
            } else if (SootToDexUtils.fitsSigned16(secondOpConstant) && !binaryOperation.equals("SHL") && !binaryOperation.equals("SHR") && !binaryOperation.equals("USHR")) {
                this.stmtV.addInsn(buildLit16BinaryInsn(binaryOperation, firstOpReg, (short) secondOpConstant), this.origStmt);
                return;
            }
        }
        if (!(secondOperand.getType() instanceof PrimType)) {
            throw new RuntimeException("Invalid value type for primitive operation");
        }
        PrimitiveType destRegType = PrimitiveType.getByName(this.destinationReg.getType().toString());
        Register secondOpReg = this.regAlloc.asImmediate(secondOperand, this.constantV);
        Register orgDestReg = this.destinationReg;
        if (isSmallerThan(destRegType, PrimitiveType.INT)) {
            this.destinationReg = this.regAlloc.asTmpReg(IntType.v());
        } else if (isBiggerThan(PrimitiveType.getByName(secondOpReg.getType().toString()), destRegType)) {
            this.destinationReg = this.regAlloc.asTmpReg(secondOpReg.getType());
        } else if (isBiggerThan(PrimitiveType.getByName(firstOpReg.getType().toString()), destRegType)) {
            this.destinationReg = this.regAlloc.asTmpReg(firstOpReg.getType());
        }
        if (this.destinationReg.getNumber() == firstOpReg.getNumber()) {
            this.stmtV.addInsn(build2AddrBinaryInsn(binaryOperation, secondOpReg), this.origStmt);
        } else {
            this.stmtV.addInsn(buildNormalBinaryInsn(binaryOperation, firstOpReg, secondOpReg), this.origStmt);
        }
        if (orgDestReg != this.destinationReg) {
            Register tempReg = this.destinationReg.m3017clone();
            this.destinationReg = orgDestReg.m3017clone();
            castPrimitive(tempReg, originalExpr, this.destinationReg.getType());
        }
    }

    private String fixIntTypeString(String typeString) {
        if (typeString.equals("boolean") || typeString.equals("byte") || typeString.equals("char") || typeString.equals("short")) {
            return "int";
        }
        return typeString;
    }

    private Insn build2AddrBinaryInsn(String binaryOperation, Register secondOpReg) {
        String localTypeString = this.destinationReg.getTypeString();
        Opcode opc = Opcode.valueOf(String.valueOf(binaryOperation) + "_" + fixIntTypeString(localTypeString).toUpperCase() + "_2ADDR");
        return new Insn12x(opc, this.destinationReg, secondOpReg);
    }

    private Insn buildNormalBinaryInsn(String binaryOperation, Register firstOpReg, Register secondOpReg) {
        String localTypeString = firstOpReg.getTypeString();
        Opcode opc = Opcode.valueOf(String.valueOf(binaryOperation) + "_" + fixIntTypeString(localTypeString).toUpperCase());
        return new Insn23x(opc, this.destinationReg, firstOpReg, secondOpReg);
    }

    private Insn buildLit16BinaryInsn(String binaryOperation, Register firstOpReg, short secondOpLieteral) {
        Opcode opc = Opcode.valueOf(String.valueOf(binaryOperation) + "_INT_LIT16");
        return new Insn22s(opc, this.destinationReg, firstOpReg, secondOpLieteral);
    }

    private Insn buildLit8BinaryInsn(String binaryOperation, Register firstOpReg, byte secondOpLiteral) {
        Opcode opc = Opcode.valueOf(String.valueOf(binaryOperation) + "_INT_LIT8");
        return new Insn22b(opc, this.destinationReg, firstOpReg, secondOpLiteral);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseAddExpr(AddExpr ae) {
        buildCalculatingBinaryInsn("ADD", ae.getOp1(), ae.getOp2(), ae);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseSubExpr(SubExpr se) {
        buildCalculatingBinaryInsn("SUB", se.getOp1(), se.getOp2(), se);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseMulExpr(MulExpr me) {
        buildCalculatingBinaryInsn("MUL", me.getOp1(), me.getOp2(), me);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseDivExpr(DivExpr de) {
        buildCalculatingBinaryInsn("DIV", de.getOp1(), de.getOp2(), de);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseRemExpr(RemExpr re) {
        buildCalculatingBinaryInsn("REM", re.getOp1(), re.getOp2(), re);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseAndExpr(AndExpr ae) {
        buildCalculatingBinaryInsn("AND", ae.getOp1(), ae.getOp2(), ae);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseOrExpr(OrExpr oe) {
        buildCalculatingBinaryInsn("OR", oe.getOp1(), oe.getOp2(), oe);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseXorExpr(XorExpr xe) {
        Value firstOperand = xe.getOp1();
        Value secondOperand = xe.getOp2();
        this.constantV.setOrigStmt(this.origStmt);
        if (secondOperand.equals(IntConstant.v(-1)) || secondOperand.equals(LongConstant.v(-1L))) {
            PrimitiveType destRegType = PrimitiveType.getByName(this.destinationReg.getType().toString());
            Register orgDestReg = this.destinationReg;
            if (isBiggerThan(PrimitiveType.getByName(secondOperand.getType().toString()), destRegType)) {
                this.destinationReg = this.regAlloc.asTmpReg(IntType.v());
            }
            if (secondOperand.equals(IntConstant.v(-1))) {
                Register sourceReg = this.regAlloc.asImmediate(firstOperand, this.constantV);
                this.stmtV.addInsn(new Insn12x(Opcode.NOT_INT, this.destinationReg, sourceReg), this.origStmt);
            } else if (secondOperand.equals(LongConstant.v(-1L))) {
                Register sourceReg2 = this.regAlloc.asImmediate(firstOperand, this.constantV);
                this.stmtV.addInsn(new Insn12x(Opcode.NOT_LONG, this.destinationReg, sourceReg2), this.origStmt);
            }
            if (orgDestReg != this.destinationReg) {
                Register tempReg = this.destinationReg.m3017clone();
                this.destinationReg = orgDestReg.m3017clone();
                castPrimitive(tempReg, secondOperand, this.destinationReg.getType());
                return;
            }
            return;
        }
        buildCalculatingBinaryInsn("XOR", firstOperand, secondOperand, xe);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseShlExpr(ShlExpr sle) {
        buildCalculatingBinaryInsn("SHL", sle.getOp1(), sle.getOp2(), sle);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseShrExpr(ShrExpr sre) {
        buildCalculatingBinaryInsn("SHR", sre.getOp1(), sre.getOp2(), sre);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseUshrExpr(UshrExpr usre) {
        buildCalculatingBinaryInsn("USHR", usre.getOp1(), usre.getOp2(), usre);
    }

    private Insn buildComparingBinaryInsn(String binaryOperation, Value firstOperand, Value secondOperand) {
        InsnWithOffset comparingBinaryInsn;
        this.constantV.setOrigStmt(this.origStmt);
        Value realFirstOperand = fixNullConstant(firstOperand);
        Value realSecondOperand = fixNullConstant(secondOperand);
        Register firstOpReg = this.regAlloc.asImmediate(realFirstOperand, this.constantV);
        String opcodeName = "IF_" + binaryOperation;
        boolean secondOpIsInt = realSecondOperand instanceof IntConstant;
        boolean secondOpIsZero = secondOpIsInt && ((IntConstant) realSecondOperand).value == 0;
        if (secondOpIsZero) {
            Opcode opc = Opcode.valueOf(opcodeName.concat("Z"));
            comparingBinaryInsn = new Insn21t(opc, firstOpReg);
            comparingBinaryInsn.setTarget(this.targetForOffset);
        } else {
            Opcode opc2 = Opcode.valueOf(opcodeName);
            Register secondOpReg = this.regAlloc.asImmediate(realSecondOperand, this.constantV);
            comparingBinaryInsn = new Insn22t(opc2, firstOpReg, secondOpReg);
            comparingBinaryInsn.setTarget(this.targetForOffset);
        }
        return comparingBinaryInsn;
    }

    private Value fixNullConstant(Value potentialNullConstant) {
        if (potentialNullConstant instanceof NullConstant) {
            return IntConstant.v(0);
        }
        return potentialNullConstant;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseEqExpr(EqExpr ee) {
        this.stmtV.addInsn(buildComparingBinaryInsn("EQ", ee.getOp1(), ee.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseGeExpr(GeExpr ge) {
        this.stmtV.addInsn(buildComparingBinaryInsn("GE", ge.getOp1(), ge.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseGtExpr(GtExpr ge) {
        this.stmtV.addInsn(buildComparingBinaryInsn("GT", ge.getOp1(), ge.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLeExpr(LeExpr le) {
        this.stmtV.addInsn(buildComparingBinaryInsn("LE", le.getOp1(), le.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLtExpr(LtExpr le) {
        this.stmtV.addInsn(buildComparingBinaryInsn("LT", le.getOp1(), le.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNeExpr(NeExpr ne) {
        this.stmtV.addInsn(buildComparingBinaryInsn("NE", ne.getOp1(), ne.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmpExpr(CmpExpr v) {
        this.stmtV.addInsn(buildCmpInsn("CMP_LONG", v.getOp1(), v.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmpgExpr(CmpgExpr v) {
        this.stmtV.addInsn(buildCmpInsn("CMPG", v.getOp1(), v.getOp2()), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCmplExpr(CmplExpr v) {
        this.stmtV.addInsn(buildCmpInsn("CMPL", v.getOp1(), v.getOp2()), this.origStmt);
    }

    private Insn buildCmpInsn(String opcodePrefix, Value firstOperand, Value secondOperand) {
        Opcode opc;
        this.constantV.setOrigStmt(this.origStmt);
        Register firstReg = this.regAlloc.asImmediate(firstOperand, this.constantV);
        Register secondReg = this.regAlloc.asImmediate(secondOperand, this.constantV);
        if (opcodePrefix.equals("CMP_LONG")) {
            opc = Opcode.CMP_LONG;
        } else if (firstReg.isFloat()) {
            opc = Opcode.valueOf(String.valueOf(opcodePrefix) + "_FLOAT");
        } else if (firstReg.isDouble()) {
            opc = Opcode.valueOf(String.valueOf(opcodePrefix) + "_DOUBLE");
        } else {
            throw new RuntimeException("unsupported type of operands for cmp* opcode: " + firstOperand.getType());
        }
        return new Insn23x(opc, this.destinationReg, firstReg, secondReg);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseLengthExpr(LengthExpr le) {
        Value array = le.getOp();
        this.constantV.setOrigStmt(this.origStmt);
        Register arrayReg = this.regAlloc.asImmediate(array, this.constantV);
        this.stmtV.addInsn(new Insn12x(Opcode.ARRAY_LENGTH, this.destinationReg, arrayReg), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNegExpr(NegExpr ne) {
        Opcode opc;
        Value source = ne.getOp();
        this.constantV.setOrigStmt(this.origStmt);
        Register sourceReg = this.regAlloc.asImmediate(source, this.constantV);
        Type type = source.getType();
        if (type instanceof IntegerType) {
            opc = Opcode.NEG_INT;
        } else if (type instanceof FloatType) {
            opc = Opcode.NEG_FLOAT;
        } else if (type instanceof DoubleType) {
            opc = Opcode.NEG_DOUBLE;
        } else if (type instanceof LongType) {
            opc = Opcode.NEG_LONG;
        } else {
            throw new RuntimeException("unsupported value type for neg-* opcode: " + type);
        }
        this.stmtV.addInsn(new Insn12x(opc, this.destinationReg, sourceReg), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseInstanceOfExpr(InstanceOfExpr ioe) {
        Value referenceToCheck = ioe.getOp();
        this.constantV.setOrigStmt(this.origStmt);
        Register referenceToCheckReg = this.regAlloc.asImmediate(referenceToCheck, this.constantV);
        TypeReference checkType = DexPrinter.toTypeReference(ioe.getCheckType());
        this.stmtV.addInsn(new Insn22c(Opcode.INSTANCE_OF, this.destinationReg, referenceToCheckReg, checkType), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseCastExpr(CastExpr ce) {
        Type castType = ce.getCastType();
        Value source = ce.getOp();
        this.constantV.setOrigStmt(this.origStmt);
        Register sourceReg = this.regAlloc.asImmediate(source, this.constantV);
        if (SootToDexUtils.isObject(castType)) {
            castObject(sourceReg, castType);
        } else {
            castPrimitive(sourceReg, source, castType);
        }
    }

    protected void castObject(Register sourceReg, Type castType) {
        TypeReference castTypeItem = DexPrinter.toTypeReference(castType);
        if (sourceReg.getNumber() == this.destinationReg.getNumber()) {
            this.stmtV.addInsn(new Insn21c(Opcode.CHECK_CAST, this.destinationReg, castTypeItem), this.origStmt);
            return;
        }
        Register tmp = this.regAlloc.asTmpReg(sourceReg.getType());
        this.stmtV.addInsn(StmtVisitor.buildMoveInsn(tmp, sourceReg), this.origStmt);
        this.stmtV.addInsn(new Insn21c(Opcode.CHECK_CAST, tmp.m3017clone(), castTypeItem), this.origStmt);
        this.stmtV.addInsn(StmtVisitor.buildMoveInsn(this.destinationReg, tmp.m3017clone()), this.origStmt);
    }

    private void castPrimitive(Register sourceReg, Value source, Type castSootType) {
        PrimitiveType castType = PrimitiveType.getByName(castSootType.toString());
        if (castType == PrimitiveType.INT && (source.getType() instanceof NullType)) {
            source = IntConstant.v(0);
        }
        Type srcType = source.getType();
        if (srcType instanceof RefType) {
            throw new RuntimeException("Trying to cast reference type " + srcType + " to a primitive");
        }
        PrimitiveType sourceType = PrimitiveType.getByName(srcType.toString());
        if (castType == PrimitiveType.BOOLEAN) {
            castType = PrimitiveType.INT;
            sourceType = PrimitiveType.INT;
        }
        if (shouldCastFromInt(sourceType, castType)) {
            PrimitiveType sourceType2 = PrimitiveType.INT;
            Opcode opc = getCastOpc(sourceType2, castType);
            this.stmtV.addInsn(new Insn12x(opc, this.destinationReg, sourceReg), this.origStmt);
        } else if (isMoveCompatible(sourceType, castType)) {
            if (this.destinationReg.getNumber() != sourceReg.getNumber()) {
                this.stmtV.addInsn(StmtVisitor.buildMoveInsn(this.destinationReg, sourceReg), this.origStmt);
            } else if (!this.origStmt.getBoxesPointingToThis().isEmpty()) {
                this.stmtV.addInsn(new Insn10x(Opcode.NOP), this.origStmt);
            }
        } else if (needsCastThroughInt(sourceType, castType)) {
            Opcode castToIntOpc = getCastOpc(sourceType, PrimitiveType.INT);
            Opcode castFromIntOpc = getCastOpc(PrimitiveType.INT, castType);
            Register tmp = this.regAlloc.asTmpReg(IntType.v());
            this.stmtV.addInsn(new Insn12x(castToIntOpc, tmp, sourceReg), this.origStmt);
            this.stmtV.addInsn(new Insn12x(castFromIntOpc, this.destinationReg, tmp.m3017clone()), this.origStmt);
        } else {
            Opcode opc2 = getCastOpc(sourceType, castType);
            this.stmtV.addInsn(new Insn12x(opc2, this.destinationReg, sourceReg), this.origStmt);
        }
    }

    private boolean needsCastThroughInt(PrimitiveType sourceType, PrimitiveType castType) {
        return isEqualOrBigger(sourceType, PrimitiveType.LONG) && !isEqualOrBigger(castType, PrimitiveType.INT);
    }

    private boolean isMoveCompatible(PrimitiveType sourceType, PrimitiveType castType) {
        if (sourceType != castType) {
            if (castType == PrimitiveType.INT && !isBiggerThan(sourceType, PrimitiveType.INT)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean shouldCastFromInt(PrimitiveType sourceType, PrimitiveType castType) {
        if (isEqualOrBigger(sourceType, PrimitiveType.INT) || castType == PrimitiveType.INT) {
            return false;
        }
        return true;
    }

    private boolean isEqualOrBigger(PrimitiveType type, PrimitiveType relativeTo) {
        return type.compareTo(relativeTo) >= 0;
    }

    private boolean isBiggerThan(PrimitiveType type, PrimitiveType relativeTo) {
        return type.compareTo(relativeTo) > 0;
    }

    private boolean isSmallerThan(PrimitiveType type, PrimitiveType relativeTo) {
        return type.compareTo(relativeTo) < 0;
    }

    private Opcode getCastOpc(PrimitiveType sourceType, PrimitiveType castType) {
        Opcode opc = Opcode.valueOf(String.valueOf(sourceType.getName().toUpperCase()) + "_TO_" + castType.getName().toUpperCase());
        if (opc == null) {
            throw new RuntimeException("unsupported cast from " + sourceType + " to " + castType);
        }
        return opc;
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewArrayExpr(NewArrayExpr nae) {
        Value size = nae.getSize();
        this.constantV.setOrigStmt(this.origStmt);
        Register sizeReg = this.regAlloc.asImmediate(size, this.constantV);
        Type baseType = nae.getBaseType();
        int numDimensions = 1;
        while (baseType instanceof ArrayType) {
            ArrayType at = (ArrayType) baseType;
            baseType = at.getElementType();
            numDimensions++;
        }
        ArrayType arrayType = ArrayType.v(baseType, numDimensions);
        TypeReference arrayTypeItem = DexPrinter.toTypeReference(arrayType);
        this.stmtV.addInsn(new Insn22c(Opcode.NEW_ARRAY, this.destinationReg, sizeReg, arrayTypeItem), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
        this.constantV.setOrigStmt(this.origStmt);
        if (nmae.getSizeCount() > 255) {
            throw new RuntimeException("number of dimensions is too high (> 255) for the filled-new-array* opcodes: " + nmae.getSizeCount());
        }
        short dimensions = (short) nmae.getSizeCount();
        ArrayType arrayType = ArrayType.v(nmae.getBaseType().baseType, dimensions);
        TypeReference arrayTypeItem = DexPrinter.toTypeReference(arrayType);
        List<Register> dimensionSizeRegs = new ArrayList<>();
        for (int i = 0; i < dimensions; i++) {
            Value currentDimensionSize = nmae.getSize(i);
            Register currentReg = this.regAlloc.asImmediate(currentDimensionSize, this.constantV);
            dimensionSizeRegs.add(currentReg);
        }
        if (dimensions <= 5) {
            Register[] paddedRegs = pad35cRegs(dimensionSizeRegs);
            this.stmtV.addInsn(new Insn35c(Opcode.FILLED_NEW_ARRAY, dimensions, paddedRegs[0], paddedRegs[1], paddedRegs[2], paddedRegs[3], paddedRegs[4], arrayTypeItem), null);
        } else {
            this.stmtV.addInsn(new Insn3rc(Opcode.FILLED_NEW_ARRAY_RANGE, dimensionSizeRegs, dimensions, arrayTypeItem), null);
        }
        this.stmtV.addInsn(new Insn11x(Opcode.MOVE_RESULT_OBJECT, this.destinationReg), this.origStmt);
    }

    @Override // soot.jimple.ExprSwitch
    public void caseNewExpr(NewExpr ne) {
        TypeReference baseType = DexPrinter.toTypeReference(ne.getBaseType());
        this.stmtV.addInsn(new Insn21c(Opcode.NEW_INSTANCE, this.destinationReg, baseType), this.origStmt);
    }

    public int getLastInvokeInstructionPosition() {
        return this.lastInvokeInstructionPosition;
    }
}
