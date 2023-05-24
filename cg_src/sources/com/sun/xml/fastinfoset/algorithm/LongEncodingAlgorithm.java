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
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/LongEncodingAlgorithm.class */
public class LongEncodingAlgorithm extends IntegerEncodingAlgorithm {
    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 8 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfLong", new Object[]{8}));
        }
        return octetLength / 8;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 8;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
        long[] data = new long[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToLongArray(data, 0, b, start, length);
        return data;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s) throws IOException {
        return decodeFromInputStreamToIntArray(s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
        if (!(data instanceof long[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
        }
        long[] ldata = (long[]) data;
        encodeToOutputStreamFromLongArray(ldata, s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List longList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.fastinfoset.algorithm.LongEncodingAlgorithm.1
            @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String lStringValue = cb.subSequence(start2, end).toString();
                longList.add(Long.valueOf(lStringValue));
            }
        });
        return generateArrayFromList(longList);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void convertToCharacters(Object data, StringBuffer s) {
        if (!(data instanceof long[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
        }
        long[] ldata = (long[]) data;
        convertToCharactersFromLongArray(ldata, s);
    }

    public final void decodeFromBytesToLongArray(long[] ldata, int istart, byte[] b, int start, int length) {
        int start2;
        int start3;
        int start4;
        int start5;
        int start6;
        int start7;
        int start8;
        int size = length / 8;
        for (int i = 0; i < size; i++) {
            int i2 = istart;
            istart++;
            int i3 = start;
            start = start + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1;
            ldata[i2] = ((b[i3] & 255) << 56) | ((b[start2] & 255) << 48) | ((b[start3] & 255) << 40) | ((b[start4] & 255) << 32) | ((b[start5] & 255) << 24) | ((b[start6] & 255) << 16) | ((b[start7] & 255) << 8) | (b[start8] & 255);
        }
    }

    public final long[] decodeFromInputStreamToIntArray(InputStream s) throws IOException {
        List longList = new ArrayList();
        byte[] b = new byte[8];
        while (true) {
            int n = s.read(b);
            if (n != 8) {
                if (n != -1) {
                    while (n != 8) {
                        int m = s.read(b, n, 8 - n);
                        if (m == -1) {
                            throw new EOFException();
                        }
                        n += m;
                    }
                    continue;
                } else {
                    return generateArrayFromList(longList);
                }
            }
            long l = (b[0] << 56) + ((b[1] & 255) << 48) + ((b[2] & 255) << 40) + ((b[3] & 255) << 32) + ((b[4] & 255) << 24) + ((b[5] & 255) << 16) + ((b[6] & 255) << 8) + ((b[7] & 255) << 0);
            longList.add(Long.valueOf(l));
        }
    }

    public final void encodeToOutputStreamFromLongArray(long[] ldata, OutputStream s) throws IOException {
        for (long bits : ldata) {
            s.write((int) ((bits >>> 56) & 255));
            s.write((int) ((bits >>> 48) & 255));
            s.write((int) ((bits >>> 40) & 255));
            s.write((int) ((bits >>> 32) & 255));
            s.write((int) ((bits >>> 24) & 255));
            s.write((int) ((bits >>> 16) & 255));
            s.write((int) ((bits >>> 8) & 255));
            s.write((int) (bits & 255));
        }
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
        encodeToBytesFromLongArray((long[]) array, astart, alength, b, start);
    }

    public final void encodeToBytesFromLongArray(long[] ldata, int lstart, int llength, byte[] b, int start) {
        int lend = lstart + llength;
        for (int i = lstart; i < lend; i++) {
            long bits = ldata[i];
            int i2 = start;
            int start2 = start + 1;
            b[i2] = (byte) ((bits >>> 56) & 255);
            int start3 = start2 + 1;
            b[start2] = (byte) ((bits >>> 48) & 255);
            int start4 = start3 + 1;
            b[start3] = (byte) ((bits >>> 40) & 255);
            int start5 = start4 + 1;
            b[start4] = (byte) ((bits >>> 32) & 255);
            int start6 = start5 + 1;
            b[start5] = (byte) ((bits >>> 24) & 255);
            int start7 = start6 + 1;
            b[start6] = (byte) ((bits >>> 16) & 255);
            int start8 = start7 + 1;
            b[start7] = (byte) ((bits >>> 8) & 255);
            start = start8 + 1;
            b[start8] = (byte) (bits & 255);
        }
    }

    public final void convertToCharactersFromLongArray(long[] ldata, StringBuffer s) {
        int end = ldata.length - 1;
        for (int i = 0; i <= end; i++) {
            s.append(Long.toString(ldata[i]));
            if (i != end) {
                s.append(' ');
            }
        }
    }

    public final long[] generateArrayFromList(List array) {
        long[] ldata = new long[array.size()];
        for (int i = 0; i < ldata.length; i++) {
            ldata[i] = ((Long) array.get(i)).longValue();
        }
        return ldata;
    }
}
