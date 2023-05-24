package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.taskdefs.condition.IsSigned;
import org.apache.tools.ant.types.DataType;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SignedSelector.class */
public class SignedSelector extends DataType implements FileSelector {
    private IsSigned isSigned = new IsSigned();

    public void setName(String name) {
        this.isSigned.setName(name);
    }

    @Override // org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        if (file.isDirectory()) {
            return false;
        }
        this.isSigned.setProject(getProject());
        this.isSigned.setFile(file);
        return this.isSigned.eval();
    }
}
