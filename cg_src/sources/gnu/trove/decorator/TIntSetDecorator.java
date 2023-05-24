package gnu.trove.decorator;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/decorator/TIntSetDecorator.class */
public class TIntSetDecorator extends AbstractSet<Integer> implements Set<Integer>, Externalizable {
    static final long serialVersionUID = 1;
    protected TIntSet _set;

    public TIntSetDecorator() {
    }

    public TIntSetDecorator(TIntSet set) {
        this._set = set;
    }

    public TIntSet getSet() {
        return this._set;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(Integer value) {
        return value != null && this._set.add(value.intValue());
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public boolean equals(Object other) {
        int v;
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
                    if (val instanceof Integer) {
                        v = ((Integer) val).intValue();
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
        return (value instanceof Integer) && this._set.remove(((Integer) value).intValue());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() { // from class: gnu.trove.decorator.TIntSetDecorator.1
            private final TIntIterator it;

            {
                this.it = TIntSetDecorator.this._set.iterator();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Integer next() {
                return Integer.valueOf(this.it.next());
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
        if (o instanceof Integer) {
            return this._set.contains(((Integer) o).intValue());
        }
        return false;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._set = (TIntSet) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(this._set);
    }
}
