package gnu.trove.list.linked;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.list.TCharList;
import gnu.trove.procedure.TCharProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TCharLinkedList.class */
public class TCharLinkedList implements TCharList, Externalizable {
    char no_entry_value;
    int size;
    TCharLink head = null;
    TCharLink tail = this.head;

    public TCharLinkedList() {
    }

    public TCharLinkedList(char no_entry_value) {
        this.no_entry_value = no_entry_value;
    }

    public TCharLinkedList(TCharList list) {
        this.no_entry_value = list.getNoEntryValue();
        TCharIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            char next = iterator.next();
            add(next);
        }
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public char getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public int size() {
        return this.size;
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public boolean add(char val) {
        TCharLink l = new TCharLink(val);
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

    @Override // gnu.trove.list.TCharList
    public void add(char[] vals) {
        for (char val : vals) {
            add(val);
        }
    }

    @Override // gnu.trove.list.TCharList
    public void add(char[] vals, int offset, int length) {
        for (int i = 0; i < length; i++) {
            char val = vals[offset + i];
            add(val);
        }
    }

    @Override // gnu.trove.list.TCharList
    public void insert(int offset, char value) {
        TCharLinkedList tmp = new TCharLinkedList();
        tmp.add(value);
        insert(offset, tmp);
    }

    @Override // gnu.trove.list.TCharList
    public void insert(int offset, char[] values) {
        insert(offset, link(values, 0, values.length));
    }

    @Override // gnu.trove.list.TCharList
    public void insert(int offset, char[] values, int valOffset, int len) {
        insert(offset, link(values, valOffset, len));
    }

    void insert(int offset, TCharLinkedList tmp) {
        TCharLink l = getLinkAt(offset);
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
            TCharLink prev = l.getPrevious();
            l.getPrevious().setNext(tmp.head);
            tmp.tail.setNext(l);
            l.setPrevious(tmp.tail);
            tmp.head.setPrevious(prev);
        }
    }

    static TCharLinkedList link(char[] values, int valOffset, int len) {
        TCharLinkedList ret = new TCharLinkedList();
        for (int i = 0; i < len; i++) {
            ret.add(values[valOffset + i]);
        }
        return ret;
    }

    @Override // gnu.trove.list.TCharList
    public char get(int offset) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TCharLink l = getLinkAt(offset);
        if (no(l)) {
            return this.no_entry_value;
        }
        return l.getValue();
    }

    public TCharLink getLinkAt(int offset) {
        if (offset >= size()) {
            return null;
        }
        if (offset <= (size() >>> 1)) {
            return getLink(this.head, 0, offset, true);
        }
        return getLink(this.tail, size() - 1, offset, false);
    }

    private static TCharLink getLink(TCharLink l, int idx, int offset) {
        return getLink(l, idx, offset, true);
    }

    private static TCharLink getLink(TCharLink l, int idx, int offset, boolean next) {
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

    @Override // gnu.trove.list.TCharList
    public char set(int offset, char val) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TCharLink l = getLinkAt(offset);
        if (no(l)) {
            throw new IndexOutOfBoundsException("at offset " + offset);
        }
        char prev = l.getValue();
        l.setValue(val);
        return prev;
    }

    @Override // gnu.trove.list.TCharList
    public void set(int offset, char[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TCharList
    public void set(int offset, char[] values, int valOffset, int length) {
        for (int i = 0; i < length; i++) {
            char value = values[valOffset + i];
            set(offset + i, value);
        }
    }

    @Override // gnu.trove.list.TCharList
    public char replace(int offset, char val) {
        return set(offset, val);
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public void clear() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public boolean remove(char value) {
        boolean changed = false;
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (l.getValue() == value) {
                    changed = true;
                    removeLink(l);
                }
                tCharLink = l.getNext();
            } else {
                return changed;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLink(TCharLink l) {
        if (no(l)) {
            return;
        }
        this.size--;
        TCharLink prev = l.getPrevious();
        TCharLink next = l.getNext();
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

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty()) {
            return false;
        }
        for (Object o : collection) {
            if (o instanceof Character) {
                Character i = (Character) o;
                if (!contains(i.charValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(TCharCollection collection) {
        if (isEmpty()) {
            return false;
        }
        TCharIterator it = collection.iterator();
        while (it.hasNext()) {
            char i = it.next();
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TCharCollection
    public boolean containsAll(char[] array) {
        if (isEmpty()) {
            return false;
        }
        for (char i : array) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(Collection<? extends Character> collection) {
        boolean ret = false;
        for (Character v : collection) {
            if (add(v.charValue())) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(TCharCollection collection) {
        boolean ret = false;
        TCharIterator it = collection.iterator();
        while (it.hasNext()) {
            char i = it.next();
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TCharCollection
    public boolean addAll(char[] array) {
        boolean ret = false;
        for (char i : array) {
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        TCharIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(Character.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(TCharCollection collection) {
        boolean modified = false;
        TCharIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TCharCollection
    public boolean retainAll(char[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TCharIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) < 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        TCharIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(Character.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(TCharCollection collection) {
        boolean modified = false;
        TCharIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TCharCollection
    public boolean removeAll(char[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TCharIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) >= 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.list.TCharList
    public char removeAt(int offset) {
        TCharLink l = getLinkAt(offset);
        if (no(l)) {
            throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
        }
        char prev = l.getValue();
        removeLink(l);
        return prev;
    }

    @Override // gnu.trove.list.TCharList
    public void remove(int offset, int length) {
        for (int i = 0; i < length; i++) {
            removeAt(offset);
        }
    }

    @Override // gnu.trove.list.TCharList
    public void transformValues(TCharFunction function) {
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                l.setValue(function.execute(l.getValue()));
                tCharLink = l.getNext();
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TCharList
    public void reverse() {
        TCharLink h = this.head;
        TCharLink t = this.tail;
        TCharLink l = this.head;
        while (got(l)) {
            TCharLink next = l.getNext();
            TCharLink prev = l.getPrevious();
            TCharLink tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        this.head = t;
        this.tail = h;
    }

    @Override // gnu.trove.list.TCharList
    public void reverse(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to : " + from + ">" + to);
        }
        TCharLink start = getLinkAt(from);
        TCharLink stop = getLinkAt(to);
        TCharLink tmp = null;
        TCharLink tmpHead = start.getPrevious();
        TCharLink l = start;
        while (l != stop) {
            TCharLink next = l.getNext();
            TCharLink prev = l.getPrevious();
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

    @Override // gnu.trove.list.TCharList
    public void shuffle(Random rand) {
        for (int i = 0; i < this.size; i++) {
            TCharLink l = getLinkAt(rand.nextInt(size()));
            removeLink(l);
            add(l.getValue());
        }
    }

    @Override // gnu.trove.list.TCharList
    public TCharList subList(int begin, int end) {
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
        TCharLinkedList ret = new TCharLinkedList();
        TCharLink tmp = getLinkAt(begin);
        for (int i = begin; i < end; i++) {
            ret.add(tmp.getValue());
            tmp = tmp.getNext();
        }
        return ret;
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public char[] toArray() {
        return toArray(new char[this.size], 0, this.size);
    }

    @Override // gnu.trove.list.TCharList
    public char[] toArray(int offset, int len) {
        return toArray(new char[len], offset, 0, len);
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public char[] toArray(char[] dest) {
        return toArray(dest, 0, this.size);
    }

    @Override // gnu.trove.list.TCharList
    public char[] toArray(char[] dest, int offset, int len) {
        return toArray(dest, offset, 0, len);
    }

    @Override // gnu.trove.list.TCharList
    public char[] toArray(char[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= size()) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        TCharLink tmp = getLinkAt(source_pos);
        for (int i = 0; i < len; i++) {
            dest[dest_pos + i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return dest;
    }

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public boolean forEach(TCharProcedure procedure) {
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tCharLink = l.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TCharList
    public boolean forEachDescending(TCharProcedure procedure) {
        TCharLink tCharLink = this.tail;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tCharLink = l.getPrevious();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TCharList
    public void sort() {
        sort(0, this.size);
    }

    @Override // gnu.trove.list.TCharList
    public void sort(int fromIndex, int toIndex) {
        TCharList tmp = subList(fromIndex, toIndex);
        char[] vals = tmp.toArray();
        Arrays.sort(vals);
        set(fromIndex, vals);
    }

    @Override // gnu.trove.list.TCharList
    public void fill(char val) {
        fill(0, this.size, val);
    }

    @Override // gnu.trove.list.TCharList
    public void fill(int fromIndex, int toIndex, char val) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        TCharLink l = getLinkAt(fromIndex);
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

    @Override // gnu.trove.list.TCharList
    public int binarySearch(char value) {
        return binarySearch(value, 0, size());
    }

    @Override // gnu.trove.list.TCharList
    public int binarySearch(char value, int fromIndex, int toIndex) {
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
        TCharLink fromLink = getLinkAt(fromIndex);
        int to = toIndex;
        while (from < to) {
            int mid = (from + to) >>> 1;
            TCharLink middle = getLink(fromLink, from, mid);
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

    @Override // gnu.trove.list.TCharList
    public int indexOf(char value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TCharList
    public int indexOf(int offset, char value) {
        int count = offset;
        TCharLink linkAt = getLinkAt(offset);
        while (true) {
            TCharLink l = linkAt;
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

    @Override // gnu.trove.list.TCharList
    public int lastIndexOf(char value) {
        return lastIndexOf(0, value);
    }

    @Override // gnu.trove.list.TCharList
    public int lastIndexOf(int offset, char value) {
        if (isEmpty()) {
            return -1;
        }
        int last = -1;
        int count = offset;
        TCharLink linkAt = getLinkAt(offset);
        while (true) {
            TCharLink l = linkAt;
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

    @Override // gnu.trove.list.TCharList, gnu.trove.TCharCollection
    public boolean contains(char value) {
        if (isEmpty()) {
            return false;
        }
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (l.getValue() != value) {
                    tCharLink = l.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.TCharCollection
    public TCharIterator iterator() {
        return new TCharIterator() { // from class: gnu.trove.list.linked.TCharLinkedList.1
            TCharLink l;
            TCharLink current;

            {
                this.l = TCharLinkedList.this.head;
            }

            @Override // gnu.trove.iterator.TCharIterator
            public char next() {
                if (TCharLinkedList.no(this.l)) {
                    throw new NoSuchElementException();
                }
                char ret = this.l.getValue();
                this.current = this.l;
                this.l = this.l.getNext();
                return ret;
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return TCharLinkedList.got(this.l);
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this.current != null) {
                    TCharLinkedList.this.removeLink(this.current);
                    this.current = null;
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }

    @Override // gnu.trove.list.TCharList
    public TCharList grep(TCharProcedure condition) {
        TCharList ret = new TCharLinkedList();
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tCharLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TCharList
    public TCharList inverseGrep(TCharProcedure condition) {
        TCharList ret = new TCharLinkedList();
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (!condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tCharLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TCharList
    public char max() {
        char ret = 0;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (ret < l.getValue()) {
                    ret = l.getValue();
                }
                tCharLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TCharList
    public char min() {
        char ret = 65535;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                if (ret > l.getValue()) {
                    ret = l.getValue();
                }
                tCharLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TCharList
    public char sum() {
        char sum = 0;
        TCharLink tCharLink = this.head;
        while (true) {
            TCharLink l = tCharLink;
            if (got(l)) {
                sum = (char) (sum + l.getValue());
                tCharLink = l.getNext();
            } else {
                return sum;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TCharLinkedList$TCharLink.class */
    public static class TCharLink {
        char value;
        TCharLink previous;
        TCharLink next;

        TCharLink(char value) {
            this.value = value;
        }

        public char getValue() {
            return this.value;
        }

        public void setValue(char value) {
            this.value = value;
        }

        public TCharLink getPrevious() {
            return this.previous;
        }

        public void setPrevious(TCharLink previous) {
            this.previous = previous;
        }

        public TCharLink getNext() {
            return this.next;
        }

        public void setNext(TCharLink next) {
            this.next = next;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TCharLinkedList$RemoveProcedure.class */
    class RemoveProcedure implements TCharProcedure {
        boolean changed = false;

        RemoveProcedure() {
        }

        @Override // gnu.trove.procedure.TCharProcedure
        public boolean execute(char value) {
            if (TCharLinkedList.this.remove(value)) {
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
        out.writeChar(this.no_entry_value);
        out.writeInt(this.size);
        TCharIterator iterator = iterator();
        while (iterator.hasNext()) {
            char next = iterator.next();
            out.writeChar(next);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.no_entry_value = in.readChar();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            add(in.readChar());
        }
    }

    static boolean got(Object ref) {
        return ref != null;
    }

    static boolean no(Object ref) {
        return ref == null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    @Override // gnu.trove.TCharCollection
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
            gnu.trove.list.linked.TCharLinkedList r0 = (gnu.trove.list.linked.TCharLinkedList) r0
            r5 = r0
            r0 = r3
            char r0 = r0.no_entry_value
            r1 = r5
            char r1 = r1.no_entry_value
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
            gnu.trove.iterator.TCharIterator r0 = r0.iterator()
            r6 = r0
            r0 = r5
            gnu.trove.iterator.TCharIterator r0 = r0.iterator()
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
            char r0 = r0.next()
            r1 = r7
            char r1 = r1.next()
            if (r0 == r1) goto L42
            r0 = 0
            return r0
        L69:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.list.linked.TCharLinkedList.equals(java.lang.Object):boolean");
    }

    @Override // gnu.trove.TCharCollection
    public int hashCode() {
        int result = HashFunctions.hash((int) this.no_entry_value);
        int result2 = (31 * result) + this.size;
        TCharIterator iterator = iterator();
        while (iterator.hasNext()) {
            result2 = (31 * result2) + HashFunctions.hash((int) iterator.next());
        }
        return result2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        TCharIterator it = iterator();
        while (it.hasNext()) {
            char next = it.next();
            buf.append(next);
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
