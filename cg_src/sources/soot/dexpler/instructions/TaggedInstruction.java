package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/TaggedInstruction.class */
public abstract class TaggedInstruction extends DexlibAbstractInstruction {
    private Tag instructionTag;

    public TaggedInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
        this.instructionTag = null;
    }

    public void setTag(Tag t) {
        this.instructionTag = t;
    }

    public Tag getTag() {
        if (this.instructionTag == null) {
            throw new RuntimeException("Must tag instruction first! (0x" + Integer.toHexString(this.codeAddress) + ": " + this.instruction + ")");
        }
        return this.instructionTag;
    }
}
