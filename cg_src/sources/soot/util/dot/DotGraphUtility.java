package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/util/dot/DotGraphUtility.class */
public class DotGraphUtility {
    private static final Logger logger = LoggerFactory.getLogger(DotGraphUtility.class);

    public static String replaceQuotes(String original) {
        boolean z;
        byte[] ord = original.getBytes();
        int quotes = 0;
        boolean escapeActive = false;
        for (byte element : ord) {
            switch (element) {
                case 34:
                    quotes++;
                    if (escapeActive) {
                        quotes++;
                        break;
                    }
                    break;
                case 92:
                    z = true;
                    continue;
                    escapeActive = z;
            }
            z = false;
            escapeActive = z;
        }
        if (quotes == 0) {
            return original;
        }
        byte[] newsrc = new byte[ord.length + quotes];
        int i = 0;
        int j = 0;
        int n = ord.length;
        while (i < n) {
            if (ord[i] == 34) {
                if (i > 0 && ord[i - 1] == 92) {
                    int i2 = j;
                    j++;
                    newsrc[i2] = 92;
                }
                int i3 = j;
                j++;
                newsrc[i3] = 92;
            }
            newsrc[j] = ord[i];
            i++;
            j++;
        }
        return new String(newsrc);
    }

    public static String replaceReturns(String original) {
        byte[] ord = original.getBytes();
        int quotes = 0;
        for (byte element : ord) {
            if (element == 10) {
                quotes++;
            }
        }
        if (quotes == 0) {
            return original;
        }
        byte[] newsrc = new byte[ord.length + quotes];
        int i = 0;
        int j = 0;
        int n = ord.length;
        while (i < n) {
            if (ord[i] == 10) {
                int i2 = j;
                j++;
                newsrc[i2] = 92;
                newsrc[j] = 110;
            } else {
                newsrc[j] = ord[i];
            }
            i++;
            j++;
        }
        return new String(newsrc);
    }

    public static void renderLine(OutputStream out, String content, int indent) throws IOException {
        char[] indentChars = new char[indent];
        Arrays.fill(indentChars, ' ');
        StringBuilder sb = new StringBuilder();
        sb.append(indentChars).append(content).append('\n');
        out.write(sb.toString().getBytes());
    }
}
