package org.apache.tools.ant.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LinkedHashtable.class */
public class LinkedHashtable<K, V> extends Hashtable<K, V> {
    private static final long serialVersionUID = 1;
    private final LinkedHashMap<K, V> map;

    public LinkedHashtable() {
        this.map = new LinkedHashMap<>();
    }

    public LinkedHashtable(int initialCapacity) {
        this.map = new LinkedHashMap<>(initialCapacity);
    }

    public LinkedHashtable(int initialCapacity, float loadFactor) {
        this.map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    public LinkedHashtable(Map<K, V> m) {
        this.map = new LinkedHashMap<>(m);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized void clear() {
        this.map.clear();
    }

    @Override // java.util.Hashtable
    public boolean contains(Object value) {
        return containsKey(value);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized boolean containsKey(Object value) {
        return this.map.containsKey(value);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Enumeration<V> elements() {
        return Collections.enumeration(values());
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized boolean equals(Object o) {
        return this.map.equals(o);
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized V get(Object k) {
        return this.map.get(k);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized int hashCode() {
        return this.map.hashCode();
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Enumeration<K> keys() {
        return Collections.enumeration(keySet());
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized Set<K> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized V put(K k, V v) {
        return this.map.put(k, v);
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized V remove(Object k) {
        return this.map.remove(k);
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public synchronized int size() {
        return this.map.size();
    }

    @Override // java.util.Hashtable
    public synchronized String toString() {
        return this.map.toString();
    }

    @Override // java.util.Hashtable, java.util.Map
    public synchronized Collection<V> values() {
        return this.map.values();
    }
}
