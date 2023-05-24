package fj.data;

import fj.Equal;
import fj.F;
import fj.Function;
import fj.Hash;
import fj.P;
import fj.P2;
import fj.Unit;
import fj.data.List;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/HashMap.class */
public final class HashMap<K, V> implements Iterable<K> {
    private final java.util.HashMap<HashMap<K, V>.Key<K>, V> m;
    private final Equal<K> e;
    private final Hash<K> h;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/HashMap$Key.class */
    public final class Key<K> {
        private final K k;
        private final Equal<K> e;
        private final Hash<K> h;

        Key(K k, Equal<K> e, Hash<K> h) {
            this.k = k;
            this.e = e;
            this.h = h;
        }

        K k() {
            return this.k;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public boolean equals(Object o) {
            return (o instanceof Key) && this.e.eq(this.k, ((Key) o).k());
        }

        public int hashCode() {
            return this.h.hash((Hash<K>) this.k);
        }
    }

    @Override // java.lang.Iterable
    public Iterator<K> iterator() {
        return keys().iterator();
    }

    public HashMap(Equal<K> e, Hash<K> h) {
        this.m = new java.util.HashMap<>();
        this.e = e;
        this.h = h;
    }

    public HashMap(Map<K, V> map, Equal<K> e, Hash<K> h) {
        this(e, h);
        for (K key : map.keySet()) {
            set(key, map.get(key));
        }
    }

    public HashMap(Equal<K> e, Hash<K> h, int initialCapacity) {
        this.m = new java.util.HashMap<>(initialCapacity);
        this.e = e;
        this.h = h;
    }

    public HashMap(Map<K, V> map) {
        this(map, Equal.anyEqual(), Hash.anyHash());
    }

    public HashMap(Equal<K> e, Hash<K> h, int initialCapacity, float loadFactor) {
        this.m = new java.util.HashMap<>(initialCapacity, loadFactor);
        this.e = e;
        this.h = h;
    }

    public static <K, V> HashMap<K, V> hashMap() {
        Equal<K> e = Equal.anyEqual();
        Hash<K> h = Hash.anyHash();
        return new HashMap<>(e, h);
    }

    public boolean eq(K k1, K k2) {
        return this.e.eq(k1, k2);
    }

    public int hash(K k) {
        return this.h.hash((Hash<K>) k);
    }

    public Option<V> get(K k) {
        return Option.fromNull(this.m.get(new Key(k, this.e, this.h)));
    }

    public F<K, Option<V>> get() {
        return new F<K, Option<V>>() { // from class: fj.data.HashMap.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public Option<V> f(K k) {
                return HashMap.this.get(k);
            }
        };
    }

    public void clear() {
        this.m.clear();
    }

    public boolean contains(K k) {
        return this.m.containsKey(new Key(k, this.e, this.h));
    }

    public List<K> keys() {
        List.Buffer<K> b = new List.Buffer<>();
        for (HashMap<K, V>.Key<K> k : this.m.keySet()) {
            b.snoc(k.k());
        }
        return b.toList();
    }

    public List<V> values() {
        return (List<V>) keys().map((F<K, V>) new F<K, V>() { // from class: fj.data.HashMap.2
            @Override // fj.F
            public V f(K k) {
                return (V) HashMap.this.m.get(new Key(k, HashMap.this.e, HashMap.this.h));
            }
        });
    }

    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    public int size() {
        return this.m.size();
    }

    public void set(K k, V v) {
        if (v != null) {
            this.m.put(new Key<>(k, this.e, this.h), v);
        }
    }

    public void delete(K k) {
        this.m.remove(new Key(k, this.e, this.h));
    }

    public Option<V> getDelete(K k) {
        return Option.fromNull(this.m.remove(new Key(k, this.e, this.h)));
    }

    public <A, B> HashMap<A, B> map(F<K, A> keyFunction, F<V, B> valueFunction, Equal<A> equal, Hash<A> hash) {
        HashMap<A, B> hashMap = new HashMap<>(equal, hash);
        Iterator<K> it = keys().iterator();
        while (it.hasNext()) {
            K key = it.next();
            A newKey = keyFunction.f(key);
            B newValue = valueFunction.f(get(key).some());
            hashMap.set(newKey, newValue);
        }
        return hashMap;
    }

    public <A, B> HashMap<A, B> map(F<K, A> keyFunction, F<V, B> valueFunction) {
        return map(keyFunction, valueFunction, Equal.anyEqual(), Hash.anyHash());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B> HashMap<A, B> map(F<P2<K, V>, P2<A, B>> function, Equal<A> equal, Hash<A> hash) {
        return from(toStream().map(function), equal, hash);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B> HashMap<A, B> map(F<P2<K, V>, P2<A, B>> function) {
        return from(toStream().map(function));
    }

    public <A> HashMap<A, V> mapKeys(F<K, A> keyFunction, Equal<A> equal, Hash<A> hash) {
        return (HashMap<A, V>) map(keyFunction, Function.identity(), equal, hash);
    }

    public <A> HashMap<A, V> mapKeys(F<K, A> function) {
        return mapKeys(function, Equal.anyEqual(), Hash.anyHash());
    }

    public <B> HashMap<K, B> mapValues(F<V, B> function) {
        return (HashMap<K, B>) map(Function.identity(), function, (Equal<K>) this.e, (Hash<K>) this.h);
    }

    public void foreachDoEffect(Effect1<P2<K, V>> effect) {
        toStream().foreachDoEffect(effect);
    }

    public void foreach(F<P2<K, V>, Unit> function) {
        toStream().foreach(function);
    }

    public List<P2<K, V>> toList() {
        return (List<P2<K, V>>) keys().map((F<K, P2<K, V>>) new F<K, P2<K, V>>() { // from class: fj.data.HashMap.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            @Override // fj.F
            public P2<K, V> f(K k) {
                return P.p(k, HashMap.this.get(k).some());
            }
        });
    }

    public Collection<P2<K, V>> toCollection() {
        return toList().toCollection();
    }

    public Stream<P2<K, V>> toStream() {
        return toList().toStream();
    }

    public Option<P2<K, V>> toOption() {
        return toList().toOption();
    }

    public Array<P2<K, V>> toArray() {
        return toList().toArray();
    }

    public Map<K, V> toMap() {
        java.util.HashMap<K, V> result = new java.util.HashMap<>();
        Iterator<K> it = keys().iterator();
        while (it.hasNext()) {
            K key = it.next();
            result.put(key, get(key).some());
        }
        return result;
    }

    public static <K, V> HashMap<K, V> from(Iterable<P2<K, V>> entries) {
        return from(entries, Equal.anyEqual(), Hash.anyHash());
    }

    public static <K, V> HashMap<K, V> from(Iterable<P2<K, V>> entries, Equal<K> equal, Hash<K> hash) {
        HashMap<K, V> map = new HashMap<>(equal, hash);
        for (P2<K, V> entry : entries) {
            map.set(entry._1(), entry._2());
        }
        return map;
    }
}
