package polyglot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TransformingIterator.class */
public final class TransformingIterator implements Iterator {
    Object next_item;
    Iterator current_iter;
    int index;
    Iterator[] backing_iterators;
    Transformation transformation;

    public TransformingIterator(Iterator iter, Transformation trans) {
        this(new Iterator[]{iter}, trans);
    }

    public TransformingIterator(Collection iters, Transformation trans) {
        this.index = 0;
        this.backing_iterators = (Iterator[]) iters.toArray(new Iterator[0]);
        this.transformation = trans;
        if (this.backing_iterators.length > 0) {
            this.current_iter = this.backing_iterators[0];
        }
        findNextItem();
    }

    public TransformingIterator(Iterator[] iters, Transformation trans) {
        this.index = 0;
        this.backing_iterators = (Iterator[]) iters.clone();
        this.transformation = trans;
        if (iters.length > 0) {
            this.current_iter = iters[0];
        }
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
        throw new UnsupportedOperationException("TransformingIterator.remove");
    }

    private void findNextItem() {
        while (this.current_iter != null) {
            while (this.current_iter.hasNext()) {
                Object o = this.current_iter.next();
                Object res = this.transformation.transform(o);
                if (res != Transformation.NOTHING) {
                    this.next_item = res;
                    return;
                }
            }
            this.index++;
            if (this.index < this.backing_iterators.length) {
                this.current_iter = this.backing_iterators[this.index];
            } else {
                this.current_iter = null;
            }
        }
        this.next_item = null;
    }
}
