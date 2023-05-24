package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.stream.Stream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/NoneSelector.class */
public class NoneSelector extends BaseSelectorContainer {
    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (hasSelectors()) {
            buf.append("{noneselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        return Stream.of((Object[]) getSelectors(getProject())).noneMatch(s -> {
            return s.isSelected(basedir, filename, file);
        });
    }
}
