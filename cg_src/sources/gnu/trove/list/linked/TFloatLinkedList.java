package gnu.trove.list.linked;

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
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TFloatLinkedList.class */
public class TFloatLinkedList implements TFloatList, Externalizable {
    float no_entry_value;
    int size;
    TFloatLink head = null;
    TFloatLink tail = this.head;

    public TFloatLinkedList() {
    }

    public TFloatLinkedList(float no_entry_value) {
        this.no_entry_value = no_entry_value;
    }

    public TFloatLinkedList(TFloatList list) {
        this.no_entry_value = list.getNoEntryValue();
        TFloatIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            float next = iterator.next();
            add(next);
        }
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public float getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public int size() {
        return this.size;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean add(float val) {
        TFloatLink l = new TFloatLink(val);
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

    @Override // gnu.trove.list.TFloatList
    public void add(float[] vals) {
        for (float val : vals) {
            add(val);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void add(float[] vals, int offset, int length) {
        for (int i = 0; i < length; i++) {
            float val = vals[offset + i];
            add(val);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float value) {
        TFloatLinkedList tmp = new TFloatLinkedList();
        tmp.add(value);
        insert(offset, tmp);
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float[] values) {
        insert(offset, link(values, 0, values.length));
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float[] values, int valOffset, int len) {
        insert(offset, link(values, valOffset, len));
    }

    void insert(int offset, TFloatLinkedList tmp) {
        TFloatLink l = getLinkAt(offset);
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
            TFloatLink prev = l.getPrevious();
            l.getPrevious().setNext(tmp.head);
            tmp.tail.setNext(l);
            l.setPrevious(tmp.tail);
            tmp.head.setPrevious(prev);
        }
    }

    static TFloatLinkedList link(float[] values, int valOffset, int len) {
        TFloatLinkedList ret = new TFloatLinkedList();
        for (int i = 0; i < len; i++) {
            ret.add(values[valOffset + i]);
        }
        return ret;
    }

    @Override // gnu.trove.list.TFloatList
    public float get(int offset) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TFloatLink l = getLinkAt(offset);
        if (no(l)) {
            return this.no_entry_value;
        }
        return l.getValue();
    }

    public TFloatLink getLinkAt(int offset) {
        if (offset >= size()) {
            return null;
        }
        if (offset <= (size() >>> 1)) {
            return getLink(this.head, 0, offset, true);
        }
        return getLink(this.tail, size() - 1, offset, false);
    }

    private static TFloatLink getLink(TFloatLink l, int idx, int offset) {
        return getLink(l, idx, offset, true);
    }

    private static TFloatLink getLink(TFloatLink l, int idx, int offset, boolean next) {
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

    @Override // gnu.trove.list.TFloatList
    public float set(int offset, float val) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TFloatLink l = getLinkAt(offset);
        if (no(l)) {
            throw new IndexOutOfBoundsException("at offset " + offset);
        }
        float prev = l.getValue();
        l.setValue(val);
        return prev;
    }

    @Override // gnu.trove.list.TFloatList
    public void set(int offset, float[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TFloatList
    public void set(int offset, float[] values, int valOffset, int length) {
        for (int i = 0; i < length; i++) {
            float value = values[valOffset + i];
            set(offset + i, value);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public float replace(int offset, float val) {
        return set(offset, val);
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public void clear() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean remove(float value) {
        boolean changed = false;
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (l.getValue() == value) {
                    changed = true;
                    removeLink(l);
                }
                tFloatLink = l.getNext();
            } else {
                return changed;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLink(TFloatLink l) {
        if (no(l)) {
            return;
        }
        this.size--;
        TFloatLink prev = l.getPrevious();
        TFloatLink next = l.getNext();
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

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty()) {
            return false;
        }
        for (Object o : collection) {
            if (o instanceof Float) {
                Float i = (Float) o;
                if (!contains(i.floatValue())) {
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
        if (isEmpty()) {
            return false;
        }
        TFloatIterator it = collection.iterator();
        while (it.hasNext()) {
            float i = it.next();
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean containsAll(float[] array) {
        if (isEmpty()) {
            return false;
        }
        for (float i : array) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(Collection<? extends Float> collection) {
        boolean ret = false;
        for (Float v : collection) {
            if (add(v.floatValue())) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(TFloatCollection collection) {
        boolean ret = false;
        TFloatIterator it = collection.iterator();
        while (it.hasNext()) {
            float i = it.next();
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean addAll(float[] array) {
        boolean ret = false;
        for (float i : array) {
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
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
        Arrays.sort(array);
        boolean modified = false;
        TFloatIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) < 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        TFloatIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(Float.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(TFloatCollection collection) {
        boolean modified = false;
        TFloatIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean removeAll(float[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TFloatIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) >= 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.list.TFloatList
    public float removeAt(int offset) {
        TFloatLink l = getLinkAt(offset);
        if (no(l)) {
            throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
        }
        float prev = l.getValue();
        removeLink(l);
        return prev;
    }

    @Override // gnu.trove.list.TFloatList
    public void remove(int offset, int length) {
        for (int i = 0; i < length; i++) {
            removeAt(offset);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void transformValues(TFloatFunction function) {
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                l.setValue(function.execute(l.getValue()));
                tFloatLink = l.getNext();
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void reverse() {
        TFloatLink h = this.head;
        TFloatLink t = this.tail;
        TFloatLink l = this.head;
        while (got(l)) {
            TFloatLink next = l.getNext();
            TFloatLink prev = l.getPrevious();
            TFloatLink tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        this.head = t;
        this.tail = h;
    }

    @Override // gnu.trove.list.TFloatList
    public void reverse(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to : " + from + ">" + to);
        }
        TFloatLink start = getLinkAt(from);
        TFloatLink stop = getLinkAt(to);
        TFloatLink tmp = null;
        TFloatLink tmpHead = start.getPrevious();
        TFloatLink l = start;
        while (l != stop) {
            TFloatLink next = l.getNext();
            TFloatLink prev = l.getPrevious();
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

    @Override // gnu.trove.list.TFloatList
    public void shuffle(Random rand) {
        for (int i = 0; i < this.size; i++) {
            TFloatLink l = getLinkAt(rand.nextInt(size()));
            removeLink(l);
            add(l.getValue());
        }
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList subList(int begin, int end) {
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
        TFloatLinkedList ret = new TFloatLinkedList();
        TFloatLink tmp = getLinkAt(begin);
        for (int i = begin; i < end; i++) {
            ret.add(tmp.getValue());
            tmp = tmp.getNext();
        }
        return ret;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public float[] toArray() {
        return toArray(new float[this.size], 0, this.size);
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(int offset, int len) {
        return toArray(new float[len], offset, 0, len);
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public float[] toArray(float[] dest) {
        return toArray(dest, 0, this.size);
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(float[] dest, int offset, int len) {
        return toArray(dest, offset, 0, len);
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(float[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= size()) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        TFloatLink tmp = getLinkAt(source_pos);
        for (int i = 0; i < len; i++) {
            dest[dest_pos + i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return dest;
    }

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean forEach(TFloatProcedure procedure) {
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tFloatLink = l.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TFloatList
    public boolean forEachDescending(TFloatProcedure procedure) {
        TFloatLink tFloatLink = this.tail;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tFloatLink = l.getPrevious();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void sort() {
        sort(0, this.size);
    }

    @Override // gnu.trove.list.TFloatList
    public void sort(int fromIndex, int toIndex) {
        TFloatList tmp = subList(fromIndex, toIndex);
        float[] vals = tmp.toArray();
        Arrays.sort(vals);
        set(fromIndex, vals);
    }

    @Override // gnu.trove.list.TFloatList
    public void fill(float val) {
        fill(0, this.size, val);
    }

    @Override // gnu.trove.list.TFloatList
    public void fill(int fromIndex, int toIndex, float val) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        TFloatLink l = getLinkAt(fromIndex);
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

    @Override // gnu.trove.list.TFloatList
    public int binarySearch(float value) {
        return binarySearch(value, 0, size());
    }

    @Override // gnu.trove.list.TFloatList
    public int binarySearch(float value, int fromIndex, int toIndex) {
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
        TFloatLink fromLink = getLinkAt(fromIndex);
        int to = toIndex;
        while (from < to) {
            int mid = (from + to) >>> 1;
            TFloatLink middle = getLink(fromLink, from, mid);
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

    @Override // gnu.trove.list.TFloatList
    public int indexOf(float value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TFloatList
    public int indexOf(int offset, float value) {
        int count = offset;
        TFloatLink linkAt = getLinkAt(offset);
        while (true) {
            TFloatLink l = linkAt;
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

    @Override // gnu.trove.list.TFloatList
    public int lastIndexOf(float value) {
        return lastIndexOf(0, value);
    }

    @Override // gnu.trove.list.TFloatList
    public int lastIndexOf(int offset, float value) {
        if (isEmpty()) {
            return -1;
        }
        int last = -1;
        int count = offset;
        TFloatLink linkAt = getLinkAt(offset);
        while (true) {
            TFloatLink l = linkAt;
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

    @Override // gnu.trove.list.TFloatList, gnu.trove.TFloatCollection
    public boolean contains(float value) {
        if (isEmpty()) {
            return false;
        }
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (l.getValue() != value) {
                    tFloatLink = l.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.TFloatCollection
    public TFloatIterator iterator() {
        return new TFloatIterator() { // from class: gnu.trove.list.linked.TFloatLinkedList.1
            TFloatLink l;
            TFloatLink current;

            {
                this.l = TFloatLinkedList.this.head;
            }

            @Override // gnu.trove.iterator.TFloatIterator
            public float next() {
                if (TFloatLinkedList.no(this.l)) {
                    throw new NoSuchElementException();
                }
                float ret = this.l.getValue();
                this.current = this.l;
                this.l = this.l.getNext();
                return ret;
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return TFloatLinkedList.got(this.l);
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this.current != null) {
                    TFloatLinkedList.this.removeLink(this.current);
                    this.current = null;
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList grep(TFloatProcedure condition) {
        TFloatList ret = new TFloatLinkedList();
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tFloatLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList inverseGrep(TFloatProcedure condition) {
        TFloatList ret = new TFloatLinkedList();
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (!condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tFloatLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TFloatList
    public float max() {
        float ret = Float.NEGATIVE_INFINITY;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (ret < l.getValue()) {
                    ret = l.getValue();
                }
                tFloatLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TFloatList
    public float min() {
        float ret = Float.POSITIVE_INFINITY;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                if (ret > l.getValue()) {
                    ret = l.getValue();
                }
                tFloatLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TFloatList
    public float sum() {
        float sum = 0.0f;
        TFloatLink tFloatLink = this.head;
        while (true) {
            TFloatLink l = tFloatLink;
            if (got(l)) {
                sum += l.getValue();
                tFloatLink = l.getNext();
            } else {
                return sum;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TFloatLinkedList$TFloatLink.class */
    public static class TFloatLink {
        float value;
        TFloatLink previous;
        TFloatLink next;

        TFloatLink(float value) {
            this.value = value;
        }

        public float getValue() {
            return this.value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public TFloatLink getPrevious() {
            return this.previous;
        }

        public void setPrevious(TFloatLink previous) {
            this.previous = previous;
        }

        public TFloatLink getNext() {
            return this.next;
        }

        public void setNext(TFloatLink next) {
            this.next = next;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TFloatLinkedList$RemoveProcedure.class */
    class RemoveProcedure implements TFloatProcedure {
        boolean changed = false;

        RemoveProcedure() {
        }

        @Override // gnu.trove.procedure.TFloatProcedure
        public boolean execute(float value) {
            if (TFloatLinkedList.this.remove(value)) {
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
        out.writeFloat(this.no_entry_value);
        out.writeInt(this.size);
        TFloatIterator iterator = iterator();
        while (iterator.hasNext()) {
            float next = iterator.next();
            out.writeFloat(next);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.no_entry_value = in.readFloat();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            add(in.readFloat());
        }
    }

    static boolean got(Object ref) {
        return ref != null;
    }

    static boolean no(Object ref) {
        return ref == null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004c  */
    @Override // gnu.trove.TFloatCollection
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
            gnu.trove.list.linked.TFloatLinkedList r0 = (gnu.trove.list.linked.TFloatLinkedList) r0
            r5 = r0
            r0 = r3
            float r0 = r0.no_entry_value
            r1 = r5
            float r1 = r1.no_entry_value
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L2b
            r0 = 0
            return r0
        L2b:
            r0 = r3
            int r0 = r0.size
            r1 = r5
            int r1 = r1.size
            if (r0 == r1) goto L38
            r0 = 0
            return r0
        L38:
            r0 = r3
            gnu.trove.iterator.TFloatIterator r0 = r0.iterator()
            r6 = r0
            r0 = r5
            gnu.trove.iterator.TFloatIterator r0 = r0.iterator()
            r7 = r0
        L43:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L6b
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L58
            r0 = 0
            return r0
        L58:
            r0 = r6
            float r0 = r0.next()
            r1 = r7
            float r1 = r1.next()
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L43
            r0 = 0
            return r0
        L6b:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.list.linked.TFloatLinkedList.equals(java.lang.Object):boolean");
    }

    @Override // gnu.trove.TFloatCollection
    public int hashCode() {
        int result = HashFunctions.hash(this.no_entry_value);
        int result2 = (31 * result) + this.size;
        TFloatIterator iterator = iterator();
        while (iterator.hasNext()) {
            result2 = (31 * result2) + HashFunctions.hash(iterator.next());
        }
        return result2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        TFloatIterator it = iterator();
        while (it.hasNext()) {
            float next = it.next();
            buf.append(next);
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
