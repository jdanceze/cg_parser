package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/ZipFileSet.class */
public class ZipFileSet extends ArchiveFileSet {
    public ZipFileSet() {
    }

    protected ZipFileSet(FileSet fileset) {
        super(fileset);
    }

    protected ZipFileSet(ZipFileSet fileset) {
        super((ArchiveFileSet) fileset);
    }

    @Override // org.apache.tools.ant.types.ArchiveFileSet
    protected ArchiveScanner newArchiveScanner() {
        ZipScanner zs = new ZipScanner();
        zs.setEncoding(getEncoding());
        return zs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.AbstractFileSet
    public AbstractFileSet getRef(Project p) {
        dieOnCircularReference(p);
        Object o = getRefid().getReferencedObject(p);
        if (o instanceof ZipFileSet) {
            return (AbstractFileSet) o;
        }
        if (o instanceof FileSet) {
            ZipFileSet zfs = new ZipFileSet((FileSet) o);
            configureFileSet(zfs);
            return zfs;
        }
        String msg = getRefid().getRefId() + " doesn't denote a zipfileset or a fileset";
        throw new BuildException(msg);
    }

    @Override // org.apache.tools.ant.types.ArchiveFileSet
    protected AbstractFileSet getRef() {
        return getRef(getProject());
    }

    @Override // org.apache.tools.ant.types.ArchiveFileSet, org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.AbstractFileSet, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        if (isReference()) {
            return getRef().clone();
        }
        return super.clone();
    }
}
