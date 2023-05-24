package org.apache.tools.ant.util;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/FileNameMapper.class */
public interface FileNameMapper {
    void setFrom(String str);

    void setTo(String str);

    String[] mapFileName(String str);
}
