package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Resources.class */
public class Resources extends DataType implements AppendableResourceCollection {
    public static final ResourceCollection NONE = new ResourceCollection() { // from class: org.apache.tools.ant.types.resources.Resources.1
        @Override // org.apache.tools.ant.types.ResourceCollection
        public boolean isFilesystemOnly() {
            return true;
        }

        @Override // java.lang.Iterable
        public Iterator<Resource> iterator() {
            return Resources.EMPTY_ITERATOR;
        }

        @Override // org.apache.tools.ant.types.ResourceCollection
        public int size() {
            return 0;
        }
    };
    public static final Iterator<Resource> EMPTY_ITERATOR = new Iterator<Resource>() { // from class: org.apache.tools.ant.types.resources.Resources.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Resource next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
    private List<ResourceCollection> rc;
    private Collection<Resource> coll;
    private boolean cache = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Resources$MyCollection.class */
    public class MyCollection extends AbstractCollection<Resource> {
        private Collection<Resource> cached;

        MyCollection() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return getCache().size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Resource> iterator() {
            return getCache().iterator();
        }

        private synchronized Collection<Resource> getCache() {
            Collection<Resource> coll = this.cached;
            if (coll == null) {
                coll = new ArrayList<>();
                MyIterator myIterator = new MyIterator();
                Objects.requireNonNull(coll);
                myIterator.forEachRemaining((v1) -> {
                    r1.add(v1);
                });
                if (Resources.this.cache) {
                    this.cached = coll;
                }
            }
            return coll;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Resources$MyCollection$MyIterator.class */
        public class MyIterator implements Iterator<Resource> {
            private Iterator<ResourceCollection> rci;
            private Iterator<Resource> ri;

            private MyIterator() {
                this.rci = Resources.this.getNested().iterator();
                this.ri = null;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                boolean result;
                boolean z = this.ri != null && this.ri.hasNext();
                while (true) {
                    result = z;
                    if (result || !this.rci.hasNext()) {
                        break;
                    }
                    this.ri = this.rci.next().iterator();
                    z = this.ri.hasNext();
                }
                return result;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Resource next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return this.ri.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    public Resources() {
    }

    public Resources(Project project) {
        setProject(project);
    }

    public synchronized void setCache(boolean b) {
        this.cache = b;
    }

    @Override // org.apache.tools.ant.types.resources.AppendableResourceCollection
    public synchronized void add(ResourceCollection c) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        if (this.rc == null) {
            this.rc = Collections.synchronizedList(new ArrayList());
        }
        this.rc.add(c);
        invalidateExistingIterators();
        this.coll = null;
        setChecked(false);
    }

    @Override // java.lang.Iterable
    public synchronized Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        validate();
        return new FailFast(this, this.coll.iterator());
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        if (isReference()) {
            return getRef().size();
        }
        validate();
        return this.coll.size();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        validate();
        return getNested().stream().allMatch((v0) -> {
            return v0.isFilesystemOnly();
        });
    }

    @Override // org.apache.tools.ant.types.DataType
    public synchronized String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        validate();
        if (this.coll == null || this.coll.isEmpty()) {
            return "";
        }
        return (String) this.coll.stream().map((v0) -> {
            return v0.toString();
        }).collect(Collectors.joining(File.pathSeparator));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        for (ResourceCollection resourceCollection : getNested()) {
            if (resourceCollection instanceof DataType) {
                pushAndInvokeCircularReferenceCheck((DataType) resourceCollection, stk, p);
            }
        }
        setChecked(true);
    }

    protected void invalidateExistingIterators() {
        FailFast.invalidate(this);
    }

    private ResourceCollection getRef() {
        return (ResourceCollection) getCheckedRef(ResourceCollection.class);
    }

    private synchronized void validate() {
        dieOnCircularReference();
        this.coll = this.coll == null ? new MyCollection() : this.coll;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized List<ResourceCollection> getNested() {
        return this.rc == null ? Collections.emptyList() : this.rc;
    }
}
