package soot.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
/* loaded from: gencallgraphv3.jar:soot/util/StringTools.class */
public class StringTools {
    public static final String lineSeparator;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StringTools.class.desiredAssertionStatus();
        lineSeparator = System.getProperty("line.separator");
    }

    public static String getEscapedStringOf(String fromString) {
        char[] charArray;
        StringBuilder whole = new StringBuilder();
        if ($assertionsDisabled || !lineSeparator.isEmpty()) {
            int cr = lineSeparator.charAt(0);
            int lf = lineSeparator.length() == 2 ? lineSeparator.charAt(1) : (char) 65535;
            for (char ch : fromString.toCharArray()) {
                if (ch != '\\' && ((ch >= ' ' && ch <= '~') || ch == cr || ch == lf)) {
                    whole.append(ch);
                } else {
                    whole.append(getUnicodeStringFromChar(ch));
                }
            }
            return whole.toString();
        }
        throw new AssertionError();
    }

    public static String getQuotedStringOf(String fromString) {
        int fromStringLen = fromString.length();
        StringBuilder toStringBuffer = new StringBuilder(fromStringLen + 20);
        toStringBuffer.append("\"");
        for (int i = 0; i < fromStringLen; i++) {
            char ch = fromString.charAt(i);
            switch (ch) {
                case '\t':
                    toStringBuffer.append("\\t");
                    break;
                case '\n':
                    toStringBuffer.append("\\n");
                    break;
                case '\f':
                    toStringBuffer.append("\\f");
                    break;
                case '\r':
                    toStringBuffer.append("\\r");
                    break;
                case '\"':
                    toStringBuffer.append("\\\"");
                    break;
                case '\'':
                    toStringBuffer.append("\\'");
                    break;
                case '\\':
                    toStringBuffer.append("\\\\");
                    break;
                default:
                    if (ch >= ' ' && ch <= '~') {
                        toStringBuffer.append(ch);
                        break;
                    } else {
                        toStringBuffer.append(getUnicodeStringFromChar(ch));
                        break;
                    }
            }
        }
        toStringBuffer.append("\"");
        return toStringBuffer.toString();
    }

    public static String getUnicodeStringFromChar(char ch) {
        String s = Integer.toHexString(ch);
        switch (s.length()) {
            case 1:
                return "\\u000" + s;
            case 2:
                return "\\u00" + s;
            case 3:
                return "\\u0" + s;
            case 4:
                return "\\u" + s;
            default:
                throw new AssertionError("invalid hex string '" + s + "' from char '" + ch + "'");
        }
    }

    public static String getUnEscapedStringOf(String str) {
        StringBuilder buf = new StringBuilder();
        CharacterIterator iter = new StringCharacterIterator(str);
        char first = iter.first();
        while (true) {
            char ch = first;
            if (ch != 65535) {
                if (ch != '\\') {
                    buf.append(ch);
                } else {
                    char ch2 = iter.next();
                    if (ch2 == '\\') {
                        buf.append(ch2);
                    } else {
                        char format = getCFormatChar(ch2);
                        if (format != 0) {
                            buf.append(format);
                        } else if (ch2 == 'u') {
                            StringBuilder mini = new StringBuilder(4);
                            for (int i = 0; i < 4; i++) {
                                mini.append(iter.next());
                            }
                            buf.append((char) Integer.parseInt(mini.toString(), 16));
                        } else {
                            throw new RuntimeException("Unexpected char: " + ch2);
                        }
                    }
                }
                first = iter.next();
            } else {
                return buf.toString();
            }
        }
    }

    public static char getCFormatChar(char c) {
        switch (c) {
            case '\"':
                return '\"';
            case '\'':
                return '\'';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            default:
                return (char) 0;
        }
    }
}
