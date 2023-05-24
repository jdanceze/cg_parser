package org.apache.tools.ant.types.resources;

import java.util.Iterator;
import java.util.Stack;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.resources.selectors.ResourceSelectorContainer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Restrict.class */
public class Restrict extends ResourceSelectorContainer implements ResourceCollection {
    private LazyResourceCollectionWrapper w = new LazyResourceCollectionWrapper() { // from class: org.apache.tools.ant.types.resources.Restrict.1
        @Override // org.apache.tools.ant.types.resources.LazyResourceCollectionWrapper
        protected boolean filterResource(Resource r) {
            return Restrict.this.getResourceSelectors().stream().anyMatch(rsel -> {
                return !rsel.isSelected(r);
            });
        }
    };

    public synchronized void add(ResourceCollection c) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (c == null) {
            return;
        }
        this.w.add(c);
        setChecked(false);
    }

    public synchronized void setCache(boolean b) {
        this.w.setCache(b);
    }

    public synchronized boolean isCache() {
        return this.w.isCache();
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelectorContainer
    public synchronized void add(ResourceSelector s) {
        if (s == null) {
            return;
        }
        super.add(s);
        FailFast.invalidate(this);
    }

    @Override // java.lang.Iterable
    public final synchronized Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        dieOnCircularReference();
        return this.w.iterator();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        if (isReference()) {
            return getRef().size();
        }
        dieOnCircularReference();
        return this.w.size();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        dieOnCircularReference();
        return this.w.isFilesystemOnly();
    }

    @Override // org.apache.tools.ant.types.DataType
    public synchronized String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        dieOnCircularReference();
        return this.w.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelectorContainer, org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
        if (isChecked()) {
            return;
        }
        super.dieOnCircularReference(stk, p);
        if (!isReference()) {
            pushAndInvokeCircularReferenceCheck(this.w, stk, p);
            setChecked(true);
        }
    }

    private Restrict getRef() {
        return (Restrict) getCheckedRef(Restrict.class);
    }
}
