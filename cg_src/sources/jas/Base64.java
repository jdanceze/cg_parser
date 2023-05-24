package jas;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/Base64.class */
public class Base64 {
    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static byte[] codes = new byte[256];

    public static char[] encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        int i = 0;
        int index = 0;
        while (i < data.length) {
            boolean quad = false;
            boolean trip = false;
            int val = (255 & data[i]) << 8;
            if (i + 1 < data.length) {
                val |= 255 & data[i + 1];
                trip = true;
            }
            int val2 = val << 8;
            if (i + 2 < data.length) {
                val2 |= 255 & data[i + 2];
                quad = true;
            }
            out[index + 3] = alphabet[quad ? val2 & 63 : 64];
            int val3 = val2 >> 6;
            out[index + 2] = alphabet[trip ? val3 & 63 : 64];
            int val4 = val3 >> 6;
            out[index + 1] = alphabet[val4 & 63];
            out[index + 0] = alphabet[(val4 >> 6) & 63];
            i += 3;
            index += 4;
        }
        return out;
    }

    public static byte[] decode(char[] data) {
        int tempLen = data.length;
        for (int ix = 0; ix < data.length; ix++) {
            if (data[ix] > 255 || codes[data[ix]] < 0) {
                tempLen--;
            }
        }
        int len = (tempLen / 4) * 3;
        if (tempLen % 4 == 3) {
            len += 2;
        }
        if (tempLen % 4 == 2) {
            len++;
        }
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for (int ix2 = 0; ix2 < data.length; ix2++) {
            int value = data[ix2] > 255 ? (byte) -1 : codes[data[ix2]];
            if (value >= 0) {
                shift += 6;
                accum = (accum << 6) | value;
                if (shift >= 8) {
                    shift -= 8;
                    int i = index;
                    index++;
                    out[i] = (byte) ((accum >> shift) & 255);
                }
            }
        }
        if (index != out.length) {
            throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ")");
        }
        return out;
    }

    static {
        for (int i = 0; i < 256; i++) {
            codes[i] = -1;
        }
        for (int i2 = 65; i2 <= 90; i2++) {
            codes[i2] = (byte) (i2 - 65);
        }
        for (int i3 = 97; i3 <= 122; i3++) {
            codes[i3] = (byte) ((26 + i3) - 97);
        }
        for (int i4 = 48; i4 <= 57; i4++) {
            codes[i4] = (byte) ((52 + i4) - 48);
        }
        codes[43] = 62;
        codes[47] = 63;
    }
}
