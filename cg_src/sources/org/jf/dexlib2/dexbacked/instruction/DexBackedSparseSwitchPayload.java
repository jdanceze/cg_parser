package org.jf.dexlib2.dexbacked.instruction;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/instruction/DexBackedSparseSwitchPayload.class */
public class DexBackedSparseSwitchPayload extends DexBackedInstruction implements SparseSwitchPayload {
    public final int elementCount;
    private static final int ELEMENT_COUNT_OFFSET = 2;
    private static final int KEYS_OFFSET = 4;

    public DexBackedSparseSwitchPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
        super(dexFile, Opcode.SPARSE_SWITCH_PAYLOAD, instructionStart);
        this.elementCount = dexFile.getDataBuffer().readUshort(instructionStart + 2);
    }

    @Override // org.jf.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<? extends SwitchElement> getSwitchElements() {
        return new FixedSizeList<SwitchElement>() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedSparseSwitchPayload.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
            @Nonnull
            public SwitchElement readItem(final int index) {
                return new SwitchElement() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedSparseSwitchPayload.1.1
                    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
                    public int getKey() {
                        return DexBackedSparseSwitchPayload.this.dexFile.getDataBuffer().readInt(DexBackedSparseSwitchPayload.this.instructionStart + 4 + (index * 4));
                    }

                    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
                    public int getOffset() {
                        return DexBackedSparseSwitchPayload.this.dexFile.getDataBuffer().readInt(DexBackedSparseSwitchPayload.this.instructionStart + 4 + (DexBackedSparseSwitchPayload.this.elementCount * 4) + (index * 4));
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedSparseSwitchPayload.this.elementCount;
            }
        };
    }

    @Override // org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction, org.jf.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return 2 + (this.elementCount * 4);
    }
}
