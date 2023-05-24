package org.apache.tools.ant.util;

import java.util.Enumeration;
import java.util.Hashtable;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LazyHashtable.class */
public class LazyHashtable<K, V> extends Hashtable<K, V> {
    protected boolean initAllDone = false;

    protected void initAll() {
        if (this.initAllDone) {
            return;
        }
        this.initAllDone = true;
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Enumeration<V> elements() {
        initAll();
        return super.elements();
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public boolean isEmpty() {
        initAll();
        return super.isEmpty();
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public int size() {
        initAll();
        return super.size();
    }

    @Override // java.util.Hashtable
    public boolean contains(Object value) {
        initAll();
        return super.contains(value);
    }

    @Override // java.util.Hashtable, java.util.Map
    public boolean containsKey(Object value) {
        initAll();
        return super.containsKey(value);
    }

    @Override // java.util.Hashtable, java.util.Map
    public boolean containsValue(Object value) {
        return contains(value);
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Enumeration<K> keys() {
        initAll();
        return super.keys();
    }
}
