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
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/DoubleEncodingAlgorithm.class */
public class DoubleEncodingAlgorithm extends IEEE754FloatingPointEncodingAlgorithm {
    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 8 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthIsNotMultipleOfDouble", new Object[]{8}));
        }
        return octetLength / 8;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 8;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
        double[] data = new double[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToDoubleArray(data, 0, b, start, length);
        return data;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s) throws IOException {
        return decodeFromInputStreamToDoubleArray(s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
        if (!(data instanceof double[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotDouble"));
        }
        double[] fdata = (double[]) data;
        encodeToOutputStreamFromDoubleArray(fdata, s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List doubleList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.fastinfoset.algorithm.DoubleEncodingAlgorithm.1
            @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String fStringValue = cb.subSequence(start2, end).toString();
                doubleList.add(Double.valueOf(fStringValue));
            }
        });
        return generateArrayFromList(doubleList);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (!(data instanceof double[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotDouble"));
        }
        double[] fdata = (double[]) data;
        convertToCharactersFromDoubleArray(fdata, s);
    }

    public final void decodeFromBytesToDoubleArray(double[] data, int fstart, byte[] b, int start, int length) {
        int start2;
        int start3;
        int start4;
        int start5;
        int start6;
        int start7;
        int start8;
        int size = length / 8;
        for (int i = 0; i < size; i++) {
            int i2 = start;
            start = start + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1;
            long bits = ((b[i2] & 255) << 56) | ((b[start2] & 255) << 48) | ((b[start3] & 255) << 40) | ((b[start4] & 255) << 32) | ((b[start5] & 255) << 24) | ((b[start6] & 255) << 16) | ((b[start7] & 255) << 8) | (b[start8] & 255);
            int i3 = fstart;
            fstart++;
            data[i3] = Double.longBitsToDouble(bits);
        }
    }

    public final double[] decodeFromInputStreamToDoubleArray(InputStream s) throws IOException {
        List doubleList = new ArrayList();
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
                    return generateArrayFromList(doubleList);
                }
            }
            long bits = ((b[0] & 255) << 56) | ((b[1] & 255) << 48) | ((b[2] & 255) << 40) | ((b[3] & 255) << 32) | ((b[4] & 255) << 24) | ((b[5] & 255) << 16) | ((b[6] & 255) << 8) | (b[7] & 255);
            doubleList.add(Double.valueOf(Double.longBitsToDouble(bits)));
        }
    }

    public final void encodeToOutputStreamFromDoubleArray(double[] fdata, OutputStream s) throws IOException {
        for (double d : fdata) {
            long bits = Double.doubleToLongBits(d);
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
        encodeToBytesFromDoubleArray((double[]) array, astart, alength, b, start);
    }

    public final void encodeToBytesFromDoubleArray(double[] fdata, int fstart, int flength, byte[] b, int start) {
        int fend = fstart + flength;
        for (int i = fstart; i < fend; i++) {
            long bits = Double.doubleToLongBits(fdata[i]);
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

    public final void convertToCharactersFromDoubleArray(double[] fdata, StringBuffer s) {
        int end = fdata.length - 1;
        for (int i = 0; i <= end; i++) {
            s.append(Double.toString(fdata[i]));
            if (i != end) {
                s.append(' ');
            }
        }
    }

    public final double[] generateArrayFromList(List array) {
        double[] fdata = new double[array.size()];
        for (int i = 0; i < fdata.length; i++) {
            fdata[i] = ((Double) array.get(i)).doubleValue();
        }
        return fdata;
    }
}
