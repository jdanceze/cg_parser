package org.apache.tools.ant.util;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/FlatFileNameMapper.class */
public class FlatFileNameMapper implements FileNameMapper {
    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setFrom(String from) {
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setTo(String to) {
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        if (sourceFileName == null) {
            return null;
        }
        return new String[]{new File(sourceFileName).getName()};
    }
}
