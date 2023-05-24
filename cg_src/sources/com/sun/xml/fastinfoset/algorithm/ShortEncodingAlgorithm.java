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
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/ShortEncodingAlgorithm.class */
public class ShortEncodingAlgorithm extends IntegerEncodingAlgorithm {
    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 2 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfShort", new Object[]{2}));
        }
        return octetLength / 2;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 2;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
        short[] data = new short[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToShortArray(data, 0, b, start, length);
        return data;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s) throws IOException {
        return decodeFromInputStreamToShortArray(s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
        if (!(data instanceof short[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotShortArray"));
        }
        short[] idata = (short[]) data;
        encodeToOutputStreamFromShortArray(idata, s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List shortList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.fastinfoset.algorithm.ShortEncodingAlgorithm.1
            @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String iStringValue = cb.subSequence(start2, end).toString();
                shortList.add(Short.valueOf(iStringValue));
            }
        });
        return generateArrayFromList(shortList);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (!(data instanceof short[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotShortArray"));
        }
        short[] idata = (short[]) data;
        convertToCharactersFromShortArray(idata, s);
    }

    public final void decodeFromBytesToShortArray(short[] sdata, int istart, byte[] b, int start, int length) {
        int size = length / 2;
        for (int i = 0; i < size; i++) {
            int i2 = istart;
            istart++;
            int i3 = start;
            int start2 = start + 1;
            start = start2 + 1;
            sdata[i2] = (short) (((b[i3] & 255) << 8) | (b[start2] & 255));
        }
    }

    public final short[] decodeFromInputStreamToShortArray(InputStream s) throws IOException {
        List shortList = new ArrayList();
        byte[] b = new byte[2];
        while (true) {
            int n = s.read(b);
            if (n != 2) {
                if (n != -1) {
                    while (n != 2) {
                        int m = s.read(b, n, 2 - n);
                        if (m == -1) {
                            throw new EOFException();
                        }
                        n += m;
                    }
                    continue;
                } else {
                    return generateArrayFromList(shortList);
                }
            }
            int i = ((b[0] & 255) << 8) | (b[1] & 255);
            shortList.add(Short.valueOf((short) i));
        }
    }

    public final void encodeToOutputStreamFromShortArray(short[] idata, OutputStream s) throws IOException {
        for (short s2 : idata) {
            s.write((s2 >>> 8) & 255);
            s.write(s2 & 255);
        }
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
        encodeToBytesFromShortArray((short[]) array, astart, alength, b, start);
    }

    public final void encodeToBytesFromShortArray(short[] sdata, int istart, int ilength, byte[] b, int start) {
        int iend = istart + ilength;
        for (int i = istart; i < iend; i++) {
            short bits = sdata[i];
            int i2 = start;
            int start2 = start + 1;
            b[i2] = (byte) ((bits >>> 8) & 255);
            start = start2 + 1;
            b[start2] = (byte) (bits & 255);
        }
    }

    public final void convertToCharactersFromShortArray(short[] sdata, StringBuffer s) {
        int end = sdata.length - 1;
        for (int i = 0; i <= end; i++) {
            s.append(Short.toString(sdata[i]));
            if (i != end) {
                s.append(' ');
            }
        }
    }

    public final short[] generateArrayFromList(List array) {
        short[] sdata = new short[array.size()];
        for (int i = 0; i < sdata.length; i++) {
            sdata[i] = ((Short) array.get(i)).shortValue();
        }
        return sdata;
    }
}
