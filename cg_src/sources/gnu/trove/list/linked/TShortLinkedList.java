package gnu.trove.list.linked;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.list.TShortList;
import gnu.trove.procedure.TShortProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TShortLinkedList.class */
public class TShortLinkedList implements TShortList, Externalizable {
    short no_entry_value;
    int size;
    TShortLink head = null;
    TShortLink tail = this.head;

    public TShortLinkedList() {
    }

    public TShortLinkedList(short no_entry_value) {
        this.no_entry_value = no_entry_value;
    }

    public TShortLinkedList(TShortList list) {
        this.no_entry_value = list.getNoEntryValue();
        TShortIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            short next = iterator.next();
            add(next);
        }
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public short getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public int size() {
        return this.size;
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public boolean add(short val) {
        TShortLink l = new TShortLink(val);
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

    @Override // gnu.trove.list.TShortList
    public void add(short[] vals) {
        for (short val : vals) {
            add(val);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void add(short[] vals, int offset, int length) {
        for (int i = 0; i < length; i++) {
            short val = vals[offset + i];
            add(val);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short value) {
        TShortLinkedList tmp = new TShortLinkedList();
        tmp.add(value);
        insert(offset, tmp);
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short[] values) {
        insert(offset, link(values, 0, values.length));
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short[] values, int valOffset, int len) {
        insert(offset, link(values, valOffset, len));
    }

    void insert(int offset, TShortLinkedList tmp) {
        TShortLink l = getLinkAt(offset);
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
            TShortLink prev = l.getPrevious();
            l.getPrevious().setNext(tmp.head);
            tmp.tail.setNext(l);
            l.setPrevious(tmp.tail);
            tmp.head.setPrevious(prev);
        }
    }

    static TShortLinkedList link(short[] values, int valOffset, int len) {
        TShortLinkedList ret = new TShortLinkedList();
        for (int i = 0; i < len; i++) {
            ret.add(values[valOffset + i]);
        }
        return ret;
    }

    @Override // gnu.trove.list.TShortList
    public short get(int offset) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TShortLink l = getLinkAt(offset);
        if (no(l)) {
            return this.no_entry_value;
        }
        return l.getValue();
    }

    public TShortLink getLinkAt(int offset) {
        if (offset >= size()) {
            return null;
        }
        if (offset <= (size() >>> 1)) {
            return getLink(this.head, 0, offset, true);
        }
        return getLink(this.tail, size() - 1, offset, false);
    }

    private static TShortLink getLink(TShortLink l, int idx, int offset) {
        return getLink(l, idx, offset, true);
    }

    private static TShortLink getLink(TShortLink l, int idx, int offset, boolean next) {
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

    @Override // gnu.trove.list.TShortList
    public short set(int offset, short val) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TShortLink l = getLinkAt(offset);
        if (no(l)) {
            throw new IndexOutOfBoundsException("at offset " + offset);
        }
        short prev = l.getValue();
        l.setValue(val);
        return prev;
    }

    @Override // gnu.trove.list.TShortList
    public void set(int offset, short[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TShortList
    public void set(int offset, short[] values, int valOffset, int length) {
        for (int i = 0; i < length; i++) {
            short value = values[valOffset + i];
            set(offset + i, value);
        }
    }

    @Override // gnu.trove.list.TShortList
    public short replace(int offset, short val) {
        return set(offset, val);
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public void clear() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public boolean remove(short value) {
        boolean changed = false;
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (l.getValue() == value) {
                    changed = true;
                    removeLink(l);
                }
                tShortLink = l.getNext();
            } else {
                return changed;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLink(TShortLink l) {
        if (no(l)) {
            return;
        }
        this.size--;
        TShortLink prev = l.getPrevious();
        TShortLink next = l.getNext();
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

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty()) {
            return false;
        }
        for (Object o : collection) {
            if (o instanceof Short) {
                Short i = (Short) o;
                if (!contains(i.shortValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(TShortCollection collection) {
        if (isEmpty()) {
            return false;
        }
        TShortIterator it = collection.iterator();
        while (it.hasNext()) {
            short i = it.next();
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TShortCollection
    public boolean containsAll(short[] array) {
        if (isEmpty()) {
            return false;
        }
        for (short i : array) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(Collection<? extends Short> collection) {
        boolean ret = false;
        for (Short v : collection) {
            if (add(v.shortValue())) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(TShortCollection collection) {
        boolean ret = false;
        TShortIterator it = collection.iterator();
        while (it.hasNext()) {
            short i = it.next();
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TShortCollection
    public boolean addAll(short[] array) {
        boolean ret = false;
        for (short i : array) {
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        TShortIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(Short.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(TShortCollection collection) {
        boolean modified = false;
        TShortIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TShortCollection
    public boolean retainAll(short[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TShortIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) < 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        TShortIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(Short.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(TShortCollection collection) {
        boolean modified = false;
        TShortIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TShortCollection
    public boolean removeAll(short[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TShortIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) >= 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.list.TShortList
    public short removeAt(int offset) {
        TShortLink l = getLinkAt(offset);
        if (no(l)) {
            throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
        }
        short prev = l.getValue();
        removeLink(l);
        return prev;
    }

    @Override // gnu.trove.list.TShortList
    public void remove(int offset, int length) {
        for (int i = 0; i < length; i++) {
            removeAt(offset);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void transformValues(TShortFunction function) {
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                l.setValue(function.execute(l.getValue()));
                tShortLink = l.getNext();
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TShortList
    public void reverse() {
        TShortLink h = this.head;
        TShortLink t = this.tail;
        TShortLink l = this.head;
        while (got(l)) {
            TShortLink next = l.getNext();
            TShortLink prev = l.getPrevious();
            TShortLink tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        this.head = t;
        this.tail = h;
    }

    @Override // gnu.trove.list.TShortList
    public void reverse(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to : " + from + ">" + to);
        }
        TShortLink start = getLinkAt(from);
        TShortLink stop = getLinkAt(to);
        TShortLink tmp = null;
        TShortLink tmpHead = start.getPrevious();
        TShortLink l = start;
        while (l != stop) {
            TShortLink next = l.getNext();
            TShortLink prev = l.getPrevious();
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

    @Override // gnu.trove.list.TShortList
    public void shuffle(Random rand) {
        for (int i = 0; i < this.size; i++) {
            TShortLink l = getLinkAt(rand.nextInt(size()));
            removeLink(l);
            add(l.getValue());
        }
    }

    @Override // gnu.trove.list.TShortList
    public TShortList subList(int begin, int end) {
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
        TShortLinkedList ret = new TShortLinkedList();
        TShortLink tmp = getLinkAt(begin);
        for (int i = begin; i < end; i++) {
            ret.add(tmp.getValue());
            tmp = tmp.getNext();
        }
        return ret;
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public short[] toArray() {
        return toArray(new short[this.size], 0, this.size);
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(int offset, int len) {
        return toArray(new short[len], offset, 0, len);
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public short[] toArray(short[] dest) {
        return toArray(dest, 0, this.size);
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(short[] dest, int offset, int len) {
        return toArray(dest, offset, 0, len);
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(short[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= size()) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        TShortLink tmp = getLinkAt(source_pos);
        for (int i = 0; i < len; i++) {
            dest[dest_pos + i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return dest;
    }

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public boolean forEach(TShortProcedure procedure) {
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tShortLink = l.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TShortList
    public boolean forEachDescending(TShortProcedure procedure) {
        TShortLink tShortLink = this.tail;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tShortLink = l.getPrevious();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TShortList
    public void sort() {
        sort(0, this.size);
    }

    @Override // gnu.trove.list.TShortList
    public void sort(int fromIndex, int toIndex) {
        TShortList tmp = subList(fromIndex, toIndex);
        short[] vals = tmp.toArray();
        Arrays.sort(vals);
        set(fromIndex, vals);
    }

    @Override // gnu.trove.list.TShortList
    public void fill(short val) {
        fill(0, this.size, val);
    }

    @Override // gnu.trove.list.TShortList
    public void fill(int fromIndex, int toIndex, short val) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        TShortLink l = getLinkAt(fromIndex);
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

    @Override // gnu.trove.list.TShortList
    public int binarySearch(short value) {
        return binarySearch(value, 0, size());
    }

    @Override // gnu.trove.list.TShortList
    public int binarySearch(short value, int fromIndex, int toIndex) {
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
        TShortLink fromLink = getLinkAt(fromIndex);
        int to = toIndex;
        while (from < to) {
            int mid = (from + to) >>> 1;
            TShortLink middle = getLink(fromLink, from, mid);
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

    @Override // gnu.trove.list.TShortList
    public int indexOf(short value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TShortList
    public int indexOf(int offset, short value) {
        int count = offset;
        TShortLink linkAt = getLinkAt(offset);
        while (true) {
            TShortLink l = linkAt;
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

    @Override // gnu.trove.list.TShortList
    public int lastIndexOf(short value) {
        return lastIndexOf(0, value);
    }

    @Override // gnu.trove.list.TShortList
    public int lastIndexOf(int offset, short value) {
        if (isEmpty()) {
            return -1;
        }
        int last = -1;
        int count = offset;
        TShortLink linkAt = getLinkAt(offset);
        while (true) {
            TShortLink l = linkAt;
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

    @Override // gnu.trove.list.TShortList, gnu.trove.TShortCollection
    public boolean contains(short value) {
        if (isEmpty()) {
            return false;
        }
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (l.getValue() != value) {
                    tShortLink = l.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.TShortCollection
    public TShortIterator iterator() {
        return new TShortIterator() { // from class: gnu.trove.list.linked.TShortLinkedList.1
            TShortLink l;
            TShortLink current;

            {
                this.l = TShortLinkedList.this.head;
            }

            @Override // gnu.trove.iterator.TShortIterator
            public short next() {
                if (TShortLinkedList.no(this.l)) {
                    throw new NoSuchElementException();
                }
                short ret = this.l.getValue();
                this.current = this.l;
                this.l = this.l.getNext();
                return ret;
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return TShortLinkedList.got(this.l);
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this.current != null) {
                    TShortLinkedList.this.removeLink(this.current);
                    this.current = null;
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }

    @Override // gnu.trove.list.TShortList
    public TShortList grep(TShortProcedure condition) {
        TShortList ret = new TShortLinkedList();
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tShortLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TShortList
    public TShortList inverseGrep(TShortProcedure condition) {
        TShortList ret = new TShortLinkedList();
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (!condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tShortLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TShortList
    public short max() {
        short ret = Short.MIN_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (ret < l.getValue()) {
                    ret = l.getValue();
                }
                tShortLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TShortList
    public short min() {
        short ret = Short.MAX_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                if (ret > l.getValue()) {
                    ret = l.getValue();
                }
                tShortLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TShortList
    public short sum() {
        short sum = 0;
        TShortLink tShortLink = this.head;
        while (true) {
            TShortLink l = tShortLink;
            if (got(l)) {
                sum = (short) (sum + l.getValue());
                tShortLink = l.getNext();
            } else {
                return sum;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TShortLinkedList$TShortLink.class */
    public static class TShortLink {
        short value;
        TShortLink previous;
        TShortLink next;

        TShortLink(short value) {
            this.value = value;
        }

        public short getValue() {
            return this.value;
        }

        public void setValue(short value) {
            this.value = value;
        }

        public TShortLink getPrevious() {
            return this.previous;
        }

        public void setPrevious(TShortLink previous) {
            this.previous = previous;
        }

        public TShortLink getNext() {
            return this.next;
        }

        public void setNext(TShortLink next) {
            this.next = next;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TShortLinkedList$RemoveProcedure.class */
    class RemoveProcedure implements TShortProcedure {
        boolean changed = false;

        RemoveProcedure() {
        }

        @Override // gnu.trove.procedure.TShortProcedure
        public boolean execute(short value) {
            if (TShortLinkedList.this.remove(value)) {
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
        out.writeShort(this.no_entry_value);
        out.writeInt(this.size);
        TShortIterator iterator = iterator();
        while (iterator.hasNext()) {
            short next = iterator.next();
            out.writeShort(next);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.no_entry_value = in.readShort();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            add(in.readShort());
        }
    }

    static boolean got(Object ref) {
        return ref != null;
    }

    static boolean no(Object ref) {
        return ref == null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    @Override // gnu.trove.TShortCollection
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
            gnu.trove.list.linked.TShortLinkedList r0 = (gnu.trove.list.linked.TShortLinkedList) r0
            r5 = r0
            r0 = r3
            short r0 = r0.no_entry_value
            r1 = r5
            short r1 = r1.no_entry_value
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
            gnu.trove.iterator.TShortIterator r0 = r0.iterator()
            r6 = r0
            r0 = r5
            gnu.trove.iterator.TShortIterator r0 = r0.iterator()
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
            short r0 = r0.next()
            r1 = r7
            short r1 = r1.next()
            if (r0 == r1) goto L42
            r0 = 0
            return r0
        L69:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.list.linked.TShortLinkedList.equals(java.lang.Object):boolean");
    }

    @Override // gnu.trove.TShortCollection
    public int hashCode() {
        int result = HashFunctions.hash((int) this.no_entry_value);
        int result2 = (31 * result) + this.size;
        TShortIterator iterator = iterator();
        while (iterator.hasNext()) {
            result2 = (31 * result2) + HashFunctions.hash((int) iterator.next());
        }
        return result2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        TShortIterator it = iterator();
        while (it.hasNext()) {
            short next = it.next();
            buf.append((int) next);
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
