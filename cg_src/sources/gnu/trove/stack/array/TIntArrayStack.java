package gnu.trove.stack.array;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.stack.TIntStack;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/array/TIntArrayStack.class */
public class TIntArrayStack implements TIntStack, Externalizable {
    static final long serialVersionUID = 1;
    protected TIntArrayList _list;
    public static final int DEFAULT_CAPACITY = 10;

    public TIntArrayStack() {
        this(10);
    }

    public TIntArrayStack(int capacity) {
        this._list = new TIntArrayList(capacity);
    }

    public TIntArrayStack(int capacity, int no_entry_value) {
        this._list = new TIntArrayList(capacity, no_entry_value);
    }

    public TIntArrayStack(TIntStack stack) {
        if (stack instanceof TIntArrayStack) {
            TIntArrayStack array_stack = (TIntArrayStack) stack;
            this._list = new TIntArrayList(array_stack._list);
            return;
        }
        throw new UnsupportedOperationException("Only support TIntArrayStack");
    }

    @Override // gnu.trove.stack.TIntStack
    public int getNoEntryValue() {
        return this._list.getNoEntryValue();
    }

    @Override // gnu.trove.stack.TIntStack
    public void push(int val) {
        this._list.add(val);
    }

    @Override // gnu.trove.stack.TIntStack
    public int pop() {
        return this._list.removeAt(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TIntStack
    public int peek() {
        return this._list.get(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TIntStack
    public int size() {
        return this._list.size();
    }

    @Override // gnu.trove.stack.TIntStack
    public void clear() {
        this._list.clear();
    }

    @Override // gnu.trove.stack.TIntStack
    public int[] toArray() {
        int[] retval = this._list.toArray();
        reverse(retval, 0, size());
        return retval;
    }

    @Override // gnu.trove.stack.TIntStack
    public void toArray(int[] dest) {
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

    private void reverse(int[] dest, int from, int to) {
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

    private void swap(int[] dest, int i, int j) {
        int tmp = dest[i];
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
        TIntArrayStack that = (TIntArrayStack) o;
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
        this._list = (TIntArrayList) in.readObject();
    }
}
