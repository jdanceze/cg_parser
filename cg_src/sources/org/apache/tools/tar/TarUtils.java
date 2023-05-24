package org.apache.tools.tar;

import com.sun.istack.localization.Localizable;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import org.apache.tools.zip.ZipEncoding;
import org.apache.tools.zip.ZipEncodingHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/tar/TarUtils.class */
public class TarUtils {
    private static final int BYTE_MASK = 255;
    static final ZipEncoding DEFAULT_ENCODING = ZipEncodingHelper.getZipEncoding(null);
    static final ZipEncoding FALLBACK_ENCODING = new ZipEncoding() { // from class: org.apache.tools.tar.TarUtils.1
        @Override // org.apache.tools.zip.ZipEncoding
        public boolean canEncode(String name) {
            return true;
        }

        @Override // org.apache.tools.zip.ZipEncoding
        public ByteBuffer encode(String name) {
            int length = name.length();
            byte[] buf = new byte[length];
            for (int i = 0; i < length; i++) {
                buf[i] = (byte) name.charAt(i);
            }
            return ByteBuffer.wrap(buf);
        }

        @Override // org.apache.tools.zip.ZipEncoding
        public String decode(byte[] buffer) {
            byte b;
            StringBuilder result = new StringBuilder(buffer.length);
            int length = buffer.length;
            for (int i = 0; i < length && (b = buffer[i]) != 0; i++) {
                result.append((char) (b & 255));
            }
            return result.toString();
        }
    };

    private TarUtils() {
    }

    public static long parseOctal(byte[] buffer, int offset, int length) {
        long result = 0;
        int end = offset + length;
        int start = offset;
        if (length < 2) {
            throw new IllegalArgumentException("Length " + length + " must be at least 2");
        }
        if (buffer[start] == 0) {
            return 0L;
        }
        while (start < end && buffer[start] == 32) {
            start++;
        }
        byte b = buffer[end - 1];
        while (true) {
            byte trailer = b;
            if (start >= end || !(trailer == 0 || trailer == 32)) {
                break;
            }
            end--;
            b = buffer[end - 1];
        }
        while (start < end) {
            byte currentByte = buffer[start];
            if (currentByte < 48 || currentByte > 55) {
                throw new IllegalArgumentException(exceptionMessage(buffer, offset, length, start, currentByte));
            }
            result = (result << 3) + (currentByte - 48);
            start++;
        }
        return result;
    }

    public static long parseOctalOrBinary(byte[] buffer, int offset, int length) {
        if ((buffer[offset] & 128) == 0) {
            return parseOctal(buffer, offset, length);
        }
        boolean negative = buffer[offset] == -1;
        if (length < 9) {
            return parseBinaryLong(buffer, offset, length, negative);
        }
        return parseBinaryBigInteger(buffer, offset, length, negative);
    }

    private static long parseBinaryLong(byte[] buffer, int offset, int length, boolean negative) {
        if (length >= 9) {
            throw new IllegalArgumentException(String.format("At offset %d, %d byte binary number exceeds maximum signed long value", Integer.valueOf(offset), Integer.valueOf(length)));
        }
        long val = 0;
        for (int i = 1; i < length; i++) {
            val = (val << 8) + (buffer[offset + i] & 255);
        }
        if (negative) {
            val = (val - 1) ^ (((long) Math.pow(2.0d, (length - 1) * 8.0d)) - 1);
        }
        return negative ? -val : val;
    }

    private static long parseBinaryBigInteger(byte[] buffer, int offset, int length, boolean negative) {
        byte[] remainder = new byte[length - 1];
        System.arraycopy(buffer, offset + 1, remainder, 0, length - 1);
        BigInteger val = new BigInteger(remainder);
        if (negative) {
            val = val.add(BigInteger.valueOf(-1L)).not();
        }
        if (val.bitLength() > 63) {
            throw new IllegalArgumentException(String.format("At offset %d, %d byte binary number exceeds maximum signed long value", Integer.valueOf(offset), Integer.valueOf(length)));
        }
        return negative ? -val.longValue() : val.longValue();
    }

    public static boolean parseBoolean(byte[] buffer, int offset) {
        return buffer[offset] == 1;
    }

    private static String exceptionMessage(byte[] buffer, int offset, int length, int current, byte currentByte) {
        String string = new String(buffer, offset, length);
        return String.format("Invalid byte %s at offset %d in '%s' len=%d", Byte.valueOf(currentByte), Integer.valueOf(current - offset), string.replaceAll(Localizable.NOT_LOCALIZABLE, "{NUL}"), Integer.valueOf(length));
    }

    public static String parseName(byte[] buffer, int offset, int length) {
        try {
            return parseName(buffer, offset, length, DEFAULT_ENCODING);
        } catch (IOException e) {
            try {
                return parseName(buffer, offset, length, FALLBACK_ENCODING);
            } catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    public static String parseName(byte[] buffer, int offset, int length, ZipEncoding encoding) throws IOException {
        int len = length;
        while (len > 0 && buffer[(offset + len) - 1] == 0) {
            len--;
        }
        if (len > 0) {
            byte[] b = new byte[len];
            System.arraycopy(buffer, offset, b, 0, len);
            return encoding.decode(b);
        }
        return "";
    }

    public static int formatNameBytes(String name, byte[] buf, int offset, int length) {
        try {
            return formatNameBytes(name, buf, offset, length, DEFAULT_ENCODING);
        } catch (IOException e) {
            try {
                return formatNameBytes(name, buf, offset, length, FALLBACK_ENCODING);
            } catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    public static int formatNameBytes(String name, byte[] buf, int offset, int length, ZipEncoding encoding) throws IOException {
        ByteBuffer b;
        int len = name.length();
        ByteBuffer encode = encoding.encode(name);
        while (true) {
            b = encode;
            if (b.limit() <= length || len <= 0) {
                break;
            }
            len--;
            encode = encoding.encode(name.substring(0, len));
        }
        int limit = b.limit() - b.position();
        System.arraycopy(b.array(), b.arrayOffset(), buf, offset, limit);
        for (int i = limit; i < length; i++) {
            buf[offset + i] = 0;
        }
        return offset + length;
    }

    public static void formatUnsignedOctalString(long value, byte[] buffer, int offset, int length) {
        int remaining = length - 1;
        if (value == 0) {
            remaining--;
            buffer[offset + remaining] = 48;
        } else {
            long val = value;
            while (remaining >= 0 && val != 0) {
                buffer[offset + remaining] = (byte) (48 + ((byte) (val & 7)));
                val >>>= 3;
                remaining--;
            }
            if (val != 0) {
                throw new IllegalArgumentException(String.format("%d=%s will not fit in octal number buffer of length %d", Long.valueOf(value), Long.toOctalString(value), Integer.valueOf(length)));
            }
        }
        while (remaining >= 0) {
            buffer[offset + remaining] = 48;
            remaining--;
        }
    }

    public static int formatOctalBytes(long value, byte[] buf, int offset, int length) {
        int idx = length - 2;
        formatUnsignedOctalString(value, buf, offset, idx);
        buf[offset + idx] = 32;
        buf[offset + idx + 1] = 0;
        return offset + length;
    }

    public static int formatLongOctalBytes(long value, byte[] buf, int offset, int length) {
        int idx = length - 1;
        formatUnsignedOctalString(value, buf, offset, idx);
        buf[offset + idx] = 32;
        return offset + length;
    }

    public static int formatLongOctalOrBinaryBytes(long value, byte[] buf, int offset, int length) {
        long maxAsOctalChar = length == 8 ? TarConstants.MAXID : TarConstants.MAXSIZE;
        boolean negative = value < 0;
        if (!negative && value <= maxAsOctalChar) {
            return formatLongOctalBytes(value, buf, offset, length);
        }
        if (length < 9) {
            formatLongBinary(value, buf, offset, length, negative);
        }
        formatBigIntegerBinary(value, buf, offset, length, negative);
        buf[offset] = (byte) (negative ? 255 : 128);
        return offset + length;
    }

    private static void formatLongBinary(long value, byte[] buf, int offset, int length, boolean negative) {
        int bits = (length - 1) * 8;
        long max = 1 << bits;
        long val = Math.abs(value);
        if (val >= max) {
            throw new IllegalArgumentException("Value " + value + " is too large for " + length + " byte field.");
        }
        if (negative) {
            val = ((val ^ (max - 1)) | (255 << bits)) + 1;
        }
        for (int i = (offset + length) - 1; i >= offset; i--) {
            buf[i] = (byte) val;
            val >>= 8;
        }
    }

    private static void formatBigIntegerBinary(long value, byte[] buf, int offset, int length, boolean negative) {
        BigInteger val = BigInteger.valueOf(value);
        byte[] b = val.toByteArray();
        int len = b.length;
        int off = (offset + length) - len;
        System.arraycopy(b, 0, buf, off, len);
        byte fill = (byte) (negative ? 255 : 0);
        for (int i = offset + 1; i < off; i++) {
            buf[i] = fill;
        }
    }

    public static int formatCheckSumOctalBytes(long value, byte[] buf, int offset, int length) {
        int idx = length - 2;
        formatUnsignedOctalString(value, buf, offset, idx);
        buf[offset + idx] = 0;
        buf[offset + idx + 1] = 32;
        return offset + length;
    }

    public static long computeCheckSum(byte[] buf) {
        long sum = 0;
        for (byte element : buf) {
            sum += 255 & element;
        }
        return sum;
    }
}
