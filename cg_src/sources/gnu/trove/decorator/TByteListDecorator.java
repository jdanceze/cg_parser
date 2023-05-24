package gnu.trove.decorator;

import gnu.trove.list.TByteList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TByteListDecorator.class */
public class TByteListDecorator extends AbstractList<Byte> implements List<Byte>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TByteList list;

    public TByteListDecorator() {
    }

    public TByteListDecorator(TByteList list) {
        this.list = list;
    }

    public TByteList getList() {
        return this.list;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Byte get(int index) {
        byte value = this.list.get(index);
        if (value == this.list.getNoEntryValue()) {
            return null;
        }
        return Byte.valueOf(value);
    }

    @Override // java.util.AbstractList, java.util.List
    public Byte set(int index, Byte value) {
        byte previous_value = this.list.set(index, value.byteValue());
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Byte.valueOf(previous_value);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Byte value) {
        this.list.insert(index, value.byteValue());
    }

    @Override // java.util.AbstractList, java.util.List
    public Byte remove(int index) {
        byte previous_value = this.list.removeAt(index);
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Byte.valueOf(previous_value);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.list = (TByteList) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this.list);
    }
}
