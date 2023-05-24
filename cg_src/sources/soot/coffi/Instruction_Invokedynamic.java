package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Invokedynamic.class */
public class Instruction_Invokedynamic extends Instruction_intindex {
    private static final Logger logger = LoggerFactory.getLogger(Instruction_Invokedynamic.class);
    public short invoke_dynamic_index;
    public short reserved;

    public Instruction_Invokedynamic() {
        super((byte) -70);
        this.name = "invokedynamic";
        this.calls = true;
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        this.invoke_dynamic_index = getShort(bc, index);
        int index2 = index + 2;
        this.reserved = getShort(bc, index2);
        if (this.reserved > 0) {
            logger.debug("reserved value in invokedynamic is " + ((int) this.reserved));
        }
        return index2 + 2;
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = this.code;
        shortToBytes(this.invoke_dynamic_index, bc, index2);
        int index3 = index2 + 2;
        shortToBytes(this.reserved, bc, index3);
        return index3 + 2;
    }
}
