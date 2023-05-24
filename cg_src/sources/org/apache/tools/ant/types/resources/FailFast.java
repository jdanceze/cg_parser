package org.apache.tools.ant.types.resources;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.WeakHashMap;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/FailFast.class */
class FailFast implements Iterator<Resource> {
    private static final WeakHashMap<Object, Set<FailFast>> MAP = new WeakHashMap<>();
    private final Object parent;
    private Iterator<Resource> wrapped;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void invalidate(Object o) {
        Set<FailFast> s = MAP.get(o);
        if (s != null) {
            s.clear();
        }
    }

    private static synchronized void add(FailFast f) {
        MAP.computeIfAbsent(f.parent, k -> {
            return new HashSet();
        }).add(f);
    }

    private static synchronized void remove(FailFast f) {
        Set<FailFast> s = MAP.get(f.parent);
        if (s != null) {
            s.remove(f);
        }
    }

    private static synchronized void failFast(FailFast f) {
        Set<FailFast> s = MAP.get(f.parent);
        if (!s.contains(f)) {
            throw new ConcurrentModificationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FailFast(Object o, Iterator<Resource> i) {
        if (o == null) {
            throw new IllegalArgumentException("parent object is null");
        }
        if (i == null) {
            throw new IllegalArgumentException("cannot wrap null iterator");
        }
        this.parent = o;
        if (i.hasNext()) {
            this.wrapped = i;
            add(this);
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.wrapped == null) {
            return false;
        }
        failFast(this);
        return this.wrapped.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Resource next() {
        if (this.wrapped == null || !this.wrapped.hasNext()) {
            throw new NoSuchElementException();
        }
        failFast(this);
        try {
            return this.wrapped.next();
        } finally {
            if (!this.wrapped.hasNext()) {
                this.wrapped = null;
                remove(this);
            }
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
