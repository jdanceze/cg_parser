package gnu.trove.decorator;

import gnu.trove.list.TDoubleList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TDoubleListDecorator.class */
public class TDoubleListDecorator extends AbstractList<Double> implements List<Double>, Externalizable, Cloneable {
    static final long serialVersionUID = 1;
    protected TDoubleList list;

    public TDoubleListDecorator() {
    }

    public TDoubleListDecorator(TDoubleList list) {
        this.list = list;
    }

    public TDoubleList getList() {
        return this.list;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.list.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Double get(int index) {
        double value = this.list.get(index);
        if (value == this.list.getNoEntryValue()) {
            return null;
        }
        return Double.valueOf(value);
    }

    @Override // java.util.AbstractList, java.util.List
    public Double set(int index, Double value) {
        double previous_value = this.list.set(index, value.doubleValue());
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Double.valueOf(previous_value);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Double value) {
        this.list.insert(index, value.doubleValue());
    }

    @Override // java.util.AbstractList, java.util.List
    public Double remove(int index) {
        double previous_value = this.list.removeAt(index);
        if (previous_value == this.list.getNoEntryValue()) {
            return null;
        }
        return Double.valueOf(previous_value);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this.list = (TDoubleList) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this.list);
    }
}
