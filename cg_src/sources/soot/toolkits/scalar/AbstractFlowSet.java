package soot.toolkits.scalar;

import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/AbstractFlowSet.class */
public abstract class AbstractFlowSet<T> implements FlowSet<T> {
    @Override // soot.toolkits.scalar.FlowSet
    /* renamed from: clone */
    public abstract AbstractFlowSet<T> mo2534clone();

    @Override // soot.toolkits.scalar.FlowSet
    public abstract boolean isEmpty();

    @Override // soot.toolkits.scalar.FlowSet
    public abstract int size();

    @Override // soot.toolkits.scalar.FlowSet
    public abstract void add(T t);

    @Override // soot.toolkits.scalar.FlowSet
    public abstract void remove(T t);

    @Override // soot.toolkits.scalar.FlowSet
    public abstract boolean contains(T t);

    @Override // soot.toolkits.scalar.FlowSet, java.lang.Iterable
    public abstract Iterator<T> iterator();

    @Override // soot.toolkits.scalar.FlowSet
    public abstract List<T> toList();

    @Override // soot.toolkits.scalar.FlowSet
    public FlowSet<T> emptySet() {
        FlowSet<T> t = mo2534clone();
        t.clear();
        return t;
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void copy(FlowSet<T> dest) {
        if (this == dest) {
            return;
        }
        dest.clear();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            dest.add(t);
        }
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void clear() {
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            remove(t);
        }
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void union(FlowSet<T> other) {
        if (this == other) {
            return;
        }
        union(other, this);
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void union(FlowSet<T> other, FlowSet<T> dest) {
        if (dest != this && dest != other) {
            dest.clear();
        }
        if (dest != null && dest != this) {
            Iterator<T> it = iterator();
            while (it.hasNext()) {
                T t = it.next();
                dest.add(t);
            }
        }
        if (other != null && dest != other) {
            for (T t2 : other) {
                dest.add(t2);
            }
        }
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void intersection(FlowSet<T> other) {
        if (this == other) {
            return;
        }
        intersection(other, this);
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void intersection(FlowSet<T> other, FlowSet<T> dest) {
        FlowSet<T> elements;
        FlowSet<T> flowSet;
        if (dest == this && dest == other) {
            return;
        }
        if (dest == this) {
            elements = this;
            flowSet = other;
        } else {
            elements = other;
            flowSet = this;
        }
        dest.clear();
        for (T t : elements) {
            if (flowSet.contains(t)) {
                dest.add(t);
            }
        }
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void difference(FlowSet<T> other) {
        difference(other, this);
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void difference(FlowSet<T> other, FlowSet<T> dest) {
        if (dest == this && dest == other) {
            dest.clear();
            return;
        }
        FlowSet<T> flowSet = other == dest ? other.mo2534clone() : other;
        dest.clear();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            if (!flowSet.contains(t)) {
                dest.add(t);
            }
        }
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void add(T obj, FlowSet<T> dest) {
        if (dest != this) {
            copy(dest);
        }
        dest.add(obj);
    }

    @Override // soot.toolkits.scalar.FlowSet
    public void remove(T obj, FlowSet<T> dest) {
        if (dest != this) {
            copy(dest);
        }
        dest.remove(obj);
    }

    @Override // soot.toolkits.scalar.FlowSet
    public boolean isSubSet(FlowSet<T> other) {
        if (other == this) {
            return true;
        }
        for (T t : other) {
            if (!contains(t)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object o) {
        if (!(o instanceof FlowSet)) {
            return false;
        }
        FlowSet<T> other = (FlowSet) o;
        if (size() != other.size()) {
            return false;
        }
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            if (!other.contains(t)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = 1;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            result += t.hashCode();
        }
        return result;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("{");
        boolean isFirst = true;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T t = it.next();
            if (!isFirst) {
                buffer.append(", ");
            }
            isFirst = false;
            buffer.append(t);
        }
        buffer.append("}");
        return buffer.toString();
    }
}
