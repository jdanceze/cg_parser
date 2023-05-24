package com.sun.xml.fastinfoset.algorithm;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/IntEncodingAlgorithm.class */
public class IntEncodingAlgorithm extends IntegerEncodingAlgorithm {
    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 4 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfInt", new Object[]{4}));
        }
        return octetLength / 4;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 4;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
        int[] data = new int[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToIntArray(data, 0, b, start, length);
        return data;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s) throws IOException {
        return decodeFromInputStreamToIntArray(s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
        if (!(data instanceof int[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotIntArray"));
        }
        int[] idata = (int[]) data;
        encodeToOutputStreamFromIntArray(idata, s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List integerList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.fastinfoset.algorithm.IntEncodingAlgorithm.1
            @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String iStringValue = cb.subSequence(start2, end).toString();
                integerList.add(Integer.valueOf(iStringValue));
            }
        });
        return generateArrayFromList(integerList);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (!(data instanceof int[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotIntArray"));
        }
        int[] idata = (int[]) data;
        convertToCharactersFromIntArray(idata, s);
    }

    public final void decodeFromBytesToIntArray(int[] idata, int istart, byte[] b, int start, int length) {
        int size = length / 4;
        for (int i = 0; i < size; i++) {
            int i2 = istart;
            istart++;
            int i3 = start;
            int start2 = start + 1;
            int start3 = start2 + 1;
            int start4 = start3 + 1;
            start = start4 + 1;
            idata[i2] = ((b[i3] & 255) << 24) | ((b[start2] & 255) << 16) | ((b[start3] & 255) << 8) | (b[start4] & 255);
        }
    }

    public final int[] decodeFromInputStreamToIntArray(InputStream s) throws IOException {
        List integerList = new ArrayList();
        byte[] b = new byte[4];
        while (true) {
            int n = s.read(b);
            if (n != 4) {
                if (n != -1) {
                    while (n != 4) {
                        int m = s.read(b, n, 4 - n);
                        if (m == -1) {
                            throw new EOFException();
                        }
                        n += m;
                    }
                    continue;
                } else {
                    return generateArrayFromList(integerList);
                }
            }
            int i = ((b[0] & 255) << 24) | ((b[1] & 255) << 16) | ((b[2] & 255) << 8) | (b[3] & 255);
            integerList.add(Integer.valueOf(i));
        }
    }

    public final void encodeToOutputStreamFromIntArray(int[] idata, OutputStream s) throws IOException {
        for (int bits : idata) {
            s.write((bits >>> 24) & 255);
            s.write((bits >>> 16) & 255);
            s.write((bits >>> 8) & 255);
            s.write(bits & 255);
        }
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
        encodeToBytesFromIntArray((int[]) array, astart, alength, b, start);
    }

    public final void encodeToBytesFromIntArray(int[] idata, int istart, int ilength, byte[] b, int start) {
        int iend = istart + ilength;
        for (int i = istart; i < iend; i++) {
            int bits = idata[i];
            int i2 = start;
            int start2 = start + 1;
            b[i2] = (byte) ((bits >>> 24) & 255);
            int start3 = start2 + 1;
            b[start2] = (byte) ((bits >>> 16) & 255);
            int start4 = start3 + 1;
            b[start3] = (byte) ((bits >>> 8) & 255);
            start = start4 + 1;
            b[start4] = (byte) (bits & 255);
        }
    }

    public final void convertToCharactersFromIntArray(int[] idata, StringBuffer s) {
        int end = idata.length - 1;
        for (int i = 0; i <= end; i++) {
            s.append(Integer.toString(idata[i]));
            if (i != end) {
                s.append(' ');
            }
        }
    }

    public final int[] generateArrayFromList(List array) {
        int[] idata = new int[array.size()];
        for (int i = 0; i < idata.length; i++) {
            idata[i] = ((Integer) array.get(i)).intValue();
        }
        return idata;
    }
}
