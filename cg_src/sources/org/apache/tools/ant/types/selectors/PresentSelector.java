package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/PresentSelector.class */
public class PresentSelector extends BaseSelector {
    private File targetdir = null;
    private Mapper mapperElement = null;
    private FileNameMapper map = null;
    private boolean destmustexist = true;

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder("{presentselector targetdir: ");
        if (this.targetdir == null) {
            buf.append("NOT YET SET");
        } else {
            buf.append(this.targetdir.getName());
        }
        buf.append(" present: ");
        if (this.destmustexist) {
            buf.append("both");
        } else {
            buf.append("srconly");
        }
        if (this.map != null) {
            buf.append(this.map.toString());
        } else if (this.mapperElement != null) {
            buf.append(this.mapperElement.toString());
        }
        buf.append("}");
        return buf.toString();
    }

    public void setTargetdir(File targetdir) {
        this.targetdir = targetdir;
    }

    public Mapper createMapper() throws BuildException {
        if (this.map != null || this.mapperElement != null) {
            throw new BuildException(Expand.ERROR_MULTIPLE_MAPPERS);
        }
        this.mapperElement = new Mapper(getProject());
        return this.mapperElement;
    }

    public void addConfigured(FileNameMapper fileNameMapper) {
        if (this.map != null || this.mapperElement != null) {
            throw new BuildException(Expand.ERROR_MULTIPLE_MAPPERS);
        }
        this.map = fileNameMapper;
    }

    public void setPresent(FilePresence fp) {
        if (fp.getIndex() == 0) {
            this.destmustexist = false;
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (this.targetdir == null) {
            setError("The targetdir attribute is required.");
        }
        if (this.map == null) {
            if (this.mapperElement == null) {
                this.map = new IdentityMapper();
                return;
            }
            this.map = this.mapperElement.getImplementation();
            if (this.map == null) {
                setError("Could not set <mapper> element.");
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        String[] destfiles = this.map.mapFileName(filename);
        if (destfiles == null) {
            return false;
        }
        if (destfiles.length != 1 || destfiles[0] == null) {
            throw new BuildException("Invalid destination file results for " + this.targetdir + " with filename " + filename);
        }
        String destname = destfiles[0];
        File destfile = FileUtils.getFileUtils().resolveFile(this.targetdir, destname);
        return destfile.exists() == this.destmustexist;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/PresentSelector$FilePresence.class */
    public static class FilePresence extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"srconly", "both"};
        }
    }
}
