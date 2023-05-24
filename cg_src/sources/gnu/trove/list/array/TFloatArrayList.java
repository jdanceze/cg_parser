package gnu.trove.list.array;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.list.TFloatList;
import gnu.trove.procedure.TFloatProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/array/TFloatArrayList.class */
public class TFloatArrayList implements TFloatList, Externalizable {
    static final long serialVersionUID = 1;
    protected float[] _data;
    protected int _pos;
    protected static final int DEFAULT_CAPACITY = 10;
    protected float no_entry_value;

    public TFloatArrayList() {
        this(10, 0.0f);
    }

    public TFloatArrayList(int capacity) {
        this(capacity, 0.0f);
    }

    public TFloatArrayList(int capacity, float no_entry_value) {
        this._data = new float[capacity];
        this._pos = 0;
        this.no_entry_value = no_entry_value;
    }

    public TFloatArrayList(TFloatCollection collection) {
        this(collection.size());
        addAll(collection);
    }

    public TFloatArrayList(float[] values) {
        this(values.length);
        add(values);
    }

    protected TFloatArrayList(float[] values, float no_entry_value, boolean wrap) {
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

    public static TFloatArrayList wrap(float[] values) {
        return wrap(values, 0.0f);
    }

    public static TFloatArrayList wrap(float[] values, float no_entry_value) {
        return new TFloatArrayList(values, no_entry_value, true) { // from class: gnu.trove.list.array.TFloatArrayList.1
            @Override // gnu.trove.list.array.TFloatArrayList
            public void ensureCapacity(int capacity) {
                if (capacity > this._data.length) {
                    throw new IllegalStateException("Can not grow ArrayList wrapped external array");
                }
            }
        };
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public float getNoEntryValue() {
        return this.no_entry_value;
    }

    public void ensureCapacity(int capacity) {
        if (capacity > this._data.length) {
            int newCap = Math.max(this._data.length << 1, capacity);
            float[] tmp = new float[newCap];
            System.arraycopy(this._data, 0, tmp, 0, this._data.length);
            this._data = tmp;
        }
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public int size() {
        return this._pos;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean isEmpty() {
        return this._pos == 0;
    }

    public void trimToSize() {
        if (this._data.length > size()) {
            float[] tmp = new float[size()];
            toArray(tmp, 0, tmp.length);
            this._data = tmp;
        }
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean add(float val) {
        ensureCapacity(this._pos + 1);
        float[] fArr = this._data;
        int i = this._pos;
        this._pos = i + 1;
        fArr[i] = val;
        return true;
    }

    @Override // gnu.trove.list.TFloatList
    public void add(float[] vals) {
        add(vals, 0, vals.length);
    }

    @Override // gnu.trove.list.TFloatList
    public void add(float[] vals, int offset, int length) {
        ensureCapacity(this._pos + length);
        System.arraycopy(vals, offset, this._data, this._pos, length);
        this._pos += length;
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float value) {
        if (offset == this._pos) {
            add(value);
            return;
        }
        ensureCapacity(this._pos + 1);
        System.arraycopy(this._data, offset, this._data, offset + 1, this._pos - offset);
        this._data[offset] = value;
        this._pos++;
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float[] values) {
        insert(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float[] values, int valOffset, int len) {
        if (offset == this._pos) {
            add(values, valOffset, len);
            return;
        }
        ensureCapacity(this._pos + len);
        System.arraycopy(this._data, offset, this._data, offset + len, this._pos - offset);
        System.arraycopy(values, valOffset, this._data, offset, len);
        this._pos += len;
    }

    @Override // gnu.trove.list.TFloatList
    public float get(int offset) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return this._data[offset];
    }

    public float getQuick(int offset) {
        return this._data[offset];
    }

    @Override // gnu.trove.list.TFloatList
    public float set(int offset, float val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        float prev_val = this._data[offset];
        this._data[offset] = val;
        return prev_val;
    }

    @Override // gnu.trove.list.TFloatList
    public float replace(int offset, float val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        float old = this._data[offset];
        this._data[offset] = val;
        return old;
    }

    @Override // gnu.trove.list.TFloatList
    public void set(int offset, float[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TFloatList
    public void set(int offset, float[] values, int valOffset, int length) {
        if (offset < 0 || offset + length > this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(values, valOffset, this._data, offset, length);
    }

    public void setQuick(int offset, float val) {
        this._data[offset] = val;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public void clear() {
        clear(10);
    }

    public void clear(int capacity) {
        this._data = new float[capacity];
        this._pos = 0;
    }

    public void reset() {
        this._pos = 0;
        Arrays.fill(this._data, this.no_entry_value);
    }

    public void resetQuick() {
        this._pos = 0;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean remove(float value) {
        for (int index = 0; index < this._pos; index++) {
            if (value == this._data[index]) {
                remove(index, 1);
                return true;
            }
        }
        return false;
    }

    @Override // gnu.trove.list.TFloatList
    public float removeAt(int offset) {
        float old = get(offset);
        remove(offset, 1);
        return old;
    }

    @Override // gnu.trove.list.TFloatList
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

    @Override // gnu.trove.TFloatCollection
    public TFloatIterator iterator() {
        return new TFloatArrayIterator(0);
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (element instanceof Float) {
                float c = ((Float) element).floatValue();
                if (!contains(c)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(TFloatCollection collection) {
        if (this == collection) {
            return true;
        }
        TFloatIterator iter = collection.iterator();
        while (iter.hasNext()) {
            float element = iter.next();
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(float[] array) {
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

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(Collection<? extends Float> collection) {
        boolean changed = false;
        for (Float element : collection) {
            float e = element.floatValue();
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(TFloatCollection collection) {
        boolean changed = false;
        TFloatIterator iter = collection.iterator();
        while (iter.hasNext()) {
            float element = iter.next();
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(float[] array) {
        boolean changed = false;
        for (float element : array) {
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        TFloatIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(Float.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(TFloatCollection collection) {
        if (this == collection) {
            return false;
        }
        boolean modified = false;
        TFloatIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean retainAll(float[] array) {
        boolean changed = false;
        Arrays.sort(array);
        float[] data = this._data;
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

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        for (Object element : collection) {
            if (element instanceof Float) {
                float c = ((Float) element).floatValue();
                if (remove(c)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(TFloatCollection collection) {
        if (collection == this) {
            clear();
            return true;
        }
        boolean changed = false;
        TFloatIterator iter = collection.iterator();
        while (iter.hasNext()) {
            float element = iter.next();
            if (remove(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(float[] array) {
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

    @Override // gnu.trove.list.TFloatList
    public void transformValues(TFloatFunction function) {
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

    @Override // gnu.trove.list.TFloatList
    public void reverse() {
        reverse(0, this._pos);
    }

    @Override // gnu.trove.list.TFloatList
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

    @Override // gnu.trove.list.TFloatList
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
        float tmp = this._data[i];
        this._data[i] = this._data[j];
        this._data[j] = tmp;
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList subList(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end index " + end + " greater than begin index " + begin);
        }
        if (begin < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        if (end > this._data.length) {
            throw new IndexOutOfBoundsException("end index < " + this._data.length);
        }
        TFloatArrayList list = new TFloatArrayList(end - begin);
        for (int i = begin; i < end; i++) {
            list.add(this._data[i]);
        }
        return list;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public float[] toArray() {
        return toArray(0, this._pos);
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(int offset, int len) {
        float[] rv = new float[len];
        toArray(rv, offset, len);
        return rv;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public float[] toArray(float[] dest) {
        int len = dest.length;
        if (dest.length > this._pos) {
            len = this._pos;
            dest[len] = this.no_entry_value;
        }
        toArray(dest, 0, len);
        return dest;
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(float[] dest, int offset, int len) {
        if (len == 0) {
            return dest;
        }
        if (offset < 0 || offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(this._data, offset, dest, 0, len);
        return dest;
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(float[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        System.arraycopy(this._data, source_pos, dest, dest_pos, len);
        return dest;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof TFloatArrayList) {
            TFloatArrayList that = (TFloatArrayList) other;
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

    @Override // gnu.trove.TFloatCollection
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

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean forEach(TFloatProcedure procedure) {
        for (int i = 0; i < this._pos; i++) {
            if (!procedure.execute(this._data[i])) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.list.TFloatList
    public boolean forEachDescending(TFloatProcedure procedure) {
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

    @Override // gnu.trove.list.TFloatList
    public void sort() {
        Arrays.sort(this._data, 0, this._pos);
    }

    @Override // gnu.trove.list.TFloatList
    public void sort(int fromIndex, int toIndex) {
        Arrays.sort(this._data, fromIndex, toIndex);
    }

    @Override // gnu.trove.list.TFloatList
    public void fill(float val) {
        Arrays.fill(this._data, 0, this._pos, val);
    }

    @Override // gnu.trove.list.TFloatList
    public void fill(int fromIndex, int toIndex, float val) {
        if (toIndex > this._pos) {
            ensureCapacity(toIndex);
            this._pos = toIndex;
        }
        Arrays.fill(this._data, fromIndex, toIndex, val);
    }

    @Override // gnu.trove.list.TFloatList
    public int binarySearch(float value) {
        return binarySearch(value, 0, this._pos);
    }

    @Override // gnu.trove.list.TFloatList
    public int binarySearch(float value, int fromIndex, int toIndex) {
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
            float midVal = this._data[mid];
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

    @Override // gnu.trove.list.TFloatList
    public int indexOf(float value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TFloatList
    public int indexOf(int offset, float value) {
        for (int i = offset; i < this._pos; i++) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override // gnu.trove.list.TFloatList
    public int lastIndexOf(float value) {
        return lastIndexOf(this._pos, value);
    }

    @Override // gnu.trove.list.TFloatList
    public int lastIndexOf(int offset, float value) {
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

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean contains(float value) {
        return lastIndexOf(value) >= 0;
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList grep(TFloatProcedure condition) {
        TFloatArrayList list = new TFloatArrayList();
        for (int i = 0; i < this._pos; i++) {
            if (condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList inverseGrep(TFloatProcedure condition) {
        TFloatArrayList list = new TFloatArrayList();
        for (int i = 0; i < this._pos; i++) {
            if (!condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }

    @Override // gnu.trove.list.TFloatList
    public float max() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find maximum of an empty list");
        }
        float max = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < this._pos; i++) {
            if (this._data[i] > max) {
                max = this._data[i];
            }
        }
        return max;
    }

    @Override // gnu.trove.list.TFloatList
    public float min() {
        if (size() == 0) {
            throw new IllegalStateException("cannot find minimum of an empty list");
        }
        float min = Float.POSITIVE_INFINITY;
        for (int i = 0; i < this._pos; i++) {
            if (this._data[i] < min) {
                min = this._data[i];
            }
        }
        return min;
    }

    @Override // gnu.trove.list.TFloatList
    public float sum() {
        float sum = 0.0f;
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
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/array/TFloatArrayList$TFloatArrayIterator.class */
    public class TFloatArrayIterator implements TFloatIterator {
        private int cursor;
        int lastRet = -1;

        TFloatArrayIterator(int index) {
            this.cursor = 0;
            this.cursor = index;
        }

        @Override // gnu.trove.iterator.TIterator, java.util.Iterator
        public boolean hasNext() {
            return this.cursor < TFloatArrayList.this.size();
        }

        @Override // gnu.trove.iterator.TFloatIterator
        public float next() {
            try {
                float next = TFloatArrayList.this.get(this.cursor);
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
                TFloatArrayList.this.remove(this.lastRet, 1);
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
        out.writeFloat(this.no_entry_value);
        int len = this._data.length;
        out.writeInt(len);
        for (int i = 0; i < len; i++) {
            out.writeFloat(this._data[i]);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._pos = in.readInt();
        this.no_entry_value = in.readFloat();
        int len = in.readInt();
        this._data = new float[len];
        for (int i = 0; i < len; i++) {
            this._data[i] = in.readFloat();
        }
    }
}
