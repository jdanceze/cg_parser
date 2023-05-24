package org.apache.commons.cli;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/Util.class */
class Util {
    Util() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String stripLeadingHyphens(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith(HelpFormatter.DEFAULT_LONG_OPT_PREFIX)) {
            return str.substring(2, str.length());
        }
        if (str.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
            return str.substring(1, str.length());
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String stripLeadingAndTrailingQuotes(String str) {
        if (str.startsWith("\"")) {
            str = str.substring(1, str.length());
        }
        if (str.endsWith("\"")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
