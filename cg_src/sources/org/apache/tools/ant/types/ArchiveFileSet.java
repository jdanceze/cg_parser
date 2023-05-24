package org.apache.tools.ant.types;

import java.io.File;
import java.util.Iterator;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/ArchiveFileSet.class */
public abstract class ArchiveFileSet extends FileSet {
    private static final int BASE_OCTAL = 8;
    public static final int DEFAULT_DIR_MODE = 16877;
    public static final int DEFAULT_FILE_MODE = 33188;
    private Resource src;
    private String prefix;
    private String fullpath;
    private boolean hasDir;
    private int fileMode;
    private int dirMode;
    private boolean fileModeHasBeenSet;
    private boolean dirModeHasBeenSet;
    private static final String ERROR_DIR_AND_SRC_ATTRIBUTES = "Cannot set both dir and src attributes";
    private static final String ERROR_PATH_AND_PREFIX = "Cannot set both fullpath and prefix attributes";
    private boolean errorOnMissingArchive;
    private String encoding;

    protected abstract ArchiveScanner newArchiveScanner();

    public ArchiveFileSet() {
        this.src = null;
        this.prefix = "";
        this.fullpath = "";
        this.hasDir = false;
        this.fileMode = 33188;
        this.dirMode = 16877;
        this.fileModeHasBeenSet = false;
        this.dirModeHasBeenSet = false;
        this.errorOnMissingArchive = true;
        this.encoding = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArchiveFileSet(FileSet fileset) {
        super(fileset);
        this.src = null;
        this.prefix = "";
        this.fullpath = "";
        this.hasDir = false;
        this.fileMode = 33188;
        this.dirMode = 16877;
        this.fileModeHasBeenSet = false;
        this.dirModeHasBeenSet = false;
        this.errorOnMissingArchive = true;
        this.encoding = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArchiveFileSet(ArchiveFileSet fileset) {
        super(fileset);
        this.src = null;
        this.prefix = "";
        this.fullpath = "";
        this.hasDir = false;
        this.fileMode = 33188;
        this.dirMode = 16877;
        this.fileModeHasBeenSet = false;
        this.dirModeHasBeenSet = false;
        this.errorOnMissingArchive = true;
        this.encoding = null;
        this.src = fileset.src;
        this.prefix = fileset.prefix;
        this.fullpath = fileset.fullpath;
        this.hasDir = fileset.hasDir;
        this.fileMode = fileset.fileMode;
        this.dirMode = fileset.dirMode;
        this.fileModeHasBeenSet = fileset.fileModeHasBeenSet;
        this.dirModeHasBeenSet = fileset.dirModeHasBeenSet;
        this.errorOnMissingArchive = fileset.errorOnMissingArchive;
        this.encoding = fileset.encoding;
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet
    public void setDir(File dir) throws BuildException {
        checkAttributesAllowed();
        if (this.src != null) {
            throw new BuildException(ERROR_DIR_AND_SRC_ATTRIBUTES);
        }
        super.setDir(dir);
        this.hasDir = true;
    }

    public void addConfigured(ResourceCollection a) {
        checkChildrenAllowed();
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported as archives");
        }
        setSrcResource(a.iterator().next());
    }

    public void setSrc(File srcFile) {
        setSrcResource(new FileResource(srcFile));
    }

    public void setSrcResource(Resource src) {
        checkArchiveAttributesAllowed();
        if (this.hasDir) {
            throw new BuildException(ERROR_DIR_AND_SRC_ATTRIBUTES);
        }
        this.src = src;
        setChecked(false);
    }

    public File getSrc(Project p) {
        if (isReference()) {
            return ((ArchiveFileSet) getRef(p)).getSrc(p);
        }
        return getSrc();
    }

    public void setErrorOnMissingArchive(boolean errorOnMissingArchive) {
        checkAttributesAllowed();
        this.errorOnMissingArchive = errorOnMissingArchive;
    }

    public File getSrc() {
        if (isReference()) {
            return ((ArchiveFileSet) getCheckedRef(ArchiveFileSet.class)).getSrc();
        }
        dieOnCircularReference();
        if (this.src == null) {
            return null;
        }
        return (File) this.src.asOptional(FileProvider.class).map((v0) -> {
            return v0.getFile();
        }).orElse(null);
    }

    protected AbstractFileSet getRef() {
        return (AbstractFileSet) getCheckedRef(AbstractFileSet.class);
    }

    public void setPrefix(String prefix) {
        checkArchiveAttributesAllowed();
        if (!prefix.isEmpty() && !this.fullpath.isEmpty()) {
            throw new BuildException(ERROR_PATH_AND_PREFIX);
        }
        this.prefix = prefix;
    }

    public String getPrefix(Project p) {
        if (isReference()) {
            return ((ArchiveFileSet) getRef(p)).getPrefix(p);
        }
        dieOnCircularReference(p);
        return this.prefix;
    }

    public void setFullpath(String fullpath) {
        checkArchiveAttributesAllowed();
        if (!this.prefix.isEmpty() && !fullpath.isEmpty()) {
            throw new BuildException(ERROR_PATH_AND_PREFIX);
        }
        this.fullpath = fullpath;
    }

    public String getFullpath(Project p) {
        if (isReference()) {
            return ((ArchiveFileSet) getRef(p)).getFullpath(p);
        }
        dieOnCircularReference(p);
        return this.fullpath;
    }

    public void setEncoding(String enc) {
        checkAttributesAllowed();
        this.encoding = enc;
    }

    public String getEncoding() {
        if (isReference()) {
            AbstractFileSet ref = getRef();
            if (ref instanceof ArchiveFileSet) {
                return ((ArchiveFileSet) ref).getEncoding();
            }
            return null;
        }
        return this.encoding;
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet
    public DirectoryScanner getDirectoryScanner(Project p) {
        if (isReference()) {
            return getRef(p).getDirectoryScanner(p);
        }
        dieOnCircularReference();
        if (this.src == null) {
            return super.getDirectoryScanner(p);
        }
        if (!this.src.isExists() && this.errorOnMissingArchive) {
            throw new BuildException("The archive " + this.src.getName() + " doesn't exist");
        }
        if (this.src.isDirectory()) {
            throw new BuildException("The archive " + this.src.getName() + " can't be a directory");
        }
        ArchiveScanner as = newArchiveScanner();
        as.setErrorOnMissingArchive(this.errorOnMissingArchive);
        as.setSrc(this.src);
        super.setDir(p.getBaseDir());
        setupDirectoryScanner(as, p);
        as.init();
        return as;
    }

    @Override // org.apache.tools.ant.types.FileSet, java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return ((ResourceCollection) getRef()).iterator();
        }
        if (this.src == null) {
            return super.iterator();
        }
        return ((ArchiveScanner) getDirectoryScanner()).getResourceFiles(getProject());
    }

    @Override // org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return ((ResourceCollection) getRef()).size();
        }
        if (this.src == null) {
            return super.size();
        }
        return getDirectoryScanner().getIncludedFilesCount();
    }

    @Override // org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        if (isReference()) {
            return ((ArchiveFileSet) getRef()).isFilesystemOnly();
        }
        dieOnCircularReference();
        return this.src == null;
    }

    public void setFileMode(String octalString) {
        checkArchiveAttributesAllowed();
        integerSetFileMode(Integer.parseInt(octalString, 8));
    }

    public void integerSetFileMode(int mode) {
        this.fileModeHasBeenSet = true;
        this.fileMode = 32768 | mode;
    }

    public int getFileMode(Project p) {
        if (isReference()) {
            return ((ArchiveFileSet) getRef(p)).getFileMode(p);
        }
        dieOnCircularReference();
        return this.fileMode;
    }

    public boolean hasFileModeBeenSet() {
        if (isReference()) {
            return ((ArchiveFileSet) getRef()).hasFileModeBeenSet();
        }
        dieOnCircularReference();
        return this.fileModeHasBeenSet;
    }

    public void setDirMode(String octalString) {
        checkArchiveAttributesAllowed();
        integerSetDirMode(Integer.parseInt(octalString, 8));
    }

    public void integerSetDirMode(int mode) {
        this.dirModeHasBeenSet = true;
        this.dirMode = 16384 | mode;
    }

    public int getDirMode(Project p) {
        if (isReference()) {
            return ((ArchiveFileSet) getRef(p)).getDirMode(p);
        }
        dieOnCircularReference();
        return this.dirMode;
    }

    public boolean hasDirModeBeenSet() {
        if (isReference()) {
            return ((ArchiveFileSet) getRef()).hasDirModeBeenSet();
        }
        dieOnCircularReference();
        return this.dirModeHasBeenSet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void configureFileSet(ArchiveFileSet zfs) {
        zfs.setPrefix(this.prefix);
        zfs.setFullpath(this.fullpath);
        zfs.fileModeHasBeenSet = this.fileModeHasBeenSet;
        zfs.fileMode = this.fileMode;
        zfs.dirModeHasBeenSet = this.dirModeHasBeenSet;
        zfs.dirMode = this.dirMode;
    }

    @Override // org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        if (isReference()) {
            return ((ArchiveFileSet) getCheckedRef(ArchiveFileSet.class)).clone();
        }
        return super.clone();
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType
    public String toString() {
        if (this.hasDir && getProject() != null) {
            return super.toString();
        }
        if (this.src == null) {
            return null;
        }
        return this.src.getName();
    }

    @Deprecated
    public String getPrefix() {
        return this.prefix;
    }

    @Deprecated
    public String getFullpath() {
        return this.fullpath;
    }

    @Deprecated
    public int getFileMode() {
        return this.fileMode;
    }

    @Deprecated
    public int getDirMode() {
        return this.dirMode;
    }

    private void checkArchiveAttributesAllowed() {
        if (getProject() == null || (isReference() && (getRefid().getReferencedObject(getProject()) instanceof ArchiveFileSet))) {
            checkAttributesAllowed();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        super.dieOnCircularReference(stk, p);
        if (!isReference()) {
            if (this.src != null) {
                pushAndInvokeCircularReferenceCheck(this.src, stk, p);
            }
            setChecked(true);
        }
    }
}
