package org.apache.tools.ant.types.selectors;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/WritableSelector.class */
public class WritableSelector implements FileSelector {
    @Override // org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        return file != null && file.canWrite();
    }
}
