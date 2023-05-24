package org.apache.tools.ant.types.resources;

import java.util.Iterator;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/BCFileSet.class */
public class BCFileSet extends FileSet {
    public BCFileSet() {
    }

    public BCFileSet(FileSet fs) {
        super(fs);
    }

    @Override // org.apache.tools.ant.types.FileSet, java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        FileResourceIterator result = new FileResourceIterator(getProject(), getDir());
        result.addFiles(getDirectoryScanner().getIncludedFiles());
        result.addFiles(getDirectoryScanner().getIncludedDirectories());
        return result;
    }

    @Override // org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        return getDirectoryScanner().getIncludedFilesCount() + getDirectoryScanner().getIncludedDirsCount();
    }

    private FileSet getRef() {
        return (FileSet) getCheckedRef(FileSet.class);
    }
}
