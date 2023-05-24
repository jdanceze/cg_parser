package soot.toDex;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.immutable.reference.ImmutableStringReference;
import org.jf.dexlib2.immutable.reference.ImmutableTypeReference;
import soot.jimple.AbstractConstantSwitch;
import soot.jimple.ClassConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn11n;
import soot.toDex.instructions.Insn21c;
import soot.toDex.instructions.Insn21s;
import soot.toDex.instructions.Insn31i;
import soot.toDex.instructions.Insn51l;
/* loaded from: gencallgraphv3.jar:soot/toDex/ConstantVisitor.class */
public class ConstantVisitor extends AbstractConstantSwitch {
    private StmtVisitor stmtV;
    private Register destinationReg;
    private Stmt origStmt;

    public ConstantVisitor(StmtVisitor stmtV) {
        this.stmtV = stmtV;
    }

    public void setDestination(Register destinationReg) {
        this.destinationReg = destinationReg;
    }

    public void setOrigStmt(Stmt stmt) {
        this.origStmt = stmt;
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void defaultCase(Object o) {
        throw new Error("unknown Object (" + o.getClass() + ") as Constant: " + o);
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void caseStringConstant(StringConstant s) {
        StringReference ref = new ImmutableStringReference(s.value);
        this.stmtV.addInsn(new Insn21c(Opcode.CONST_STRING, this.destinationReg, ref), this.origStmt);
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void caseClassConstant(ClassConstant c) {
        TypeReference referencedClass = new ImmutableTypeReference(c.getValue());
        this.stmtV.addInsn(new Insn21c(Opcode.CONST_CLASS, this.destinationReg, referencedClass), this.origStmt);
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void caseLongConstant(LongConstant l) {
        long constant = l.value;
        this.stmtV.addInsn(buildConstWideInsn(constant), this.origStmt);
    }

    private Insn buildConstWideInsn(long literal) {
        if (SootToDexUtils.fitsSigned16(literal)) {
            return new Insn21s(Opcode.CONST_WIDE_16, this.destinationReg, (short) literal);
        }
        if (SootToDexUtils.fitsSigned32(literal)) {
            return new Insn31i(Opcode.CONST_WIDE_32, this.destinationReg, (int) literal);
        }
        return new Insn51l(Opcode.CONST_WIDE, this.destinationReg, literal);
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void caseDoubleConstant(DoubleConstant d) {
        long longBits = Double.doubleToLongBits(d.value);
        this.stmtV.addInsn(buildConstWideInsn(longBits), this.origStmt);
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void caseFloatConstant(FloatConstant f) {
        int intBits = Float.floatToIntBits(f.value);
        this.stmtV.addInsn(buildConstInsn(intBits), this.origStmt);
    }

    private Insn buildConstInsn(int literal) {
        if (SootToDexUtils.fitsSigned4(literal)) {
            return new Insn11n(Opcode.CONST_4, this.destinationReg, (byte) literal);
        }
        if (SootToDexUtils.fitsSigned16(literal)) {
            return new Insn21s(Opcode.CONST_16, this.destinationReg, (short) literal);
        }
        return new Insn31i(Opcode.CONST, this.destinationReg, literal);
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void caseIntConstant(IntConstant i) {
        this.stmtV.addInsn(buildConstInsn(i.value), this.origStmt);
    }

    @Override // soot.jimple.AbstractConstantSwitch, soot.jimple.ConstantSwitch
    public void caseNullConstant(NullConstant v) {
        this.stmtV.addInsn(buildConstInsn(0), this.origStmt);
    }
}
