package org.apache.tools.ant.util.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/regexp/Jdk14RegexpRegexp.class */
public class Jdk14RegexpRegexp extends Jdk14RegexpMatcher implements Regexp {
    private static final int DECIMAL = 10;

    protected int getSubsOptions(int options) {
        int subsOptions = 1;
        if (RegexpUtil.hasFlag(options, 16)) {
            subsOptions = 16;
        }
        return subsOptions;
    }

    @Override // org.apache.tools.ant.util.regexp.Regexp
    public String substitute(String input, String argument, int options) throws BuildException {
        StringBuilder subst = new StringBuilder();
        int i = 0;
        while (i < argument.length()) {
            char c = argument.charAt(i);
            if (c == '$') {
                subst.append('\\');
                subst.append('$');
            } else if (c == '\\') {
                i++;
                if (i < argument.length()) {
                    char c2 = argument.charAt(i);
                    int value = Character.digit(c2, 10);
                    if (value > -1) {
                        subst.append('$').append(value);
                    } else {
                        subst.append(c2);
                    }
                } else {
                    subst.append('\\');
                }
            } else {
                subst.append(c);
            }
            i++;
        }
        int sOptions = getSubsOptions(options);
        Pattern p = getCompiledPattern(options);
        StringBuffer sb = new StringBuffer();
        Matcher m = p.matcher(input);
        if (RegexpUtil.hasFlag(sOptions, 16)) {
            sb.append(m.replaceAll(subst.toString()));
        } else if (m.find()) {
            m.appendReplacement(sb, subst.toString());
            m.appendTail(sb);
        } else {
            sb.append(input);
        }
        return sb.toString();
    }
}
