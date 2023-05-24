package soot;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/AbstractUnitAllMapTo.class */
public class AbstractUnitAllMapTo<K, V> extends AbstractMap<K, V> {
    protected V dest;

    public AbstractUnitAllMapTo(V dest) {
        this.dest = dest;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        return this.dest;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
