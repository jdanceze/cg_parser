package org.jf.dexlib2.util;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/AlignmentUtils.class */
public abstract class AlignmentUtils {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AlignmentUtils.class.desiredAssertionStatus();
    }

    public static int alignOffset(int offset, int alignment) {
        int mask = alignment - 1;
        if ($assertionsDisabled || (alignment >= 0 && (mask & alignment) == 0)) {
            return (offset + mask) & (mask ^ (-1));
        }
        throw new AssertionError();
    }

    public static boolean isAligned(int offset, int alignment) {
        return offset % alignment == 0;
    }
}
