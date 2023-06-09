package gnu.trove.stack.array;

import gnu.trove.list.array.TShortArrayList;
import gnu.trove.stack.TShortStack;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/stack/array/TShortArrayStack.class */
public class TShortArrayStack implements TShortStack, Externalizable {
    static final long serialVersionUID = 1;
    protected TShortArrayList _list;
    public static final int DEFAULT_CAPACITY = 10;

    public TShortArrayStack() {
        this(10);
    }

    public TShortArrayStack(int capacity) {
        this._list = new TShortArrayList(capacity);
    }

    public TShortArrayStack(int capacity, short no_entry_value) {
        this._list = new TShortArrayList(capacity, no_entry_value);
    }

    public TShortArrayStack(TShortStack stack) {
        if (stack instanceof TShortArrayStack) {
            TShortArrayStack array_stack = (TShortArrayStack) stack;
            this._list = new TShortArrayList(array_stack._list);
            return;
        }
        throw new UnsupportedOperationException("Only support TShortArrayStack");
    }

    @Override // gnu.trove.stack.TShortStack
    public short getNoEntryValue() {
        return this._list.getNoEntryValue();
    }

    @Override // gnu.trove.stack.TShortStack
    public void push(short val) {
        this._list.add(val);
    }

    @Override // gnu.trove.stack.TShortStack
    public short pop() {
        return this._list.removeAt(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TShortStack
    public short peek() {
        return this._list.get(this._list.size() - 1);
    }

    @Override // gnu.trove.stack.TShortStack
    public int size() {
        return this._list.size();
    }

    @Override // gnu.trove.stack.TShortStack
    public void clear() {
        this._list.clear();
    }

    @Override // gnu.trove.stack.TShortStack
    public short[] toArray() {
        short[] retval = this._list.toArray();
        reverse(retval, 0, size());
        return retval;
    }

    @Override // gnu.trove.stack.TShortStack
    public void toArray(short[] dest) {
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

    private void reverse(short[] dest, int from, int to) {
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

    private void swap(short[] dest, int i, int j) {
        short tmp = dest[i];
        dest[i] = dest[j];
        dest[j] = tmp;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("{");
        for (int i = this._list.size() - 1; i > 0; i--) {
            buf.append((int) this._list.get(i));
            buf.append(", ");
        }
        if (size() > 0) {
            buf.append((int) this._list.get(0));
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
        TShortArrayStack that = (TShortArrayStack) o;
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
        this._list = (TShortArrayList) in.readObject();
    }
}
