package org.apache.tools.ant.util.regexp;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/regexp/RegexpUtil.class */
public class RegexpUtil {
    public static boolean hasFlag(int options, int flag) {
        return (options & flag) > 0;
    }

    public static int removeFlag(int options, int flag) {
        return options & ((-1) - flag);
    }

    public static int asOptions(String flags) {
        int options = 0;
        if (flags != null) {
            options = asOptions(!flags.contains("i"), flags.contains("m"), flags.contains("s"));
            if (flags.contains("g")) {
                options |= 16;
            }
        }
        return options;
    }

    public static int asOptions(boolean caseSensitive) {
        return asOptions(caseSensitive, false, false);
    }

    public static int asOptions(boolean caseSensitive, boolean multiLine, boolean singleLine) {
        int options = 0;
        if (!caseSensitive) {
            options = 0 | 256;
        }
        if (multiLine) {
            options |= 4096;
        }
        if (singleLine) {
            options |= 65536;
        }
        return options;
    }
}
