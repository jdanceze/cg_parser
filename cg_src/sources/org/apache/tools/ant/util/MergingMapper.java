package org.apache.tools.ant.util;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/MergingMapper.class */
public class MergingMapper implements FileNameMapper {
    protected String[] mergedFile = null;

    public MergingMapper() {
    }

    public MergingMapper(String to) {
        setTo(to);
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setFrom(String from) {
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setTo(String to) {
        this.mergedFile = new String[]{to};
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        return this.mergedFile;
    }
}
