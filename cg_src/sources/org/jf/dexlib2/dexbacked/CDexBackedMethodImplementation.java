package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.raw.CodeItem;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/CDexBackedMethodImplementation.class */
public class CDexBackedMethodImplementation extends DexBackedMethodImplementation {
    public CDexBackedMethodImplementation(@Nonnull DexBackedDexFile dexFile, @Nonnull DexBackedMethod method, int codeOffset) {
        super(dexFile, method, codeOffset);
    }

    public int getInsCount() {
        int insCount = (this.dexFile.getDataBuffer().readUshort(this.codeOffset) >> CodeItem.CDEX_INS_COUNT_SHIFT) & 15;
        if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_INS_COUNT) != 0) {
            int preheaderCount = 1;
            if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE) != 0) {
                preheaderCount = 1 + 2;
            }
            if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_REGISTER_COUNT) != 0) {
                preheaderCount++;
            }
            insCount += this.dexFile.getDataBuffer().readUshort(this.codeOffset - (2 * preheaderCount));
        }
        return insCount;
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedMethodImplementation, org.jf.dexlib2.iface.MethodImplementation
    public int getRegisterCount() {
        int registerCount = ((this.dexFile.getDataBuffer().readUshort(this.codeOffset) >> CodeItem.CDEX_REGISTER_COUNT_SHIFT) & 15) + getInsCount();
        if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_REGISTER_COUNT) != 0) {
            int preheaderCount = 1;
            if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE) > 0) {
                preheaderCount = 1 + 2;
            }
            registerCount += this.dexFile.getDataBuffer().readUshort(this.codeOffset - (2 * preheaderCount));
        }
        return registerCount;
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedMethodImplementation
    protected int getInstructionsSize() {
        int instructionsSize = this.dexFile.getDataBuffer().readUshort(this.codeOffset + CodeItem.CDEX_INSTRUCTIONS_SIZE_AND_PREHEADER_FLAGS_OFFSET) >> CodeItem.CDEX_INSTRUCTIONS_SIZE_SHIFT;
        if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE) != 0) {
            instructionsSize = instructionsSize + this.dexFile.getDataBuffer().readUshort(this.codeOffset - 2) + (this.dexFile.getDataBuffer().readUshort(this.codeOffset - 4) << 16);
        }
        return instructionsSize;
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedMethodImplementation
    protected int getInstructionsStartOffset() {
        return this.codeOffset + 4;
    }

    private int getPreheaderFlags() {
        return this.dexFile.getDataBuffer().readUshort(this.codeOffset + CodeItem.CDEX_INSTRUCTIONS_SIZE_AND_PREHEADER_FLAGS_OFFSET) & CodeItem.CDEX_PREHEADER_FLAGS_MASK;
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedMethodImplementation
    protected int getTriesSize() {
        int triesCount = (this.dexFile.getDataBuffer().readUshort(this.codeOffset) >> CodeItem.CDEX_TRIES_SIZE_SHIFT) & 15;
        if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_TRIES_COUNT) != 0) {
            int preheaderCount = Integer.bitCount(getPreheaderFlags());
            if ((getPreheaderFlags() & CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE) != 0) {
                preheaderCount++;
            }
            triesCount += this.dexFile.getDataBuffer().readUshort(this.codeOffset - (2 * preheaderCount));
        }
        return triesCount;
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedMethodImplementation
    protected int getDebugOffset() {
        CDexBackedDexFile cdexFile = (CDexBackedDexFile) this.dexFile;
        int debugTableItemOffset = (this.method.methodIndex / 16) * 4;
        int bitIndex = this.method.methodIndex % 16;
        int debugInfoOffsetsPos = cdexFile.getDebugInfoOffsetsPos();
        int debugTableOffset = debugInfoOffsetsPos + cdexFile.getDebugInfoOffsetsTableOffset();
        int debugOffsetsOffset = cdexFile.getDataBuffer().readSmallUint(debugTableOffset + debugTableItemOffset);
        DexReader reader = cdexFile.getDataBuffer().readerAt(debugInfoOffsetsPos + debugOffsetsOffset);
        int bitMask = (reader.readUbyte() << 8) + reader.readUbyte();
        if ((bitMask & (1 << bitIndex)) == 0) {
            return 0;
        }
        int offsetCount = Integer.bitCount(bitMask & (65535 >> (16 - bitIndex)));
        int baseDebugOffset = cdexFile.getDebugInfoBase();
        for (int i = 0; i < offsetCount; i++) {
            baseDebugOffset += reader.readBigUleb128();
        }
        return baseDebugOffset + reader.readBigUleb128();
    }
}
