package com.sun.xml.fastinfoset.algorithm;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/HexadecimalEncodingAlgorithm.class */
public class HexadecimalEncodingAlgorithm extends BuiltInEncodingAlgorithm {
    private static final char[] NIBBLE_TO_HEXADECIMAL_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int[] HEXADECIMAL_TO_NIBBLE_TABLE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
        byte[] data = new byte[length];
        System.arraycopy(b, start, data, 0, length);
        return data;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s) throws IOException {
        throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.notImplemented"));
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
        if (!(data instanceof byte[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotByteArray"));
        }
        s.write((byte[]) data);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        if (length == 0) {
            return new byte[0];
        }
        StringBuilder encodedValue = removeWhitespace(ch, start, length);
        int encodedLength = encodedValue.length();
        if (encodedLength == 0) {
            return new byte[0];
        }
        int valueLength = encodedValue.length() / 2;
        byte[] value = new byte[valueLength];
        int encodedIdx = 0;
        for (int i = 0; i < valueLength; i++) {
            int i2 = encodedIdx;
            int encodedIdx2 = encodedIdx + 1;
            int nibble1 = HEXADECIMAL_TO_NIBBLE_TABLE[encodedValue.charAt(i2) - '0'];
            encodedIdx = encodedIdx2 + 1;
            int nibble2 = HEXADECIMAL_TO_NIBBLE_TABLE[encodedValue.charAt(encodedIdx2) - '0'];
            value[i] = (byte) ((nibble1 << 4) | nibble2);
        }
        return value;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (data == null) {
            return;
        }
        byte[] value = (byte[]) data;
        if (value.length == 0) {
            return;
        }
        s.ensureCapacity(value.length * 2);
        for (int i = 0; i < value.length; i++) {
            s.append(NIBBLE_TO_HEXADECIMAL_TABLE[(value[i] >>> 4) & 15]);
            s.append(NIBBLE_TO_HEXADECIMAL_TABLE[value[i] & 15]);
        }
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        return octetLength * 2;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength / 2;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
        System.arraycopy((byte[]) array, astart, b, start, alength);
    }
}
