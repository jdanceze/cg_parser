package gnu.trove.decorator;

import gnu.trove.list.TIntList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TIntListDecorator.class */
public class TIntListDecorator extends AbstractList<Integer> implements List<Integer>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TIntList list;

    public TIntListDecorator() {
    }

    public TIntListDecorator(TIntList list) {
        this.list = list;
    }

    public TIntList getList() {
        return this.list;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Integer get(int index) {
        int value = this.list.get(index);
        if (value == this.list.getNoEntryValue()) {
            return null;
        }
        return Integer.valueOf(value);
    }

    @Override // java.util.AbstractList, java.util.List
    public Integer set(int index, Integer value) {
        int previous_value = this.list.set(index, value.intValue());
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Integer.valueOf(previous_value);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Integer value) {
        this.list.insert(index, value.intValue());
    }

    @Override // java.util.AbstractList, java.util.List
    public Integer remove(int index) {
        int previous_value = this.list.removeAt(index);
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Integer.valueOf(previous_value);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.list = (TIntList) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this.list);
    }
}
