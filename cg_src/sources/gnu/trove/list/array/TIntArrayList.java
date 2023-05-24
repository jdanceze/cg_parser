package gnu.trove.list.array;

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
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/array/TIntArrayList.class */
public class TIntArrayList implements TIntList, Externalizable {
    static final long serialVersionUID = 1;
    protected int[] _data;
    protected int _pos;
    protected static final int DEFAULT_CAPACITY = 10;
    protected int no_entry_value;

    public TIntArrayList() {
        this(10, 0);
    }

    public TIntArrayList(int capacity) {
        this(capacity, 0);
    }

    public TIntArrayList(int capacity, int no_entry_value) {
        this._data = new int[capacity];
        this._pos = 0;
        this.no_entry_value = no_entry_value;
    }

    public TIntArrayList(TIntCollection collection) {
        this(collection.size());
        addAll(collection);
    }

    public TIntArrayList(int[] values) {
        this(values.length);
        add(values);
    }

    protected TIntArrayList(int[] values, int no_entry_value, boolean wrap) {
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

    public static TIntArrayList wrap(int[] values) {
        return wrap(values, 0);
    }

    public static TIntArrayList wrap(int[] values, int no_entry_value) {
        return new TIntArrayList(values, no_entry_value, true) { // from class: gnu.trove.list.array.TIntArrayList.1
            @Override // gnu.trove.list.array.TIntArrayList
            public void ensureCapacity(int capacity) {
                if (capacity > this._data.length) {
                    throw new IllegalStateException("Can not grow ArrayList wrapped external array");
                }
            }
        };
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int getNoEntryValue() {
        return this.no_entry_value;
    }

    public void ensureCapacity(int capacity) {
        if (capacity > this._data.length) {
            int newCap = Math.max(this._data.length << 1, capacity);
            int[] tmp = new int[newCap];
            System.arraycopy(this._data, 0, tmp, 0, this._data.length);
            this._data = tmp;
        }
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int size() {
        return this._pos;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean isEmpty() {
        return this._pos == 0;
    }

    public void trimToSize() {
        if (this._data.length > size()) {
            int[] tmp = new int[size()];
            toArray(tmp, 0, tmp.length);
            this._data = tmp;
        }
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean add(int val) {
        ensureCapacity(this._pos + 1);
        int[] iArr = this._data;
        int i = this._pos;
        this._pos = i + 1;
        iArr[i] = val;
        return true;
    }

    @Override // gnu.trove.list.TIntList
    public void add(int[] vals) {
        add(vals, 0, vals.length);
    }

    @Override // gnu.trove.list.TIntList
    public void add(int[] vals, int offset, int length) {
        ensureCapacity(this._pos + length);
        System.arraycopy(vals, offset, this._data, this._pos, length);
        this._pos += length;
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int value) {
        if (offset == this._pos) {
            add(value);
            return;
        }
        ensureCapacity(this._pos + 1);
        System.arraycopy(this._data, offset, this._data, offset + 1, this._pos - offset);
        this._data[offset] = value;
        this._pos++;
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int[] values) {
        insert(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int[] values, int valOffset, int len) {
        if (offset == this._pos) {
            add(values, valOffset, len);
            return;
        }
        ensureCapacity(this._pos + len);
        System.arraycopy(this._data, offset, this._data, offset + len, this._pos - offset);
        System.arraycopy(values, valOffset, this._data, offset, len);
        this._pos += len;
    }

    @Override // gnu.trove.list.TIntList
    public int get(int offset) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return this._data[offset];
    }

    public int getQuick(int offset) {
        return this._data[offset];
    }

    @Override // gnu.trove.list.TIntList
    public int set(int offset, int val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        int prev_val = this._data[offset];
        this._data[offset] = val;
        return prev_val;
    }

    @Override // gnu.trove.list.TIntList
    public int replace(int offset, int val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        int old = this._data[offset];
        this._data[offset] = val;
        return old;
    }

    @Override // gnu.trove.list.TIntList
    public void set(int offset, int[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TIntList
    public void set(int offset, int[] values, int valOffset, int length) {
        if (offset < 0 || offset + length > this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(values, valOffset, this._data, offset, length);
    }

    public void setQuick(int offset, int val) {
        this._data[offset] = val;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public void clear() {
        clear(10);
    }

    public void clear(int capacity) {
        this._data = new int[capacity];
        this._pos = 0;
    }

    public void reset() {
        this._pos = 0;
        Arrays.fill(this._data, this.no_entry_value);
    }

    public void resetQuick() {
        this._pos = 0;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean remove(int value) {
        for (int index = 0; index < this._pos; index++) {
            if (value == this._data[index]) {
                remove(index, 1);
                return true;
            }
        }
        return false;
    }

    @Override // gnu.trove.list.TIntList
    public int removeAt(int offset) {
        int old = get(offset);
        remove(offset, 1);
        return old;
    }

    @Override // gnu.trove.list.TIntList
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

    @Override // gnu.trove.TIntCollection
    public TIntIterator iterator() {
        return new TIntArrayIterator(0);
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (element instanceof Integer) {
                int c = ((Integer) element).intValue();
                if (!contains(c)) {
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
        if (this == collection) {
            return true;
        }
        TIntIterator iter = collection.iterator();
        while (iter.hasNext()) {
            int element = iter.next();
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TIntCollection
    public boolean containsAll(int[] array) {
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

    @Override // gnu.trove.TIntCollection
    public boolean addAll(Collection<? extends Integer> collection) {
        boolean changed = false;
        for (Integer element : collection) {
            int e = element.intValue();
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(TIntCollection collection) {
        boolean changed = false;
        TIntIterator iter = collection.iterator();
        while (iter.hasNext()) {
            int element = iter.next();
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TIntCollection
    public boolean addAll(int[] array) {
        boolean changed = false;
        for (int element : array) {
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
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
        if (this == collection) {
            return false;
        }
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
        boolean changed = false;
        Arrays.sort(array);
        int[] data = this._data;
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

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        for (Object element : collection) {
            if (element instanceof Integer) {
                int c = ((Integer) element).intValue();
                if (remove(c)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(TIntCollection collection) {
        if (collection == this) {
            clear();
            return true;
        }
        boolean changed = false;
        TIntIterator iter = collection.iterator();
        while (iter.hasNext()) {
            int element = iter.next();
            if (remove(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TIntCollection
    public boolean removeAll(int[] array) {
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

    @Override // gnu.trove.list.TIntList
    public void transformValues(TIntFunction function) {
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

    @Override // gnu.trove.list.TIntList
    public void reverse() {
        reverse(0, this._pos);
    }

    @Override // gnu.trove.list.TIntList
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

    @Override // gnu.trove.list.TIntList
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
        int tmp = this._data[i];
        this._data[i] = this._data[j];
        this._data[j] = tmp;
    }

    @Override // gnu.trove.list.TIntList
    public TIntList subList(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end index " + end + " greater than begin index " + begin);
        }
        if (begin < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        if (end > this._data.length) {
            throw new IndexOutOfBoundsException("end index < " + this._data.length);
        }
        TIntArrayList list = new TIntArrayList(end - begin);
        for (int i = begin; i < end; i++) {
            list.add(this._data[i]);
        }
        return list;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int[] toArray() {
        return toArray(0, this._pos);
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int offset, int len) {
        int[] rv = new int[len];
        toArray(rv, offset, len);
        return rv;
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public int[] toArray(int[] dest) {
        int len = dest.length;
        if (dest.length > this._pos) {
            len = this._pos;
            dest[len] = this.no_entry_value;
        }
        toArray(dest, 0, len);
        return dest;
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int[] dest, int offset, int len) {
        if (len == 0) {
            return dest;
        }
        if (offset < 0 || offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(this._data, offset, dest, 0, len);
        return dest;
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        System.arraycopy(this._data, source_pos, dest, dest_pos, len);
        return dest;
    }

    @Override // gnu.trove.TIntCollection
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof TIntArrayList) {
            TIntArrayList that = (TIntArrayList) other;
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

    @Override // gnu.trove.TIntCollection
    public int hashCode() {
        int h = 0;
        int i = this._pos;
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                h += HashFunctions.hash(this._data[i]);
            } else {
                return h;
            }
        }
    }

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean forEach(TIntProcedure procedure) {
        for (int i = 0; i < this._pos; i++) {
            if (!procedure.execute(this._data[i])) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.list.TIntList
    public boolean forEachDescending(TIntProcedure procedure) {
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

    @Override // gnu.trove.list.TIntList
    public void sort() {
        Arrays.sort(this._data, 0, this._pos);
    }

    @Override // gnu.trove.list.TIntList
    public void sort(int fromIndex, int toIndex) {
        Arrays.sort(this._data, fromIndex, toIndex);
    }

    @Override // gnu.trove.list.TIntList
    public void fill(int val) {
        Arrays.fill(this._data, 0, this._pos, val);
    }

    @Override // gnu.trove.list.TIntList
    public void fill(int fromIndex, int toIndex, int val) {
        if (toIndex > this._pos) {
            ensureCapacity(toIndex);
            this._pos = toIndex;
        }
        Arrays.fill(this._data, fromIndex, toIndex, val);
    }

    @Override // gnu.trove.list.TIntList
    public int binarySearch(int value) {
        return binarySearch(value, 0, this._pos);
    }

    @Override // gnu.trove.list.TIntList
    public int binarySearch(int value, int fromIndex, int toIndex) {
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
            int midVal = this._data[mid];
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

    @Override // gnu.trove.list.TIntList
    public int indexOf(int value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TIntList
    public int indexOf(int offset, int value) {
        for (int i = offset; i < this._pos; i++) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override // gnu.trove.list.TIntList
    public int lastIndexOf(int value) {
        return lastIndexOf(this._pos, value);
    }

    @Override // gnu.trove.list.TIntList
    public int lastIndexOf(int offset, int value) {
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

    @Override // gnu.trove.list.TIntList, gnu.trove.TIntCollection
    public boolean contains(int value) {
        return lastIndexOf(value) >= 0;
    }

    @Override // gnu.trove.list.TIntList
    public TIntList grep(TIntProcedure condition) {
        TIntArrayList list = new TIntArrayList();
        for (int i = 0; i < this._pos; i++) {
            if (condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }

    @Override // gnu.trove.list.TIntList
    public TIntList inverseGrep(TIntProcedure condition) {
        TIntArrayList list = new TIntArrayList();
        for (int i = 0; i < this._pos; i++) {
            if (!condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }

    @Override // gnu.trove.list.TIntList
    public int max() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find maximum of an empty list");
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < this._pos; i++) {
            if (this._data[i] > max) {
                max = this._data[i];
            }
        }
        return max;
    }

    @Override // gnu.trove.list.TIntList
    public int min() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find minimum of an empty list");
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this._pos; i++) {
            if (this._data[i] < min) {
                min = this._data[i];
            }
        }
        return min;
    }

    @Override // gnu.trove.list.TIntList
    public int sum() {
        int sum = 0;
        for (int i = 0; i < this._pos; i++) {
            sum += this._data[i];
        }
        return sum;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        int end = this._pos - 1;
        for (int i = 0; i < end; i++) {
            buf.append(this._data[i]);
            buf.append(", ");
        }
        if (size() > 0) {
            buf.append(this._data[this._pos - 1]);
        }
        buf.append("}");
        return buf.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/array/TIntArrayList$TIntArrayIterator.class */
    public class TIntArrayIterator implements TIntIterator {
        private int cursor;
        int lastRet = -1;

        TIntArrayIterator(int index) {
            this.cursor = 0;
            this.cursor = index;
        }

        @Override // gnu.trove.iterator.TIterator, java.util.Iterator
        public boolean hasNext() {
            return this.cursor < TIntArrayList.this.size();
        }

        @Override // gnu.trove.iterator.TIntIterator
        public int next() {
            try {
                int next = TIntArrayList.this.get(this.cursor);
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
                TIntArrayList.this.remove(this.lastRet, 1);
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
        out.writeInt(this.no_entry_value);
        int len = this._data.length;
        out.writeInt(len);
        for (int i = 0; i < len; i++) {
            out.writeInt(this._data[i]);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._pos = in.readInt();
        this.no_entry_value = in.readInt();
        int len = in.readInt();
        this._data = new int[len];
        for (int i = 0; i < len; i++) {
            this._data[i] = in.readInt();
        }
    }
}
