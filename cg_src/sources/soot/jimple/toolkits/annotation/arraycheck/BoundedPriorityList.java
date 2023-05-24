package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/BoundedPriorityList.class */
public class BoundedPriorityList implements Collection {
    protected final List fulllist;
    protected ArrayList worklist;

    public BoundedPriorityList(List list) {
        this.fulllist = list;
        this.worklist = new ArrayList(list);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.worklist.isEmpty();
    }

    public Object removeFirst() {
        return this.worklist.remove(0);
    }

    @Override // java.util.Collection
    public boolean add(Object toadd) {
        if (contains(toadd)) {
            return false;
        }
        int index = this.fulllist.indexOf(toadd);
        ListIterator worklistIter = this.worklist.listIterator();
        while (worklistIter.hasNext()) {
            Object tocomp = worklistIter.next();
            int tmpidx = this.fulllist.indexOf(tocomp);
            if (index < tmpidx) {
                worklistIter.add(toadd);
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Collection
    public boolean addAll(Collection c) {
        boolean addedSomething = false;
        for (Object o : c) {
            addedSomething |= add(o);
        }
        return addedSomething;
    }

    public boolean addAll(int index, Collection c) {
        throw new RuntimeException("Not supported. You should use addAll(Collection) to keep priorities.");
    }

    @Override // java.util.Collection
    public void clear() {
        this.worklist.clear();
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        return this.worklist.contains(o);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection c) {
        return this.worklist.containsAll(c);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return this.worklist.iterator();
    }

    @Override // java.util.Collection
    public boolean remove(Object o) {
        return this.worklist.remove(o);
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection c) {
        return this.worklist.removeAll(c);
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection c) {
        return this.worklist.retainAll(c);
    }

    @Override // java.util.Collection
    public int size() {
        return this.worklist.size();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return this.worklist.toArray();
    }

    @Override // java.util.Collection
    public Object[] toArray(Object[] a) {
        return this.worklist.toArray(a);
    }

    public String toString() {
        return this.worklist.toString();
    }

    @Override // java.util.Collection
    public boolean equals(Object obj) {
        return this.worklist.equals(obj);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return this.worklist.hashCode();
    }
}
