package com.sun.xml.fastinfoset.algorithm;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/BASE64EncodingAlgorithm.class */
public class BASE64EncodingAlgorithm extends BuiltInEncodingAlgorithm {
    static final char[] encodeBase64 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    static final int[] decodeBase64 = {62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

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
        int blockCount = encodedLength / 4;
        int partialBlockLength = 3;
        if (encodedValue.charAt(encodedLength - 1) == '=') {
            partialBlockLength = 3 - 1;
            if (encodedValue.charAt(encodedLength - 2) == '=') {
                partialBlockLength--;
            }
        }
        int valueLength = ((blockCount - 1) * 3) + partialBlockLength;
        byte[] value = new byte[valueLength];
        int idx = 0;
        int encodedIdx = 0;
        for (int i = 0; i < blockCount; i++) {
            int i2 = encodedIdx;
            int encodedIdx2 = encodedIdx + 1;
            int x1 = decodeBase64[encodedValue.charAt(i2) - '+'];
            int encodedIdx3 = encodedIdx2 + 1;
            int x2 = decodeBase64[encodedValue.charAt(encodedIdx2) - '+'];
            int encodedIdx4 = encodedIdx3 + 1;
            int x3 = decodeBase64[encodedValue.charAt(encodedIdx3) - '+'];
            encodedIdx = encodedIdx4 + 1;
            int x4 = decodeBase64[encodedValue.charAt(encodedIdx4) - '+'];
            int i3 = idx;
            idx++;
            value[i3] = (byte) ((x1 << 2) | (x2 >> 4));
            if (idx < valueLength) {
                idx++;
                value[idx] = (byte) (((x2 & 15) << 4) | (x3 >> 2));
            }
            if (idx < valueLength) {
                int i4 = idx;
                idx++;
                value[i4] = (byte) (((x3 & 3) << 6) | x4);
            }
        }
        return value;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (data == null) {
            return;
        }
        byte[] value = (byte[]) data;
        convertToCharacters(value, 0, value.length, s);
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        return octetLength;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
        System.arraycopy((byte[]) array, astart, b, start, alength);
    }

    public final void convertToCharacters(byte[] data, int offset, int length, StringBuffer s) {
        int i;
        int i2;
        int i3;
        if (data == null || length == 0) {
            return;
        }
        int partialBlockLength = length % 3;
        if (partialBlockLength != 0) {
            i = (length / 3) + 1;
        } else {
            i = length / 3;
        }
        int blockCount = i;
        int encodedLength = blockCount * 4;
        int originalBufferSize = s.length();
        s.ensureCapacity(encodedLength + originalBufferSize);
        int idx = offset;
        int lastIdx = offset + length;
        for (int i4 = 0; i4 < blockCount; i4++) {
            int i5 = idx;
            idx++;
            int b1 = data[i5] & 255;
            if (idx < lastIdx) {
                idx++;
                i2 = data[idx] & 255;
            } else {
                i2 = 0;
            }
            int b2 = i2;
            if (idx < lastIdx) {
                int i6 = idx;
                idx++;
                i3 = data[i6] & 255;
            } else {
                i3 = 0;
            }
            int b3 = i3;
            s.append(encodeBase64[b1 >> 2]);
            s.append(encodeBase64[((b1 & 3) << 4) | (b2 >> 4)]);
            s.append(encodeBase64[((b2 & 15) << 2) | (b3 >> 6)]);
            s.append(encodeBase64[b3 & 63]);
        }
        switch (partialBlockLength) {
            case 1:
                s.setCharAt((originalBufferSize + encodedLength) - 1, '=');
                s.setCharAt((originalBufferSize + encodedLength) - 2, '=');
                return;
            case 2:
                s.setCharAt((originalBufferSize + encodedLength) - 1, '=');
                return;
            default:
                return;
        }
    }
}
