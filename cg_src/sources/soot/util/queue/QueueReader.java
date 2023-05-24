package soot.util.queue;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import soot.util.Invalidable;
/* loaded from: gencallgraphv3.jar:soot/util/queue/QueueReader.class */
public class QueueReader<E> implements Iterator<E> {
    protected E[] q;
    protected int index;

    /* JADX INFO: Access modifiers changed from: protected */
    public QueueReader(E[] eArr, int index) {
        this.q = eArr;
        this.index = index;
    }

    @Override // java.util.Iterator
    public E next() {
        while (this.q[this.index] != null) {
            if (this.index == this.q.length - 1) {
                this.q = (E[]) ((Object[]) this.q[this.index]);
                this.index = 0;
                if (this.q[this.index] == null) {
                    throw new NoSuchElementException();
                }
            }
            E e = this.q[this.index];
            if (e == ChunkedQueue.NULL_CONST) {
                e = null;
            }
            this.index++;
            if (!skip(e)) {
                return e;
            }
        }
        throw new NoSuchElementException();
    }

    protected boolean skip(Object ret) {
        if (ret instanceof Invalidable) {
            Invalidable invalidable = (Invalidable) ret;
            if (invalidable.isInvalid()) {
                return true;
            }
        }
        return ret == ChunkedQueue.DELETED_CONST;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        while (true) {
            E ret = this.q[this.index];
            if (ret == null) {
                return false;
            }
            if (this.index == this.q.length - 1) {
                this.q = (E[]) ((Object[]) ret);
                this.index = 0;
                if (this.q[this.index] == null) {
                    return false;
                }
            }
            if (skip(ret)) {
                this.index++;
            } else {
                return true;
            }
        }
    }

    public void remove(E o) {
        if (o instanceof Invalidable) {
            ((Invalidable) o).invalidate();
        } else {
            remove((Collection) Collections.singleton(o));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void remove(Collection<E> toRemove) {
        boolean allInvalidable = true;
        for (E o : toRemove) {
            if (!(o instanceof Invalidable)) {
                allInvalidable = false;
            } else {
                ((Invalidable) o).invalidate();
            }
        }
        if (allInvalidable) {
            return;
        }
        int idx = 0;
        Object[] curQ = this.q;
        while (curQ[idx] != null) {
            if (idx == curQ.length - 1) {
                curQ = (Object[]) curQ[idx];
                idx = 0;
            }
            if (toRemove.contains(curQ[idx])) {
                curQ[idx] = ChunkedQueue.DELETED_CONST;
            }
            idx++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Iterator
    public void remove() {
        this.q[this.index - 1] = ChunkedQueue.DELETED_CONST;
    }

    /* renamed from: clone */
    public QueueReader<E> m3089clone() {
        return new QueueReader<>(this.q, this.index);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String toString() {
        Object curObj;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean isFirst = true;
        int idx = this.index;
        Object[] curArray = this.q;
        while (idx < curArray.length && (curObj = curArray[idx]) != null) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            if (curObj instanceof Object[]) {
                curArray = (Object[]) curObj;
                idx = 0;
            } else {
                sb.append(curObj.toString());
                idx++;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
