package polyglot.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/NilMap.class */
public final class NilMap implements Map {
    public static final NilMap EMPTY_MAP = new NilMap();

    private NilMap() {
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return false;
    }

    @Override // java.util.Map
    public boolean containsValue(Object val) {
        return false;
    }

    @Override // java.util.Map
    public Set entrySet() {
        return Collections.EMPTY_SET;
    }

    @Override // java.util.Map
    public int hashCode() {
        return 0;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return true;
    }

    @Override // java.util.Map
    public Set keySet() {
        return Collections.EMPTY_SET;
    }

    @Override // java.util.Map
    public int size() {
        return 0;
    }

    @Override // java.util.Map
    public Collection values() {
        return Collections.EMPTY_SET;
    }

    @Override // java.util.Map
    public Object get(Object k) {
        return null;
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        return (o instanceof Map) && ((Map) o).size() == 0;
    }

    @Override // java.util.Map
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public void putAll(Map t) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public Object remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public Object put(Object o1, Object o2) {
        throw new UnsupportedOperationException();
    }
}
