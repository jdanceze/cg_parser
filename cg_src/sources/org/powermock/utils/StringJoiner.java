package org.powermock.utils;

import java.util.Arrays;
import java.util.List;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/utils/StringJoiner.class */
public class StringJoiner {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String EMPTY_STRING = "";

    public static String join(Object... linesToBreak) {
        StringBuilder out = new StringBuilder(LINE_SEPARATOR);
        return join(out, Arrays.asList(linesToBreak), LINE_SEPARATOR);
    }

    public static <T> String join(List<T> list) {
        StringBuilder out = new StringBuilder(LINE_SEPARATOR);
        return join(out, list, LINE_SEPARATOR);
    }

    public static String join(String separator, Object... linesToBreak) {
        StringBuilder out = new StringBuilder();
        return join(out, linesToBreak, separator);
    }

    private static String join(StringBuilder out, Iterable linesToBreak, String separator) {
        for (Object line : linesToBreak) {
            out.append(line.toString()).append(separator);
        }
        int lastBreak = out.lastIndexOf(separator);
        return out.replace(lastBreak, lastBreak + 1, "").toString();
    }
}
