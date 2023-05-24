package org.apache.tools.ant.util;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/Base64Converter.class */
public class Base64Converter {
    private static final int BYTE = 8;
    private static final int WORD = 16;
    private static final int BYTE_MASK = 255;
    private static final int POS_0_MASK = 63;
    private static final int POS_1_MASK = 4032;
    private static final int POS_1_SHIFT = 6;
    private static final int POS_2_MASK = 258048;
    private static final int POS_2_SHIFT = 12;
    private static final int POS_3_MASK = 16515072;
    private static final int POS_3_SHIFT = 18;
    private static final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    public static final char[] alphabet = ALPHABET;

    public String encode(String s) {
        return encode(s.getBytes());
    }

    public String encode(byte[] octetString) {
        char[] out = new char[(((octetString.length - 1) / 3) + 1) * 4];
        int outIndex = 0;
        int i = 0;
        while (i + 3 <= octetString.length) {
            int i2 = i;
            int i3 = i + 1;
            int i4 = i3 + 1;
            i = i4 + 1;
            int bits24 = ((octetString[i2] & 255) << 16) | ((octetString[i3] & 255) << 8) | (octetString[i4] & 255);
            int bits6 = (bits24 & POS_3_MASK) >> 18;
            int i5 = outIndex;
            int outIndex2 = outIndex + 1;
            out[i5] = ALPHABET[bits6];
            int bits62 = (bits24 & POS_2_MASK) >> 12;
            int outIndex3 = outIndex2 + 1;
            out[outIndex2] = ALPHABET[bits62];
            int bits63 = (bits24 & POS_1_MASK) >> 6;
            int outIndex4 = outIndex3 + 1;
            out[outIndex3] = ALPHABET[bits63];
            int bits64 = bits24 & 63;
            outIndex = outIndex4 + 1;
            out[outIndex4] = ALPHABET[bits64];
        }
        if (octetString.length - i == 2) {
            int bits242 = ((octetString[i] & 255) << 16) | ((octetString[i + 1] & 255) << 8);
            int bits65 = (bits242 & POS_3_MASK) >> 18;
            int i6 = outIndex;
            int outIndex5 = outIndex + 1;
            out[i6] = ALPHABET[bits65];
            int bits66 = (bits242 & POS_2_MASK) >> 12;
            int outIndex6 = outIndex5 + 1;
            out[outIndex5] = ALPHABET[bits66];
            int bits67 = (bits242 & POS_1_MASK) >> 6;
            int outIndex7 = outIndex6 + 1;
            out[outIndex6] = ALPHABET[bits67];
            int i7 = outIndex7 + 1;
            out[outIndex7] = '=';
        } else if (octetString.length - i == 1) {
            int bits243 = (octetString[i] & 255) << 16;
            int bits68 = (bits243 & POS_3_MASK) >> 18;
            int i8 = outIndex;
            int outIndex8 = outIndex + 1;
            out[i8] = ALPHABET[bits68];
            int bits69 = (bits243 & POS_2_MASK) >> 12;
            int outIndex9 = outIndex8 + 1;
            out[outIndex8] = ALPHABET[bits69];
            int outIndex10 = outIndex9 + 1;
            out[outIndex9] = '=';
            int i9 = outIndex10 + 1;
            out[outIndex10] = '=';
        }
        return new String(out);
    }
}
