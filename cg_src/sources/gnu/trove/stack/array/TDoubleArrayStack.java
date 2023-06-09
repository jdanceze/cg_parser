package gnu.trove.stack.array;

import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.stack.TDoubleStack;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/array/TDoubleArrayStack.class */
public class TDoubleArrayStack implements TDoubleStack, Externalizable {
    static final long serialVersionUID = 1;
    protected TDoubleArrayList _list;
    public static final int DEFAULT_CAPACITY = 10;

    public TDoubleArrayStack() {
        this(10);
    }

    public TDoubleArrayStack(int capacity) {
        this._list = new TDoubleArrayList(capacity);
    }

    public TDoubleArrayStack(int capacity, double no_entry_value) {
        this._list = new TDoubleArrayList(capacity, no_entry_value);
    }

    public TDoubleArrayStack(TDoubleStack stack) {
        if (stack instanceof TDoubleArrayStack) {
            TDoubleArrayStack array_stack = (TDoubleArrayStack) stack;
            this._list = new TDoubleArrayList(array_stack._list);
            return;
        }
        throw new UnsupportedOperationException("Only support TDoubleArrayStack");
    }

    @Override // gnu.trove.stack.TDoubleStack
    public double getNoEntryValue() {
        return this._list.getNoEntryValue();
    }

    @Override // gnu.trove.stack.TDoubleStack
    public void push(double val) {
        this._list.add(val);
    }

    @Override // gnu.trove.stack.TDoubleStack
    public double pop() {
        return this._list.removeAt(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TDoubleStack
    public double peek() {
        return this._list.get(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TDoubleStack
    public int size() {
        return this._list.size();
    }

    @Override // gnu.trove.stack.TDoubleStack
    public void clear() {
        this._list.clear();
    }

    @Override // gnu.trove.stack.TDoubleStack
    public double[] toArray() {
        double[] retval = this._list.toArray();
        reverse(retval, 0, size());
        return retval;
    }

    @Override // gnu.trove.stack.TDoubleStack
    public void toArray(double[] dest) {
        int size = size();
        int start = size - dest.length;
        if (start < 0) {
            start = 0;
        }
        int length = Math.min(size, dest.length);
        this._list.toArray(dest, start, length);
        reverse(dest, 0, length);
        if (dest.length > size) {
            dest[size] = this._list.getNoEntryValue();
        }
    }

    private void reverse(double[] dest, int from, int to) {
        if (from == to) {
            return;
        }
        if (from > to) {
            throw new IllegalArgumentException("from cannot be greater than to");
        }
        int i = from;
        for (int j = to - 1; i < j; j--) {
            swap(dest, i, j);
            i++;
        }
    }

    private void swap(double[] dest, int i, int j) {
        double tmp = dest[i];
        dest[i] = dest[j];
        dest[j] = tmp;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        for (int i = this._list.size() - 1; i > 0; i--) {
            buf.append(this._list.get(i));
            buf.append(", ");
        }
        if (size() > 0) {
            buf.append(this._list.get(0));
        }
        buf.append("}");
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TDoubleArrayStack that = (TDoubleArrayStack) o;
        return this._list.equals(that._list);
    }

    public int hashCode() {
        return this._list.hashCode();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._list);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._list = (TDoubleArrayList) in.readObject();
    }
}
