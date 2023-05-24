package org.apache.tools.zip;

import android.widget.ExpandableListView;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipLong.class */
public final class ZipLong implements Cloneable {
    private static final int BYTE_1 = 1;
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private static final int BYTE_2 = 2;
    private static final int BYTE_2_MASK = 16711680;
    private static final int BYTE_2_SHIFT = 16;
    private static final int BYTE_3 = 3;
    private static final long BYTE_3_MASK = 4278190080L;
    private static final int BYTE_3_SHIFT = 24;
    private final long value;
    public static final ZipLong CFH_SIG = new ZipLong(33639248);
    public static final ZipLong LFH_SIG = new ZipLong(67324752);
    public static final ZipLong DD_SIG = new ZipLong(134695760);
    static final ZipLong ZIP64_MAGIC = new ZipLong((long) ExpandableListView.PACKED_POSITION_VALUE_NULL);

    public ZipLong(long value) {
        this.value = value;
    }

    public ZipLong(byte[] bytes) {
        this(bytes, 0);
    }

    public ZipLong(byte[] bytes, int offset) {
        this.value = getValue(bytes, offset);
    }

    public byte[] getBytes() {
        return getBytes(this.value);
    }

    public long getValue() {
        return this.value;
    }

    public static byte[] getBytes(long value) {
        byte[] result = new byte[4];
        putLong(value, result, 0);
        return result;
    }

    public static void putLong(long value, byte[] buf, int offset) {
        int offset2 = offset + 1;
        buf[offset] = (byte) (value & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((value & 65280) >> 8);
        buf[offset3] = (byte) ((value & 16711680) >> 16);
        buf[offset3 + 1] = (byte) ((value & BYTE_3_MASK) >> 24);
    }

    public void putLong(byte[] buf, int offset) {
        putLong(this.value, buf, offset);
    }

    public static long getValue(byte[] bytes, int offset) {
        long value = (bytes[offset + 3] << 24) & BYTE_3_MASK;
        return value + ((bytes[offset + 2] << 16) & 16711680) + ((bytes[offset + 1] << 8) & 65280) + (bytes[offset] & 255);
    }

    public static long getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    public boolean equals(Object o) {
        return (o instanceof ZipLong) && this.value == ((ZipLong) o).getValue();
    }

    public int hashCode() {
        return (int) this.value;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }

    public String toString() {
        return "ZipLong value: " + this.value;
    }
}
