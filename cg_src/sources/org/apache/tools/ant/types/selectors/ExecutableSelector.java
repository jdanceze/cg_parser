package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.nio.file.Files;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/ExecutableSelector.class */
public class ExecutableSelector implements FileSelector {
    @Override // org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        return file != null && Files.isExecutable(file.toPath());
    }
}
