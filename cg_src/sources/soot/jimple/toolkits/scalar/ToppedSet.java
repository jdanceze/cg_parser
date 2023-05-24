package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.List;
import soot.toolkits.scalar.AbstractFlowSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/ToppedSet.class */
public class ToppedSet<T> extends AbstractFlowSet<T> {
    protected final FlowSet<T> underlyingSet;
    protected boolean isTop;

    public ToppedSet(FlowSet<T> under) {
        this.underlyingSet = under;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    /* renamed from: clone */
    public ToppedSet<T> mo2534clone() {
        ToppedSet<T> newSet = new ToppedSet<>(this.underlyingSet.mo2534clone());
        newSet.setTop(isTop());
        return newSet;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void copy(FlowSet<T> d) {
        if (this != d) {
            ToppedSet<T> dest = (ToppedSet) d;
            dest.isTop = this.isTop;
            if (!isTop()) {
                this.underlyingSet.copy(dest.underlyingSet);
            }
        }
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public FlowSet<T> emptySet() {
        return new ToppedSet(this.underlyingSet.emptySet());
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void clear() {
        this.isTop = false;
        this.underlyingSet.clear();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void union(FlowSet<T> o, FlowSet<T> d) {
        if ((o instanceof ToppedSet) && (d instanceof ToppedSet)) {
            ToppedSet<T> other = (ToppedSet) o;
            ToppedSet<T> dest = (ToppedSet) d;
            if (isTop()) {
                copy(dest);
                return;
            } else if (other.isTop()) {
                other.copy(dest);
                return;
            } else {
                this.underlyingSet.union(other.underlyingSet, dest.underlyingSet);
                dest.setTop(false);
                return;
            }
        }
        super.union(o, d);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void intersection(FlowSet<T> o, FlowSet<T> d) {
        if (isTop()) {
            o.copy(d);
            return;
        }
        ToppedSet<T> other = (ToppedSet) o;
        ToppedSet<T> dest = (ToppedSet) d;
        if (other.isTop()) {
            copy(dest);
            return;
        }
        this.underlyingSet.intersection(other.underlyingSet, dest.underlyingSet);
        dest.setTop(false);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void difference(FlowSet<T> o, FlowSet<T> d) {
        ToppedSet<T> other = (ToppedSet) o;
        ToppedSet<T> dest = (ToppedSet) d;
        if (isTop()) {
            if (other.isTop()) {
                dest.clear();
            } else if (other.underlyingSet instanceof BoundedFlowSet) {
                ((BoundedFlowSet) other.underlyingSet).complement(dest);
            } else {
                throw new RuntimeException("can't take difference!");
            }
        } else if (other.isTop()) {
            dest.clear();
        } else {
            this.underlyingSet.difference(other.underlyingSet, dest.underlyingSet);
        }
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean isEmpty() {
        if (isTop()) {
            return false;
        }
        return this.underlyingSet.isEmpty();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public int size() {
        if (isTop()) {
            throw new UnsupportedOperationException();
        }
        return this.underlyingSet.size();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void add(T obj) {
        if (!isTop()) {
            this.underlyingSet.add(obj);
        }
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void remove(T obj) {
        if (!isTop()) {
            this.underlyingSet.remove(obj);
        }
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean contains(T obj) {
        if (isTop()) {
            return true;
        }
        return this.underlyingSet.contains(obj);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public List<T> toList() {
        if (isTop()) {
            throw new UnsupportedOperationException();
        }
        return this.underlyingSet.toList();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet
    public int hashCode() {
        int hash = (97 * 7) + this.underlyingSet.hashCode();
        return (97 * hash) + (isTop() ? 1 : 0);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ToppedSet<?> other = (ToppedSet) obj;
        return isTop() == other.isTop() && this.underlyingSet.equals(other.underlyingSet);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet
    public String toString() {
        return isTop() ? "{TOP}" : this.underlyingSet.toString();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet, java.lang.Iterable
    public Iterator<T> iterator() {
        if (isTop()) {
            throw new UnsupportedOperationException();
        }
        return this.underlyingSet.iterator();
    }

    public void setTop(boolean top) {
        this.isTop = top;
    }

    public boolean isTop() {
        return this.isTop;
    }
}
