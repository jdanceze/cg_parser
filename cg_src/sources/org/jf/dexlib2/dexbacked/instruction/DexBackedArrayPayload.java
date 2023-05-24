package org.jf.dexlib2.dexbacked.instruction;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/instruction/DexBackedArrayPayload.class */
public class DexBackedArrayPayload extends DexBackedInstruction implements ArrayPayload {
    public static final Opcode OPCODE = Opcode.ARRAY_PAYLOAD;
    public final int elementWidth;
    public final int elementCount;
    private static final int ELEMENT_WIDTH_OFFSET = 2;
    private static final int ELEMENT_COUNT_OFFSET = 4;
    private static final int ELEMENTS_OFFSET = 8;

    public DexBackedArrayPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
        super(dexFile, OPCODE, instructionStart);
        int localElementWidth = dexFile.getDataBuffer().readUshort(instructionStart + 2);
        if (localElementWidth == 0) {
            this.elementWidth = 1;
            this.elementCount = 0;
            return;
        }
        this.elementWidth = localElementWidth;
        this.elementCount = dexFile.getDataBuffer().readSmallUint(instructionStart + 4);
        if (this.elementWidth * this.elementCount > 2147483647L) {
            throw new ExceptionWithContext("Invalid array-payload instruction: element width*count overflows", new Object[0]);
        }
    }

    @Override // org.jf.dexlib2.iface.instruction.formats.ArrayPayload
    public int getElementWidth() {
        return this.elementWidth;
    }

    @Override // org.jf.dexlib2.iface.instruction.formats.ArrayPayload
    @Nonnull
    public List<Number> getArrayElements() {
        final int elementsStart = this.instructionStart + 8;
        if (this.elementCount == 0) {
            return ImmutableList.of();
        }
        switch (this.elementWidth) {
            case 1:
                return new C1ReturnedList() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedArrayPayload.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Integer.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readByte(elementsStart + index));
                    }
                };
            case 2:
                return new C1ReturnedList() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedArrayPayload.2
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Integer.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readShort(elementsStart + (index * 2)));
                    }
                };
            case 3:
            case 5:
            case 6:
            case 7:
            default:
                throw new ExceptionWithContext("Invalid element width: %d", Integer.valueOf(this.elementWidth));
            case 4:
                return new C1ReturnedList() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedArrayPayload.3
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Integer.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readInt(elementsStart + (index * 4)));
                    }
                };
            case 8:
                return new C1ReturnedList() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedArrayPayload.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
                    @Nonnull
                    public Number readItem(int index) {
                        return Long.valueOf(DexBackedArrayPayload.this.dexFile.getDataBuffer().readLong(elementsStart + (index * 8)));
                    }
                };
        }
    }

    /* renamed from: org.jf.dexlib2.dexbacked.instruction.DexBackedArrayPayload$1ReturnedList  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/instruction/DexBackedArrayPayload$1ReturnedList.class */
    abstract class C1ReturnedList extends FixedSizeList<Number> {
        C1ReturnedList() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return DexBackedArrayPayload.this.elementCount;
        }
    }

    @Override // org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction, org.jf.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return 4 + (((this.elementWidth * this.elementCount) + 1) / 2);
    }
}
