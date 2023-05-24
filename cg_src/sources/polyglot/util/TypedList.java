package polyglot.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TypedList.class */
public class TypedList implements List, Serializable, Cloneable {
    static final long serialVersionUID = -1390984392613203018L;
    private Class allowed_type;
    private boolean immutable;
    private List backing_list;

    public static TypedList copy(List list, Class c, boolean immutable) {
        return new TypedList(new ArrayList(list), c, immutable);
    }

    public static TypedList copyAndCheck(List list, Class c, boolean immutable) {
        if (c != null) {
            check(list, c);
        }
        return copy(list, c, immutable);
    }

    public static void check(List list, Class c) {
        for (Object o : list) {
            if (o != null && !c.isAssignableFrom(o.getClass())) {
                throw new UnsupportedOperationException(new StringBuffer().append("Tried to add a ").append(o.getClass().getName()).append(" to a list of type ").append(c.getName()).toString());
            }
        }
    }

    public TypedList(List list, Class c, boolean immutable) {
        this.immutable = immutable;
        this.allowed_type = c;
        this.backing_list = list;
    }

    public Class getAllowedType() {
        return this.allowed_type;
    }

    public TypedList copy() {
        return (TypedList) clone();
    }

    public Object clone() {
        try {
            TypedList l = (TypedList) super.clone();
            l.backing_list = new ArrayList(this.backing_list);
            return l;
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone weirdness.");
        }
    }

    @Override // java.util.List
    public void add(int idx, Object o) {
        tryIns(o);
        this.backing_list.add(idx, o);
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(Object o) {
        tryIns(o);
        return this.backing_list.add(o);
    }

    @Override // java.util.List
    public boolean addAll(int idx, Collection coll) {
        tryIns(coll);
        return this.backing_list.addAll(idx, coll);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection coll) {
        tryIns(coll);
        return this.backing_list.addAll(coll);
    }

    @Override // java.util.List
    public ListIterator listIterator() {
        return new TypedListIterator(this.backing_list.listIterator(), this.allowed_type, this.immutable);
    }

    @Override // java.util.List
    public ListIterator listIterator(int idx) {
        return new TypedListIterator(this.backing_list.listIterator(idx), this.allowed_type, this.immutable);
    }

    @Override // java.util.List
    public Object set(int idx, Object o) {
        tryIns(o);
        return this.backing_list.set(idx, o);
    }

    @Override // java.util.List
    public List subList(int from, int to) {
        return new TypedList(this.backing_list.subList(from, to), this.allowed_type, this.immutable);
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        tryMod();
        this.backing_list.clear();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object o) {
        return this.backing_list.contains(o);
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection coll) {
        return this.backing_list.containsAll(coll);
    }

    @Override // java.util.List, java.util.Collection
    public boolean equals(Object o) {
        return this.backing_list.equals(o);
    }

    @Override // java.util.List
    public Object get(int idx) {
        return this.backing_list.get(idx);
    }

    @Override // java.util.List, java.util.Collection
    public int hashCode() {
        return this.backing_list.hashCode();
    }

    @Override // java.util.List
    public int indexOf(Object o) {
        return this.backing_list.indexOf(o);
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.backing_list.isEmpty();
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return listIterator();
    }

    @Override // java.util.List
    public int lastIndexOf(Object o) {
        return this.backing_list.lastIndexOf(o);
    }

    @Override // java.util.List
    public Object remove(int idx) {
        tryMod();
        return this.backing_list.remove(idx);
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object o) {
        tryMod();
        return this.backing_list.remove(o);
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection coll) {
        tryMod();
        return this.backing_list.removeAll(coll);
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection coll) {
        tryMod();
        return this.backing_list.retainAll(coll);
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.backing_list.size();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        return this.backing_list.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray(Object[] oa) {
        return this.backing_list.toArray(oa);
    }

    public String toString() {
        return this.backing_list.toString();
    }

    private final void tryIns(Object o) {
        if (this.immutable) {
            throw new UnsupportedOperationException("Add to an immutable TypedListIterator");
        }
        if (this.allowed_type != null && !this.allowed_type.isAssignableFrom(o.getClass())) {
            String why = new StringBuffer().append("Tried to add a ").append(o.getClass().getName()).append(" to a list of type ").append(this.allowed_type.getName()).toString();
            throw new UnsupportedOperationException(why);
        }
    }

    private final void tryIns(Collection coll) {
        if (this.immutable) {
            throw new UnsupportedOperationException("Add to an immutable TypedListIterator");
        }
        for (Object o : coll) {
            if (this.allowed_type != null && !this.allowed_type.isAssignableFrom(o.getClass())) {
                String why = new StringBuffer().append("Tried to add a ").append(o.getClass().getName()).append(" to a list of type ").append(this.allowed_type.getName()).toString();
                throw new UnsupportedOperationException(why);
            }
        }
    }

    private final void tryMod() {
        if (this.immutable) {
            throw new UnsupportedOperationException("Change to an immutable TypedListIterator");
        }
    }
}
