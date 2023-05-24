package polyglot.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/NestedMap.class */
public class NestedMap extends AbstractMap implements Map {
    private HashMap myMap;
    private int nShadowed;
    private Set setView;
    private Map superMap;
    private Predicate entryKeyNotInMyMap = new Predicate(this) { // from class: polyglot.util.NestedMap.1
        private final NestedMap this$0;

        {
            this.this$0 = this;
        }

        @Override // polyglot.util.Predicate
        public boolean isTrue(Object o) {
            Map.Entry ent = (Map.Entry) o;
            return !this.this$0.myMap.containsKey(ent.getKey());
        }
    };
    private Predicate keyNotInMyMap = new Predicate(this) { // from class: polyglot.util.NestedMap.2
        private final NestedMap this$0;

        {
            this.this$0 = this;
        }

        @Override // polyglot.util.Predicate
        public boolean isTrue(Object o) {
            return !this.this$0.myMap.containsKey(o);
        }
    };

    public NestedMap(Map containing) {
        this.superMap = containing == null ? NilMap.EMPTY_MAP : containing;
        this.myMap = new HashMap();
        this.setView = new EntrySet(this, null);
        this.nShadowed = 0;
    }

    public Map getContainingMap() {
        if (this.superMap instanceof NilMap) {
            return null;
        }
        return this.superMap;
    }

    public void release(Object key) {
        this.myMap.remove(key);
    }

    public Map getInnerMap() {
        return this.myMap;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set entrySet() {
        return this.setView;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return (this.superMap.size() + this.myMap.size()) - this.nShadowed;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return this.myMap.containsKey(key) || this.superMap.containsKey(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        if (this.myMap.containsKey(key)) {
            return this.myMap.get(key);
        }
        return this.superMap.get(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(Object key, Object value) {
        if (this.myMap.containsKey(key)) {
            return this.myMap.put(key, value);
        }
        Object oldV = this.superMap.get(key);
        this.myMap.put(key, value);
        this.nShadowed++;
        return oldV;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object remove(Object key) {
        throw new UnsupportedOperationException("Remove from NestedMap");
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        throw new UnsupportedOperationException("Clear in NestedMap");
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/NestedMap$KeySet.class */
    public final class KeySet extends AbstractSet {
        private final NestedMap this$0;

        public KeySet(NestedMap this$0) {
            this.this$0 = this$0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator iterator() {
            return new ConcatenatedIterator(this.this$0.myMap.keySet().iterator(), new FilteringIterator(this.this$0.superMap.keySet(), this.this$0.keyNotInMyMap));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return this.this$0.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Remove from NestedMap.keySet");
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/NestedMap$EntrySet.class */
    private final class EntrySet extends AbstractSet {
        private final NestedMap this$0;

        private EntrySet(NestedMap this$0) {
            this.this$0 = this$0;
        }

        EntrySet(NestedMap x0, AnonymousClass1 x1) {
            this(x0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator iterator() {
            return new ConcatenatedIterator(this.this$0.myMap.entrySet().iterator(), new FilteringIterator(this.this$0.superMap.entrySet(), this.this$0.entryKeyNotInMyMap));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry ent = (Map.Entry) o;
                Object entKey = ent.getKey();
                Object entVal = ent.getValue();
                if (entVal == null) {
                    return this.this$0.containsKey(entKey) && this.this$0.get(entKey) == null;
                }
                Object val = this.this$0.get(entKey);
                return val != null && val.equals(entVal);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Remove from NestedMap.entrySet");
        }
    }
}
