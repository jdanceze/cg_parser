package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.optional.native2ascii.Native2AsciiAdapter;
import org.apache.tools.ant.taskdefs.optional.native2ascii.Native2AsciiAdapterFactory;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;
import org.apache.tools.ant.util.facade.ImplementationSpecificArgument;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Native2Ascii.class */
public class Native2Ascii extends MatchingTask {
    private Mapper mapper;
    private FacadeTaskHelper facade;
    private boolean reverse = false;
    private String encoding = null;
    private File srcDir = null;
    private File destDir = null;
    private String extension = null;
    private Native2AsciiAdapter nestedAdapter = null;

    public Native2Ascii() {
        this.facade = null;
        this.facade = new FacadeTaskHelper(Native2AsciiAdapterFactory.getDefault());
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean getReverse() {
        return this.reverse;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setSrc(File srcDir) {
        this.srcDir = srcDir;
    }

    public void setDest(File destDir) {
        this.destDir = destDir;
    }

    public void setExt(String ext) {
        this.extension = ext;
    }

    public void setImplementation(String impl) {
        if ("default".equals(impl)) {
            this.facade.setImplementation(Native2AsciiAdapterFactory.getDefault());
        } else {
            this.facade.setImplementation(impl);
        }
    }

    public Mapper createMapper() throws BuildException {
        if (this.mapper != null) {
            throw new BuildException(Expand.ERROR_MULTIPLE_MAPPERS, getLocation());
        }
        this.mapper = new Mapper(getProject());
        return this.mapper;
    }

    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    public ImplementationSpecificArgument createArg() {
        ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
        this.facade.addImplementationArgument(arg);
        return arg;
    }

    public Path createImplementationClasspath() {
        return this.facade.getImplementationClasspath(getProject());
    }

    public void add(Native2AsciiAdapter adapter) {
        if (this.nestedAdapter != null) {
            throw new BuildException("Can't have more than one native2ascii adapter");
        }
        this.nestedAdapter = adapter;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        FileNameMapper m;
        if (this.srcDir == null) {
            this.srcDir = getProject().resolveFile(".");
        }
        if (this.destDir == null) {
            throw new BuildException("The dest attribute must be set.");
        }
        if (this.srcDir.equals(this.destDir) && this.extension == null && this.mapper == null) {
            throw new BuildException("The ext attribute or a mapper must be set if src and dest dirs are the same.");
        }
        if (this.mapper == null) {
            if (this.extension == null) {
                m = new IdentityMapper();
            } else {
                m = new ExtMapper();
            }
        } else {
            m = this.mapper.getImplementation();
        }
        DirectoryScanner scanner = getDirectoryScanner(this.srcDir);
        String[] files = scanner.getIncludedFiles();
        SourceFileScanner sfs = new SourceFileScanner(this);
        String[] files2 = sfs.restrict(files, this.srcDir, this.destDir, m);
        int count = files2.length;
        if (count == 0) {
            return;
        }
        String message = "Converting " + count + " file" + (count != 1 ? "s" : "") + " from ";
        log(message + this.srcDir + " to " + this.destDir);
        for (String file : files2) {
            String[] dest = m.mapFileName(file);
            if (dest != null && dest.length > 0) {
                convert(file, dest[0]);
            }
        }
    }

    private void convert(String srcName, String destName) throws BuildException {
        File srcFile = new File(this.srcDir, srcName);
        File destFile = new File(this.destDir, destName);
        if (srcFile.equals(destFile)) {
            throw new BuildException("file %s would overwrite itself", srcFile);
        }
        String parentName = destFile.getParent();
        if (parentName != null) {
            File parentFile = new File(parentName);
            if (!parentFile.exists() && !parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new BuildException("cannot create parent directory %s", parentName);
            }
        }
        log("converting " + srcName, 3);
        Native2AsciiAdapter ad = this.nestedAdapter != null ? this.nestedAdapter : Native2AsciiAdapterFactory.getAdapter(this.facade.getImplementation(), this, createImplementationClasspath());
        if (!ad.convert(this, srcFile, destFile)) {
            throw new BuildException("conversion failed");
        }
    }

    public String[] getCurrentArgs() {
        return this.facade.getArgs();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Native2Ascii$ExtMapper.class */
    private class ExtMapper implements FileNameMapper {
        private ExtMapper() {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setFrom(String s) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setTo(String s) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public String[] mapFileName(String fileName) {
            int lastDot = fileName.lastIndexOf(46);
            return lastDot >= 0 ? new String[]{fileName.substring(0, lastDot) + Native2Ascii.this.extension} : new String[]{fileName + Native2Ascii.this.extension};
        }
    }
}
