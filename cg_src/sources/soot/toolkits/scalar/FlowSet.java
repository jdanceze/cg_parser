package soot.toolkits.scalar;

import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowSet.class */
public interface FlowSet<T> extends Iterable<T> {
    @Override // 
    /* renamed from: clone */
    FlowSet<T> mo2534clone();

    FlowSet<T> emptySet();

    void copy(FlowSet<T> flowSet);

    void clear();

    void union(FlowSet<T> flowSet);

    void union(FlowSet<T> flowSet, FlowSet<T> flowSet2);

    void intersection(FlowSet<T> flowSet);

    void intersection(FlowSet<T> flowSet, FlowSet<T> flowSet2);

    void difference(FlowSet<T> flowSet);

    void difference(FlowSet<T> flowSet, FlowSet<T> flowSet2);

    boolean isEmpty();

    int size();

    void add(T t);

    void add(T t, FlowSet<T> flowSet);

    void remove(T t);

    void remove(T t, FlowSet<T> flowSet);

    boolean contains(T t);

    boolean isSubSet(FlowSet<T> flowSet);

    @Override // java.lang.Iterable
    Iterator<T> iterator();

    List<T> toList();

    default void copyFreshToExisting(FlowSet<T> dest) {
        copy(dest);
    }
}
