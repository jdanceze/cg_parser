package org.apache.tools.ant.types.resources;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ArchiveFileSet;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.TarFileSet;
import org.apache.tools.ant.types.ZipFileSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Archives.class */
public class Archives extends DataType implements ResourceCollection, Cloneable {
    private Union zips = new Union();
    private Union tars = new Union();

    public Union createZips() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        return this.zips;
    }

    public Union createTars() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        setChecked(false);
        return this.tars;
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        if (isReference()) {
            return getRef().size();
        }
        dieOnCircularReference();
        return streamArchives().mapToInt((v0) -> {
            return v0.size();
        }).sum();
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        dieOnCircularReference();
        Stream<R> flatMap = streamArchives().flatMap((v0) -> {
            return v0.stream();
        });
        Objects.requireNonNull(Resource.class);
        return flatMap.map((v1) -> {
            return r1.cast(v1);
        }).iterator();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        dieOnCircularReference();
        return false;
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) {
        if (!this.zips.getResourceCollections().isEmpty() || !this.tars.getResourceCollections().isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        try {
            Archives a = (Archives) super.clone();
            a.zips = (Union) this.zips.clone();
            a.tars = (Union) this.tars.clone();
            return a;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    protected Iterator<ArchiveFileSet> grabArchives() {
        return streamArchives().iterator();
    }

    private Stream<ArchiveFileSet> streamArchives() {
        List<ArchiveFileSet> l = new LinkedList<>();
        Iterator<Resource> it = this.zips.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            l.add(configureArchive(new ZipFileSet(), r));
        }
        Iterator<Resource> it2 = this.tars.iterator();
        while (it2.hasNext()) {
            Resource r2 = it2.next();
            l.add(configureArchive(new TarFileSet(), r2));
        }
        return l.stream();
    }

    protected ArchiveFileSet configureArchive(ArchiveFileSet afs, Resource src) {
        afs.setProject(getProject());
        afs.setSrcResource(src);
        return afs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        pushAndInvokeCircularReferenceCheck(this.zips, stk, p);
        pushAndInvokeCircularReferenceCheck(this.tars, stk, p);
        setChecked(true);
    }

    private Archives getRef() {
        return (Archives) getCheckedRef(Archives.class);
    }
}
