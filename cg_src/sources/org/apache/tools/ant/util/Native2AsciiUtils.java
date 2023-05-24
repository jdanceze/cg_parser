package org.apache.tools.ant.util;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/Native2AsciiUtils.class */
public class Native2AsciiUtils {
    private static final int MAX_ASCII = 127;

    public static String native2ascii(String line) {
        char[] charArray;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c <= 127) {
                sb.append(c);
            } else {
                sb.append(String.format("\\u%04x", Integer.valueOf(c)));
            }
        }
        return sb.toString();
    }

    public static String ascii2native(String line) {
        int unescaped;
        StringBuilder sb = new StringBuilder();
        int inputLen = line.length();
        int i = 0;
        while (i < inputLen) {
            char c = line.charAt(i);
            if (c != '\\' || i >= inputLen - 5) {
                sb.append(c);
            } else {
                i++;
                char u = line.charAt(i);
                if (u == 'u' && (unescaped = tryParse(line, i + 1)) >= 0) {
                    sb.append((char) unescaped);
                    i += 4;
                } else {
                    sb.append(c).append(u);
                }
            }
            i++;
        }
        return sb.toString();
    }

    private static int tryParse(String line, int startIdx) {
        try {
            return Integer.parseInt(line.substring(startIdx, startIdx + 4), 16);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
