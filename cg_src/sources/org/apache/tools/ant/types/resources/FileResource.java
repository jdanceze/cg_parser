package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.OpenOption;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/FileResource.class */
public class FileResource extends Resource implements Touchable, FileProvider, ResourceFactory, Appendable {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final int NULL_FILE = Resource.getMagicNumber("null file".getBytes());
    private File file;
    private File baseDir;

    public FileResource() {
    }

    public FileResource(File b, String name) {
        this.baseDir = b;
        this.file = FILE_UTILS.resolveFile(b, name);
    }

    public FileResource(File f) {
        setFile(f);
    }

    public FileResource(Project p, File f) {
        this(f);
        setProject(p);
    }

    public FileResource(Project p, String s) {
        this(p, p.resolveFile(s));
    }

    public void setFile(File f) {
        checkAttributesAllowed();
        this.file = f;
        if (f != null) {
            if (getBaseDir() == null || !FILE_UTILS.isLeadingPath(getBaseDir(), f)) {
                setBaseDir(f.getParentFile());
            }
        }
    }

    @Override // org.apache.tools.ant.types.resources.FileProvider
    public File getFile() {
        if (isReference()) {
            return getRef().getFile();
        }
        dieOnCircularReference();
        synchronized (this) {
            if (this.file == null) {
                File d = getBaseDir();
                String n = super.getName();
                if (n != null) {
                    setFile(FILE_UTILS.resolveFile(d, n));
                }
            }
        }
        return this.file;
    }

    public void setBaseDir(File b) {
        checkAttributesAllowed();
        this.baseDir = b;
    }

    public File getBaseDir() {
        if (isReference()) {
            return getRef().getBaseDir();
        }
        dieOnCircularReference();
        return this.baseDir;
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (this.file != null || this.baseDir != null) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.Resource
    public String getName() {
        if (isReference()) {
            return getRef().getName();
        }
        File b = getBaseDir();
        return b == null ? getNotNullFile().getName() : FILE_UTILS.removeLeadingPath(b, getNotNullFile());
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isExists() {
        return isReference() ? getRef().isExists() : getNotNullFile().exists();
    }

    @Override // org.apache.tools.ant.types.Resource
    public long getLastModified() {
        if (isReference()) {
            return getRef().getLastModified();
        }
        return getNotNullFile().lastModified();
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean isDirectory() {
        return isReference() ? getRef().isDirectory() : getNotNullFile().isDirectory();
    }

    @Override // org.apache.tools.ant.types.Resource
    public long getSize() {
        return isReference() ? getRef().getSize() : getNotNullFile().length();
    }

    @Override // org.apache.tools.ant.types.Resource
    public InputStream getInputStream() throws IOException {
        return isReference() ? getRef().getInputStream() : java.nio.file.Files.newInputStream(getNotNullFile().toPath(), new OpenOption[0]);
    }

    @Override // org.apache.tools.ant.types.Resource
    public OutputStream getOutputStream() throws IOException {
        if (isReference()) {
            return getRef().getOutputStream();
        }
        return getOutputStream(false);
    }

    @Override // org.apache.tools.ant.types.resources.Appendable
    public OutputStream getAppendOutputStream() throws IOException {
        if (isReference()) {
            return getRef().getAppendOutputStream();
        }
        return getOutputStream(true);
    }

    private OutputStream getOutputStream(boolean append) throws IOException {
        File f = getNotNullFile();
        if (f.exists()) {
            if (java.nio.file.Files.isSymbolicLink(f.toPath()) && f.isFile() && !append) {
                f.delete();
            }
        } else {
            File p = f.getParentFile();
            if (p != null && !p.exists()) {
                p.mkdirs();
            }
        }
        return FileUtils.newOutputStream(f.toPath(), append);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.tools.ant.types.Resource, java.lang.Comparable
    public int compareTo(Resource another) {
        if (isReference()) {
            return getRef().compareTo(another);
        }
        if (equals(another)) {
            return 0;
        }
        FileProvider otherFP = (FileProvider) another.as(FileProvider.class);
        if (otherFP != null) {
            File f = getFile();
            if (f == null) {
                return -1;
            }
            File of = otherFP.getFile();
            if (of == null) {
                return 1;
            }
            int compareFiles = f.compareTo(of);
            return compareFiles != 0 ? compareFiles : getName().compareTo(another.getName());
        }
        return super.compareTo(another);
    }

    @Override // org.apache.tools.ant.types.Resource
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (isReference()) {
            return getRef().equals(another);
        }
        if (another == null || !another.getClass().equals(getClass())) {
            return false;
        }
        FileResource otherfr = (FileResource) another;
        return getFile() == null ? otherfr.getFile() == null : getFile().equals(otherfr.getFile()) && getName().equals(otherfr.getName());
    }

    @Override // org.apache.tools.ant.types.Resource
    public int hashCode() {
        if (isReference()) {
            return getRef().hashCode();
        }
        return MAGIC * (getFile() == null ? NULL_FILE : getFile().hashCode());
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        if (this.file == null) {
            return "(unbound file resource)";
        }
        String absolutePath = this.file.getAbsolutePath();
        return FILE_UTILS.normalize(absolutePath).getAbsolutePath();
    }

    @Override // org.apache.tools.ant.types.Resource, org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        dieOnCircularReference();
        return true;
    }

    @Override // org.apache.tools.ant.types.resources.Touchable
    public void touch(long modTime) {
        if (isReference()) {
            getRef().touch(modTime);
        } else if (!getNotNullFile().setLastModified(modTime)) {
            log("Failed to change file modification time", 1);
        }
    }

    protected File getNotNullFile() {
        if (getFile() == null) {
            throw new BuildException("file attribute is null!");
        }
        dieOnCircularReference();
        return getFile();
    }

    @Override // org.apache.tools.ant.types.ResourceFactory
    public Resource getResource(String path) {
        File newfile = FILE_UTILS.resolveFile(getFile(), path);
        FileResource fileResource = new FileResource(newfile);
        if (FILE_UTILS.isLeadingPath(getBaseDir(), newfile)) {
            fileResource.setBaseDir(getBaseDir());
        }
        return fileResource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.Resource
    public FileResource getRef() {
        return (FileResource) getCheckedRef(FileResource.class);
    }
}
