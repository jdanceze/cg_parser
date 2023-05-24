package polyglot.util;

import java.util.ListIterator;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TypedListIterator.class */
public class TypedListIterator implements ListIterator {
    private Class allowed_type;
    private boolean immutable;
    private ListIterator backing_iterator;

    public TypedListIterator(ListIterator iter, Class c, boolean immutable) {
        this.immutable = immutable;
        this.allowed_type = c;
        this.backing_iterator = iter;
    }

    public Class getAllowedType() {
        return this.allowed_type;
    }

    @Override // java.util.ListIterator
    public void add(Object o) {
        tryIns(o);
        this.backing_iterator.add(o);
    }

    @Override // java.util.ListIterator
    public void set(Object o) {
        tryIns(o);
        this.backing_iterator.set(o);
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public boolean hasNext() {
        return this.backing_iterator.hasNext();
    }

    @Override // java.util.ListIterator
    public boolean hasPrevious() {
        return this.backing_iterator.hasPrevious();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public Object next() {
        return this.backing_iterator.next();
    }

    @Override // java.util.ListIterator
    public int nextIndex() {
        return this.backing_iterator.nextIndex();
    }

    @Override // java.util.ListIterator
    public Object previous() {
        return this.backing_iterator.previous();
    }

    @Override // java.util.ListIterator
    public int previousIndex() {
        return this.backing_iterator.previousIndex();
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public void remove() {
        if (this.immutable) {
            throw new UnsupportedOperationException("Remove from an immutable TypedListIterator");
        }
        this.backing_iterator.remove();
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
}
