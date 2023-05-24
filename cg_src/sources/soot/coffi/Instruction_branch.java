package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_branch.class */
abstract class Instruction_branch extends Instruction {
    private static final Logger logger = LoggerFactory.getLogger(Instruction_branch.class);
    public int arg_i;
    public Instruction target;

    public Instruction_branch(byte c) {
        super(c);
        this.branches = true;
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + "[label_" + Integer.toString(this.target.label) + "]";
    }

    @Override // soot.coffi.Instruction
    public void offsetToPointer(ByteCode bc) {
        this.target = bc.locateInst(this.arg_i + this.label);
        if (this.target == null) {
            logger.warn("can't locate target of instruction");
            logger.debug(" which should be at byte address " + (this.label + this.arg_i));
            return;
        }
        this.target.labelled = true;
    }

    @Override // soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = {this.target, next};
        return i;
    }

    @Override // soot.coffi.Instruction
    public String toString() {
        return String.valueOf(super.toString()) + "\t" + this.target.label;
    }
}
