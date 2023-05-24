package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/BaseExtendSelector.class */
public abstract class BaseExtendSelector extends BaseSelector implements ExtendFileSelector {
    protected Parameter[] parameters = null;

    @Override // org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public abstract boolean isSelected(File file, String str, File file2) throws BuildException;

    @Override // org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        this.parameters = parameters;
    }

    protected Parameter[] getParameters() {
        return this.parameters;
    }
}
