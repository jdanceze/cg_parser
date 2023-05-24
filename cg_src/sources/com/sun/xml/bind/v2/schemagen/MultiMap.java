package com.sun.xml.bind.v2.schemagen;

import java.lang.Comparable;
import java.util.Map;
import java.util.TreeMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/MultiMap.class */
public final class MultiMap<K extends Comparable<K>, V> extends TreeMap<K, V> {
    private final V many;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.TreeMap, java.util.AbstractMap, java.util.Map
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((MultiMap<K, V>) ((Comparable) obj), (Comparable) obj2);
    }

    public MultiMap(V many) {
        this.many = many;
    }

    public V put(K key, V value) {
        V old = (V) super.put((MultiMap<K, V>) key, (K) value);
        if (old != null && !old.equals(value)) {
            super.put((MultiMap<K, V>) key, (K) this.many);
        }
        return old;
    }

    @Override // java.util.TreeMap, java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
}
