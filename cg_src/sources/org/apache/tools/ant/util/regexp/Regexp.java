package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/regexp/Regexp.class */
public interface Regexp extends RegexpMatcher {
    public static final int REPLACE_FIRST = 1;
    public static final int REPLACE_ALL = 16;

    String substitute(String str, String str2, int i) throws BuildException;
}
