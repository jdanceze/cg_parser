package org.apache.http.client.utils;

import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/utils/Rfc3492Idn.class */
public class Rfc3492Idn implements Idn {
    private static final int base = 36;
    private static final int tmin = 1;
    private static final int tmax = 26;
    private static final int skew = 38;
    private static final int damp = 700;
    private static final int initial_bias = 72;
    private static final int initial_n = 128;
    private static final char delimiter = '-';
    private static final String ACE_PREFIX = "xn--";

    private int adapt(int delta, int numpoints, boolean firsttime) {
        int delta2 = firsttime ? delta / 700 : delta / 2;
        int delta3 = delta2 + (delta2 / numpoints);
        int i = 0;
        while (true) {
            int k = i;
            if (delta3 > 455) {
                delta3 /= 35;
                i = k + 36;
            } else {
                return k + ((36 * delta3) / (delta3 + 38));
            }
        }
    }

    private int digit(char c) {
        if (c < 'A' || c > 'Z') {
            if (c < 'a' || c > 'z') {
                if (c < '0' || c > '9') {
                    throw new IllegalArgumentException("illegal digit: " + c);
                }
                return (c - '0') + 26;
            }
            return c - 'a';
        }
        return c - 'A';
    }

    @Override // org.apache.http.client.utils.Idn
    public String toUnicode(String punycode) {
        StringBuilder unicode = new StringBuilder(punycode.length());
        StringTokenizer tok = new StringTokenizer(punycode, ".");
        while (tok.hasMoreTokens()) {
            String t = tok.nextToken();
            if (unicode.length() > 0) {
                unicode.append('.');
            }
            if (t.startsWith(ACE_PREFIX)) {
                t = decode(t.substring(4));
            }
            unicode.append(t);
        }
        return unicode.toString();
    }

    protected String decode(String input) {
        int t;
        int n = 128;
        int i = 0;
        int bias = 72;
        StringBuilder output = new StringBuilder(input.length());
        int lastdelim = input.lastIndexOf(45);
        if (lastdelim != -1) {
            output.append(input.subSequence(0, lastdelim));
            input = input.substring(lastdelim + 1);
        }
        while (input.length() > 0) {
            int oldi = i;
            int w = 1;
            int k = 36;
            while (input.length() != 0) {
                char c = input.charAt(0);
                input = input.substring(1);
                int digit = digit(c);
                i += digit * w;
                if (k <= bias + 1) {
                    t = 1;
                } else if (k >= bias + 26) {
                    t = 26;
                } else {
                    t = k - bias;
                }
                if (digit < t) {
                    break;
                }
                w *= 36 - t;
                k += 36;
            }
            bias = adapt(i - oldi, output.length() + 1, oldi == 0);
            n += i / (output.length() + 1);
            int i2 = i % (output.length() + 1);
            output.insert(i2, (char) n);
            i = i2 + 1;
        }
        return output.toString();
    }
}
