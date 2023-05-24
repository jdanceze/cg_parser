package org.jf.util;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/util/NibbleUtils.class */
public abstract class NibbleUtils {
    public static int extractHighSignedNibble(int value) {
        return (value << 24) >> 28;
    }

    public static int extractLowSignedNibble(int value) {
        return (value << 28) >> 28;
    }

    public static int extractHighUnsignedNibble(int value) {
        return (value & 240) >>> 4;
    }

    public static int extractLowUnsignedNibble(int value) {
        return value & 15;
    }
}
