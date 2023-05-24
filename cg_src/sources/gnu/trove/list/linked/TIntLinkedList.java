package gnu.trove.list.linked;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.procedure.TIntProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TIntLinkedList.class */
public class TIntLinkedList implements TIntList, Externalizable {
    int no_entry_value;
    int size;
    TIntLink head = null;
    TIntLink tail = this.head;

    public TIntLinkedList() {
    }

    public TIntLinkedList(int no_entry_value) {
        this.no_entry_value = no_entry_value;
    }

    public TIntLinkedList(TIntList list) {
        this.no_entry_value = list.getNoEntryValue();
        TIntIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            int next = iterator.next();
            add(next);
        }
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int size() {
        return this.size;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean add(int val) {
        TIntLink l = new TIntLink(val);
        if (no(this.head)) {
            this.head = l;
            this.tail = l;
        } else {
            l.setPrevious(this.tail);
            this.tail.setNext(l);
            this.tail = l;
        }
        this.size++;
        return true;
    }

    @Override // gnu.trove.list.TIntList
    public void add(int[] vals) {
        for (int val : vals) {
            add(val);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void add(int[] vals, int offset, int length) {
        for (int i = 0; i < length; i++) {
            int val = vals[offset + i];
            add(val);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int value) {
        TIntLinkedList tmp = new TIntLinkedList();
        tmp.add(value);
        insert(offset, tmp);
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int[] values) {
        insert(offset, link(values, 0, values.length));
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int[] values, int valOffset, int len) {
        insert(offset, link(values, valOffset, len));
    }

    void insert(int offset, TIntLinkedList tmp) {
        TIntLink l = getLinkAt(offset);
        this.size += tmp.size;
        if (l == this.head) {
            tmp.tail.setNext(this.head);
            this.head.setPrevious(tmp.tail);
            this.head = tmp.head;
        } else if (no(l)) {
            if (this.size == 0) {
                this.head = tmp.head;
                this.tail = tmp.tail;
                return;
            }
            this.tail.setNext(tmp.head);
            tmp.head.setPrevious(this.tail);
            this.tail = tmp.tail;
        } else {
            TIntLink prev = l.getPrevious();
            l.getPrevious().setNext(tmp.head);
            tmp.tail.setNext(l);
            l.setPrevious(tmp.tail);
            tmp.head.setPrevious(prev);
        }
    }

    static TIntLinkedList link(int[] values, int valOffset, int len) {
        TIntLinkedList ret = new TIntLinkedList();
        for (int i = 0; i < len; i++) {
            ret.add(values[valOffset + i]);
        }
        return ret;
    }

    @Override // gnu.trove.list.TIntList
    public int get(int offset) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TIntLink l = getLinkAt(offset);
        if (no(l)) {
            return this.no_entry_value;
        }
        return l.getValue();
    }

    public TIntLink getLinkAt(int offset) {
        if (offset >= size()) {
            return null;
        }
        if (offset <= (size() >>> 1)) {
            return getLink(this.head, 0, offset, true);
        }
        return getLink(this.tail, size() - 1, offset, false);
    }

    private static TIntLink getLink(TIntLink l, int idx, int offset) {
        return getLink(l, idx, offset, true);
    }

    private static TIntLink getLink(TIntLink l, int idx, int offset, boolean next) {
        int i = idx;
        while (got(l)) {
            if (i == offset) {
                return l;
            }
            i += next ? 1 : -1;
            l = next ? l.getNext() : l.getPrevious();
        }
        return null;
    }

    @Override // gnu.trove.list.TIntList
    public int set(int offset, int val) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TIntLink l = getLinkAt(offset);
        if (no(l)) {
            throw new IndexOutOfBoundsException("at offset " + offset);
        }
        int prev = l.getValue();
        l.setValue(val);
        return prev;
    }

    @Override // gnu.trove.list.TIntList
    public void set(int offset, int[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TIntList
    public void set(int offset, int[] values, int valOffset, int length) {
        for (int i = 0; i < length; i++) {
            int value = values[valOffset + i];
            set(offset + i, value);
        }
    }

    @Override // gnu.trove.list.TIntList
    public int replace(int offset, int val) {
        return set(offset, val);
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public void clear() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean remove(int value) {
        boolean changed = false;
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (l.getValue() == value) {
                    changed = true;
                    removeLink(l);
                }
                tIntLink = l.getNext();
            } else {
                return changed;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLink(TIntLink l) {
        if (no(l)) {
            return;
        }
        this.size--;
        TIntLink prev = l.getPrevious();
        TIntLink next = l.getNext();
        if (got(prev)) {
            prev.setNext(next);
        } else {
            this.head = next;
        }
        if (got(next)) {
            next.setPrevious(prev);
        } else {
            this.tail = prev;
        }
        l.setNext(null);
        l.setPrevious(null);
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty()) {
            return false;
        }
        for (Object o : collection) {
            if (o instanceof Integer) {
                Integer i = (Integer) o;
                if (!contains(i.intValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(TIntCollection collection) {
        if (isEmpty()) {
            return false;
        }
        TIntIterator it = collection.iterator();
        while (it.hasNext()) {
            int i = it.next();
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(int[] array) {
        if (isEmpty()) {
            return false;
        }
        for (int i : array) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(Collection<? extends Integer> collection) {
        boolean ret = false;
        for (Integer v : collection) {
            if (add(v.intValue())) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(TIntCollection collection) {
        boolean ret = false;
        TIntIterator it = collection.iterator();
        while (it.hasNext()) {
            int i = it.next();
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(int[] array) {
        boolean ret = false;
        for (int i : array) {
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        TIntIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(Integer.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(TIntCollection collection) {
        boolean modified = false;
        TIntIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TIntCollection
    public boolean retainAll(int[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TIntIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) < 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        TIntIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(Integer.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(TIntCollection collection) {
        boolean modified = false;
        TIntIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(int[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TIntIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) >= 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.list.TIntList
    public int removeAt(int offset) {
        TIntLink l = getLinkAt(offset);
        if (no(l)) {
            throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
        }
        int prev = l.getValue();
        removeLink(l);
        return prev;
    }

    @Override // gnu.trove.list.TIntList
    public void remove(int offset, int length) {
        for (int i = 0; i < length; i++) {
            removeAt(offset);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void transformValues(TIntFunction function) {
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                l.setValue(function.execute(l.getValue()));
                tIntLink = l.getNext();
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public void reverse() {
        TIntLink h = this.head;
        TIntLink t = this.tail;
        TIntLink l = this.head;
        while (got(l)) {
            TIntLink next = l.getNext();
            TIntLink prev = l.getPrevious();
            TIntLink tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        this.head = t;
        this.tail = h;
    }

    @Override // gnu.trove.list.TIntList
    public void reverse(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to : " + from + ">" + to);
        }
        TIntLink start = getLinkAt(from);
        TIntLink stop = getLinkAt(to);
        TIntLink tmp = null;
        TIntLink tmpHead = start.getPrevious();
        TIntLink l = start;
        while (l != stop) {
            TIntLink next = l.getNext();
            TIntLink prev = l.getPrevious();
            tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        if (got(tmp)) {
            tmpHead.setNext(tmp);
            stop.setPrevious(tmpHead);
        }
        start.setNext(stop);
        stop.setPrevious(start);
    }

    @Override // gnu.trove.list.TIntList
    public void shuffle(Random rand) {
        for (int i = 0; i < this.size; i++) {
            TIntLink l = getLinkAt(rand.nextInt(size()));
            removeLink(l);
            add(l.getValue());
        }
    }

    @Override // gnu.trove.list.TIntList
    public TIntList subList(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("begin index " + begin + " greater than end index " + end);
        }
        if (this.size < begin) {
            throw new IllegalArgumentException("begin index " + begin + " greater than last index " + this.size);
        }
        if (begin < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        if (end > this.size) {
            throw new IndexOutOfBoundsException("end index < " + this.size);
        }
        TIntLinkedList ret = new TIntLinkedList();
        TIntLink tmp = getLinkAt(begin);
        for (int i = begin; i < end; i++) {
            ret.add(tmp.getValue());
            tmp = tmp.getNext();
        }
        return ret;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int[] toArray() {
        return toArray(new int[this.size], 0, this.size);
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int offset, int len) {
        return toArray(new int[len], offset, 0, len);
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int[] toArray(int[] dest) {
        return toArray(dest, 0, this.size);
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int[] dest, int offset, int len) {
        return toArray(dest, offset, 0, len);
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= size()) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        TIntLink tmp = getLinkAt(source_pos);
        for (int i = 0; i < len; i++) {
            dest[dest_pos + i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return dest;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean forEach(TIntProcedure procedure) {
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tIntLink = l.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public boolean forEachDescending(TIntProcedure procedure) {
        TIntLink tIntLink = this.tail;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tIntLink = l.getPrevious();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public void sort() {
        sort(0, this.size);
    }

    @Override // gnu.trove.list.TIntList
    public void sort(int fromIndex, int toIndex) {
        TIntList tmp = subList(fromIndex, toIndex);
        int[] vals = tmp.toArray();
        Arrays.sort(vals);
        set(fromIndex, vals);
    }

    @Override // gnu.trove.list.TIntList
    public void fill(int val) {
        fill(0, this.size, val);
    }

    @Override // gnu.trove.list.TIntList
    public void fill(int fromIndex, int toIndex, int val) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        TIntLink l = getLinkAt(fromIndex);
        if (toIndex > this.size) {
            for (int i = fromIndex; i < this.size; i++) {
                l.setValue(val);
                l = l.getNext();
            }
            for (int i2 = this.size; i2 < toIndex; i2++) {
                add(val);
            }
            return;
        }
        for (int i3 = fromIndex; i3 < toIndex; i3++) {
            l.setValue(val);
            l = l.getNext();
        }
    }

    @Override // gnu.trove.list.TIntList
    public int binarySearch(int value) {
        return binarySearch(value, 0, size());
    }

    @Override // gnu.trove.list.TIntList
    public int binarySearch(int value, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        if (toIndex > this.size) {
            throw new IndexOutOfBoundsException("end index > size: " + toIndex + " > " + this.size);
        }
        if (toIndex < fromIndex) {
            return -(fromIndex + 1);
        }
        int from = fromIndex;
        TIntLink fromLink = getLinkAt(fromIndex);
        int to = toIndex;
        while (from < to) {
            int mid = (from + to) >>> 1;
            TIntLink middle = getLink(fromLink, from, mid);
            if (middle.getValue() == value) {
                return mid;
            }
            if (middle.getValue() < value) {
                from = mid + 1;
                fromLink = middle.next;
            } else {
                to = mid - 1;
            }
        }
        return -(from + 1);
    }

    @Override // gnu.trove.list.TIntList
    public int indexOf(int value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TIntList
    public int indexOf(int offset, int value) {
        int count = offset;
        TIntLink linkAt = getLinkAt(offset);
        while (true) {
            TIntLink l = linkAt;
            if (got(l.getNext())) {
                if (l.getValue() == value) {
                    return count;
                }
                count++;
                linkAt = l.getNext();
            } else {
                return -1;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public int lastIndexOf(int value) {
        return lastIndexOf(0, value);
    }

    @Override // gnu.trove.list.TIntList
    public int lastIndexOf(int offset, int value) {
        if (isEmpty()) {
            return -1;
        }
        int last = -1;
        int count = offset;
        TIntLink linkAt = getLinkAt(offset);
        while (true) {
            TIntLink l = linkAt;
            if (got(l.getNext())) {
                if (l.getValue() == value) {
                    last = count;
                }
                count++;
                linkAt = l.getNext();
            } else {
                return last;
            }
        }
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean contains(int value) {
        if (isEmpty()) {
            return false;
        }
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (l.getValue() != value) {
                    tIntLink = l.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.TIntCollection
    public TIntIterator iterator() {
        return new TIntIterator() { // from class: gnu.trove.list.linked.TIntLinkedList.1
            TIntLink l;
            TIntLink current;

            {
                this.l = TIntLinkedList.this.head;
            }

            @Override // gnu.trove.iterator.TIntIterator
            public int next() {
                if (TIntLinkedList.no(this.l)) {
                    throw new NoSuchElementException();
                }
                int ret = this.l.getValue();
                this.current = this.l;
                this.l = this.l.getNext();
                return ret;
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return TIntLinkedList.got(this.l);
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this.current != null) {
                    TIntLinkedList.this.removeLink(this.current);
                    this.current = null;
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }

    @Override // gnu.trove.list.TIntList
    public TIntList grep(TIntProcedure condition) {
        TIntList ret = new TIntLinkedList();
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tIntLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public TIntList inverseGrep(TIntProcedure condition) {
        TIntList ret = new TIntLinkedList();
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (!condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tIntLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public int max() {
        int ret = Integer.MIN_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (ret < l.getValue()) {
                    ret = l.getValue();
                }
                tIntLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public int min() {
        int ret = Integer.MAX_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                if (ret > l.getValue()) {
                    ret = l.getValue();
                }
                tIntLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TIntList
    public int sum() {
        int sum = 0;
        TIntLink tIntLink = this.head;
        while (true) {
            TIntLink l = tIntLink;
            if (got(l)) {
                sum += l.getValue();
                tIntLink = l.getNext();
            } else {
                return sum;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TIntLinkedList$TIntLink.class */
    public static class TIntLink {
        int value;
        TIntLink previous;
        TIntLink next;

        TIntLink(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public TIntLink getPrevious() {
            return this.previous;
        }

        public void setPrevious(TIntLink previous) {
            this.previous = previous;
        }

        public TIntLink getNext() {
            return this.next;
        }

        public void setNext(TIntLink next) {
            this.next = next;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TIntLinkedList$RemoveProcedure.class */
    class RemoveProcedure implements TIntProcedure {
        boolean changed = false;

        RemoveProcedure() {
        }

        @Override // gnu.trove.procedure.TIntProcedure
        public boolean execute(int value) {
            if (TIntLinkedList.this.remove(value)) {
                this.changed = true;
                return true;
            }
            return true;
        }

        public boolean isChanged() {
            return this.changed;
        }
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeInt(this.no_entry_value);
        out.writeInt(this.size);
        TIntIterator iterator = iterator();
        while (iterator.hasNext()) {
            int next = iterator.next();
            out.writeInt(next);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.no_entry_value = in.readInt();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            add(in.readInt());
        }
    }

    static boolean got(Object ref) {
        return ref != null;
    }

    static boolean no(Object ref) {
        return ref == null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    @Override // gnu.trove.TIntCollection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            if (r0 != r1) goto L7
            r0 = 1
            return r0
        L7:
            r0 = r4
            if (r0 == 0) goto L16
            r0 = r3
            java.lang.Class r0 = r0.getClass()
            r1 = r4
            java.lang.Class r1 = r1.getClass()
            if (r0 == r1) goto L18
        L16:
            r0 = 0
            return r0
        L18:
            r0 = r4
            gnu.trove.list.linked.TIntLinkedList r0 = (gnu.trove.list.linked.TIntLinkedList) r0
            r5 = r0
            r0 = r3
            int r0 = r0.no_entry_value
            r1 = r5
            int r1 = r1.no_entry_value
            if (r0 == r1) goto L2a
            r0 = 0
            return r0
        L2a:
            r0 = r3
            int r0 = r0.size
            r1 = r5
            int r1 = r1.size
            if (r0 == r1) goto L37
            r0 = 0
            return r0
        L37:
            r0 = r3
            gnu.trove.iterator.TIntIterator r0 = r0.iterator()
            r6 = r0
            r0 = r5
            gnu.trove.iterator.TIntIterator r0 = r0.iterator()
            r7 = r0
        L42:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L69
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L57
            r0 = 0
            return r0
        L57:
            r0 = r6
            int r0 = r0.next()
            r1 = r7
            int r1 = r1.next()
            if (r0 == r1) goto L42
            r0 = 0
            return r0
        L69:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.list.linked.TIntLinkedList.equals(java.lang.Object):boolean");
    }

    @Override // gnu.trove.TIntCollection
    public int hashCode() {
        int result = HashFunctions.hash(this.no_entry_value);
        int result2 = (31 * result) + this.size;
        TIntIterator iterator = iterator();
        while (iterator.hasNext()) {
            result2 = (31 * result2) + HashFunctions.hash(iterator.next());
        }
        return result2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        TIntIterator it = iterator();
        while (it.hasNext()) {
            int next = it.next();
            buf.append(next);
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
