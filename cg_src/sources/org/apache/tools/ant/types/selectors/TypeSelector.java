package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/TypeSelector.class */
public class TypeSelector extends BaseExtendSelector {
    public static final String TYPE_KEY = "type";
    private String type = null;

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return "{typeselector type: " + this.type + "}";
    }

    public void setType(FileType fileTypes) {
        this.type = fileTypes.getValue();
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramname = parameter.getName();
                if ("type".equalsIgnoreCase(paramname)) {
                    FileType t = new FileType();
                    t.setValue(parameter.getValue());
                    setType(t);
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (this.type == null) {
            setError("The type attribute is required");
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        if (file.isDirectory()) {
            return this.type.equals("dir");
        }
        return this.type.equals("file");
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/TypeSelector$FileType.class */
    public static class FileType extends EnumeratedAttribute {
        public static final String FILE = "file";
        public static final String DIR = "dir";

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"file", "dir"};
        }
    }
}
