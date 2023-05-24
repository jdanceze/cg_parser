package org.apache.tools.ant.util;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/UnicodeUtil.class */
public class UnicodeUtil {
    private UnicodeUtil() {
    }

    public static StringBuffer EscapeUnicode(char ch) {
        StringBuffer unicodeBuf = new StringBuffer("u0000");
        String s = Integer.toHexString(ch);
        for (int i = 0; i < s.length(); i++) {
            unicodeBuf.setCharAt((unicodeBuf.length() - s.length()) + i, s.charAt(i));
        }
        return unicodeBuf;
    }
}
