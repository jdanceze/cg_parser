package org.apache.tools.ant.types;

import java.util.Iterator;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResourceIterator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/FileSet.class */
public class FileSet extends AbstractFileSet implements ResourceCollection {
    public FileSet() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FileSet(FileSet fileset) {
        super(fileset);
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        if (isReference()) {
            return getRef().clone();
        }
        return super.clone();
    }

    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        return new FileResourceIterator(getProject(), getDir(getProject()), getDirectoryScanner().getIncludedFiles());
    }

    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        return getDirectoryScanner().getIncludedFilesCount();
    }

    public boolean isFilesystemOnly() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.AbstractFileSet
    public AbstractFileSet getRef(Project p) {
        return (AbstractFileSet) getCheckedRef(FileSet.class, getDataTypeName(), p);
    }

    private FileSet getRef() {
        return (FileSet) getCheckedRef(FileSet.class);
    }
}
