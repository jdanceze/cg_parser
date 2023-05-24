package gnu.trove.stack.array;

import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.stack.TFloatStack;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/array/TFloatArrayStack.class */
public class TFloatArrayStack implements TFloatStack, Externalizable {
    static final long serialVersionUID = 1;
    protected TFloatArrayList _list;
    public static final int DEFAULT_CAPACITY = 10;

    public TFloatArrayStack() {
        this(10);
    }

    public TFloatArrayStack(int capacity) {
        this._list = new TFloatArrayList(capacity);
    }

    public TFloatArrayStack(int capacity, float no_entry_value) {
        this._list = new TFloatArrayList(capacity, no_entry_value);
    }

    public TFloatArrayStack(TFloatStack stack) {
        if (stack instanceof TFloatArrayStack) {
            TFloatArrayStack array_stack = (TFloatArrayStack) stack;
            this._list = new TFloatArrayList(array_stack._list);
            return;
        }
        throw new UnsupportedOperationException("Only support TFloatArrayStack");
    }

    @Override // gnu.trove.stack.TFloatStack
    public float getNoEntryValue() {
        return this._list.getNoEntryValue();
    }

    @Override // gnu.trove.stack.TFloatStack
    public void push(float val) {
        this._list.add(val);
    }

    @Override // gnu.trove.stack.TFloatStack
    public float pop() {
        return this._list.removeAt(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TFloatStack
    public float peek() {
        return this._list.get(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TFloatStack
    public int size() {
        return this._list.size();
    }

    @Override // gnu.trove.stack.TFloatStack
    public void clear() {
        this._list.clear();
    }

    @Override // gnu.trove.stack.TFloatStack
    public float[] toArray() {
        float[] retval = this._list.toArray();
        reverse(retval, 0, size());
        return retval;
    }

    @Override // gnu.trove.stack.TFloatStack
    public void toArray(float[] dest) {
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

    private void reverse(float[] dest, int from, int to) {
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

    private void swap(float[] dest, int i, int j) {
        float tmp = dest[i];
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
        TFloatArrayStack that = (TFloatArrayStack) o;
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
        this._list = (TFloatArrayList) in.readObject();
    }
}
