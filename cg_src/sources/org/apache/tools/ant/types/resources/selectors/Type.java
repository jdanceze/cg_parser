package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Type.class */
public class Type implements ResourceSelector {
    private static final String FILE_ATTR = "file";
    private static final String DIR_ATTR = "dir";
    private FileDir type = null;
    public static final Type FILE = new Type(new FileDir("file"));
    public static final Type DIR = new Type(new FileDir("dir"));
    private static final String ANY_ATTR = "any";
    public static final Type ANY = new Type(new FileDir(ANY_ATTR));

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Type$FileDir.class */
    public static class FileDir extends EnumeratedAttribute {
        private static final String[] VALUES = {"file", "dir", Type.ANY_ATTR};

        public FileDir() {
        }

        public FileDir(String value) {
            setValue(value);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return VALUES;
        }
    }

    public Type() {
    }

    public Type(FileDir fd) {
        setType(fd);
    }

    public void setType(FileDir fd) {
        this.type = fd;
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        if (this.type == null) {
            throw new BuildException("The type attribute is required.");
        }
        int i = this.type.getIndex();
        return i == 2 || (!r.isDirectory() ? i != 0 : i != 1);
    }
}
