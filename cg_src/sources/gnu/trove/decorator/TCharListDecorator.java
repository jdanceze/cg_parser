package gnu.trove.decorator;

import gnu.trove.list.TCharList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TCharListDecorator.class */
public class TCharListDecorator extends AbstractList<Character> implements List<Character>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TCharList list;

    public TCharListDecorator() {
    }

    public TCharListDecorator(TCharList list) {
        this.list = list;
    }

    public TCharList getList() {
        return this.list;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Character get(int index) {
        char value = this.list.get(index);
        if (value == this.list.getNoEntryValue()) {
            return null;
        }
        return Character.valueOf(value);
    }

    @Override // java.util.AbstractList, java.util.List
    public Character set(int index, Character value) {
        char previous_value = this.list.set(index, value.charValue());
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Character.valueOf(previous_value);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Character value) {
        this.list.insert(index, value.charValue());
    }

    @Override // java.util.AbstractList, java.util.List
    public Character remove(int index) {
        char previous_value = this.list.removeAt(index);
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Character.valueOf(previous_value);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.list = (TCharList) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this.list);
    }
}
