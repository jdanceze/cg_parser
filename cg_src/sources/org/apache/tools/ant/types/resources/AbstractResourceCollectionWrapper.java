package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Iterator;
import java.util.Stack;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/AbstractResourceCollectionWrapper.class */
public abstract class AbstractResourceCollectionWrapper extends DataType implements ResourceCollection, Cloneable {
    private static final String ONE_NESTED_MESSAGE = " expects exactly one nested resource collection.";
    private ResourceCollection rc;
    private boolean cache = true;

    protected abstract Iterator<Resource> createIterator();

    protected abstract int getSize();

    public synchronized void setCache(boolean b) {
        this.cache = b;
    }

    public synchronized boolean isCache() {
        return this.cache;
    }

    public synchronized void add(ResourceCollection c) throws BuildException {
        Project p;
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        if (this.rc != null) {
            throw oneNested();
        }
        this.rc = c;
        if (Project.getProject(this.rc) == null && (p = getProject()) != null) {
            p.setProjectReference(this.rc);
        }
        setChecked(false);
    }

    @Override // java.lang.Iterable
    public final synchronized Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        dieOnCircularReference();
        return new FailFast(this, createIterator());
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        if (isReference()) {
            return getRef().size();
        }
        dieOnCircularReference();
        return getSize();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        dieOnCircularReference();
        if (this.rc == null || this.rc.isFilesystemOnly()) {
            return true;
        }
        Iterator<Resource> it = iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            if (r.as(FileProvider.class) == null) {
                return false;
            }
        }
        return true;
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
        if (this.rc instanceof DataType) {
            pushAndInvokeCircularReferenceCheck((DataType) this.rc, stk, p);
        }
        setChecked(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized ResourceCollection getResourceCollection() {
        dieOnCircularReference();
        if (this.rc == null) {
            throw oneNested();
        }
        return this.rc;
    }

    @Override // org.apache.tools.ant.types.DataType
    public synchronized String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        if (isEmpty()) {
            return "";
        }
        return (String) stream().map((v0) -> {
            return v0.toString();
        }).collect(Collectors.joining(File.pathSeparator));
    }

    private AbstractResourceCollectionWrapper getRef() {
        return (AbstractResourceCollectionWrapper) getCheckedRef(AbstractResourceCollectionWrapper.class);
    }

    private BuildException oneNested() {
        return new BuildException(super.toString() + ONE_NESTED_MESSAGE);
    }
}
