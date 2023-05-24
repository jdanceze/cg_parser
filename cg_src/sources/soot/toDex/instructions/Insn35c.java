package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c;
import org.jf.dexlib2.iface.reference.Reference;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn35c.class */
public class Insn35c extends AbstractInsn implements FiveRegInsn {
    private int regCount;
    private final Reference referencedItem;

    public Insn35c(Opcode opc, int regCount, Register regD, Register regE, Register regF, Register regG, Register regA, Reference referencedItem) {
        super(opc);
        this.regCount = regCount;
        this.regs.add(regD);
        this.regs.add(regE);
        this.regs.add(regF);
        this.regs.add(regG);
        this.regs.add(regA);
        this.referencedItem = referencedItem;
    }

    @Override // soot.toDex.instructions.FiveRegInsn
    public Register getRegD() {
        return this.regs.get(0);
    }

    @Override // soot.toDex.instructions.FiveRegInsn
    public Register getRegE() {
        return this.regs.get(1);
    }

    @Override // soot.toDex.instructions.FiveRegInsn
    public Register getRegF() {
        return this.regs.get(2);
    }

    @Override // soot.toDex.instructions.FiveRegInsn
    public Register getRegG() {
        return this.regs.get(3);
    }

    @Override // soot.toDex.instructions.FiveRegInsn
    public Register getRegA() {
        return this.regs.get(4);
    }

    private static boolean isImplicitWide(Register firstReg, Register secondReg) {
        return firstReg.isWide() && secondReg.isEmptyReg();
    }

    private static int getPossiblyWideNumber(Register reg, Register previousReg) {
        if (isImplicitWide(previousReg, reg)) {
            return previousReg.getNumber() + 1;
        }
        return reg.getNumber();
    }

    private int[] getRealRegNumbers() {
        Register regD = getRegD();
        Register regE = getRegE();
        Register regF = getRegF();
        Register regG = getRegG();
        Register regA = getRegA();
        int[] realRegNumbers = {regD.getNumber(), getPossiblyWideNumber(regE, regD), getPossiblyWideNumber(regF, regE), getPossiblyWideNumber(regG, regF), getPossiblyWideNumber(regA, regG)};
        return realRegNumbers;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        int[] realRegNumbers = getRealRegNumbers();
        byte regDNumber = (byte) realRegNumbers[0];
        byte regENumber = (byte) realRegNumbers[1];
        byte regFNumber = (byte) realRegNumbers[2];
        byte regGNumber = (byte) realRegNumbers[3];
        byte regANumber = (byte) realRegNumbers[4];
        return new BuilderInstruction35c(this.opc, this.regCount, regDNumber, regENumber, regFNumber, regGNumber, regANumber, this.referencedItem);
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        BitSet incompatRegs = new BitSet(5);
        int[] realRegNumbers = getRealRegNumbers();
        for (int i = 0; i < realRegNumbers.length; i++) {
            boolean isCompatible = Register.fitsByte(realRegNumbers[i], false);
            if (!isCompatible) {
                incompatRegs.set(i);
                Register possibleSecondHalf = this.regs.get(i);
                if (possibleSecondHalf.isEmptyReg() && i > 0) {
                    Register possibleFirstHalf = this.regs.get(i - 1);
                    if (possibleFirstHalf.isWide()) {
                        incompatRegs.set(i - 1);
                    }
                }
            }
        }
        return incompatRegs;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    public String toString() {
        return String.valueOf(super.toString()) + " (" + this.regCount + " regs), ref: " + this.referencedItem;
    }
}
