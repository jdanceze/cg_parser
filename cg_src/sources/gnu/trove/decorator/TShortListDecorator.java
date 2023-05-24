package gnu.trove.decorator;

import gnu.trove.list.TShortList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TShortListDecorator.class */
public class TShortListDecorator extends AbstractList<Short> implements List<Short>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TShortList list;

    public TShortListDecorator() {
    }

    public TShortListDecorator(TShortList list) {
        this.list = list;
    }

    public TShortList getList() {
        return this.list;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Short get(int index) {
        short value = this.list.get(index);
        if (value == this.list.getNoEntryValue()) {
            return null;
        }
        return Short.valueOf(value);
    }

    @Override // java.util.AbstractList, java.util.List
    public Short set(int index, Short value) {
        short previous_value = this.list.set(index, value.shortValue());
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Short.valueOf(previous_value);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Short value) {
        this.list.insert(index, value.shortValue());
    }

    @Override // java.util.AbstractList, java.util.List
    public Short remove(int index) {
        short previous_value = this.list.removeAt(index);
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Short.valueOf(previous_value);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.list = (TShortList) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this.list);
    }
}
