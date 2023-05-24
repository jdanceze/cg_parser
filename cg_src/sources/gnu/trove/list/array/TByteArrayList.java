package gnu.trove.list.array;

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
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/array/TByteArrayList.class */
public class TByteArrayList implements TByteList, Externalizable {
    static final long serialVersionUID = 1;
    protected byte[] _data;
    protected int _pos;
    protected static final int DEFAULT_CAPACITY = 10;
    protected byte no_entry_value;

    public TByteArrayList() {
        this(10, (byte) 0);
    }

    public TByteArrayList(int capacity) {
        this(capacity, (byte) 0);
    }

    public TByteArrayList(int capacity, byte no_entry_value) {
        this._data = new byte[capacity];
        this._pos = 0;
        this.no_entry_value = no_entry_value;
    }

    public TByteArrayList(TByteCollection collection) {
        this(collection.size());
        addAll(collection);
    }

    public TByteArrayList(byte[] values) {
        this(values.length);
        add(values);
    }

    protected TByteArrayList(byte[] values, byte no_entry_value, boolean wrap) {
        if (!wrap) {
            throw new IllegalStateException("Wrong call");
        }
        if (values == null) {
            throw new IllegalArgumentException("values can not be null");
        }
        this._data = values;
        this._pos = values.length;
        this.no_entry_value = no_entry_value;
    }

    public static TByteArrayList wrap(byte[] values) {
        return wrap(values, (byte) 0);
    }

    public static TByteArrayList wrap(byte[] values, byte no_entry_value) {
        return new TByteArrayList(values, no_entry_value, true) { // from class: gnu.trove.list.array.TByteArrayList.1
            @Override // gnu.trove.list.array.TByteArrayList
            public void ensureCapacity(int capacity) {
                if (capacity > this._data.length) {
                    throw new IllegalStateException("Can not grow ArrayList wrapped external array");
                }
            }
        };
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public byte getNoEntryValue() {
        return this.no_entry_value;
    }

    public void ensureCapacity(int capacity) {
        if (capacity > this._data.length) {
            int newCap = Math.max(this._data.length << 1, capacity);
            byte[] tmp = new byte[newCap];
            System.arraycopy(this._data, 0, tmp, 0, this._data.length);
            this._data = tmp;
        }
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public int size() {
        return this._pos;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean isEmpty() {
        return this._pos == 0;
    }

    public void trimToSize() {
        if (this._data.length > size()) {
            byte[] tmp = new byte[size()];
            toArray(tmp, 0, tmp.length);
            this._data = tmp;
        }
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean add(byte val) {
        ensureCapacity(this._pos + 1);
        byte[] bArr = this._data;
        int i = this._pos;
        this._pos = i + 1;
        bArr[i] = val;
        return true;
    }

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals) {
        add(vals, 0, vals.length);
    }

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals, int offset, int length) {
        ensureCapacity(this._pos + length);
        System.arraycopy(vals, offset, this._data, this._pos, length);
        this._pos += length;
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte value) {
        if (offset == this._pos) {
            add(value);
            return;
        }
        ensureCapacity(this._pos + 1);
        System.arraycopy(this._data, offset, this._data, offset + 1, this._pos - offset);
        this._data[offset] = value;
        this._pos++;
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values) {
        insert(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values, int valOffset, int len) {
        if (offset == this._pos) {
            add(values, valOffset, len);
            return;
        }
        ensureCapacity(this._pos + len);
        System.arraycopy(this._data, offset, this._data, offset + len, this._pos - offset);
        System.arraycopy(values, valOffset, this._data, offset, len);
        this._pos += len;
    }

    @Override // gnu.trove.list.TByteList
    public byte get(int offset) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return this._data[offset];
    }

    public byte getQuick(int offset) {
        return this._data[offset];
    }

    @Override // gnu.trove.list.TByteList
    public byte set(int offset, byte val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        byte prev_val = this._data[offset];
        this._data[offset] = val;
        return prev_val;
    }

    @Override // gnu.trove.list.TByteList
    public byte replace(int offset, byte val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        byte old = this._data[offset];
        this._data[offset] = val;
        return old;
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values, int valOffset, int length) {
        if (offset < 0 || offset + length > this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(values, valOffset, this._data, offset, length);
    }

    public void setQuick(int offset, byte val) {
        this._data[offset] = val;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public void clear() {
        clear(10);
    }

    public void clear(int capacity) {
        this._data = new byte[capacity];
        this._pos = 0;
    }

    public void reset() {
        this._pos = 0;
        Arrays.fill(this._data, this.no_entry_value);
    }

    public void resetQuick() {
        this._pos = 0;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean remove(byte value) {
        for (int index = 0; index < this._pos; index++) {
            if (value == this._data[index]) {
                remove(index, 1);
                return true;
            }
        }
        return false;
    }

    @Override // gnu.trove.list.TByteList
    public byte removeAt(int offset) {
        byte old = get(offset);
        remove(offset, 1);
        return old;
    }

    @Override // gnu.trove.list.TByteList
    public void remove(int offset, int length) {
        if (length == 0) {
            return;
        }
        if (offset < 0 || offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        if (offset == 0) {
            System.arraycopy(this._data, length, this._data, 0, this._pos - length);
        } else if (this._pos - length != offset) {
            System.arraycopy(this._data, offset + length, this._data, offset, this._pos - (offset + length));
        }
        this._pos -= length;
    }

    @Override // gnu.trove.TByteCollection
    public TByteIterator iterator() {
        return new TByteArrayIterator(0);
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (element instanceof Byte) {
                byte c = ((Byte) element).byteValue();
                if (!contains(c)) {
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
        if (this == collection) {
            return true;
        }
        TByteIterator iter = collection.iterator();
        while (iter.hasNext()) {
            byte element = iter.next();
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TByteCollection
    public boolean containsAll(byte[] array) {
        int i = array.length;
        do {
            int i2 = i;
            i--;
            if (i2 <= 0) {
                return true;
            }
        } while (contains(array[i]));
        return false;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(Collection<? extends Byte> collection) {
        boolean changed = false;
        for (Byte element : collection) {
            byte e = element.byteValue();
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(TByteCollection collection) {
        boolean changed = false;
        TByteIterator iter = collection.iterator();
        while (iter.hasNext()) {
            byte element = iter.next();
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TByteCollection
    public boolean addAll(byte[] array) {
        boolean changed = false;
        for (byte element : array) {
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
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
        if (this == collection) {
            return false;
        }
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
        boolean changed = false;
        Arrays.sort(array);
        byte[] data = this._data;
        int i = this._pos;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (Arrays.binarySearch(array, data[i]) < 0) {
                    remove(i, 1);
                    changed = true;
                }
            } else {
                return changed;
            }
        }
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        for (Object element : collection) {
            if (element instanceof Byte) {
                byte c = ((Byte) element).byteValue();
                if (remove(c)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(TByteCollection collection) {
        if (collection == this) {
            clear();
            return true;
        }
        boolean changed = false;
        TByteIterator iter = collection.iterator();
        while (iter.hasNext()) {
            byte element = iter.next();
            if (remove(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TByteCollection
    public boolean removeAll(byte[] array) {
        boolean changed = false;
        int i = array.length;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (remove(array[i])) {
                    changed = true;
                }
            } else {
                return changed;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public void transformValues(TByteFunction function) {
        int i = this._pos;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                this._data[i] = function.execute(this._data[i]);
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TByteList
    public void reverse() {
        reverse(0, this._pos);
    }

    @Override // gnu.trove.list.TByteList
    public void reverse(int from, int to) {
        if (from == to) {
            return;
        }
        if (from > to) {
            throw new IllegalArgumentException("from cannot be greater than to");
        }
        int i = from;
        for (int j = to - 1; i < j; j--) {
            swap(i, j);
            i++;
        }
    }

    @Override // gnu.trove.list.TByteList
    public void shuffle(Random rand) {
        int i = this._pos;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 1) {
                swap(i, rand.nextInt(i));
            } else {
                return;
            }
        }
    }

    private void swap(int i, int j) {
        byte tmp = this._data[i];
        this._data[i] = this._data[j];
        this._data[j] = tmp;
    }

    @Override // gnu.trove.list.TByteList
    public TByteList subList(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end index " + end + " greater than begin index " + begin);
        }
        if (begin < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        if (end > this._data.length) {
            throw new IndexOutOfBoundsException("end index < " + this._data.length);
        }
        TByteArrayList list = new TByteArrayList(end - begin);
        for (int i = begin; i < end; i++) {
            list.add(this._data[i]);
        }
        return list;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public byte[] toArray() {
        return toArray(0, this._pos);
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(int offset, int len) {
        byte[] rv = new byte[len];
        toArray(rv, offset, len);
        return rv;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public byte[] toArray(byte[] dest) {
        int len = dest.length;
        if (dest.length > this._pos) {
            len = this._pos;
            dest[len] = this.no_entry_value;
        }
        toArray(dest, 0, len);
        return dest;
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int offset, int len) {
        if (len == 0) {
            return dest;
        }
        if (offset < 0 || offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(this._data, offset, dest, 0, len);
        return dest;
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        System.arraycopy(this._data, source_pos, dest, dest_pos, len);
        return dest;
    }

    @Override // gnu.trove.TByteCollection
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof TByteArrayList) {
            TByteArrayList that = (TByteArrayList) other;
            if (that.size() != size()) {
                return false;
            }
            int i = this._pos;
            do {
                int i2 = i;
                i--;
                if (i2 <= 0) {
                    return true;
                }
            } while (this._data[i] == that._data[i]);
            return false;
        }
        return false;
    }

    @Override // gnu.trove.TByteCollection
    public int hashCode() {
        int h = 0;
        int i = this._pos;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                h += HashFunctions.hash((int) this._data[i]);
            } else {
                return h;
            }
        }
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean forEach(TByteProcedure procedure) {
        for (int i = 0; i < this._pos; i++) {
            if (!procedure.execute(this._data[i])) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.list.TByteList
    public boolean forEachDescending(TByteProcedure procedure) {
        int i = this._pos;
        do {
            int i2 = i;
            i--;
            if (i2 <= 0) {
                return true;
            }
        } while (procedure.execute(this._data[i]));
        return false;
    }

    @Override // gnu.trove.list.TByteList
    public void sort() {
        Arrays.sort(this._data, 0, this._pos);
    }

    @Override // gnu.trove.list.TByteList
    public void sort(int fromIndex, int toIndex) {
        Arrays.sort(this._data, fromIndex, toIndex);
    }

    @Override // gnu.trove.list.TByteList
    public void fill(byte val) {
        Arrays.fill(this._data, 0, this._pos, val);
    }

    @Override // gnu.trove.list.TByteList
    public void fill(int fromIndex, int toIndex, byte val) {
        if (toIndex > this._pos) {
            ensureCapacity(toIndex);
            this._pos = toIndex;
        }
        Arrays.fill(this._data, fromIndex, toIndex, val);
    }

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value) {
        return binarySearch(value, 0, this._pos);
    }

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > this._pos) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            byte midVal = this._data[mid];
            if (midVal < value) {
                low = mid + 1;
            } else if (midVal > value) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    @Override // gnu.trove.list.TByteList
    public int indexOf(byte value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TByteList
    public int indexOf(int offset, byte value) {
        for (int i = offset; i < this._pos; i++) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(byte value) {
        return lastIndexOf(this._pos, value);
    }

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(int offset, byte value) {
        int i = offset;
        do {
            int i2 = i;
            i--;
            if (i2 <= 0) {
                return -1;
            }
        } while (this._data[i] != value);
        return i;
    }

    @Override // gnu.trove.list.TByteList, gnu.trove.TByteCollection
    public boolean contains(byte value) {
        return lastIndexOf(value) >= 0;
    }

    @Override // gnu.trove.list.TByteList
    public TByteList grep(TByteProcedure condition) {
        TByteArrayList list = new TByteArrayList();
        for (int i = 0; i < this._pos; i++) {
            if (condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }

    @Override // gnu.trove.list.TByteList
    public TByteList inverseGrep(TByteProcedure condition) {
        TByteArrayList list = new TByteArrayList();
        for (int i = 0; i < this._pos; i++) {
            if (!condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }

    @Override // gnu.trove.list.TByteList
    public byte max() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find maximum of an empty list");
        }
        byte max = Byte.MIN_VALUE;
        for (int i = 0; i < this._pos; i++) {
            if (this._data[i] > max) {
                max = this._data[i];
            }
        }
        return max;
    }

    @Override // gnu.trove.list.TByteList
    public byte min() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find minimum of an empty list");
        }
        byte min = Byte.MAX_VALUE;
        for (int i = 0; i < this._pos; i++) {
            if (this._data[i] < min) {
                min = this._data[i];
            }
        }
        return min;
    }

    @Override // gnu.trove.list.TByteList
    public byte sum() {
        byte sum = 0;
        for (int i = 0; i < this._pos; i++) {
            sum = (byte) (sum + this._data[i]);
        }
        return sum;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        int end = this._pos - 1;
        for (int i = 0; i < end; i++) {
            buf.append((int) this._data[i]);
            buf.append(", ");
        }
        if (size() > 0) {
            buf.append((int) this._data[this._pos - 1]);
        }
        buf.append("}");
        return buf.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/array/TByteArrayList$TByteArrayIterator.class */
    public class TByteArrayIterator implements TByteIterator {
        private int cursor;
        int lastRet = -1;

        TByteArrayIterator(int index) {
            this.cursor = 0;
            this.cursor = index;
        }

        @Override // gnu.trove.iterator.TIterator, java.util.Iterator
        public boolean hasNext() {
            return this.cursor < TByteArrayList.this.size();
        }

        @Override // gnu.trove.iterator.TByteIterator
        public byte next() {
            try {
                byte next = TByteArrayList.this.get(this.cursor);
                int i = this.cursor;
                this.cursor = i + 1;
                this.lastRet = i;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override // gnu.trove.iterator.TIterator, java.util.Iterator
        public void remove() {
            if (this.lastRet == -1) {
                throw new IllegalStateException();
            }
            try {
                TByteArrayList.this.remove(this.lastRet, 1);
                if (this.lastRet < this.cursor) {
                    this.cursor--;
                }
                this.lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeInt(this._pos);
        out.writeByte(this.no_entry_value);
        int len = this._data.length;
        out.writeInt(len);
        for (int i = 0; i < len; i++) {
            out.writeByte(this._data[i]);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._pos = in.readInt();
        this.no_entry_value = in.readByte();
        int len = in.readInt();
        this._data = new byte[len];
        for (int i = 0; i < len; i++) {
            this._data[i] = in.readByte();
        }
    }
}
