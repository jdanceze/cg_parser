package org.apache.tools.ant.taskdefs;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTLiaison.class */
public interface XSLTLiaison {
    public static final String FILE_PROTOCOL_PREFIX = "file://";

    void setStylesheet(File file) throws Exception;

    void addParam(String str, String str2) throws Exception;

    void transform(File file, File file2) throws Exception;
}
