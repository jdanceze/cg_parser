package soot.util;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/util/WeakMapNumberer.class */
public class WeakMapNumberer<T extends Numberable> implements IterableNumberer<T> {
    final Map<T, Integer> map = new WeakHashMap();
    final Map<Integer, WeakReference<T>> rmap = new WeakHashMap();
    int nextIndex = 1;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ long get(Object obj) {
        return get((WeakMapNumberer<T>) ((Numberable) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return remove((WeakMapNumberer<T>) ((Numberable) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ void add(Object obj) {
        add((WeakMapNumberer<T>) ((Numberable) obj));
    }

    public synchronized void add(T o) {
        if (o.getNumber() == 0 && !this.map.containsKey(o)) {
            Integer key = Integer.valueOf(this.nextIndex);
            this.map.put(o, key);
            this.rmap.put(key, new WeakReference<>(o));
            int i = this.nextIndex;
            this.nextIndex = i + 1;
            o.setNumber(i);
        }
    }

    public boolean remove(T o) {
        if (o == null) {
            return false;
        }
        int num = o.getNumber();
        if (num == 0) {
            return false;
        }
        o.setNumber(0);
        Integer i = this.map.remove(o);
        if (i == null) {
            return false;
        }
        this.rmap.remove(i);
        return true;
    }

    public long get(T o) {
        if (o == null) {
            return 0L;
        }
        Integer i = this.map.get(o);
        if (i == null) {
            throw new RuntimeException("couldn't find " + o);
        }
        return i.intValue();
    }

    @Override // soot.util.Numberer
    public T get(long number) {
        if (number == 0) {
            return null;
        }
        return this.rmap.get(Integer.valueOf((int) number)).get();
    }

    @Override // soot.util.Numberer
    public int size() {
        return this.nextIndex - 1;
    }

    public boolean contains(T o) {
        return this.map.containsKey(o);
    }

    @Override // soot.util.IterableNumberer, java.lang.Iterable
    public Iterator<T> iterator() {
        return this.map.keySet().iterator();
    }
}
