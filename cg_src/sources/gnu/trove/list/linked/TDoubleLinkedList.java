package gnu.trove.list.linked;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.list.TDoubleList;
import gnu.trove.procedure.TDoubleProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TDoubleLinkedList.class */
public class TDoubleLinkedList implements TDoubleList, Externalizable {
    double no_entry_value;
    int size;
    TDoubleLink head = null;
    TDoubleLink tail = this.head;

    public TDoubleLinkedList() {
    }

    public TDoubleLinkedList(double no_entry_value) {
        this.no_entry_value = no_entry_value;
    }

    public TDoubleLinkedList(TDoubleList list) {
        this.no_entry_value = list.getNoEntryValue();
        TDoubleIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            double next = iterator.next();
            add(next);
        }
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public double getNoEntryValue() {
        return this.no_entry_value;
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public int size() {
        return this.size;
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public boolean add(double val) {
        TDoubleLink l = new TDoubleLink(val);
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

    @Override // gnu.trove.list.TDoubleList
    public void add(double[] vals) {
        for (double val : vals) {
            add(val);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void add(double[] vals, int offset, int length) {
        for (int i = 0; i < length; i++) {
            double val = vals[offset + i];
            add(val);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double value) {
        TDoubleLinkedList tmp = new TDoubleLinkedList();
        tmp.add(value);
        insert(offset, tmp);
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double[] values) {
        insert(offset, link(values, 0, values.length));
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double[] values, int valOffset, int len) {
        insert(offset, link(values, valOffset, len));
    }

    void insert(int offset, TDoubleLinkedList tmp) {
        TDoubleLink l = getLinkAt(offset);
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
            TDoubleLink prev = l.getPrevious();
            l.getPrevious().setNext(tmp.head);
            tmp.tail.setNext(l);
            l.setPrevious(tmp.tail);
            tmp.head.setPrevious(prev);
        }
    }

    static TDoubleLinkedList link(double[] values, int valOffset, int len) {
        TDoubleLinkedList ret = new TDoubleLinkedList();
        for (int i = 0; i < len; i++) {
            ret.add(values[valOffset + i]);
        }
        return ret;
    }

    @Override // gnu.trove.list.TDoubleList
    public double get(int offset) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TDoubleLink l = getLinkAt(offset);
        if (no(l)) {
            return this.no_entry_value;
        }
        return l.getValue();
    }

    public TDoubleLink getLinkAt(int offset) {
        if (offset >= size()) {
            return null;
        }
        if (offset <= (size() >>> 1)) {
            return getLink(this.head, 0, offset, true);
        }
        return getLink(this.tail, size() - 1, offset, false);
    }

    private static TDoubleLink getLink(TDoubleLink l, int idx, int offset) {
        return getLink(l, idx, offset, true);
    }

    private static TDoubleLink getLink(TDoubleLink l, int idx, int offset, boolean next) {
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

    @Override // gnu.trove.list.TDoubleList
    public double set(int offset, double val) {
        if (offset > this.size) {
            throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + this.size);
        }
        TDoubleLink l = getLinkAt(offset);
        if (no(l)) {
            throw new IndexOutOfBoundsException("at offset " + offset);
        }
        double prev = l.getValue();
        l.setValue(val);
        return prev;
    }

    @Override // gnu.trove.list.TDoubleList
    public void set(int offset, double[] values) {
        set(offset, values, 0, values.length);
    }

    @Override // gnu.trove.list.TDoubleList
    public void set(int offset, double[] values, int valOffset, int length) {
        for (int i = 0; i < length; i++) {
            double value = values[valOffset + i];
            set(offset + i, value);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public double replace(int offset, double val) {
        return set(offset, val);
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public void clear() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public boolean remove(double value) {
        boolean changed = false;
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (l.getValue() == value) {
                    changed = true;
                    removeLink(l);
                }
                tDoubleLink = l.getNext();
            } else {
                return changed;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeLink(TDoubleLink l) {
        if (no(l)) {
            return;
        }
        this.size--;
        TDoubleLink prev = l.getPrevious();
        TDoubleLink next = l.getNext();
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

    @Override // gnu.trove.TDoubleCollection
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty()) {
            return false;
        }
        for (Object o : collection) {
            if (o instanceof Double) {
                Double i = (Double) o;
                if (!contains(i.doubleValue())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean containsAll(TDoubleCollection collection) {
        if (isEmpty()) {
            return false;
        }
        TDoubleIterator it = collection.iterator();
        while (it.hasNext()) {
            double i = it.next();
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean containsAll(double[] array) {
        if (isEmpty()) {
            return false;
        }
        for (double i : array) {
            if (!contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean addAll(Collection<? extends Double> collection) {
        boolean ret = false;
        for (Double v : collection) {
            if (add(v.doubleValue())) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean addAll(TDoubleCollection collection) {
        boolean ret = false;
        TDoubleIterator it = collection.iterator();
        while (it.hasNext()) {
            double i = it.next();
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean addAll(double[] array) {
        boolean ret = false;
        for (double i : array) {
            if (add(i)) {
                ret = true;
            }
        }
        return ret;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        TDoubleIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(Double.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean retainAll(TDoubleCollection collection) {
        boolean modified = false;
        TDoubleIterator iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean retainAll(double[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TDoubleIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) < 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        TDoubleIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(Double.valueOf(iter.next()))) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean removeAll(TDoubleCollection collection) {
        boolean modified = false;
        TDoubleIterator iter = iterator();
        while (iter.hasNext()) {
            if (collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean removeAll(double[] array) {
        Arrays.sort(array);
        boolean modified = false;
        TDoubleIterator iter = iterator();
        while (iter.hasNext()) {
            if (Arrays.binarySearch(array, iter.next()) >= 0) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // gnu.trove.list.TDoubleList
    public double removeAt(int offset) {
        TDoubleLink l = getLinkAt(offset);
        if (no(l)) {
            throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
        }
        double prev = l.getValue();
        removeLink(l);
        return prev;
    }

    @Override // gnu.trove.list.TDoubleList
    public void remove(int offset, int length) {
        for (int i = 0; i < length; i++) {
            removeAt(offset);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void transformValues(TDoubleFunction function) {
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                l.setValue(function.execute(l.getValue()));
                tDoubleLink = l.getNext();
            } else {
                return;
            }
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void reverse() {
        TDoubleLink h = this.head;
        TDoubleLink t = this.tail;
        TDoubleLink l = this.head;
        while (got(l)) {
            TDoubleLink next = l.getNext();
            TDoubleLink prev = l.getPrevious();
            TDoubleLink tmp = l;
            l = l.getNext();
            tmp.setNext(prev);
            tmp.setPrevious(next);
        }
        this.head = t;
        this.tail = h;
    }

    @Override // gnu.trove.list.TDoubleList
    public void reverse(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("from > to : " + from + ">" + to);
        }
        TDoubleLink start = getLinkAt(from);
        TDoubleLink stop = getLinkAt(to);
        TDoubleLink tmp = null;
        TDoubleLink tmpHead = start.getPrevious();
        TDoubleLink l = start;
        while (l != stop) {
            TDoubleLink next = l.getNext();
            TDoubleLink prev = l.getPrevious();
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

    @Override // gnu.trove.list.TDoubleList
    public void shuffle(Random rand) {
        for (int i = 0; i < this.size; i++) {
            TDoubleLink l = getLinkAt(rand.nextInt(size()));
            removeLink(l);
            add(l.getValue());
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList subList(int begin, int end) {
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
        TDoubleLinkedList ret = new TDoubleLinkedList();
        TDoubleLink tmp = getLinkAt(begin);
        for (int i = begin; i < end; i++) {
            ret.add(tmp.getValue());
            tmp = tmp.getNext();
        }
        return ret;
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public double[] toArray() {
        return toArray(new double[this.size], 0, this.size);
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(int offset, int len) {
        return toArray(new double[len], offset, 0, len);
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public double[] toArray(double[] dest) {
        return toArray(dest, 0, this.size);
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(double[] dest, int offset, int len) {
        return toArray(dest, offset, 0, len);
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(double[] dest, int source_pos, int dest_pos, int len) {
        if (len == 0) {
            return dest;
        }
        if (source_pos < 0 || source_pos >= size()) {
            throw new ArrayIndexOutOfBoundsException(source_pos);
        }
        TDoubleLink tmp = getLinkAt(source_pos);
        for (int i = 0; i < len; i++) {
            dest[dest_pos + i] = tmp.getValue();
            tmp = tmp.getNext();
        }
        return dest;
    }

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public boolean forEach(TDoubleProcedure procedure) {
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tDoubleLink = l.getNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public boolean forEachDescending(TDoubleProcedure procedure) {
        TDoubleLink tDoubleLink = this.tail;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (procedure.execute(l.getValue())) {
                    tDoubleLink = l.getPrevious();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void sort() {
        sort(0, this.size);
    }

    @Override // gnu.trove.list.TDoubleList
    public void sort(int fromIndex, int toIndex) {
        TDoubleList tmp = subList(fromIndex, toIndex);
        double[] vals = tmp.toArray();
        Arrays.sort(vals);
        set(fromIndex, vals);
    }

    @Override // gnu.trove.list.TDoubleList
    public void fill(double val) {
        fill(0, this.size, val);
    }

    @Override // gnu.trove.list.TDoubleList
    public void fill(int fromIndex, int toIndex, double val) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        TDoubleLink l = getLinkAt(fromIndex);
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

    @Override // gnu.trove.list.TDoubleList
    public int binarySearch(double value) {
        return binarySearch(value, 0, size());
    }

    @Override // gnu.trove.list.TDoubleList
    public int binarySearch(double value, int fromIndex, int toIndex) {
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
        TDoubleLink fromLink = getLinkAt(fromIndex);
        int to = toIndex;
        while (from < to) {
            int mid = (from + to) >>> 1;
            TDoubleLink middle = getLink(fromLink, from, mid);
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

    @Override // gnu.trove.list.TDoubleList
    public int indexOf(double value) {
        return indexOf(0, value);
    }

    @Override // gnu.trove.list.TDoubleList
    public int indexOf(int offset, double value) {
        int count = offset;
        TDoubleLink linkAt = getLinkAt(offset);
        while (true) {
            TDoubleLink l = linkAt;
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

    @Override // gnu.trove.list.TDoubleList
    public int lastIndexOf(double value) {
        return lastIndexOf(0, value);
    }

    @Override // gnu.trove.list.TDoubleList
    public int lastIndexOf(int offset, double value) {
        if (isEmpty()) {
            return -1;
        }
        int last = -1;
        int count = offset;
        TDoubleLink linkAt = getLinkAt(offset);
        while (true) {
            TDoubleLink l = linkAt;
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

    @Override // gnu.trove.list.TDoubleList, gnu.trove.TDoubleCollection
    public boolean contains(double value) {
        if (isEmpty()) {
            return false;
        }
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (l.getValue() != value) {
                    tDoubleLink = l.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // gnu.trove.TDoubleCollection
    public TDoubleIterator iterator() {
        return new TDoubleIterator() { // from class: gnu.trove.list.linked.TDoubleLinkedList.1
            TDoubleLink l;
            TDoubleLink current;

            {
                this.l = TDoubleLinkedList.this.head;
            }

            @Override // gnu.trove.iterator.TDoubleIterator
            public double next() {
                if (TDoubleLinkedList.no(this.l)) {
                    throw new NoSuchElementException();
                }
                double ret = this.l.getValue();
                this.current = this.l;
                this.l = this.l.getNext();
                return ret;
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public boolean hasNext() {
                return TDoubleLinkedList.got(this.l);
            }

            @Override // gnu.trove.iterator.TIterator, java.util.Iterator
            public void remove() {
                if (this.current != null) {
                    TDoubleLinkedList.this.removeLink(this.current);
                    this.current = null;
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList grep(TDoubleProcedure condition) {
        TDoubleList ret = new TDoubleLinkedList();
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tDoubleLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList inverseGrep(TDoubleProcedure condition) {
        TDoubleList ret = new TDoubleLinkedList();
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (!condition.execute(l.getValue())) {
                    ret.add(l.getValue());
                }
                tDoubleLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public double max() {
        double ret = Double.NEGATIVE_INFINITY;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (ret < l.getValue()) {
                    ret = l.getValue();
                }
                tDoubleLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public double min() {
        double ret = Double.POSITIVE_INFINITY;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                if (ret > l.getValue()) {
                    ret = l.getValue();
                }
                tDoubleLink = l.getNext();
            } else {
                return ret;
            }
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public double sum() {
        double sum = 0.0d;
        TDoubleLink tDoubleLink = this.head;
        while (true) {
            TDoubleLink l = tDoubleLink;
            if (got(l)) {
                sum += l.getValue();
                tDoubleLink = l.getNext();
            } else {
                return sum;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TDoubleLinkedList$TDoubleLink.class */
    public static class TDoubleLink {
        double value;
        TDoubleLink previous;
        TDoubleLink next;

        TDoubleLink(double value) {
            this.value = value;
        }

        public double getValue() {
            return this.value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public TDoubleLink getPrevious() {
            return this.previous;
        }

        public void setPrevious(TDoubleLink previous) {
            this.previous = previous;
        }

        public TDoubleLink getNext() {
            return this.next;
        }

        public void setNext(TDoubleLink next) {
            this.next = next;
        }
    }

    /* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/linked/TDoubleLinkedList$RemoveProcedure.class */
    class RemoveProcedure implements TDoubleProcedure {
        boolean changed = false;

        RemoveProcedure() {
        }

        @Override // gnu.trove.procedure.TDoubleProcedure
        public boolean execute(double value) {
            if (TDoubleLinkedList.this.remove(value)) {
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
        out.writeDouble(this.no_entry_value);
        out.writeInt(this.size);
        TDoubleIterator iterator = iterator();
        while (iterator.hasNext()) {
            double next = iterator.next();
            out.writeDouble(next);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.no_entry_value = in.readDouble();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            add(in.readDouble());
        }
    }

    static boolean got(Object ref) {
        return ref != null;
    }

    static boolean no(Object ref) {
        return ref == null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004c  */
    @Override // gnu.trove.TDoubleCollection
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
            gnu.trove.list.linked.TDoubleLinkedList r0 = (gnu.trove.list.linked.TDoubleLinkedList) r0
            r7 = r0
            r0 = r5
            double r0 = r0.no_entry_value
            r1 = r7
            double r1 = r1.no_entry_value
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
            gnu.trove.iterator.TDoubleIterator r0 = r0.iterator()
            r8 = r0
            r0 = r7
            gnu.trove.iterator.TDoubleIterator r0 = r0.iterator()
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
            double r0 = r0.next()
            r1 = r9
            double r1 = r1.next()
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L43
            r0 = 0
            return r0
        L6b:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.trove.list.linked.TDoubleLinkedList.equals(java.lang.Object):boolean");
    }

    @Override // gnu.trove.TDoubleCollection
    public int hashCode() {
        int result = HashFunctions.hash(this.no_entry_value);
        int result2 = (31 * result) + this.size;
        TDoubleIterator iterator = iterator();
        while (iterator.hasNext()) {
            result2 = (31 * result2) + HashFunctions.hash(iterator.next());
        }
        return result2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        TDoubleIterator it = iterator();
        while (it.hasNext()) {
            double next = it.next();
            buf.append(next);
            if (it.hasNext()) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}
