package org.apache.tools.ant.types.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/LazyResourceCollectionWrapper.class */
public class LazyResourceCollectionWrapper extends AbstractResourceCollectionWrapper {
    private Iterator<Resource> filteringIterator;
    private final List<Resource> cachedResources = new ArrayList();
    private final Supplier<Iterator<Resource>> filteringIteratorSupplier = () -> {
        return new FilteringIterator(getResourceCollection().iterator());
    };

    @Override // org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper
    protected Iterator<Resource> createIterator() {
        if (isCache()) {
            if (this.filteringIterator == null) {
                this.filteringIterator = this.filteringIteratorSupplier.get();
            }
            return new CachedIterator(this.filteringIterator);
        }
        return this.filteringIteratorSupplier.get();
    }

    @Override // org.apache.tools.ant.types.resources.AbstractResourceCollectionWrapper
    protected int getSize() {
        Iterator<Resource> it = createIterator();
        int size = 0;
        while (it.hasNext()) {
            it.next();
            size++;
        }
        return size;
    }

    protected boolean filterResource(Resource r) {
        return false;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/LazyResourceCollectionWrapper$FilteringIterator.class */
    private class FilteringIterator implements Iterator<Resource> {
        Resource next = null;
        boolean ended = false;
        protected final Iterator<Resource> it;

        FilteringIterator(Iterator<Resource> it) {
            this.it = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.ended) {
                return false;
            }
            while (this.next == null) {
                if (!this.it.hasNext()) {
                    this.ended = true;
                    return false;
                }
                this.next = this.it.next();
                if (LazyResourceCollectionWrapper.this.filterResource(this.next)) {
                    this.next = null;
                }
            }
            return true;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Resource next() {
            if (!hasNext()) {
                throw new UnsupportedOperationException();
            }
            Resource r = this.next;
            this.next = null;
            return r;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/LazyResourceCollectionWrapper$CachedIterator.class */
    public class CachedIterator implements Iterator<Resource> {
        int cursor = 0;
        private final Iterator<Resource> it;

        public CachedIterator(Iterator<Resource> it) {
            this.it = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            synchronized (LazyResourceCollectionWrapper.this.cachedResources) {
                if (LazyResourceCollectionWrapper.this.cachedResources.size() > this.cursor) {
                    return true;
                }
                if (!this.it.hasNext()) {
                    return false;
                }
                Resource r = this.it.next();
                LazyResourceCollectionWrapper.this.cachedResources.add(r);
                return true;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Resource next() {
            Resource resource;
            if (hasNext()) {
                synchronized (LazyResourceCollectionWrapper.this.cachedResources) {
                    List list = LazyResourceCollectionWrapper.this.cachedResources;
                    int i = this.cursor;
                    this.cursor = i + 1;
                    resource = (Resource) list.get(i);
                }
                return resource;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
