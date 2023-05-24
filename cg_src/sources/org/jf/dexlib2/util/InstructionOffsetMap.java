package org.jf.dexlib2.util;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/InstructionOffsetMap.class */
public class InstructionOffsetMap {
    @Nonnull
    private final int[] instructionCodeOffsets;

    public InstructionOffsetMap(@Nonnull List<? extends Instruction> instructions) {
        this.instructionCodeOffsets = new int[instructions.size()];
        int codeOffset = 0;
        for (int i = 0; i < instructions.size(); i++) {
            this.instructionCodeOffsets[i] = codeOffset;
            codeOffset += instructions.get(i).getCodeUnits();
        }
    }

    public int getInstructionIndexAtCodeOffset(int codeOffset) {
        return getInstructionIndexAtCodeOffset(codeOffset, true);
    }

    public int getInstructionIndexAtCodeOffset(int codeOffset, boolean exact) {
        int index = Arrays.binarySearch(this.instructionCodeOffsets, codeOffset);
        if (index < 0) {
            if (exact) {
                throw new InvalidInstructionOffset(codeOffset);
            }
            return (index ^ (-1)) - 1;
        }
        return index;
    }

    public int getInstructionCodeOffset(int index) {
        if (index < 0 || index >= this.instructionCodeOffsets.length) {
            throw new InvalidInstructionIndex(index);
        }
        return this.instructionCodeOffsets[index];
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/InstructionOffsetMap$InvalidInstructionOffset.class */
    public static class InvalidInstructionOffset extends ExceptionWithContext {
        private final int instructionOffset;

        public InvalidInstructionOffset(int instructionOffset) {
            super("No instruction at offset %d", Integer.valueOf(instructionOffset));
            this.instructionOffset = instructionOffset;
        }

        public int getInstructionOffset() {
            return this.instructionOffset;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/InstructionOffsetMap$InvalidInstructionIndex.class */
    public static class InvalidInstructionIndex extends ExceptionWithContext {
        private final int instructionIndex;

        public InvalidInstructionIndex(int instructionIndex) {
            super("Instruction index out of bounds: %d", Integer.valueOf(instructionIndex));
            this.instructionIndex = instructionIndex;
        }

        public int getInstructionIndex() {
            return this.instructionIndex;
        }
    }
}
