package org.jf.dexlib2.dexbacked;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.raw.OdexHeaderItem;
import org.jf.dexlib2.dexbacked.util.VariableSizeList;
import org.jf.dexlib2.util.DexUtil;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedOdexFile.class */
public class DexBackedOdexFile extends DexBackedDexFile {
    private static final int DEPENDENCY_COUNT_OFFSET = 12;
    private static final int DEPENDENCY_START_OFFSET = 16;
    private final byte[] odexBuf;

    public DexBackedOdexFile(@Nonnull Opcodes opcodes, @Nonnull byte[] odexBuf, byte[] dexBuf) {
        super(opcodes, dexBuf);
        this.odexBuf = odexBuf;
    }

    @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile
    public boolean supportsOptimizedOpcodes() {
        return true;
    }

    @Nonnull
    public List<String> getDependencies() {
        int dexOffset = OdexHeaderItem.getDexOffset(this.odexBuf);
        int dependencyOffset = OdexHeaderItem.getDependenciesOffset(this.odexBuf) - dexOffset;
        final DexBuffer fromStartBuffer = new DexBuffer(getBuffer().buf, 0);
        int dependencyCount = fromStartBuffer.readInt(dependencyOffset + 12);
        return new VariableSizeList<String>(getDataBuffer(), dependencyOffset + 16, dependencyCount) { // from class: org.jf.dexlib2.dexbacked.DexBackedOdexFile.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.VariableSizeList
            public String readNextItem(@Nonnull DexReader reader, int index) {
                int length = reader.readInt();
                int offset = reader.getOffset();
                reader.moveRelative(length + 20);
                try {
                    return new String(fromStartBuffer.buf, offset, length - 1, "US-ASCII");
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    @Nonnull
    public static DexBackedOdexFile fromInputStream(@Nonnull Opcodes opcodes, @Nonnull InputStream is) throws IOException {
        DexUtil.verifyOdexHeader(is);
        is.reset();
        byte[] odexBuf = new byte[40];
        ByteStreams.readFully(is, odexBuf);
        int dexOffset = OdexHeaderItem.getDexOffset(odexBuf);
        if (dexOffset > 40) {
            ByteStreams.skipFully(is, dexOffset - 40);
        }
        byte[] dexBuf = ByteStreams.toByteArray(is);
        return new DexBackedOdexFile(opcodes, odexBuf, dexBuf);
    }

    public int getOdexVersion() {
        return OdexHeaderItem.getVersion(this.odexBuf, 0);
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedOdexFile$NotAnOdexFile.class */
    public static class NotAnOdexFile extends RuntimeException {
        public NotAnOdexFile() {
        }

        public NotAnOdexFile(Throwable cause) {
            super(cause);
        }

        public NotAnOdexFile(String message) {
            super(message);
        }

        public NotAnOdexFile(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
