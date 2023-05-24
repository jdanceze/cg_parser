package gnu.trove.decorator;

import gnu.trove.list.TLongList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TLongListDecorator.class */
public class TLongListDecorator extends AbstractList<Long> implements List<Long>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TLongList list;

    public TLongListDecorator() {
    }

    public TLongListDecorator(TLongList list) {
        this.list = list;
    }

    public TLongList getList() {
        return this.list;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Long get(int index) {
        long value = this.list.get(index);
        if (value == this.list.getNoEntryValue()) {
            return null;
        }
        return Long.valueOf(value);
    }

    @Override // java.util.AbstractList, java.util.List
    public Long set(int index, Long value) {
        long previous_value = this.list.set(index, value.longValue());
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Long.valueOf(previous_value);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Long value) {
        this.list.insert(index, value.longValue());
    }

    @Override // java.util.AbstractList, java.util.List
    public Long remove(int index) {
        long previous_value = this.list.removeAt(index);
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Long.valueOf(previous_value);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.list = (TLongList) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this.list);
    }
}
