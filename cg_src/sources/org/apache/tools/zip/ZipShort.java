package org.apache.tools.zip;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipShort.class */
public final class ZipShort implements Cloneable {
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private final int value;

    public ZipShort(int value) {
        this.value = value;
    }

    public ZipShort(byte[] bytes) {
        this(bytes, 0);
    }

    public ZipShort(byte[] bytes, int offset) {
        this.value = getValue(bytes, offset);
    }

    public byte[] getBytes() {
        byte[] result = new byte[2];
        putShort(this.value, result, 0);
        return result;
    }

    public static void putShort(int value, byte[] buf, int offset) {
        buf[offset] = (byte) (value & 255);
        buf[offset + 1] = (byte) ((value & 65280) >> 8);
    }

    public int getValue() {
        return this.value;
    }

    public static byte[] getBytes(int value) {
        byte[] result = {(byte) (value & 255), (byte) ((value & 65280) >> 8)};
        return result;
    }

    public static int getValue(byte[] bytes, int offset) {
        int value = (bytes[offset + 1] << 8) & 65280;
        return value + (bytes[offset] & 255);
    }

    public static int getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    public boolean equals(Object o) {
        return (o instanceof ZipShort) && this.value == ((ZipShort) o).getValue();
    }

    public int hashCode() {
        return this.value;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }

    public String toString() {
        return "ZipShort value: " + this.value;
    }
}
