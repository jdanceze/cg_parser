package gnu.trove.list.linked;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.TLongList;
import gnu.trove.procedure.TLongProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TLongLinkedList.class */
public class TLongLinkedList implements TLongList, Externalizable {
    long no_entry_value;
    int size;
    TLongLink head = null;
    TLongLink tail = this.head;

    public TLongLinkedList() {
    }

    public TLongLinkedList(long no_entry_value) {
        this.no_entry_value = no_entry_value;
    }

    public TLongLinkedList(TLongList list) {
        this.no_entry_value = list.getNoEntryValue();
        TLongIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            long next = iterator.next();
            add(next);
        }
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public long getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public int size() {
        return this.size;
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public boolean add(long val) {
        TLongLink l = new TLongLink(val);
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

    @Override // gnu.trove.list.TLongList
    public void add(long[] vals) {
        for (long val : vals) {
            add(val);
        }
    }

    @Override // gnu.trove.list.TLongList
    public void add(long[] vals, int offset, int length) {
        for (int i = 0; i < length; i++) {
            long val = vals[offset + i];
            add(val);
        }
    }

    @Override // gnu.trove.list.TLongList
    public void insert(int offset, long value) {
        TLongLinkedList tmp = new TLongLinkedList();
        tmp.add(value);
        insert(offset, tmp);
    }

    @Override // gnu.trove.list.TLongList
    public void insert(int offset, long[] values) {
        insert(offset, link(values, 0, values.length));
    }

    @Override // gnu.trove.list.TLongList
    public void insert(int offset, long[] values, int valOffset, int len) {
        insert(offset, link(values, valOffset, len));
    }

    void insert(int offset, TLongLinkedList tmp) {
        TLongLink l = getLinkAt(offset);
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
            TLongLink prev = l.getPrevious();
            l.getPrevious().setNext(tmp.head);
            tmp.tail.setNext(l);
            l.setPrevious(tmp.tail);
            tmp.head.setPrevious(prev);
        }
    }

    static TLongLinkedList link(long[] values, int valOffset, int len) {
        TLongLinkedList ret = new TLongLinkedList();
        for (int i = 0; i < len; i++) {
            ret.add(values[valOffset + i]);
        }
        return ret;
    }

    @Override // gnu.trove.list.TLongList
    public long get(int offset) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TLongLink l = getLinkAt(offset);
        if (no(l)) {
            return this.no_entry_value;
        }
        return l.getValue();
    }

    public TLongLink getLinkAt(int offset) {
        if (offset >= size()) {
            return null;
        }
        if (offset <= (size() >>> 1)) {
            return getLink(this.head, 0, offset, true);
        }
        return getLink(this.tail, size() - 1, offset, false);
    }

    private static TLongLink getLink(TLongLink l, int idx, int offset) {
        return getLink(l, idx, offset, true);
    }

    private static TLongLink getLink(TLongLink l, int idx, int offset, boolean next) {
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

    @Override // gnu.trove.list.TLongList
    public long set(int offset, long val) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TLongLink l = getLinkAt(offset);
        if (no(l)) {
            throw new IndexOutOfBoundsException("at offset " + offset);
        }
        long prev = l.getValue();
        l.setValue(val);
        return prev;
    }

    @Override // gnu.trove.list.TLongList
    public void set(int offset, long[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TLongList
    public void set(int offset, long[] values, int valOffset, int length) {
        for (int i = 0; i < length; i++) {
            long value = values[valOffset + i];
            set(offset + i, value);
        }
    }

    @Override // gnu.trove.list.TLongList
    public long replace(int offset, long val) {
        return set(offset, val);
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public void clear() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public boolean remove(long value) {
        boolean changed = false;
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (l.getValue() == value) {
                    changed = true;
                    removeLink(l);
                }
                tLongLink = l.getNext();
            } else {
                return changed;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLink(TLongLink l) {
        if (no(l)) {
            return;
        }
        this.size--;
        TLongLink prev = l.getPrevious();
        TLongLink next = l.getNext();
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

    @Override // gnu.trove.TLongCollection
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty()) {
            return false;
        }
        for (Object o : collection) {
            if (o instanceof Long) {
                Long i = (Long) o;
                if (!contains(i.longValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TLongCollection
    public boolean containsAll(TLongCollection collection) {
        if (isEmpty()) {
            return false;
        }
        TLongIterator it = collection.iterator();
        while (it.hasNext()) {
            long i = it.next();
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TLongCollection
    public boolean containsAll(long[] array) {
        if (isEmpty()) {
            return false;
        }
        for (long i : array) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TLongCollection
    public boolean addAll(Collection<? extends Long> collection) {
        boolean ret = false;
        for (Long v : collection) {
            if (add(v.longValue())) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TLongCollection
    public boolean addAll(TLongCollection collection) {
        boolean ret = false;
        TLongIterator it = collection.iterator();
        while (it.hasNext()) {
            long i = it.next();
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TLongCollection
    public boolean addAll(long[] array) {
        boolean ret = false;
        for (long i : array) {
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TLongCollection
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        TLongIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(Long.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TLongCollection
    public boolean retainAll(TLongCollection collection) {
        boolean modified = false;
        TLongIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TLongCollection
    public boolean retainAll(long[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TLongIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) < 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TLongCollection
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        TLongIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(Long.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TLongCollection
    public boolean removeAll(TLongCollection collection) {
        boolean modified = false;
        TLongIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TLongCollection
    public boolean removeAll(long[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TLongIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) >= 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.list.TLongList
    public long removeAt(int offset) {
        TLongLink l = getLinkAt(offset);
        if (no(l)) {
            throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
        }
        long prev = l.getValue();
        removeLink(l);
        return prev;
    }

    @Override // gnu.trove.list.TLongList
    public void remove(int offset, int length) {
        for (int i = 0; i < length; i++) {
            removeAt(offset);
        }
    }

    @Override // gnu.trove.list.TLongList
    public void transformValues(TLongFunction function) {
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                l.setValue(function.execute(l.getValue()));
                tLongLink = l.getNext();
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TLongList
    public void reverse() {
        TLongLink h = this.head;
        TLongLink t = this.tail;
        TLongLink l = this.head;
        while (got(l)) {
            TLongLink next = l.getNext();
            TLongLink prev = l.getPrevious();
            TLongLink tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        this.head = t;
        this.tail = h;
    }

    @Override // gnu.trove.list.TLongList
    public void reverse(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to : " + from + ">" + to);
        }
        TLongLink start = getLinkAt(from);
        TLongLink stop = getLinkAt(to);
        TLongLink tmp = null;
        TLongLink tmpHead = start.getPrevious();
        TLongLink l = start;
        while (l != stop) {
            TLongLink next = l.getNext();
            TLongLink prev = l.getPrevious();
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

    @Override // gnu.trove.list.TLongList
    public void shuffle(Random rand) {
        for (int i = 0; i < this.size; i++) {
            TLongLink l = getLinkAt(rand.nextInt(size()));
            removeLink(l);
            add(l.getValue());
        }
    }

    @Override // gnu.trove.list.TLongList
    public TLongList subList(int begin, int end) {
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
        TLongLinkedList ret = new TLongLinkedList();
        TLongLink tmp = getLinkAt(begin);
        for (int i = begin; i < end; i++) {
            ret.add(tmp.getValue());
            tmp = tmp.getNext();
        }
        return ret;
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public long[] toArray() {
        return toArray(new long[this.size], 0, this.size);
    }

    @Override // gnu.trove.list.TLongList
    public long[] toArray(int offset, int len) {
        return toArray(new long[len], offset, 0, len);
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public long[] toArray(long[] dest) {
        return toArray(dest, 0, this.size);
    }

    @Override // gnu.trove.list.TLongList
    public long[] toArray(long[] dest, int offset, int len) {
        return toArray(dest, offset, 0, len);
    }

    @Override // gnu.trove.list.TLongList
    public long[] toArray(long[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= size()) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        TLongLink tmp = getLinkAt(source_pos);
        for (int i = 0; i < len; i++) {
            dest[dest_pos + i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return dest;
    }

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public boolean forEach(TLongProcedure procedure) {
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tLongLink = l.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TLongList
    public boolean forEachDescending(TLongProcedure procedure) {
        TLongLink tLongLink = this.tail;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tLongLink = l.getPrevious();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TLongList
    public void sort() {
        sort(0, this.size);
    }

    @Override // gnu.trove.list.TLongList
    public void sort(int fromIndex, int toIndex) {
        TLongList tmp = subList(fromIndex, toIndex);
        long[] vals = tmp.toArray();
        Arrays.sort(vals);
        set(fromIndex, vals);
    }

    @Override // gnu.trove.list.TLongList
    public void fill(long val) {
        fill(0, this.size, val);
    }

    @Override // gnu.trove.list.TLongList
    public void fill(int fromIndex, int toIndex, long val) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        TLongLink l = getLinkAt(fromIndex);
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

    @Override // gnu.trove.list.TLongList
    public int binarySearch(long value) {
        return binarySearch(value, 0, size());
    }

    @Override // gnu.trove.list.TLongList
    public int binarySearch(long value, int fromIndex, int toIndex) {
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
        TLongLink fromLink = getLinkAt(fromIndex);
        int to = toIndex;
        while (from < to) {
            int mid = (from + to) >>> 1;
            TLongLink middle = getLink(fromLink, from, mid);
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

    @Override // gnu.trove.list.TLongList
    public int indexOf(long value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TLongList
    public int indexOf(int offset, long value) {
        int count = offset;
        TLongLink linkAt = getLinkAt(offset);
        while (true) {
            TLongLink l = linkAt;
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

    @Override // gnu.trove.list.TLongList
    public int lastIndexOf(long value) {
        return lastIndexOf(0, value);
    }

    @Override // gnu.trove.list.TLongList
    public int lastIndexOf(int offset, long value) {
        if (isEmpty()) {
            return -1;
        }
        int last = -1;
        int count = offset;
        TLongLink linkAt = getLinkAt(offset);
        while (true) {
            TLongLink l = linkAt;
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

    @Override // gnu.trove.list.TLongList, gnu.trove.TLongCollection
    public boolean contains(long value) {
        if (isEmpty()) {
            return false;
        }
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (l.getValue() != value) {
                    tLongLink = l.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.TLongCollection
    public TLongIterator iterator() {
        return new TLongIterator() { // from class: gnu.trove.list.linked.TLongLinkedList.1
            TLongLink l;
            TLongLink current;

            {
                this.l = TLongLinkedList.this.head;
            }

            @Override // gnu.trove.iterator.TLongIterator
            public long next() {
                if (TLongLinkedList.no(this.l)) {
                    throw new NoSuchElementException();
                }
                long ret = this.l.getValue();
                this.current = this.l;
                this.l = this.l.getNext();
                return ret;
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return TLongLinkedList.got(this.l);
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this.current != null) {
                    TLongLinkedList.this.removeLink(this.current);
                    this.current = null;
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }

    @Override // gnu.trove.list.TLongList
    public TLongList grep(TLongProcedure condition) {
        TLongList ret = new TLongLinkedList();
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tLongLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TLongList
    public TLongList inverseGrep(TLongProcedure condition) {
        TLongList ret = new TLongLinkedList();
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (!condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tLongLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TLongList
    public long max() {
        long ret = Long.MIN_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (ret < l.getValue()) {
                    ret = l.getValue();
                }
                tLongLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TLongList
    public long min() {
        long ret = Long.MAX_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                if (ret > l.getValue()) {
                    ret = l.getValue();
                }
                tLongLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TLongList
    public long sum() {
        long sum = 0;
        TLongLink tLongLink = this.head;
        while (true) {
            TLongLink l = tLongLink;
            if (got(l)) {
                sum += l.getValue();
                tLongLink = l.getNext();
            } else {
                return sum;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TLongLinkedList$TLongLink.class */
    public static class TLongLink {
        long value;
        TLongLink previous;
        TLongLink next;

        TLongLink(long value) {
            this.value = value;
        }

        public long getValue() {
            return this.value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public TLongLink getPrevious() {
            return this.previous;
        }

        public void setPrevious(TLongLink previous) {
            this.previous = previous;
        }

        public TLongLink getNext() {
            return this.next;
        }

        public void setNext(TLongLink next) {
            this.next = next;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TLongLinkedList$RemoveProcedure.class */
    class RemoveProcedure implements TLongProcedure {
        boolean changed = false;

        RemoveProcedure() {
        }

        @Override // gnu.trove.procedure.TLongProcedure
        public boolean execute(long value) {
            if (TLongLinkedList.this.remove(value)) {
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
        out.writeLong(this.no_entry_value);
        out.writeInt(this.size);
        TLongIterator iterator = iterator();
        while (iterator.hasNext()) {
            long next = iterator.next();
            out.writeLong(next);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.no_entry_value = in.readLong();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            add(in.readLong());
        }
    }

    static boolean got(Object ref) {
        return ref != null;
    }

    static boolean no(Object ref) {
        return ref == null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004c  */
    @Override // gnu.trove.TLongCollection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(java.lang.Object r6) {
        /*
            r5 = this;
            r0 = r5
            r1 = r6
            if (r0 != r1) goto L7
            r0 = 1
            return r0
        L7:
            r0 = r6
            if (r0 == 0) goto L16
            r0 = r5
            java.lang.Class r0 = r0.getClass()
            r1 = r6
            java.lang.Class r1 = r1.getClass()
            if (r0 == r1) goto L18
        L16:
            r0 = 0
            return r0
        L18:
            r0 = r6
            gnu.trove.list.linked.TLongLinkedList r0 = (gnu.trove.list.linked.TLongLinkedList) r0
            r7 = r0
            r0 = r5
            long r0 = r0.no_entry_value
            r1 = r7
            long r1 = r1.no_entry_value
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L2b
            r0 = 0
            return r0
        L2b:
            r0 = r5
            int r0 = r0.size
            r1 = r7
            int r1 = r1.size
            if (r0 == r1) goto L38
            r0 = 0
            return r0
        L38:
            r0 = r5
            gnu.trove.iterator.TLongIterator r0 = r0.iterator()
            r8 = r0
            r0 = r7
            gnu.trove.iterator.TLongIterator r0 = r0.iterator()
            r9 = r0
        L43:
            r0 = r8
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L6b
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L58
            r0 = 0
            return r0
        L58:
            r0 = r8
            long r0 = r0.next()
            r1 = r9
            long r1 = r1.next()
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L43
            r0 = 0
            return r0
        L6b:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.list.linked.TLongLinkedList.equals(java.lang.Object):boolean");
    }

    @Override // gnu.trove.TLongCollection
    public int hashCode() {
        int result = HashFunctions.hash(this.no_entry_value);
        int result2 = (31 * result) + this.size;
        TLongIterator iterator = iterator();
        while (iterator.hasNext()) {
            result2 = (31 * result2) + HashFunctions.hash(iterator.next());
        }
        return result2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        TLongIterator it = iterator();
        while (it.hasNext()) {
            long next = it.next();
            buf.append(next);
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
