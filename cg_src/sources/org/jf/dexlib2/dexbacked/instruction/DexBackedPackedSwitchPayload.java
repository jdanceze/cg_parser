package org.jf.dexlib2.dexbacked.instruction;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/instruction/DexBackedPackedSwitchPayload.class */
public class DexBackedPackedSwitchPayload extends DexBackedInstruction implements PackedSwitchPayload {
    public final int elementCount;
    private static final int ELEMENT_COUNT_OFFSET = 2;
    private static final int FIRST_KEY_OFFSET = 4;
    private static final int TARGETS_OFFSET = 8;

    public DexBackedPackedSwitchPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
        super(dexFile, Opcode.PACKED_SWITCH_PAYLOAD, instructionStart);
        this.elementCount = dexFile.getDataBuffer().readUshort(instructionStart + 2);
    }

    @Override // org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload, org.jf.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<? extends SwitchElement> getSwitchElements() {
        final int firstKey = this.dexFile.getDataBuffer().readInt(this.instructionStart + 4);
        return new FixedSizeList<SwitchElement>() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedPackedSwitchPayload.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
            @Nonnull
            public SwitchElement readItem(final int index) {
                return new SwitchElement() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedPackedSwitchPayload.1.1
                    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
                    public int getKey() {
                        return firstKey + index;
                    }

                    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
                    public int getOffset() {
                        return DexBackedPackedSwitchPayload.this.dexFile.getDataBuffer().readInt(DexBackedPackedSwitchPayload.this.instructionStart + 8 + (index * 4));
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedPackedSwitchPayload.this.elementCount;
            }
        };
    }

    @Override // org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction, org.jf.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return 4 + (this.elementCount * 2);
    }
}
