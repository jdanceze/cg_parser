package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/BaseResourceCollectionContainer.class */
public abstract class BaseResourceCollectionContainer extends DataType implements AppendableResourceCollection, Cloneable {
    private List<ResourceCollection> rc = new ArrayList();
    private Collection<Resource> coll = null;
    private boolean cache = true;

    protected abstract Collection<Resource> getCollection();

    public BaseResourceCollectionContainer() {
    }

    public BaseResourceCollectionContainer(Project project) {
        setProject(project);
    }

    public synchronized void setCache(boolean b) {
        this.cache = b;
    }

    public synchronized boolean isCache() {
        return this.cache;
    }

    public synchronized void clear() throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.rc.clear();
        FailFast.invalidate(this);
        this.coll = null;
        setChecked(false);
    }

    @Override // org.apache.tools.ant.types.resources.AppendableResourceCollection
    public synchronized void add(ResourceCollection c) throws BuildException {
        Project p;
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        if (Project.getProject(c) == null && (p = getProject()) != null) {
            p.setProjectReference(c);
        }
        this.rc.add(c);
        FailFast.invalidate(this);
        this.coll = null;
        setChecked(false);
    }

    public synchronized void addAll(Collection<? extends ResourceCollection> c) throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        try {
            c.forEach(this::add);
        } catch (ClassCastException e) {
            throw new BuildException(e);
        }
    }

    @Override // java.lang.Iterable
    public final synchronized Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        dieOnCircularReference();
        return new FailFast(this, cacheCollection().iterator());
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        if (isReference()) {
            return getRef().size();
        }
        dieOnCircularReference();
        return cacheCollection().size();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        dieOnCircularReference();
        if (this.rc.stream().allMatch((v0) -> {
            return v0.isFilesystemOnly();
        })) {
            return true;
        }
        return cacheCollection().stream().allMatch(r -> {
            return r.asOptional(FileProvider.class).isPresent();
        });
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
        for (ResourceCollection resourceCollection : this.rc) {
            if (resourceCollection instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) resourceCollection, stk, p);
            }
        }
        setChecked(true);
    }

    public final synchronized List<ResourceCollection> getResourceCollections() {
        dieOnCircularReference();
        return Collections.unmodifiableList(this.rc);
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public Object clone() {
        try {
            BaseResourceCollectionContainer c = (BaseResourceCollectionContainer) super.clone();
            c.rc = new ArrayList(this.rc);
            c.coll = null;
            return c;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    @Override // org.apache.tools.ant.types.DataType
    public synchronized String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        if (cacheCollection().isEmpty()) {
            return "";
        }
        return (String) this.coll.stream().map((v0) -> {
            return v0.toString();
        }).collect(Collectors.joining(File.pathSeparator));
    }

    private BaseResourceCollectionContainer getRef() {
        return (BaseResourceCollectionContainer) getCheckedRef(BaseResourceCollectionContainer.class);
    }

    private synchronized Collection<Resource> cacheCollection() {
        if (this.coll == null || !isCache()) {
            this.coll = getCollection();
        }
        return this.coll;
    }
}
