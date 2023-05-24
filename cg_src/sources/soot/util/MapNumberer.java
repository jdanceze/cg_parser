package soot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/util/MapNumberer.class */
public class MapNumberer<T> implements Numberer<T> {
    final Map<T, Integer> map = new HashMap();
    final ArrayList<T> al = new ArrayList<>();
    int nextIndex = 1;

    public MapNumberer() {
        this.al.add(null);
    }

    @Override // soot.util.Numberer
    public void add(T o) {
        if (!this.map.containsKey(o)) {
            this.map.put(o, Integer.valueOf(this.nextIndex));
            this.al.add(o);
            this.nextIndex++;
        }
    }

    @Override // soot.util.Numberer
    public T get(long number) {
        return this.al.get((int) number);
    }

    @Override // soot.util.Numberer
    public long get(Object o) {
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
    public int size() {
        return this.nextIndex - 1;
    }

    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    @Override // soot.util.Numberer
    public boolean remove(T o) {
        Integer i = this.map.remove(o);
        if (i == null) {
            return false;
        }
        this.al.set(i.intValue(), null);
        return true;
    }
}
