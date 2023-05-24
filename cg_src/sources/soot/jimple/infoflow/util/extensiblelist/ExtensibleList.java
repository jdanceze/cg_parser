package soot.jimple.infoflow.util.extensiblelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/extensiblelist/ExtensibleList.class */
public class ExtensibleList<T> {
    ExtensibleList<T> previous;
    int size;
    int lastLocked;
    private List<T> actualList;
    private int previousLockedAt;
    private int savedHashCode;
    private int parentHashCode;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/extensiblelist/ExtensibleList$ExtensibleListIterator.class */
    public static class ExtensibleListIterator<T> implements Iterator<T> {
        ExtensibleList<T> list;
        private ListIterator<T> it;

        public ExtensibleListIterator(ExtensibleList<T> start, ListIterator<T> itStart) {
            this.list = start;
            this.it = itStart;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.list == null) {
                return false;
            }
            if (this.it != null && this.it.hasPrevious()) {
                return this.it.hasPrevious();
            }
            while (true) {
                int l = ((ExtensibleList) this.list).previousLockedAt;
                this.list = this.list.previous;
                if (this.list == null) {
                    return false;
                }
                if (((ExtensibleList) this.list).actualList != null) {
                    if (l < 0) {
                        l = ((ExtensibleList) this.list).actualList.size();
                    }
                    if (l < 0) {
                        throw new RuntimeException("List has less than zero elements");
                    }
                    this.it = ((ExtensibleList) this.list).actualList.listIterator(l);
                    if (this.it.hasPrevious()) {
                        return true;
                    }
                }
            }
        }

        @Override // java.util.Iterator
        public T next() {
            if (!hasNext()) {
                throw new ArrayIndexOutOfBoundsException("No more elements");
            }
            return this.it.previous();
        }

        public boolean isSamePosition(ExtensibleListIterator<T> it2) {
            return ((ExtensibleList) this.list).actualList == it2.list && this.it.previousIndex() == it2.it.previousIndex();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable] */
    public ExtensibleList(ExtensibleList<T> original) {
        this.lastLocked = -1;
        this.actualList = null;
        this.savedHashCode = Integer.MIN_VALUE;
        ?? r0 = original;
        synchronized (r0) {
            this.size = original.size;
            this.previous = original;
            if (original.actualList != null) {
                this.previousLockedAt = original.actualList.size();
                original.lastLocked = Math.min(original.lastLocked, original.actualList.size());
                if (original.lastLocked == -1) {
                    original.lastLocked = original.actualList.size();
                }
            } else if (original.previous != null) {
                this.previousLockedAt = original.previousLockedAt;
                this.previous = original.previous;
            } else {
                this.previousLockedAt = 0;
            }
            this.parentHashCode = original.onlyElementHashCode();
            r0 = r0;
        }
    }

    public int hashCode() {
        return onlyElementHashCode() ^ (this.size * 15);
    }

    private int onlyElementHashCode() {
        if (this.savedHashCode != Integer.MIN_VALUE) {
            return this.savedHashCode;
        }
        int h = this.parentHashCode;
        if (this.actualList != null) {
            for (T l : this.actualList) {
                h = (31 * h) + l.hashCode();
            }
        }
        this.savedHashCode = h;
        return h;
    }

    public ExtensibleList() {
        this.lastLocked = -1;
        this.actualList = null;
        this.savedHashCode = Integer.MIN_VALUE;
        this.previousLockedAt = -1;
        this.parentHashCode = 0;
    }

    public int size() {
        return this.size;
    }

    public ExtensibleList<T> add(T add) {
        if (this.actualList == null) {
            this.actualList = new ArrayList(4);
        }
        this.actualList.add(add);
        this.size++;
        this.savedHashCode = Integer.MIN_VALUE;
        return this;
    }

    private void printStats() {
        int i = 0;
        int max = 0;
        int min = 0;
        ExtensibleList<T> extensibleList = this;
        while (true) {
            ExtensibleList<T> list = extensibleList;
            if (list != null) {
                i++;
                if (list.actualList != null) {
                    max = Math.max(max, list.actualList.size());
                }
                if (list.actualList != null) {
                    min = Math.min(min, list.actualList.size());
                }
                extensibleList = list.previous;
            } else {
                System.out.println(String.format("%d list parts for %d elements, min: %d, max: %d", Integer.valueOf(i), Integer.valueOf(this.size), Integer.valueOf(min), Integer.valueOf(max)));
                return;
            }
        }
    }

    public ExtensibleList<T> addAll(T... tArr) {
        for (T t : tArr) {
            add(t);
        }
        return this;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Object removeLast() {
        return getOrRemoveLast(true);
    }

    public ExtensibleListIterator<T> reverseIterator() {
        ListIterator<T> itStart;
        if (this.actualList != null) {
            itStart = this.actualList.listIterator(this.actualList.size());
        } else {
            itStart = null;
        }
        return new ExtensibleListIterator<>(this, itStart);
    }

    public T getLast() {
        return (T) getOrRemoveLast(false);
    }

    private Object getOrRemoveLast(boolean remove) {
        ExtensibleList<T> extensibleList;
        ExtensibleList<T> extensibleList2;
        if (this.size == 0) {
            return null;
        }
        ExtensibleList<T> check = this;
        int lockedAt = -1;
        while (check != null) {
            try {
                if (check.actualList != null && !check.actualList.isEmpty()) {
                    int elem = lockedAt == -1 ? check.actualList.size() - 1 : lockedAt;
                    if (!remove) {
                        T t = check.actualList.get(elem);
                        if (remove) {
                            ExtensibleList<T> update = this;
                            List<ExtensibleList<T>> chain = new ArrayList<>();
                            while (true) {
                                chain.add(update);
                                if (update == check) {
                                    break;
                                }
                                update = update.previous;
                            }
                            int parentHashCode = check.parentHashCode;
                            ListIterator<ExtensibleList<T>> l = chain.listIterator(chain.size());
                            while (l.hasPrevious()) {
                                ExtensibleList<T> list = l.previous();
                                list.parentHashCode = parentHashCode;
                                list.savedHashCode = Integer.MIN_VALUE;
                                parentHashCode = list.onlyElementHashCode();
                            }
                        }
                        return t;
                    } else if (elem >= check.lastLocked) {
                        T b = check.actualList.remove(elem);
                        this.size--;
                        this.savedHashCode = Integer.MIN_VALUE;
                        if (remove) {
                            ExtensibleList<T> update2 = this;
                            List<ExtensibleList<T>> chain2 = new ArrayList<>();
                            while (true) {
                                chain2.add(update2);
                                if (update2 == check) {
                                    break;
                                }
                                update2 = update2.previous;
                            }
                            int parentHashCode2 = check.parentHashCode;
                            ListIterator<ExtensibleList<T>> l2 = chain2.listIterator(chain2.size());
                            while (l2.hasPrevious()) {
                                ExtensibleList<T> list2 = l2.previous();
                                list2.parentHashCode = parentHashCode2;
                                list2.savedHashCode = Integer.MIN_VALUE;
                                parentHashCode2 = list2.onlyElementHashCode();
                            }
                        }
                        return b;
                    } else {
                        ExtensibleList<T> result = new ExtensibleList<>();
                        result.actualList = new ArrayList(this.size);
                        ExtensibleListIterator<T> it = reverseIterator();
                        it.next();
                        while (it.hasNext()) {
                            T n = it.next();
                            result.actualList.add(n);
                        }
                        Collections.reverse(result.actualList);
                        result.size = this.size - 1;
                        if (0 != 0) {
                            ExtensibleList<T> update3 = this;
                            List<ExtensibleList<T>> chain3 = new ArrayList<>();
                            while (true) {
                                chain3.add(update3);
                                if (update3 == check) {
                                    break;
                                }
                                update3 = update3.previous;
                            }
                            int parentHashCode3 = check.parentHashCode;
                            ListIterator<ExtensibleList<T>> l3 = chain3.listIterator(chain3.size());
                            while (l3.hasPrevious()) {
                                ExtensibleList<T> list3 = l3.previous();
                                list3.parentHashCode = parentHashCode3;
                                list3.savedHashCode = Integer.MIN_VALUE;
                                parentHashCode3 = list3.onlyElementHashCode();
                            }
                        }
                        return result;
                    }
                }
                lockedAt = check.previousLockedAt - 1;
                check = check.previous;
            } finally {
                if (remove) {
                    ExtensibleList<T> update4 = this;
                    List<ExtensibleList<T>> chain4 = new ArrayList<>();
                    while (true) {
                        chain4.add(update4);
                        if (update4 == check) {
                            break;
                        }
                        update4 = update4.previous;
                    }
                    int parentHashCode4 = check.parentHashCode;
                    ListIterator<ExtensibleList<T>> l4 = chain4.listIterator(chain4.size());
                    while (l4.hasPrevious()) {
                        ExtensibleList<T> list4 = l4.previous();
                        list4.parentHashCode = parentHashCode4;
                        list4.savedHashCode = Integer.MIN_VALUE;
                        parentHashCode4 = list4.onlyElementHashCode();
                    }
                }
            }
        }
        if (remove) {
            throw new RuntimeException("No element found to delete");
        }
        if (remove) {
            while (true) {
                if (extensibleList == extensibleList2) {
                    break;
                }
            }
            return null;
        }
        return null;
    }

    public boolean equals(Object obj) {
        T t1;
        T t2;
        ExtensibleList<T> other = (ExtensibleList) obj;
        if (other.size != this.size) {
            return false;
        }
        if (this.previous == other.previous) {
            if (this.previousLockedAt != other.previousLockedAt) {
                return false;
            }
            if (this.actualList == null || this.actualList.isEmpty()) {
                return other.actualList == null || other.actualList.isEmpty();
            }
            return this.actualList.equals(other.actualList);
        }
        ExtensibleListIterator<T> it1 = reverseIterator();
        ExtensibleListIterator<T> it2 = other.reverseIterator();
        do {
            boolean i1 = it1.hasNext();
            boolean i2 = it2.hasNext();
            if (i1 != i2) {
                return false;
            }
            if (!i1 || it1.isSamePosition(it2)) {
                return true;
            }
            t1 = it1.next();
            t2 = it2.next();
        } while (t1.equals(t2));
        return false;
    }

    public T getFirstSlow() {
        ExtensibleList<T> list = this;
        T first = null;
        while (true) {
            if (list.actualList != null) {
                first = list.actualList.get(0);
            }
            if (list.previous != null) {
                list = list.previous;
            } else {
                return first;
            }
        }
    }

    public ExtensibleList<T> addFirstSlow(T toAdd) {
        ExtensibleList<T> list = new ExtensibleList<>();
        list.actualList = new LinkedList();
        ExtensibleListIterator<T> it = reverseIterator();
        while (it.hasNext()) {
            list.actualList.add(0, it.next());
        }
        list.actualList.add(0, toAdd);
        list.size = this.size + 1;
        return list;
    }

    public String toString() {
        List<T> res = new ArrayList<>();
        ExtensibleListIterator<T> it = reverseIterator();
        while (it.hasNext()) {
            res.add(it.next());
        }
        Collections.reverse(res);
        return res.toString();
    }

    public List<T> getActualList() {
        return this.actualList;
    }
}
