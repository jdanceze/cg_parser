package org.apache.tools.ant.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/StringUtils.class */
public final class StringUtils {
    private static final long KILOBYTE = 1024;
    private static final long MEGABYTE = 1048576;
    private static final long GIGABYTE = 1073741824;
    private static final long TERABYTE = 1099511627776L;
    private static final long PETABYTE = 1125899906842624L;
    @Deprecated
    public static final String LINE_SEP = System.lineSeparator();

    private StringUtils() {
    }

    public static Vector<String> lineSplit(String data) {
        return split(data, 10);
    }

    public static Vector<String> split(String data, int ch) {
        Vector<String> elems = new Vector<>();
        int i = 0;
        while (true) {
            int i2 = i;
            int pos = data.indexOf(ch, i2);
            if (pos != -1) {
                String elem = data.substring(i2, pos);
                elems.addElement(elem);
                i = pos + 1;
            } else {
                elems.addElement(data.substring(i2));
                return elems;
            }
        }
    }

    @Deprecated
    public static String replace(String data, String from, String to) {
        return data.replace(from, to);
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter((Writer) sw, true);
        t.printStackTrace(pw);
        pw.flush();
        pw.close();
        return sw.toString();
    }

    public static boolean endsWith(StringBuffer buffer, String suffix) {
        if (suffix.length() > buffer.length()) {
            return false;
        }
        int bufferIndex = buffer.length() - 1;
        for (int endIndex = suffix.length() - 1; endIndex >= 0; endIndex--) {
            if (buffer.charAt(bufferIndex) != suffix.charAt(endIndex)) {
                return false;
            }
            bufferIndex--;
        }
        return true;
    }

    public static String resolveBackSlash(String input) {
        char[] charArray;
        StringBuilder b = new StringBuilder();
        boolean backSlashSeen = false;
        for (char c : input.toCharArray()) {
            if (!backSlashSeen) {
                if (c == '\\') {
                    backSlashSeen = true;
                } else {
                    b.append(c);
                }
            } else {
                switch (c) {
                    case '\\':
                        b.append('\\');
                        break;
                    case 'f':
                        b.append('\f');
                        break;
                    case 'n':
                        b.append('\n');
                        break;
                    case 'r':
                        b.append('\r');
                        break;
                    case 's':
                        b.append(" \t\n\r\f");
                        break;
                    case 't':
                        b.append('\t');
                        break;
                    default:
                        b.append(c);
                        break;
                }
                backSlashSeen = false;
            }
        }
        return b.toString();
    }

    public static long parseHumanSizes(String humanSize) throws Exception {
        long factor = 1;
        char s = humanSize.charAt(0);
        switch (s) {
            case '+':
                humanSize = humanSize.substring(1);
                break;
            case '-':
                factor = -1;
                humanSize = humanSize.substring(1);
                break;
        }
        char c = humanSize.charAt(humanSize.length() - 1);
        if (!Character.isDigit(c)) {
            int trim = 1;
            switch (c) {
                case 'G':
                    factor *= 1073741824;
                    break;
                case 'H':
                case 'I':
                case 'J':
                case 'L':
                case 'N':
                case 'O':
                case 'Q':
                case 'R':
                case 'S':
                default:
                    trim = 0;
                    break;
                case 'K':
                    factor *= 1024;
                    break;
                case 'M':
                    factor *= 1048576;
                    break;
                case 'P':
                    factor *= 1125899906842624L;
                    break;
                case 'T':
                    factor *= 1099511627776L;
                    break;
            }
            humanSize = humanSize.substring(0, humanSize.length() - trim);
        }
        try {
            return factor * Long.parseLong(humanSize);
        } catch (NumberFormatException e) {
            throw new BuildException("Failed to parse \"" + humanSize + "\"", e);
        }
    }

    public static String removeSuffix(String string, String suffix) {
        if (string.endsWith(suffix)) {
            return string.substring(0, string.length() - suffix.length());
        }
        return string;
    }

    public static String removePrefix(String string, String prefix) {
        if (string.startsWith(prefix)) {
            return string.substring(prefix.length());
        }
        return string;
    }

    public static String join(Collection<?> collection, CharSequence separator) {
        if (collection == null) {
            return "";
        }
        return (String) collection.stream().map(String::valueOf).collect(joining(separator));
    }

    public static String join(Object[] array, CharSequence separator) {
        if (array == null) {
            return "";
        }
        return join(Arrays.asList(array), separator);
    }

    private static Collector<CharSequence, ?, String> joining(CharSequence separator) {
        return separator == null ? Collectors.joining() : Collectors.joining(separator);
    }

    public static String trimToNull(String inputString) {
        if (inputString == null) {
            return null;
        }
        String tmpString = inputString.trim();
        if (tmpString.isEmpty()) {
            return null;
        }
        return tmpString;
    }
}
