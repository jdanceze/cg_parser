package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
/* loaded from: gencallgraphv3.jar:commons-codec-1.3.jar:org/apache/commons/codec/language/SoundexUtils.class */
final class SoundexUtils {
    SoundexUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String clean(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int len = str.length();
        char[] chars = new char[len];
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (Character.isLetter(str.charAt(i))) {
                int i2 = count;
                count++;
                chars[i2] = str.charAt(i);
            }
        }
        if (count == len) {
            return str.toUpperCase();
        }
        return new String(chars, 0, count).toUpperCase();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int difference(StringEncoder encoder, String s1, String s2) throws EncoderException {
        return differenceEncoded(encoder.encode(s1), encoder.encode(s2));
    }

    static int differenceEncoded(String es1, String es2) {
        if (es1 == null || es2 == null) {
            return 0;
        }
        int lengthToMatch = Math.min(es1.length(), es2.length());
        int diff = 0;
        for (int i = 0; i < lengthToMatch; i++) {
            if (es1.charAt(i) == es2.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }
}
