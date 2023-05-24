package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/FileSelector.class */
public interface FileSelector extends ResourceSelector {
    boolean isSelected(File file, String str, File file2) throws BuildException;

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    default boolean isSelected(Resource r) {
        return ((Boolean) r.asOptional(FileProvider.class).map((v0) -> {
            return v0.getFile();
        }).map(f -> {
            return Boolean.valueOf(isSelected(null, null, f));
        }).orElse(false)).booleanValue();
    }
}
