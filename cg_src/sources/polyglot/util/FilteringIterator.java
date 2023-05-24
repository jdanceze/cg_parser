package polyglot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/FilteringIterator.class */
public final class FilteringIterator implements Iterator {
    Object next_item;
    Iterator backing_iterator;
    Predicate predicate;

    public FilteringIterator(Collection coll, Predicate pred) {
        this(coll.iterator(), pred);
    }

    public FilteringIterator(Iterator iter, Predicate pred) {
        this.backing_iterator = iter;
        this.predicate = pred;
        findNextItem();
    }

    @Override // java.util.Iterator
    public Object next() {
        Object res = this.next_item;
        if (res == null) {
            throw new NoSuchElementException();
        }
        findNextItem();
        return res;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.next_item != null;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("FilteringIterator.remove");
    }

    private void findNextItem() {
        while (this.backing_iterator.hasNext()) {
            Object o = this.backing_iterator.next();
            if (this.predicate.isTrue(o)) {
                this.next_item = o;
                return;
            }
        }
        this.next_item = null;
    }
}
