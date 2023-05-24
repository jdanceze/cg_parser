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
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/FloatEncodingAlgorithm.class */
public class FloatEncodingAlgorithm extends IEEE754FloatingPointEncodingAlgorithm {
    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 4 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfFloat", new Object[]{4}));
        }
        return octetLength / 4;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        return primitiveLength * 4;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
        float[] data = new float[getPrimtiveLengthFromOctetLength(length)];
        decodeFromBytesToFloatArray(data, 0, b, start, length);
        return data;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s) throws IOException {
        return decodeFromInputStreamToFloatArray(s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
        if (!(data instanceof float[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotFloat"));
        }
        float[] fdata = (float[]) data;
        encodeToOutputStreamFromFloatArray(fdata, s);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List floatList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.fastinfoset.algorithm.FloatEncodingAlgorithm.1
            @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String fStringValue = cb.subSequence(start2, end).toString();
                floatList.add(Float.valueOf(fStringValue));
            }
        });
        return generateArrayFromList(floatList);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (!(data instanceof float[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotFloat"));
        }
        float[] fdata = (float[]) data;
        convertToCharactersFromFloatArray(fdata, s);
    }

    public final void decodeFromBytesToFloatArray(float[] data, int fstart, byte[] b, int start, int length) {
        int size = length / 4;
        for (int i = 0; i < size; i++) {
            int i2 = start;
            int start2 = start + 1;
            int start3 = start2 + 1;
            int start4 = start3 + 1;
            start = start4 + 1;
            int bits = ((b[i2] & 255) << 24) | ((b[start2] & 255) << 16) | ((b[start3] & 255) << 8) | (b[start4] & 255);
            int i3 = fstart;
            fstart++;
            data[i3] = Float.intBitsToFloat(bits);
        }
    }

    public final float[] decodeFromInputStreamToFloatArray(InputStream s) throws IOException {
        List floatList = new ArrayList();
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
                    return generateArrayFromList(floatList);
                }
            }
            int bits = ((b[0] & 255) << 24) | ((b[1] & 255) << 16) | ((b[2] & 255) << 8) | (b[3] & 255);
            floatList.add(Float.valueOf(Float.intBitsToFloat(bits)));
        }
    }

    public final void encodeToOutputStreamFromFloatArray(float[] fdata, OutputStream s) throws IOException {
        for (float f : fdata) {
            int bits = Float.floatToIntBits(f);
            s.write((bits >>> 24) & 255);
            s.write((bits >>> 16) & 255);
            s.write((bits >>> 8) & 255);
            s.write(bits & 255);
        }
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
        encodeToBytesFromFloatArray((float[]) array, astart, alength, b, start);
    }

    public final void encodeToBytesFromFloatArray(float[] fdata, int fstart, int flength, byte[] b, int start) {
        int fend = fstart + flength;
        for (int i = fstart; i < fend; i++) {
            int bits = Float.floatToIntBits(fdata[i]);
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

    public final void convertToCharactersFromFloatArray(float[] fdata, StringBuffer s) {
        int end = fdata.length - 1;
        for (int i = 0; i <= end; i++) {
            s.append(Float.toString(fdata[i]));
            if (i != end) {
                s.append(' ');
            }
        }
    }

    public final float[] generateArrayFromList(List array) {
        float[] fdata = new float[array.size()];
        for (int i = 0; i < fdata.length; i++) {
            fdata[i] = ((Float) array.get(i)).floatValue();
        }
        return fdata;
    }
}
