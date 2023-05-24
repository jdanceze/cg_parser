package soot.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/util/UnitMap.class */
public abstract class UnitMap<T> implements Map<Unit, T> {
    private final Map<Unit, T> unitToResult;

    protected abstract T mapTo(Unit unit);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Object put(Unit unit, Object obj) {
        return put2(unit, (Unit) obj);
    }

    public UnitMap(Body b) {
        this.unitToResult = new HashMap();
        map(b);
    }

    public UnitMap(UnitGraph g) {
        this(g.getBody());
    }

    public UnitMap(Body b, int initialCapacity) {
        this.unitToResult = new HashMap(initialCapacity);
        map(b);
    }

    public UnitMap(UnitGraph g, int initialCapacity) {
        this(g.getBody(), initialCapacity);
    }

    public UnitMap(Body b, int initialCapacity, float loadFactor) {
        this.unitToResult = new HashMap(initialCapacity);
        init();
        map(b);
    }

    public UnitMap(UnitGraph g, int initialCapacity, float loadFactor) {
        this(g.getBody(), initialCapacity);
    }

    private void map(Body b) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit currentUnit = it.next();
            T o = mapTo(currentUnit);
            if (o != null) {
                this.unitToResult.put(currentUnit, o);
            }
        }
    }

    protected void init() {
    }

    @Override // java.util.Map
    public void clear() {
        this.unitToResult.clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.unitToResult.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.unitToResult.containsValue(value);
    }

    @Override // java.util.Map
    public Set<Map.Entry<Unit, T>> entrySet() {
        return this.unitToResult.entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        return this.unitToResult.equals(o);
    }

    @Override // java.util.Map
    public T get(Object key) {
        return this.unitToResult.get(key);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.unitToResult.hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.unitToResult.isEmpty();
    }

    @Override // java.util.Map
    public Set<Unit> keySet() {
        return this.unitToResult.keySet();
    }

    /* renamed from: put  reason: avoid collision after fix types in other method */
    public T put2(Unit key, T value) {
        return this.unitToResult.put(key, value);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Unit, ? extends T> t) {
        this.unitToResult.putAll(t);
    }

    @Override // java.util.Map
    public T remove(Object key) {
        return this.unitToResult.remove(key);
    }

    @Override // java.util.Map
    public int size() {
        return this.unitToResult.size();
    }

    @Override // java.util.Map
    public Collection<T> values() {
        return this.unitToResult.values();
    }
}
