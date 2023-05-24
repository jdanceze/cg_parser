package org.apache.tools.ant.util.regexp;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/regexp/RegexpMatcher.class */
public interface RegexpMatcher {
    public static final int MATCH_DEFAULT = 0;
    public static final int MATCH_CASE_INSENSITIVE = 256;
    public static final int MATCH_MULTILINE = 4096;
    public static final int MATCH_SINGLELINE = 65536;

    void setPattern(String str) throws BuildException;

    String getPattern() throws BuildException;

    boolean matches(String str) throws BuildException;

    Vector<String> getGroups(String str) throws BuildException;

    boolean matches(String str, int i) throws BuildException;

    Vector<String> getGroups(String str, int i) throws BuildException;
}
