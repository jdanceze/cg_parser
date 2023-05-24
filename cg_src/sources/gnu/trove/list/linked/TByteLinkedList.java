package gnu.trove.list.linked;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.list.TByteList;
import gnu.trove.procedure.TByteProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TByteLinkedList.class */
public class TByteLinkedList implements TByteList, Externalizable {
    byte no_entry_value;
    int size;
    TByteLink head = null;
    TByteLink tail = this.head;

    public TByteLinkedList() {
    }

    public TByteLinkedList(byte no_entry_value) {
        this.no_entry_value = no_entry_value;
    }

    public TByteLinkedList(TByteList list) {
        this.no_entry_value = list.getNoEntryValue();
        TByteIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            byte next = iterator.next();
            add(next);
        }
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public byte getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public int size() {
        return this.size;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean add(byte val) {
        TByteLink l = new TByteLink(val);
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

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals) {
        for (byte val : vals) {
            add(val);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals, int offset, int length) {
        for (int i = 0; i < length; i++) {
            byte val = vals[offset + i];
            add(val);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte value) {
        TByteLinkedList tmp = new TByteLinkedList();
        tmp.add(value);
        insert(offset, tmp);
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values) {
        insert(offset, link(values, 0, values.length));
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values, int valOffset, int len) {
        insert(offset, link(values, valOffset, len));
    }

    void insert(int offset, TByteLinkedList tmp) {
        TByteLink l = getLinkAt(offset);
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
            TByteLink prev = l.getPrevious();
            l.getPrevious().setNext(tmp.head);
            tmp.tail.setNext(l);
            l.setPrevious(tmp.tail);
            tmp.head.setPrevious(prev);
        }
    }

    static TByteLinkedList link(byte[] values, int valOffset, int len) {
        TByteLinkedList ret = new TByteLinkedList();
        for (int i = 0; i < len; i++) {
            ret.add(values[valOffset + i]);
        }
        return ret;
    }

    @Override // gnu.trove.list.TByteList
    public byte get(int offset) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TByteLink l = getLinkAt(offset);
        if (no(l)) {
            return this.no_entry_value;
        }
        return l.getValue();
    }

    public TByteLink getLinkAt(int offset) {
        if (offset >= size()) {
            return null;
        }
        if (offset <= (size() >>> 1)) {
            return getLink(this.head, 0, offset, true);
        }
        return getLink(this.tail, size() - 1, offset, false);
    }

    private static TByteLink getLink(TByteLink l, int idx, int offset) {
        return getLink(l, idx, offset, true);
    }

    private static TByteLink getLink(TByteLink l, int idx, int offset, boolean next) {
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

    @Override // gnu.trove.list.TByteList
    public byte set(int offset, byte val) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TByteLink l = getLinkAt(offset);
        if (no(l)) {
            throw new IndexOutOfBoundsException("at offset " + offset);
        }
        byte prev = l.getValue();
        l.setValue(val);
        return prev;
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values, int valOffset, int length) {
        for (int i = 0; i < length; i++) {
            byte value = values[valOffset + i];
            set(offset + i, value);
        }
    }

    @Override // gnu.trove.list.TByteList
    public byte replace(int offset, byte val) {
        return set(offset, val);
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public void clear() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean remove(byte value) {
        boolean changed = false;
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (l.getValue() == value) {
                    changed = true;
                    removeLink(l);
                }
                tByteLink = l.getNext();
            } else {
                return changed;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLink(TByteLink l) {
        if (no(l)) {
            return;
        }
        this.size--;
        TByteLink prev = l.getPrevious();
        TByteLink next = l.getNext();
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

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty()) {
            return false;
        }
        for (Object o : collection) {
            if (o instanceof Byte) {
                Byte i = (Byte) o;
                if (!contains(i.byteValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(TByteCollection collection) {
        if (isEmpty()) {
            return false;
        }
        TByteIterator it = collection.iterator();
        while (it.hasNext()) {
            byte i = it.next();
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(byte[] array) {
        if (isEmpty()) {
            return false;
        }
        for (byte i : array) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(Collection<? extends Byte> collection) {
        boolean ret = false;
        for (Byte v : collection) {
            if (add(v.byteValue())) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(TByteCollection collection) {
        boolean ret = false;
        TByteIterator it = collection.iterator();
        while (it.hasNext()) {
            byte i = it.next();
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(byte[] array) {
        boolean ret = false;
        for (byte i : array) {
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        TByteIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(Byte.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(TByteCollection collection) {
        boolean modified = false;
        TByteIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TByteCollection
    public boolean retainAll(byte[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TByteIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) < 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        TByteIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(Byte.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(TByteCollection collection) {
        boolean modified = false;
        TByteIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(byte[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TByteIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) >= 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.list.TByteList
    public byte removeAt(int offset) {
        TByteLink l = getLinkAt(offset);
        if (no(l)) {
            throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
        }
        byte prev = l.getValue();
        removeLink(l);
        return prev;
    }

    @Override // gnu.trove.list.TByteList
    public void remove(int offset, int length) {
        for (int i = 0; i < length; i++) {
            removeAt(offset);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void transformValues(TByteFunction function) {
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                l.setValue(function.execute(l.getValue()));
                tByteLink = l.getNext();
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public void reverse() {
        TByteLink h = this.head;
        TByteLink t = this.tail;
        TByteLink l = this.head;
        while (got(l)) {
            TByteLink next = l.getNext();
            TByteLink prev = l.getPrevious();
            TByteLink tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        this.head = t;
        this.tail = h;
    }

    @Override // gnu.trove.list.TByteList
    public void reverse(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to : " + from + ">" + to);
        }
        TByteLink start = getLinkAt(from);
        TByteLink stop = getLinkAt(to);
        TByteLink tmp = null;
        TByteLink tmpHead = start.getPrevious();
        TByteLink l = start;
        while (l != stop) {
            TByteLink next = l.getNext();
            TByteLink prev = l.getPrevious();
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

    @Override // gnu.trove.list.TByteList
    public void shuffle(Random rand) {
        for (int i = 0; i < this.size; i++) {
            TByteLink l = getLinkAt(rand.nextInt(size()));
            removeLink(l);
            add(l.getValue());
        }
    }

    @Override // gnu.trove.list.TByteList
    public TByteList subList(int begin, int end) {
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
        TByteLinkedList ret = new TByteLinkedList();
        TByteLink tmp = getLinkAt(begin);
        for (int i = begin; i < end; i++) {
            ret.add(tmp.getValue());
            tmp = tmp.getNext();
        }
        return ret;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public byte[] toArray() {
        return toArray(new byte[this.size], 0, this.size);
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(int offset, int len) {
        return toArray(new byte[len], offset, 0, len);
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public byte[] toArray(byte[] dest) {
        return toArray(dest, 0, this.size);
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int offset, int len) {
        return toArray(dest, offset, 0, len);
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= size()) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        TByteLink tmp = getLinkAt(source_pos);
        for (int i = 0; i < len; i++) {
            dest[dest_pos + i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return dest;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean forEach(TByteProcedure procedure) {
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tByteLink = l.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public boolean forEachDescending(TByteProcedure procedure) {
        TByteLink tByteLink = this.tail;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tByteLink = l.getPrevious();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public void sort() {
        sort(0, this.size);
    }

    @Override // gnu.trove.list.TByteList
    public void sort(int fromIndex, int toIndex) {
        TByteList tmp = subList(fromIndex, toIndex);
        byte[] vals = tmp.toArray();
        Arrays.sort(vals);
        set(fromIndex, vals);
    }

    @Override // gnu.trove.list.TByteList
    public void fill(byte val) {
        fill(0, this.size, val);
    }

    @Override // gnu.trove.list.TByteList
    public void fill(int fromIndex, int toIndex, byte val) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        TByteLink l = getLinkAt(fromIndex);
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

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value) {
        return binarySearch(value, 0, size());
    }

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value, int fromIndex, int toIndex) {
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
        TByteLink fromLink = getLinkAt(fromIndex);
        int to = toIndex;
        while (from < to) {
            int mid = (from + to) >>> 1;
            TByteLink middle = getLink(fromLink, from, mid);
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

    @Override // gnu.trove.list.TByteList
    public int indexOf(byte value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TByteList
    public int indexOf(int offset, byte value) {
        int count = offset;
        TByteLink linkAt = getLinkAt(offset);
        while (true) {
            TByteLink l = linkAt;
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

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(byte value) {
        return lastIndexOf(0, value);
    }

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(int offset, byte value) {
        if (isEmpty()) {
            return -1;
        }
        int last = -1;
        int count = offset;
        TByteLink linkAt = getLinkAt(offset);
        while (true) {
            TByteLink l = linkAt;
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

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean contains(byte value) {
        if (isEmpty()) {
            return false;
        }
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (l.getValue() != value) {
                    tByteLink = l.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.TByteCollection
    public TByteIterator iterator() {
        return new TByteIterator() { // from class: gnu.trove.list.linked.TByteLinkedList.1
            TByteLink l;
            TByteLink current;

            {
                this.l = TByteLinkedList.this.head;
            }

            @Override // gnu.trove.iterator.TByteIterator
            public byte next() {
                if (TByteLinkedList.no(this.l)) {
                    throw new NoSuchElementException();
                }
                byte ret = this.l.getValue();
                this.current = this.l;
                this.l = this.l.getNext();
                return ret;
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return TByteLinkedList.got(this.l);
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this.current != null) {
                    TByteLinkedList.this.removeLink(this.current);
                    this.current = null;
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }

    @Override // gnu.trove.list.TByteList
    public TByteList grep(TByteProcedure condition) {
        TByteList ret = new TByteLinkedList();
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tByteLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public TByteList inverseGrep(TByteProcedure condition) {
        TByteList ret = new TByteLinkedList();
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (!condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tByteLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public byte max() {
        byte ret = Byte.MIN_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (ret < l.getValue()) {
                    ret = l.getValue();
                }
                tByteLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public byte min() {
        byte ret = Byte.MAX_VALUE;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                if (ret > l.getValue()) {
                    ret = l.getValue();
                }
                tByteLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public byte sum() {
        byte sum = 0;
        TByteLink tByteLink = this.head;
        while (true) {
            TByteLink l = tByteLink;
            if (got(l)) {
                sum = (byte) (sum + l.getValue());
                tByteLink = l.getNext();
            } else {
                return sum;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TByteLinkedList$TByteLink.class */
    public static class TByteLink {
        byte value;
        TByteLink previous;
        TByteLink next;

        TByteLink(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return this.value;
        }

        public void setValue(byte value) {
            this.value = value;
        }

        public TByteLink getPrevious() {
            return this.previous;
        }

        public void setPrevious(TByteLink previous) {
            this.previous = previous;
        }

        public TByteLink getNext() {
            return this.next;
        }

        public void setNext(TByteLink next) {
            this.next = next;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TByteLinkedList$RemoveProcedure.class */
    class RemoveProcedure implements TByteProcedure {
        boolean changed = false;

        RemoveProcedure() {
        }

        @Override // gnu.trove.procedure.TByteProcedure
        public boolean execute(byte value) {
            if (TByteLinkedList.this.remove(value)) {
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
        out.writeByte(this.no_entry_value);
        out.writeInt(this.size);
        TByteIterator iterator = iterator();
        while (iterator.hasNext()) {
            byte next = iterator.next();
            out.writeByte(next);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.no_entry_value = in.readByte();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            add(in.readByte());
        }
    }

    static boolean got(Object ref) {
        return ref != null;
    }

    static boolean no(Object ref) {
        return ref == null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    @Override // gnu.trove.TByteCollection
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
            gnu.trove.list.linked.TByteLinkedList r0 = (gnu.trove.list.linked.TByteLinkedList) r0
            r5 = r0
            r0 = r3
            byte r0 = r0.no_entry_value
            r1 = r5
            byte r1 = r1.no_entry_value
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
            gnu.trove.iterator.TByteIterator r0 = r0.iterator()
            r6 = r0
            r0 = r5
            gnu.trove.iterator.TByteIterator r0 = r0.iterator()
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
            byte r0 = r0.next()
            r1 = r7
            byte r1 = r1.next()
            if (r0 == r1) goto L42
            r0 = 0
            return r0
        L69:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.list.linked.TByteLinkedList.equals(java.lang.Object):boolean");
    }

    @Override // gnu.trove.TByteCollection
    public int hashCode() {
        int result = HashFunctions.hash((int) this.no_entry_value);
        int result2 = (31 * result) + this.size;
        TByteIterator iterator = iterator();
        while (iterator.hasNext()) {
            result2 = (31 * result2) + HashFunctions.hash((int) iterator.next());
        }
        return result2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        TByteIterator it = iterator();
        while (it.hasNext()) {
            byte next = it.next();
            buf.append((int) next);
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
