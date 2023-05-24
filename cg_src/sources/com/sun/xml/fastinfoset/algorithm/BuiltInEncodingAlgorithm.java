package com.sun.xml.fastinfoset.algorithm;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jvnet.fastinfoset.EncodingAlgorithm;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/BuiltInEncodingAlgorithm.class */
public abstract class BuiltInEncodingAlgorithm implements EncodingAlgorithm {
    protected static final Pattern SPACE_PATTERN = Pattern.compile("\\s");

    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/algorithm/BuiltInEncodingAlgorithm$WordListener.class */
    public interface WordListener {
        void word(int i, int i2);
    }

    public abstract int getPrimtiveLengthFromOctetLength(int i) throws EncodingAlgorithmException;

    public abstract int getOctetLengthFromPrimitiveLength(int i);

    public abstract void encodeToBytes(Object obj, int i, int i2, byte[] bArr, int i3);

    public void matchWhiteSpaceDelimnatedWords(CharBuffer cb, WordListener wl) {
        Matcher m = SPACE_PATTERN.matcher(cb);
        int i = 0;
        while (m.find()) {
            int s = m.start();
            if (s != i) {
                wl.word(i, s);
            }
            i = m.end();
        }
        if (i != cb.length()) {
            wl.word(i, cb.length());
        }
    }

    public StringBuilder removeWhitespace(char[] ch, int start, int length) {
        StringBuilder buf = new StringBuilder();
        int firstNonWS = 0;
        int idx = 0;
        while (idx < length) {
            if (Character.isWhitespace(ch[idx + start])) {
                if (firstNonWS < idx) {
                    buf.append(ch, firstNonWS + start, idx - firstNonWS);
                }
                firstNonWS = idx + 1;
            }
            idx++;
        }
        if (firstNonWS < idx) {
            buf.append(ch, firstNonWS + start, idx - firstNonWS);
        }
        return buf;
    }
}
