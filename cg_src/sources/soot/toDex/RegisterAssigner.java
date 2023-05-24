package soot.toDex;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.jf.dexlib2.Opcode;
import soot.jimple.Stmt;
import soot.toDex.instructions.AddressInsn;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn11n;
import soot.toDex.instructions.Insn21s;
import soot.toDex.instructions.Insn23x;
import soot.toDex.instructions.TwoRegInsn;
/* loaded from: gencallgraphv3.jar:soot/toDex/RegisterAssigner.class */
class RegisterAssigner {
    private RegisterAllocator regAlloc;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toDex/RegisterAssigner$InstructionIterator.class */
    public class InstructionIterator implements Iterator<Insn> {
        private final ListIterator<Insn> insnsIterator;
        private final Map<Insn, Stmt> insnStmtMap;
        private final Map<Insn, LocalRegisterAssignmentInformation> insnRegisterMap;

        public InstructionIterator(List<Insn> insns, Map<Insn, Stmt> insnStmtMap, Map<Insn, LocalRegisterAssignmentInformation> insnRegisterMap) {
            this.insnStmtMap = insnStmtMap;
            this.insnsIterator = insns.listIterator();
            this.insnRegisterMap = insnRegisterMap;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.insnsIterator.hasNext();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Insn next() {
            return this.insnsIterator.next();
        }

        public Insn previous() {
            return this.insnsIterator.previous();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.insnsIterator.remove();
        }

        public void add(Insn element, Insn forOriginal, Register newRegister) {
            LocalRegisterAssignmentInformation originalRegisterLocal = this.insnRegisterMap.get(forOriginal);
            if (originalRegisterLocal != null) {
                if (newRegister != null) {
                    this.insnRegisterMap.put(element, LocalRegisterAssignmentInformation.v(newRegister, this.insnRegisterMap.get(forOriginal).getLocal()));
                } else {
                    this.insnRegisterMap.put(element, originalRegisterLocal);
                }
            }
            if (this.insnStmtMap.containsKey(forOriginal)) {
                this.insnStmtMap.put(element, this.insnStmtMap.get(forOriginal));
            }
            this.insnsIterator.add(element);
        }

        public void set(Insn element, Insn forOriginal) {
            LocalRegisterAssignmentInformation originalRegisterLocal = this.insnRegisterMap.get(forOriginal);
            if (originalRegisterLocal != null) {
                this.insnRegisterMap.put(element, originalRegisterLocal);
                this.insnRegisterMap.remove(forOriginal);
            }
            if (this.insnStmtMap.containsKey(forOriginal)) {
                this.insnStmtMap.put(element, this.insnStmtMap.get(forOriginal));
                this.insnStmtMap.remove(forOriginal);
            }
            this.insnsIterator.set(element);
        }
    }

    public RegisterAssigner(RegisterAllocator regAlloc) {
        this.regAlloc = regAlloc;
    }

    public List<Insn> finishRegs(List<Insn> insns, Map<Insn, Stmt> insnsStmtMap, Map<Insn, LocalRegisterAssignmentInformation> instructionRegisterMap, List<LocalRegisterAssignmentInformation> parameterInstructionsList) {
        renumParamRegsToHigh(insns, parameterInstructionsList);
        reserveRegisters(insns, insnsStmtMap, parameterInstructionsList);
        InstructionIterator insnIter = new InstructionIterator(insns, insnsStmtMap, instructionRegisterMap);
        while (insnIter.hasNext()) {
            Insn oldInsn = insnIter.next();
            if (oldInsn.hasIncompatibleRegs()) {
                Insn fittingInsn = findFittingInsn(oldInsn);
                if (fittingInsn != null) {
                    insnIter.set(fittingInsn, oldInsn);
                } else {
                    fixIncompatRegs(oldInsn, insnIter);
                }
            }
        }
        return insns;
    }

    private void renumParamRegsToHigh(List<Insn> insns, List<LocalRegisterAssignmentInformation> parameterInstructionsList) {
        int regCount = this.regAlloc.getRegCount();
        int paramRegCount = this.regAlloc.getParamRegCount();
        if (paramRegCount == 0 || paramRegCount == regCount) {
            return;
        }
        for (Insn insn : insns) {
            for (Register r : insn.getRegs()) {
                renumParamRegToHigh(r, regCount, paramRegCount);
            }
        }
        for (LocalRegisterAssignmentInformation parameter : parameterInstructionsList) {
            renumParamRegToHigh(parameter.getRegister(), regCount, paramRegCount);
        }
    }

    private void renumParamRegToHigh(Register r, int regCount, int paramRegCount) {
        int oldNum = r.getNumber();
        if (oldNum >= paramRegCount) {
            int newNormalRegNum = oldNum - paramRegCount;
            r.setNumber(newNormalRegNum);
            return;
        }
        int newParamRegNum = (oldNum + regCount) - paramRegCount;
        r.setNumber(newParamRegNum);
    }

    private void reserveRegisters(List<Insn> insns, Map<Insn, Stmt> insnsStmtMap, List<LocalRegisterAssignmentInformation> parameterInstructionsList) {
        int i = 0;
        while (true) {
            int reservedRegs = i;
            int regsNeeded = getRegsNeeded(reservedRegs, insns, insnsStmtMap);
            int regsToReserve = regsNeeded - reservedRegs;
            if (regsToReserve > 0) {
                this.regAlloc.increaseRegCount(regsToReserve);
                for (Insn insn : insns) {
                    shiftRegs(insn, regsToReserve);
                }
                for (LocalRegisterAssignmentInformation info : parameterInstructionsList) {
                    Register r = info.getRegister();
                    r.setNumber(r.getNumber() + regsToReserve);
                }
                i = reservedRegs + regsToReserve;
            } else {
                return;
            }
        }
    }

    private int getRegsNeeded(int regsAlreadyReserved, List<Insn> insns, Map<Insn, Stmt> insnsStmtMap) {
        int regsNeeded = regsAlreadyReserved;
        for (int i = 0; i < insns.size(); i++) {
            Insn insn = insns.get(i);
            if (!(insn instanceof AddressInsn)) {
                Insn fittingInsn = findFittingInsn(insn);
                if (fittingInsn != null) {
                    insns.set(i, fittingInsn);
                    insnsStmtMap.put(fittingInsn, insnsStmtMap.get(insn));
                    insnsStmtMap.remove(insn);
                } else {
                    int newRegsNeeded = insn.getMinimumRegsNeeded();
                    if (newRegsNeeded > regsNeeded) {
                        regsNeeded = newRegsNeeded;
                    }
                }
            }
        }
        return regsNeeded;
    }

    private void shiftRegs(Insn insn, int shiftAmount) {
        for (Register r : insn.getRegs()) {
            r.setNumber(r.getNumber() + shiftAmount);
        }
    }

    private void fixIncompatRegs(Insn insn, InstructionIterator allInsns) {
        List<Register> regs = insn.getRegs();
        BitSet incompatRegs = insn.getIncompatibleRegs();
        Register resultReg = regs.get(0);
        boolean hasResultReg = insn.getOpcode().setsRegister() || insn.getOpcode().setsWideRegister();
        boolean isResultRegIncompat = incompatRegs.get(0);
        if (hasResultReg && isResultRegIncompat && !insn.getOpcode().name.endsWith("/2addr") && !insn.getOpcode().name.equals("check-cast")) {
            incompatRegs.clear(0);
        }
        if (incompatRegs.cardinality() > 0) {
            addMovesForIncompatRegs(insn, allInsns, regs, incompatRegs);
        }
        if (hasResultReg && isResultRegIncompat) {
            Register resultRegClone = resultReg.m3017clone();
            addMoveForIncompatResultReg(allInsns, resultRegClone, resultReg, insn);
        }
    }

    private void addMoveForIncompatResultReg(InstructionIterator insns, Register destReg, Register origResultReg, Insn curInsn) {
        if (destReg.getNumber() == 0) {
            return;
        }
        origResultReg.setNumber(0);
        Register sourceReg = new Register(destReg.getType(), 0);
        Insn extraMove = StmtVisitor.buildMoveInsn(destReg, sourceReg);
        insns.add(extraMove, curInsn, destReg);
    }

    private void addMovesForIncompatRegs(Insn curInsn, InstructionIterator insns, List<Register> regs, BitSet incompatRegs) {
        Register newRegister = null;
        Register resultReg = regs.get(0);
        boolean hasResultReg = curInsn.getOpcode().setsRegister() || curInsn.getOpcode().setsWideRegister();
        Insn moveResultInsn = null;
        insns.previous();
        int nextNewDestination = 0;
        for (int regIdx = 0; regIdx < regs.size(); regIdx++) {
            if (incompatRegs.get(regIdx)) {
                Register incompatReg = regs.get(regIdx);
                if (!incompatReg.isEmptyReg()) {
                    Register source = incompatReg.m3017clone();
                    Register destination = new Register(source.getType(), nextNewDestination);
                    nextNewDestination += SootToDexUtils.getDexWords(source.getType());
                    if (source.getNumber() != destination.getNumber()) {
                        Insn extraMove = StmtVisitor.buildMoveInsn(destination, source);
                        insns.add(extraMove, curInsn, null);
                        incompatReg.setNumber(destination.getNumber());
                        if (hasResultReg && regIdx == resultReg.getNumber()) {
                            moveResultInsn = StmtVisitor.buildMoveInsn(source, destination);
                            newRegister = destination;
                        }
                    }
                }
            }
        }
        insns.next();
        if (moveResultInsn != null) {
            insns.add(moveResultInsn, curInsn, newRegister);
        }
    }

    private Insn findFittingInsn(Insn insn) {
        if (!insn.hasIncompatibleRegs()) {
            return null;
        }
        Opcode opc = insn.getOpcode();
        if ((insn instanceof Insn11n) && opc.equals(Opcode.CONST_4)) {
            Insn11n unfittingInsn = (Insn11n) insn;
            if (unfittingInsn.getRegA().fitsShort()) {
                return new Insn21s(Opcode.CONST_16, unfittingInsn.getRegA(), unfittingInsn.getLitB());
            }
            return null;
        } else if ((insn instanceof TwoRegInsn) && opc.name.endsWith("_2ADDR")) {
            Register regA = ((TwoRegInsn) insn).getRegA();
            Register regB = ((TwoRegInsn) insn).getRegB();
            if (regA.fitsShort() && regB.fitsShort()) {
                int oldOpcLength = opc.name.length();
                String newOpcName = opc.name.substring(0, oldOpcLength - 6);
                Opcode newOpc = Opcode.valueOf(newOpcName);
                Register regAClone = regA.m3017clone();
                return new Insn23x(newOpc, regA, regAClone, regB);
            }
            return null;
        } else if ((insn instanceof TwoRegInsn) && SootToDexUtils.isNormalMove(opc)) {
            Register regA2 = ((TwoRegInsn) insn).getRegA();
            Register regB2 = ((TwoRegInsn) insn).getRegB();
            if (regA2.getNumber() != regB2.getNumber()) {
                return StmtVisitor.buildMoveInsn(regA2, regB2);
            }
            return null;
        } else {
            return null;
        }
    }
}
