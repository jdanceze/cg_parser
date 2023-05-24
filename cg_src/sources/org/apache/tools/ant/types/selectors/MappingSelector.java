package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/MappingSelector.class */
public abstract class MappingSelector extends BaseSelector {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    protected File targetdir = null;
    protected Mapper mapperElement = null;
    protected FileNameMapper map = null;
    protected int granularity = (int) FILE_UTILS.getFileTimestampGranularity();

    protected abstract boolean selectionTest(File file, File file2);

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
            throw new BuildException("Invalid destination file results for " + this.targetdir.getName() + " with filename " + filename);
        }
        String destname = destfiles[0];
        File destfile = FILE_UTILS.resolveFile(this.targetdir, destname);
        return selectionTest(file, destfile);
    }

    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }
}
