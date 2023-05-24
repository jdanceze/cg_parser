package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Pack.class */
public abstract class Pack extends Task {
    private static final int BUFFER_SIZE = 8192;
    protected File zipFile;
    protected File source;
    private Resource src;

    protected abstract void pack();

    public void setZipfile(File zipFile) {
        this.zipFile = zipFile;
    }

    public void setDestfile(File zipFile) {
        setZipfile(zipFile);
    }

    public void setSrc(File src) {
        setSrcResource(new FileResource(src));
    }

    public void setSrcResource(Resource src) {
        if (src.isDirectory()) {
            throw new BuildException("the source can't be a directory");
        }
        FileProvider fp = (FileProvider) src.as(FileProvider.class);
        if (fp != null) {
            this.source = fp.getFile();
        } else if (!supportsNonFileResources()) {
            throw new BuildException("Only FileSystem resources are supported.");
        }
        this.src = src;
    }

    public void addConfigured(ResourceCollection a) {
        if (a.size() == 0) {
            throw new BuildException("No resource selected, %s needs exactly one resource.", getTaskName());
        }
        if (a.size() != 1) {
            throw new BuildException("%s cannot handle multiple resources at once. (%d resources were selected.)", getTaskName(), Integer.valueOf(a.size()));
        }
        setSrcResource(a.iterator().next());
    }

    private void validate() throws BuildException {
        if (this.zipFile == null) {
            throw new BuildException("zipfile attribute is required", getLocation());
        }
        if (this.zipFile.isDirectory()) {
            throw new BuildException("zipfile attribute must not represent a directory!", getLocation());
        }
        if (getSrcResource() == null) {
            throw new BuildException("src attribute or nested resource is required", getLocation());
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        validate();
        Resource s = getSrcResource();
        if (!s.isExists()) {
            log("Nothing to do: " + s.toString() + " doesn't exist.");
        } else if (this.zipFile.lastModified() < s.getLastModified()) {
            log("Building: " + this.zipFile.getAbsolutePath());
            pack();
        } else {
            log("Nothing to do: " + this.zipFile.getAbsolutePath() + " is up to date.");
        }
    }

    private void zipFile(InputStream in, OutputStream zOut) throws IOException {
        byte[] buffer = new byte[8192];
        int count = 0;
        do {
            zOut.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);
    }

    protected void zipFile(File file, OutputStream zOut) throws IOException {
        zipResource(new FileResource(file), zOut);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void zipResource(Resource resource, OutputStream zOut) throws IOException {
        InputStream rIn = resource.getInputStream();
        try {
            zipFile(rIn, zOut);
            if (rIn != null) {
                rIn.close();
            }
        } catch (Throwable th) {
            if (rIn != null) {
                try {
                    rIn.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public Resource getSrcResource() {
        return this.src;
    }

    protected boolean supportsNonFileResources() {
        return false;
    }
}
