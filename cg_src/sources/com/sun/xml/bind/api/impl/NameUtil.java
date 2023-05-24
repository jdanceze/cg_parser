package com.sun.xml.bind.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.tools.tar.TarConstants;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/impl/NameUtil.class */
class NameUtil {
    protected static final int UPPER_LETTER = 0;
    protected static final int LOWER_LETTER = 1;
    protected static final int OTHER_LETTER = 2;
    protected static final int DIGIT = 3;
    protected static final int OTHER = 4;
    private static final byte[] actionTable = new byte[25];
    private static final byte ACTION_CHECK_PUNCT = 0;
    private static final byte ACTION_CHECK_C2 = 1;
    private static final byte ACTION_BREAK = 2;
    private static final byte ACTION_NOBREAK = 3;

    protected boolean isPunct(char c) {
        return c == '-' || c == '.' || c == ':' || c == '_' || c == 183 || c == 903 || c == 1757 || c == 1758;
    }

    protected static boolean isDigit(char c) {
        return (c >= '0' && c <= '9') || Character.isDigit(c);
    }

    protected static boolean isUpper(char c) {
        return (c >= 'A' && c <= 'Z') || Character.isUpperCase(c);
    }

    protected static boolean isLower(char c) {
        return (c >= 'a' && c <= 'z') || Character.isLowerCase(c);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c);
    }

    private String toLowerCase(String s) {
        return s.toLowerCase(Locale.ENGLISH);
    }

    private String toUpperCase(char c) {
        return String.valueOf(c).toUpperCase(Locale.ENGLISH);
    }

    private String toUpperCase(String s) {
        return s.toUpperCase(Locale.ENGLISH);
    }

    public String capitalize(String s) {
        if (!isLower(s.charAt(0))) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length());
        sb.append(toUpperCase(s.charAt(0)));
        sb.append(toLowerCase(s.substring(1)));
        return sb.toString();
    }

    private int nextBreak(String s, int start) {
        int n = s.length();
        int t1 = classify(s.charAt(start));
        for (int i = start + 1; i < n; i++) {
            int t0 = t1;
            char c1 = s.charAt(i);
            t1 = classify(c1);
            switch (actionTable[(t0 * 5) + t1]) {
                case 0:
                    if (!isPunct(c1)) {
                        break;
                    } else {
                        return i;
                    }
                case 1:
                    if (i < n - 1) {
                        char c2 = s.charAt(i + 1);
                        if (!isLower(c2)) {
                            break;
                        } else {
                            return i;
                        }
                    } else {
                        continue;
                    }
                case 2:
                    return i;
            }
        }
        return -1;
    }

    static {
        for (int t0 = 0; t0 < 5; t0++) {
            for (int t1 = 0; t1 < 5; t1++) {
                actionTable[(t0 * 5) + t1] = decideAction(t0, t1);
            }
        }
    }

    private static byte decideAction(int t0, int t1) {
        if (t0 == 4 && t1 == 4) {
            return (byte) 0;
        }
        if (xor(t0 == 3, t1 == 3)) {
            if (t0 != 1 || t1 == 1) {
                if (xor(t0 <= 2, t1 <= 2)) {
                    if (xor(t0 == 2, t1 == 2)) {
                        return (t0 == 0 && t1 == 0) ? (byte) 1 : (byte) 3;
                    }
                    return (byte) 2;
                }
                return (byte) 2;
            }
            return (byte) 2;
        }
        return (byte) 2;
    }

    private static boolean xor(boolean x, boolean y) {
        return (x && y) || !(x || y);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int classify(char c0) {
        switch (Character.getType(c0)) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
            case 4:
            case 5:
                return 2;
            case 6:
            case 7:
            case 8:
            default:
                return 4;
            case 9:
                return 3;
        }
    }

    public List<String> toWordList(String s) {
        ArrayList<String> ss = new ArrayList<>();
        int n = s.length();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= n) {
                break;
            }
            while (i2 < n && isPunct(s.charAt(i2))) {
                i2++;
            }
            if (i2 >= n) {
                break;
            }
            int b = nextBreak(s, i2);
            String w = b == -1 ? s.substring(i2) : s.substring(i2, b);
            ss.add(escape(capitalize(w)));
            if (b == -1) {
                break;
            }
            i = b;
        }
        return ss;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String toMixedCaseName(List<String> ss, boolean startUpper) {
        StringBuilder sb = new StringBuilder();
        if (!ss.isEmpty()) {
            sb.append(startUpper ? ss.get(0) : toLowerCase(ss.get(0)));
            for (int i = 1; i < ss.size(); i++) {
                sb.append(ss.get(i));
            }
        }
        return sb.toString();
    }

    protected String toMixedCaseVariableName(String[] ss, boolean startUpper, boolean cdrUpper) {
        if (cdrUpper) {
            for (int i = 1; i < ss.length; i++) {
                ss[i] = capitalize(ss[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (ss.length > 0) {
            sb.append(startUpper ? ss[0] : toLowerCase(ss[0]));
            for (int i2 = 1; i2 < ss.length; i2++) {
                sb.append(ss[i2]);
            }
        }
        return sb.toString();
    }

    public String toConstantName(String s) {
        return toConstantName(toWordList(s));
    }

    public String toConstantName(List<String> ss) {
        StringBuilder sb = new StringBuilder();
        if (!ss.isEmpty()) {
            sb.append(toUpperCase(ss.get(0)));
            for (int i = 1; i < ss.size(); i++) {
                sb.append('_');
                sb.append(toUpperCase(ss.get(i)));
            }
        }
        return sb.toString();
    }

    public static void escape(StringBuilder sb, String s, int start) {
        int n = s.length();
        for (int i = start; i < n; i++) {
            char c = s.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                sb.append(c);
            } else {
                sb.append('_');
                if (c <= 15) {
                    sb.append("000");
                } else if (c <= 255) {
                    sb.append(TarConstants.VERSION_POSIX);
                } else if (c <= 4095) {
                    sb.append('0');
                }
                sb.append(Integer.toString(c, 16));
            }
        }
    }

    private static String escape(String s) {
        int n = s.length();
        for (int i = 0; i < n; i++) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                StringBuilder sb = new StringBuilder(s.substring(0, i));
                escape(sb, s, i);
                return sb.toString();
            }
        }
        return s;
    }
}
