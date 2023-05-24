package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.AbstractFileSet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/MultiRootFileSet.class */
public class MultiRootFileSet extends AbstractFileSet implements ResourceCollection {
    private SetType type = SetType.file;
    private boolean cache = true;
    private List<File> baseDirs = new ArrayList();
    private Union union;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/MultiRootFileSet$SetType.class */
    public enum SetType {
        file,
        dir,
        both
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet
    public void setDir(File dir) {
        throw new BuildException(getDataTypeName() + " doesn't support the dir attribute");
    }

    public void setType(SetType type) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.type = type;
    }

    public synchronized void setCache(boolean b) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.cache = b;
    }

    public void setBaseDirs(String dirs) {
        String[] split;
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (dirs != null && !dirs.isEmpty()) {
            for (String d : dirs.split(",")) {
                this.baseDirs.add(getProject().resolveFile(d));
            }
        }
    }

    public void addConfiguredBaseDir(FileResource r) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.baseDirs.add(r.getFile());
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (!this.baseDirs.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        if (isReference()) {
            return getRef().clone();
        }
        MultiRootFileSet fs = (MultiRootFileSet) super.clone();
        fs.baseDirs = new ArrayList(this.baseDirs);
        fs.union = null;
        return fs;
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        return merge().iterator();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        return merge().size();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        return true;
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        return merge().toString();
    }

    private MultiRootFileSet getRef() {
        return (MultiRootFileSet) getCheckedRef(MultiRootFileSet.class);
    }

    private synchronized Union merge() {
        if (this.cache && this.union != null) {
            return this.union;
        }
        Union u = new Union();
        setup(u);
        if (this.cache) {
            this.union = u;
        }
        return u;
    }

    private void setup(Union u) {
        for (File d : this.baseDirs) {
            u.add(new Worker(this.type, d));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/MultiRootFileSet$Worker.class */
    public static class Worker extends AbstractFileSet implements ResourceCollection {
        private final SetType type;

        private Worker(MultiRootFileSet fs, SetType type, File dir) {
            super(fs);
            this.type = type;
            setDir(dir);
        }

        @Override // org.apache.tools.ant.types.ResourceCollection
        public boolean isFilesystemOnly() {
            return true;
        }

        @Override // java.lang.Iterable
        public Iterator<Resource> iterator() {
            String[] includedDirectories;
            DirectoryScanner ds = getDirectoryScanner();
            if (this.type == SetType.file) {
                includedDirectories = ds.getIncludedFiles();
            } else {
                includedDirectories = ds.getIncludedDirectories();
            }
            String[] names = includedDirectories;
            if (this.type == SetType.both) {
                String[] files = ds.getIncludedFiles();
                String[] merged = new String[names.length + files.length];
                System.arraycopy(names, 0, merged, 0, names.length);
                System.arraycopy(files, 0, merged, names.length, files.length);
                names = merged;
            }
            return new FileResourceIterator(getProject(), getDir(getProject()), names);
        }

        @Override // org.apache.tools.ant.types.ResourceCollection
        public int size() {
            int includedDirsCount;
            DirectoryScanner ds = getDirectoryScanner();
            if (this.type == SetType.file) {
                includedDirsCount = ds.getIncludedFilesCount();
            } else {
                includedDirsCount = ds.getIncludedDirsCount();
            }
            int count = includedDirsCount;
            if (this.type == SetType.both) {
                count += ds.getIncludedFilesCount();
            }
            return count;
        }
    }
}
