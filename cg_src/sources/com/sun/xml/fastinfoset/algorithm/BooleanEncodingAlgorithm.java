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
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/BooleanEncodingAlgorithm.class */
public class BooleanEncodingAlgorithm extends BuiltInEncodingAlgorithm {
    private static final int[] BIT_TABLE = {128, 64, 32, 16, 8, 4, 2, 1};

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public int getOctetLengthFromPrimitiveLength(int primitiveLength) {
        if (primitiveLength < 5) {
            return 1;
        }
        int div = primitiveLength / 8;
        if (div == 0) {
            return 2;
        }
        return 1 + div;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromBytes(byte[] b, int start, int length) throws EncodingAlgorithmException {
        int blength = getPrimtiveLengthFromOctetLength(length, b[start]);
        boolean[] data = new boolean[blength];
        decodeFromBytesToBooleanArray(data, 0, blength, b, start, length);
        return data;
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object decodeFromInputStream(InputStream s) throws IOException {
        List booleanList = new ArrayList();
        int value = s.read();
        if (value == -1) {
            throw new EOFException();
        }
        int unusedBits = (value >> 4) & 255;
        int bitPosition = 4;
        int bitPositionEnd = 8;
        do {
            int valueNext = s.read();
            if (valueNext == -1) {
                bitPositionEnd -= unusedBits;
            }
            while (bitPosition < bitPositionEnd) {
                int i = bitPosition;
                bitPosition++;
                booleanList.add(Boolean.valueOf((value & BIT_TABLE[i]) > 0));
            }
            value = valueNext;
        } while (value != -1);
        return generateArrayFromList(booleanList);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public void encodeToOutputStream(Object data, OutputStream s) throws IOException {
        if (!(data instanceof boolean[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotBoolean"));
        }
        boolean[] array = (boolean[]) data;
        int alength = array.length;
        int mod = (alength + 4) % 8;
        int unusedBits = mod == 0 ? 0 : 8 - mod;
        int bitPosition = 4;
        int value = unusedBits << 4;
        int astart = 0;
        while (astart < alength) {
            int i = astart;
            astart++;
            if (array[i]) {
                value |= BIT_TABLE[bitPosition];
            }
            bitPosition++;
            if (bitPosition == 8) {
                s.write(value);
                value = 0;
                bitPosition = 0;
            }
        }
        if (bitPosition != 8) {
            s.write(value);
        }
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        if (length == 0) {
            return new boolean[0];
        }
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List booleanList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.fastinfoset.algorithm.BooleanEncodingAlgorithm.1
            @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                if (cb.charAt(start2) == 't') {
                    booleanList.add(Boolean.TRUE);
                } else {
                    booleanList.add(Boolean.FALSE);
                }
            }
        });
        return generateArrayFromList(booleanList);
    }

    @Override // org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (data == null) {
            return;
        }
        boolean[] value = (boolean[]) data;
        if (value.length == 0) {
            return;
        }
        s.ensureCapacity(value.length * 5);
        int end = value.length - 1;
        for (int i = 0; i <= end; i++) {
            if (value[i]) {
                s.append("true");
            } else {
                s.append("false");
            }
            if (i != end) {
                s.append(' ');
            }
        }
    }

    public int getPrimtiveLengthFromOctetLength(int octetLength, int firstOctet) throws EncodingAlgorithmException {
        int unusedBits = (firstOctet >> 4) & 255;
        if (octetLength == 1) {
            if (unusedBits > 3) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.unusedBits4"));
            }
            return 4 - unusedBits;
        } else if (unusedBits > 7) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.unusedBits8"));
        } else {
            return ((octetLength * 8) - 4) - unusedBits;
        }
    }

    public final void decodeFromBytesToBooleanArray(boolean[] bdata, int bstart, int blength, byte[] b, int start, int length) {
        int start2 = start + 1;
        int value = b[start] & 255;
        int bitPosition = 4;
        int bend = bstart + blength;
        while (bstart < bend) {
            if (bitPosition == 8) {
                int i = start2;
                start2++;
                value = b[i] & 255;
                bitPosition = 0;
            }
            int i2 = bstart;
            bstart++;
            int i3 = bitPosition;
            bitPosition++;
            bdata[i2] = (value & BIT_TABLE[i3]) > 0;
        }
    }

    @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public void encodeToBytes(Object array, int astart, int alength, byte[] b, int start) {
        if (!(array instanceof boolean[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotBoolean"));
        }
        encodeToBytesFromBooleanArray((boolean[]) array, astart, alength, b, start);
    }

    public void encodeToBytesFromBooleanArray(boolean[] array, int astart, int alength, byte[] b, int start) {
        int mod = (alength + 4) % 8;
        int unusedBits = mod == 0 ? 0 : 8 - mod;
        int bitPosition = 4;
        int value = unusedBits << 4;
        int aend = astart + alength;
        while (astart < aend) {
            int i = astart;
            astart++;
            if (array[i]) {
                value |= BIT_TABLE[bitPosition];
            }
            bitPosition++;
            if (bitPosition == 8) {
                int i2 = start;
                start++;
                b[i2] = (byte) value;
                value = 0;
                bitPosition = 0;
            }
        }
        if (bitPosition > 0) {
            b[start] = (byte) value;
        }
    }

    private boolean[] generateArrayFromList(List array) {
        boolean[] bdata = new boolean[array.size()];
        for (int i = 0; i < bdata.length; i++) {
            bdata[i] = ((Boolean) array.get(i)).booleanValue();
        }
        return bdata;
    }
}
