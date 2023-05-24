package org.apache.commons.cli;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/OptionValidator.class */
class OptionValidator {
    OptionValidator() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void validateOption(String opt) throws IllegalArgumentException {
        if (opt == null) {
            return;
        }
        if (opt.length() == 1) {
            char ch = opt.charAt(0);
            if (!isValidOpt(ch)) {
                throw new IllegalArgumentException(new StringBuffer().append("illegal option value '").append(ch).append("'").toString());
            }
            return;
        }
        char[] chars = opt.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!isValidChar(chars[i])) {
                throw new IllegalArgumentException(new StringBuffer().append("opt contains illegal character value '").append(chars[i]).append("'").toString());
            }
        }
    }

    private static boolean isValidOpt(char c) {
        return isValidChar(c) || c == ' ' || c == '?' || c == '@';
    }

    private static boolean isValidChar(char c) {
        return Character.isJavaIdentifierPart(c);
    }
}
