package soot.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/util/IdentityHashMultiMap.class */
public class IdentityHashMultiMap<K, V> extends HashMultiMap<K, V> {
    private static final long serialVersionUID = 4960774381646981495L;

    @Override // soot.util.HashMultiMap
    protected Map<K, Set<V>> createMap(int initialSize) {
        return new IdentityHashMap(initialSize);
    }

    @Override // soot.util.HashMultiMap
    protected Set<V> newSet() {
        return new IdentityHashSet();
    }
}
