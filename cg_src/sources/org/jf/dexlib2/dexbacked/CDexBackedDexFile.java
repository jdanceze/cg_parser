package org.jf.dexlib2.dexbacked;

import java.io.UnsupportedEncodingException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.raw.CdexHeaderItem;
import org.jf.dexlib2.util.DexUtil;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/CDexBackedDexFile.class */
public class CDexBackedDexFile extends DexBackedDexFile {
    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf, int offset, boolean verifyMagic) {
        super(opcodes, buf, offset, verifyMagic);
    }

    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull DexBuffer buf) {
        super(opcodes, buf);
    }

    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf, int offset) {
        super(opcodes, buf, offset);
    }

    public CDexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf) {
        super(opcodes, buf);
    }

    public static boolean isCdex(byte[] buf, int offset) {
        if (offset + 4 > buf.length) {
            return false;
        }
        try {
            byte[] cdexMagic = "cdex".getBytes("US-ASCII");
            return buf[offset] == cdexMagic[0] && buf[offset + 1] == cdexMagic[1] && buf[offset + 2] == cdexMagic[2] && buf[offset + 3] == cdexMagic[3];
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile
    protected int getVersion(byte[] buf, int offset, boolean verifyMagic) {
        if (verifyMagic) {
            return DexUtil.verifyCdexHeader(buf, offset);
        }
        return CdexHeaderItem.getVersion(buf, offset);
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile
    protected Opcodes getDefaultOpcodes(int version) {
        return Opcodes.forApi(28);
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile
    public int getBaseDataOffset() {
        return getBuffer().readSmallUint(108);
    }

    public int getDebugInfoOffsetsPos() {
        return getBuffer().readSmallUint(116);
    }

    public int getDebugInfoOffsetsTableOffset() {
        return getBuffer().readSmallUint(120);
    }

    public int getDebugInfoBase() {
        return getBuffer().readSmallUint(124);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile
    public DexBackedMethodImplementation createMethodImplementation(@Nonnull DexBackedDexFile dexFile, @Nonnull DexBackedMethod method, int codeOffset) {
        return new CDexBackedMethodImplementation(dexFile, method, codeOffset);
    }
}
