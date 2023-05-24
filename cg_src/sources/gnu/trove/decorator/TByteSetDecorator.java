package gnu.trove.decorator;

import gnu.trove.iterator.TByteIterator;
import gnu.trove.set.TByteSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TByteSetDecorator.class */
public class TByteSetDecorator extends AbstractSet<Byte> implements Set<Byte>, Externalizable {
    static final long serialVersionUID = 1;
    protected TByteSet _set;

    public TByteSetDecorator() {
    }

    public TByteSetDecorator(TByteSet set) {
        this._set = set;
    }

    public TByteSet getSet() {
        return this._set;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(Byte value) {
        return value != null && this._set.add(value.byteValue());
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public boolean equals(Object other) {
        byte v;
        if (this._set.equals(other)) {
            return true;
        }
        if (other instanceof Set) {
            Set that = (Set) other;
            if (that.size() != this._set.size()) {
                return false;
            }
            Iterator it = that.iterator();
            int i = that.size();
            do {
                int i2 = i;
                i--;
                if (i2 > 0) {
                    Object val = it.next();
                    if (val instanceof Byte) {
                        v = ((Byte) val).byteValue();
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } while (this._set.contains(v));
            return false;
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this._set.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object value) {
        return (value instanceof Byte) && this._set.remove(((Byte) value).byteValue());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() { // from class: gnu.trove.decorator.TByteSetDecorator.1
            private final TByteIterator it;

            {
                this.it = TByteSetDecorator.this._set.iterator();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Byte next() {
                return Byte.valueOf(this.it.next());
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.it.hasNext();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.it.remove();
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this._set.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this._set.size() == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        if (o instanceof Byte) {
            return this._set.contains(((Byte) o).byteValue());
        }
        return false;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._set = (TByteSet) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._set);
    }
}
