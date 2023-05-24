package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Unpack.class */
public abstract class Unpack extends Task {
    protected File source;
    protected File dest;
    protected Resource srcResource;

    protected abstract String getDefaultExtension();

    protected abstract void extract();

    @Deprecated
    public void setSrc(String src) {
        log("DEPRECATED - The setSrc(String) method has been deprecated. Use setSrc(File) instead.");
        setSrc(getProject().resolveFile(src));
    }

    @Deprecated
    public void setDest(String dest) {
        log("DEPRECATED - The setDest(String) method has been deprecated. Use setDest(File) instead.");
        setDest(getProject().resolveFile(dest));
    }

    public void setSrc(File src) {
        setSrcResource(new FileResource(src));
    }

    public void setSrcResource(Resource src) {
        if (!src.isExists()) {
            throw new BuildException("the archive %s doesn't exist", src.getName());
        }
        if (src.isDirectory()) {
            throw new BuildException("the archive %s can't be a directory", src.getName());
        }
        FileProvider fp = (FileProvider) src.as(FileProvider.class);
        if (fp != null) {
            this.source = fp.getFile();
        } else if (!supportsNonFileResources()) {
            throw new BuildException("The source %s is not a FileSystem Only FileSystem resources are supported.", src.getName());
        }
        this.srcResource = src;
    }

    public void addConfigured(ResourceCollection a) {
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported as archives");
        }
        setSrcResource(a.iterator().next());
    }

    public void setDest(File dest) {
        this.dest = dest;
    }

    private void validate() throws BuildException {
        if (this.srcResource == null) {
            throw new BuildException("No Src specified", getLocation());
        }
        if (this.dest == null) {
            if (this.source == null) {
                throw new BuildException("dest is required when using a non-filesystem source", getLocation());
            }
            this.dest = new File(this.source.getParent());
        }
        if (this.dest.isDirectory()) {
            String defaultExtension = getDefaultExtension();
            createDestFile(defaultExtension);
        }
    }

    private void createDestFile(String defaultExtension) {
        String sourceName = this.source == null ? getLastNamePart(this.srcResource) : this.source.getName();
        int len = sourceName.length();
        if (defaultExtension != null && len > defaultExtension.length() && defaultExtension.equalsIgnoreCase(sourceName.substring(len - defaultExtension.length()))) {
            this.dest = new File(this.dest, sourceName.substring(0, len - defaultExtension.length()));
        } else {
            this.dest = new File(this.dest, sourceName);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        File savedDest = this.dest;
        try {
            validate();
            extract();
        } finally {
            this.dest = savedDest;
        }
    }

    protected boolean supportsNonFileResources() {
        return false;
    }

    private String getLastNamePart(Resource r) {
        String n = r.getName();
        int idx = n.lastIndexOf(47);
        return idx < 0 ? n : n.substring(idx + 1);
    }
}
