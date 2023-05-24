package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/CdexHeaderItem.class */
public class CdexHeaderItem {
    private static final byte[] MAGIC_VALUE = {99, 100, 101, 120, 0, 0, 0, 0};
    private static final int[] SUPPORTED_CDEX_VERSIONS = {1};
    public static final int FEATURE_FLAGS_OFFSET = 112;
    public static final int DEBUG_INFO_OFFSETS_POS_OFFSET = 116;
    public static final int DEBUG_INFO_OFFSETS_TABLE_OFFSET = 120;
    public static final int DEBUG_INFO_BASE = 124;

    public static boolean verifyMagic(byte[] buf, int offset) {
        if (buf.length - offset < 8) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (buf[offset + i] != MAGIC_VALUE[i]) {
                return false;
            }
        }
        for (int i2 = 4; i2 < 7; i2++) {
            if (buf[offset + i2] < 48 || buf[offset + i2] > 57) {
                return false;
            }
        }
        if (buf[offset + 7] != MAGIC_VALUE[7]) {
            return false;
        }
        return true;
    }

    public static int getVersion(byte[] buf, int offset) {
        if (!verifyMagic(buf, offset)) {
            return -1;
        }
        return getVersionUnchecked(buf, offset);
    }

    private static int getVersionUnchecked(byte[] buf, int offset) {
        int version = (buf[offset + 4] - 48) * 100;
        return version + ((buf[offset + 5] - 48) * 10) + (buf[offset + 6] - 48);
    }

    public static boolean isSupportedCdexVersion(int version) {
        for (int i = 0; i < SUPPORTED_CDEX_VERSIONS.length; i++) {
            if (SUPPORTED_CDEX_VERSIONS[i] == version) {
                return true;
            }
        }
        return false;
    }

    public static void annotateCdexHeaderFields(@Nonnull AnnotatedBytes out, DexBuffer buf) {
        out.annotate(4, "feature_flags: 0x%x", Integer.valueOf(buf.readInt(out.getCursor())));
        out.annotate(4, "debug_info_offsets_pos: 0x%x", Integer.valueOf(buf.readInt(out.getCursor())));
        out.annotate(4, "debug_info_offsets_table_offset: 0x%x", Integer.valueOf(buf.readInt(out.getCursor())));
        out.annotate(4, "debug_info_base: 0x%x", Integer.valueOf(buf.readInt(out.getCursor())));
    }
}
