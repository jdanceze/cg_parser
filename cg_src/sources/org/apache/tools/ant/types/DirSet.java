package org.apache.tools.ant.types;

import java.util.Iterator;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResourceIterator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/DirSet.class */
public class DirSet extends AbstractFileSet implements ResourceCollection {
    public DirSet() {
    }

    protected DirSet(DirSet dirset) {
        super(dirset);
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        if (isReference()) {
            return getRef().clone();
        }
        return super.clone();
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        return new FileResourceIterator(getProject(), getDir(getProject()), getDirectoryScanner().getIncludedDirectories());
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        return getDirectoryScanner().getIncludedDirsCount();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        return true;
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType
    public String toString() {
        return String.join(";", getDirectoryScanner().getIncludedDirectories());
    }

    @Override // org.apache.tools.ant.types.AbstractFileSet
    protected AbstractFileSet getRef(Project p) {
        return (AbstractFileSet) getCheckedRef(DirSet.class, getDataTypeName(), p);
    }

    private DirSet getRef() {
        return (DirSet) getCheckedRef(DirSet.class);
    }
}
