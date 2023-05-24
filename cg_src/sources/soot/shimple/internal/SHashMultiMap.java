package soot.shimple.internal;

import java.util.LinkedHashSet;
import java.util.Set;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/SHashMultiMap.class */
public class SHashMultiMap<K, V> extends HashMultiMap<K, V> {
    private static final long serialVersionUID = -860669798578291979L;

    public SHashMultiMap() {
    }

    public SHashMultiMap(MultiMap<K, V> m) {
        super(m);
    }

    @Override // soot.util.HashMultiMap
    protected Set<V> newSet() {
        return new LinkedHashSet(4);
    }
}
