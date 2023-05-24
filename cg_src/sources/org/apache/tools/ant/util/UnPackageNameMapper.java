package org.apache.tools.ant.util;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/UnPackageNameMapper.class */
public class UnPackageNameMapper extends GlobPatternMapper {
    @Override // org.apache.tools.ant.util.GlobPatternMapper
    protected String extractVariablePart(String name) {
        String var = name.substring(this.prefixLength, name.length() - this.postfixLength);
        return var.replace('.', File.separatorChar);
    }
}
