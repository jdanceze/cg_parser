package com.sun.xml.fastinfoset.algorithm;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.HelpFormatter;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/UUIDEncodingAlgorithm.class */
public class UUIDEncodingAlgorithm extends LongEncodingAlgorithm {
    private long _msb;
    private long _lsb;

    @Override // com.sun.xml.fastinfoset.algorithm.LongEncodingAlgorithm, com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm
    public final int getPrimtiveLengthFromOctetLength(int octetLength) throws EncodingAlgorithmException {
        if (octetLength % 16 != 0) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.lengthNotMultipleOfUUID", new Object[]{16}));
        }
        return octetLength / 8;
    }

    @Override // com.sun.xml.fastinfoset.algorithm.LongEncodingAlgorithm, org.jvnet.fastinfoset.EncodingAlgorithm
    public final Object convertFromCharacters(char[] ch, int start, int length) {
        final CharBuffer cb = CharBuffer.wrap(ch, start, length);
        final List longList = new ArrayList();
        matchWhiteSpaceDelimnatedWords(cb, new BuiltInEncodingAlgorithm.WordListener() { // from class: com.sun.xml.fastinfoset.algorithm.UUIDEncodingAlgorithm.1
            @Override // com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithm.WordListener
            public void word(int start2, int end) {
                String uuidValue = cb.subSequence(start2, end).toString();
                UUIDEncodingAlgorithm.this.fromUUIDString(uuidValue);
                longList.add(Long.valueOf(UUIDEncodingAlgorithm.this._msb));
                longList.add(Long.valueOf(UUIDEncodingAlgorithm.this._lsb));
            }
        });
        return generateArrayFromList(longList);
    }

    @Override // com.sun.xml.fastinfoset.algorithm.LongEncodingAlgorithm, org.jvnet.fastinfoset.EncodingAlgorithm
    public final void convertToCharacters(Object data, StringBuffer s) {
        if (!(data instanceof long[])) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.dataNotLongArray"));
        }
        long[] ldata = (long[]) data;
        int end = ldata.length - 2;
        for (int i = 0; i <= end; i += 2) {
            s.append(toUUIDString(ldata[i], ldata[i + 1]));
            if (i != end) {
                s.append(' ');
            }
        }
    }

    final void fromUUIDString(String name) {
        String[] components = name.split(HelpFormatter.DEFAULT_OPT_PREFIX);
        if (components.length != 5) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.invalidUUID", new Object[]{name}));
        }
        for (int i = 0; i < 5; i++) {
            components[i] = "0x" + components[i];
        }
        this._msb = Long.parseLong(components[0], 16);
        this._msb <<= 16;
        this._msb |= Long.parseLong(components[1], 16);
        this._msb <<= 16;
        this._msb |= Long.parseLong(components[2], 16);
        this._lsb = Long.parseLong(components[3], 16);
        this._lsb <<= 48;
        this._lsb |= Long.parseLong(components[4], 16);
    }

    final String toUUIDString(long msb, long lsb) {
        return digits(msb >> 32, 8) + HelpFormatter.DEFAULT_OPT_PREFIX + digits(msb >> 16, 4) + HelpFormatter.DEFAULT_OPT_PREFIX + digits(msb, 4) + HelpFormatter.DEFAULT_OPT_PREFIX + digits(lsb >> 48, 4) + HelpFormatter.DEFAULT_OPT_PREFIX + digits(lsb, 12);
    }

    final String digits(long val, int digits) {
        long hi = 1 << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
