package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.CDexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/CdexDebugOffsetTable.class */
public class CdexDebugOffsetTable {
    @Nonnull
    public static void annotate(@Nonnull DexAnnotator annotator, DexBuffer buffer) {
        DexReader reader = buffer.readerAt(annotator.getCursor());
        SectionAnnotator debugInfoAnnotator = annotator.getAnnotator(ItemType.DEBUG_INFO_ITEM);
        int methodCount = annotator.dexFile.getMethodSection().size();
        for (int methodIndex = 0; methodIndex < methodCount; methodIndex += 16) {
            annotator.annotate(0, "Offset chuck for methods %d-%d", Integer.valueOf(methodIndex), Integer.valueOf(Math.min(methodIndex + 16, methodCount)));
            annotator.indent();
            int bitmask = (reader.readUbyte() << 8) | reader.readUbyte();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                sb.append((bitmask >> i) & 1);
            }
            annotator.annotate(2, "bitmask: 0b%s", sb.reverse());
            int debugOffset = ((CDexBackedDexFile) annotator.dexFile).getDebugInfoBase();
            for (int i2 = 0; i2 < 16; i2++) {
                if ((bitmask & 1) != 0) {
                    int offsetDelta = reader.readBigUleb128();
                    debugOffset += offsetDelta;
                    annotator.annotateTo(reader.getOffset(), "[method_id: %d]: offset_delta: %d  (offset=0x%x)", Integer.valueOf(methodIndex + i2), Integer.valueOf(offsetDelta), Integer.valueOf(debugOffset));
                    debugInfoAnnotator.setItemIdentity(debugOffset, annotator.dexFile.getMethodSection().get(methodIndex + i2).toString());
                }
                bitmask >>= 1;
            }
            annotator.deindent();
        }
    }
}
