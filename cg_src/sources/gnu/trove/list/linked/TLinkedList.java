package gnu.trove.list.linked;

import gnu.trove.list.TLinkable;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TLinkedList.class */
public class TLinkedList<T extends TLinkable<T>> extends AbstractSequentialList<T> implements Externalizable {
    static final long serialVersionUID = 1;
    protected T _head;
    protected T _tail;
    protected int _size = 0;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public /* bridge */ /* synthetic */ void add(int x0, Object x1) {
        add(x0, (int) ((TLinkable) x1));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public /* bridge */ /* synthetic */ boolean add(Object x0) {
        return add((TLinkedList<T>) ((TLinkable) x0));
    }

    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public ListIterator<T> listIterator(int index) {
        return new IteratorImpl(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this._size;
    }

    public void add(int index, T linkable) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("index:" + index);
        }
        insert(index, linkable);
    }

    public boolean add(T linkable) {
        insert(this._size, linkable);
        return true;
    }

    public void addFirst(T linkable) {
        insert(0, linkable);
    }

    public void addLast(T linkable) {
        insert(size(), linkable);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        if (null != this._head) {
            TLinkable next = this._head.getNext();
            while (true) {
                TLinkable tLinkable = next;
                if (tLinkable == null) {
                    break;
                }
                TLinkable<T> prev = tLinkable.getPrevious();
                prev.setNext(null);
                tLinkable.setPrevious(null);
                next = tLinkable.getNext();
            }
            this._tail = null;
            this._head = null;
        }
        this._size = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] o = new Object[this._size];
        int i = 0;
        TLinkable tLinkable = this._head;
        while (true) {
            TLinkable link = tLinkable;
            if (link != null) {
                int i2 = i;
                i++;
                o[i2] = link;
                tLinkable = link.getNext();
            } else {
                return o;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [gnu.trove.list.TLinkable] */
    /* JADX WARN: Type inference failed for: r0v12, types: [gnu.trove.list.TLinkable] */
    public Object[] toUnlinkedArray() {
        Object[] o = new Object[this._size];
        int i = 0;
        TLinkable tLinkable = this._head;
        while (tLinkable != null) {
            o[i] = tLinkable;
            ?? r0 = tLinkable;
            tLinkable = tLinkable.getNext();
            r0.setNext(null);
            r0.setPrevious(null);
            i++;
        }
        this._size = 0;
        this._tail = null;
        this._head = null;
        return o;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [gnu.trove.list.TLinkable] */
    /* JADX WARN: Type inference failed for: r0v22, types: [gnu.trove.list.TLinkable[]] */
    public T[] toUnlinkedArray(T[] a) {
        int size = size();
        if (a.length < size) {
            a = (TLinkable[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        T link = this._head;
        while (link != null) {
            a[i] = link;
            T tmp = link;
            link = link.getNext();
            tmp.setNext(null);
            tmp.setPrevious(null);
            i++;
        }
        this._size = 0;
        this._tail = null;
        this._head = null;
        return a;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object o) {
        TLinkable tLinkable = this._head;
        while (true) {
            TLinkable tLinkable2 = tLinkable;
            if (tLinkable2 != null) {
                if (!o.equals(tLinkable2)) {
                    tLinkable = tLinkable2.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [gnu.trove.list.TLinkable] */
    /* JADX WARN: Type inference failed for: r0v19, types: [gnu.trove.list.TLinkable] */
    @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
    public T get(int index) {
        if (index < 0 || index >= this._size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this._size);
        }
        if (index > (this._size >> 1)) {
            T node = this._tail;
            for (int position = this._size - 1; position > index; position--) {
                node = node.getPrevious();
            }
            return node;
        }
        T node2 = this._head;
        for (int position2 = 0; position2 < index; position2++) {
            node2 = node2.getNext();
        }
        return node2;
    }

    public T getFirst() {
        return this._head;
    }

    public T getLast() {
        return this._tail;
    }

    public T getNext(T current) {
        return (T) current.getNext();
    }

    public T getPrevious(T current) {
        return (T) current.getPrevious();
    }

    public T removeFirst() {
        T o = this._head;
        if (o == null) {
            return null;
        }
        T n = (T) o.getNext();
        o.setNext(null);
        if (null != n) {
            n.setPrevious(null);
        }
        this._head = n;
        int i = this._size - 1;
        this._size = i;
        if (i == 0) {
            this._tail = null;
        }
        return o;
    }

    public T removeLast() {
        T o = this._tail;
        if (o == null) {
            return null;
        }
        T prev = (T) o.getPrevious();
        o.setPrevious(null);
        if (null != prev) {
            prev.setNext(null);
        }
        this._tail = prev;
        int i = this._size - 1;
        this._size = i;
        if (i == 0) {
            this._head = null;
        }
        return o;
    }

    protected void insert(int index, T linkable) {
        if (this._size == 0) {
            this._tail = linkable;
            this._head = linkable;
        } else if (index == 0) {
            linkable.setNext(this._head);
            this._head.setPrevious(linkable);
            this._head = linkable;
        } else if (index == this._size) {
            this._tail.setNext(linkable);
            linkable.setPrevious(this._tail);
            this._tail = linkable;
        } else {
            T node = get(index);
            TLinkable previous = node.getPrevious();
            if (previous != null) {
                previous.setNext(linkable);
            }
            linkable.setPrevious(previous);
            linkable.setNext(node);
            node.setPrevious(linkable);
        }
        this._size++;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(Object o) {
        if (o instanceof TLinkable) {
            TLinkable<T> link = (TLinkable) o;
            T p = link.getPrevious();
            T n = link.getNext();
            if (n == null && p == null) {
                if (o != this._head) {
                    return false;
                }
                this._tail = null;
                this._head = null;
            } else if (n == null) {
                link.setPrevious(null);
                p.setNext(null);
                this._tail = p;
            } else if (p == null) {
                link.setNext(null);
                n.setPrevious(null);
                this._head = n;
            } else {
                p.setNext(n);
                n.setPrevious(p);
                link.setNext(null);
                link.setPrevious(null);
            }
            this._size--;
            return true;
        }
        return false;
    }

    public void addBefore(T current, T newElement) {
        if (current == this._head) {
            addFirst(newElement);
        } else if (current == null) {
            addLast(newElement);
        } else {
            TLinkable previous = current.getPrevious();
            newElement.setNext(current);
            previous.setNext(newElement);
            newElement.setPrevious(previous);
            current.setPrevious(newElement);
            this._size++;
        }
    }

    public void addAfter(T current, T newElement) {
        if (current == this._tail) {
            addLast(newElement);
        } else if (current == null) {
            addFirst(newElement);
        } else {
            TLinkable next = current.getNext();
            newElement.setPrevious(current);
            newElement.setNext(next);
            current.setNext(newElement);
            next.setPrevious(newElement);
            this._size++;
        }
    }

    public boolean forEachValue(TObjectProcedure<T> procedure) {
        T t = this._head;
        while (true) {
            T node = t;
            if (node != null) {
                boolean keep_going = procedure.execute(node);
                if (!keep_going) {
                    return false;
                }
                t = (T) node.getNext();
            } else {
                return true;
            }
        }
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeInt(this._size);
        out.writeObject(this._head);
        out.writeObject(this._tail);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._size = in.readInt();
        this._head = (T) in.readObject();
        this._tail = (T) in.readObject();
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TLinkedList$IteratorImpl.class */
    protected final class IteratorImpl implements ListIterator<T> {
        private int _nextIndex;
        private T _next;
        private T _lastReturned;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.ListIterator
        public /* bridge */ /* synthetic */ void add(Object x0) {
            add((IteratorImpl) ((TLinkable) x0));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.ListIterator
        public /* bridge */ /* synthetic */ void set(Object x0) {
            set((IteratorImpl) ((TLinkable) x0));
        }

        IteratorImpl(int position) {
            this._nextIndex = 0;
            if (position < 0 || position > TLinkedList.this._size) {
                throw new IndexOutOfBoundsException();
            }
            this._nextIndex = position;
            if (position == 0) {
                this._next = TLinkedList.this._head;
            } else if (position == TLinkedList.this._size) {
                this._next = null;
            } else if (position < (TLinkedList.this._size >> 1)) {
                this._next = TLinkedList.this._head;
                for (int pos = 0; pos < position; pos++) {
                    this._next = (T) this._next.getNext();
                }
            } else {
                this._next = TLinkedList.this._tail;
                for (int pos2 = TLinkedList.this._size - 1; pos2 > position; pos2--) {
                    this._next = (T) this._next.getPrevious();
                }
            }
        }

        public final void add(T linkable) {
            this._lastReturned = null;
            this._nextIndex++;
            if (TLinkedList.this._size == 0) {
                TLinkedList.this.add((TLinkedList) linkable);
            } else {
                TLinkedList.this.addBefore(this._next, linkable);
            }
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public final boolean hasNext() {
            return this._nextIndex != TLinkedList.this._size;
        }

        @Override // java.util.ListIterator
        public final boolean hasPrevious() {
            return this._nextIndex != 0;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public final T next() {
            if (this._nextIndex == TLinkedList.this._size) {
                throw new NoSuchElementException();
            }
            this._lastReturned = this._next;
            this._next = (T) this._next.getNext();
            this._nextIndex++;
            return this._lastReturned;
        }

        @Override // java.util.ListIterator
        public final int nextIndex() {
            return this._nextIndex;
        }

        @Override // java.util.ListIterator
        public final T previous() {
            if (this._nextIndex == 0) {
                throw new NoSuchElementException();
            }
            if (this._nextIndex == TLinkedList.this._size) {
                T t = TLinkedList.this._tail;
                this._next = t;
                this._lastReturned = t;
            } else {
                T t2 = (T) this._next.getPrevious();
                this._next = t2;
                this._lastReturned = t2;
            }
            this._nextIndex--;
            return this._lastReturned;
        }

        @Override // java.util.ListIterator
        public final int previousIndex() {
            return this._nextIndex - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public final void remove() {
            if (this._lastReturned == null) {
                throw new IllegalStateException("must invoke next or previous before invoking remove");
            }
            if (this._lastReturned != this._next) {
                this._nextIndex--;
            }
            this._next = (T) this._lastReturned.getNext();
            TLinkedList.this.remove(this._lastReturned);
            this._lastReturned = null;
        }

        public final void set(T linkable) {
            if (this._lastReturned == null) {
                throw new IllegalStateException();
            }
            swap(this._lastReturned, linkable);
            this._lastReturned = linkable;
        }

        private void swap(T from, T to) {
            TLinkable previous = from.getPrevious();
            TLinkable next = from.getNext();
            TLinkable previous2 = to.getPrevious();
            TLinkable next2 = to.getNext();
            if (next == to) {
                if (previous != null) {
                    previous.setNext(to);
                }
                to.setPrevious(previous);
                to.setNext(from);
                from.setPrevious(to);
                from.setNext(next2);
                if (next2 != null) {
                    next2.setPrevious(from);
                }
            } else if (next2 == from) {
                if (previous2 != null) {
                    previous2.setNext(to);
                }
                to.setPrevious(from);
                to.setNext(next);
                from.setPrevious(previous2);
                from.setNext(to);
                if (next != null) {
                    next.setPrevious(to);
                }
            } else {
                from.setNext(next2);
                from.setPrevious(previous2);
                if (previous2 != null) {
                    previous2.setNext(from);
                }
                if (next2 != null) {
                    next2.setPrevious(from);
                }
                to.setNext(next);
                to.setPrevious(previous);
                if (previous != null) {
                    previous.setNext(to);
                }
                if (next != null) {
                    next.setPrevious(to);
                }
            }
            if (TLinkedList.this._head == from) {
                TLinkedList.this._head = to;
            } else if (TLinkedList.this._head == to) {
                TLinkedList.this._head = from;
            }
            if (TLinkedList.this._tail == from) {
                TLinkedList.this._tail = to;
            } else if (TLinkedList.this._tail == to) {
                TLinkedList.this._tail = from;
            }
            if (this._lastReturned == from) {
                this._lastReturned = to;
            } else if (this._lastReturned == to) {
                this._lastReturned = from;
            }
            if (this._next != from) {
                if (this._next == to) {
                    this._next = from;
                    return;
                }
                return;
            }
            this._next = to;
        }
    }
}
