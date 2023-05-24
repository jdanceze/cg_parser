package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/LastModifiedAlgorithm.class */
public class LastModifiedAlgorithm implements Algorithm {
    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public boolean isValid() {
        return true;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public String getValue(File file) {
        long lastModified = file.lastModified();
        if (lastModified == 0) {
            return null;
        }
        return Long.toString(lastModified);
    }
}
