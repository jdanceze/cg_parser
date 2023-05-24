package fj.data;

import fj.F;
import fj.F1Functions;
import fj.Function;
import fj.Ord;
import fj.P;
import fj.P2;
import fj.P3;
import java.util.Iterator;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/TreeMap.class */
public final class TreeMap<K, V> implements Iterable<P2<K, V>> {
    private final Set<P2<K, Option<V>>> tree;

    private TreeMap(Set<P2<K, Option<V>>> tree) {
        this.tree = tree;
    }

    private static <K, V> Ord<P2<K, V>> ord(Ord<K> keyOrd) {
        return (Ord<P2<K, V>>) keyOrd.comap(P2.__1());
    }

    public static <K, V> TreeMap<K, V> empty(Ord<K> keyOrd) {
        return new TreeMap<>(Set.empty(ord(keyOrd)));
    }

    public Option<V> get(K k) {
        Option<P2<K, Option<V>>> x = this.tree.split(P.p(k, Option.none()))._2();
        return (Option<V>) x.bind(P2.__2());
    }

    public TreeMap<K, V> set(K k, V v) {
        return new TreeMap<>(this.tree.insert(P.p(k, Option.some(v))));
    }

    public TreeMap<K, V> delete(K k) {
        return new TreeMap<>(this.tree.delete(P.p(k, Option.none())));
    }

    public int size() {
        return this.tree.size();
    }

    public boolean isEmpty() {
        return this.tree.isEmpty();
    }

    public List<V> values() {
        return List.iterableList(IterableW.join(this.tree.toList().map(Function.compose(IterableW.wrap(), P2.__2()))));
    }

    public List<K> keys() {
        return (List<K>) this.tree.toList().map(P2.__1());
    }

    public boolean contains(K k) {
        return this.tree.member(P.p(k, Option.none()));
    }

    @Override // java.lang.Iterable
    public Iterator<P2<K, V>> iterator() {
        return IterableW.join(this.tree.toStream().map(P2.map2_(IterableW.wrap())).map(P2.tuple(Function.compose(IterableW.map(), P.p2())))).iterator();
    }

    public Map<K, V> toMutableMap() {
        Map<K, V> m = new java.util.TreeMap<>();
        Iterator<P2<K, V>> it = iterator();
        while (it.hasNext()) {
            P2<K, V> e = it.next();
            m.put(e._1(), e._2());
        }
        return m;
    }

    public static <K, V> TreeMap<K, V> fromMutableMap(Ord<K> ord, Map<K, V> m) {
        TreeMap<K, V> t = empty(ord);
        for (Map.Entry<K, V> e : m.entrySet()) {
            t = t.set(e.getKey(), e.getValue());
        }
        return t;
    }

    public F<K, Option<V>> get() {
        return new F<K, Option<V>>() { // from class: fj.data.TreeMap.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public Option<V> f(K k) {
                return TreeMap.this.get(k);
            }
        };
    }

    public P2<Boolean, TreeMap<K, V>> update(K k, F<V, V> f) {
        P2<Boolean, Set<P2<K, Option<V>>>> up = this.tree.update(P.p(k, Option.none()), P2.map2_((F) Option.map().f(f)));
        return P.p(up._1(), new TreeMap(up._2()));
    }

    public TreeMap<K, V> update(K k, F<V, V> f, V v) {
        P2<Boolean, TreeMap<K, V>> up = update(k, f);
        return up._1().booleanValue() ? up._2() : set(k, v);
    }

    public P3<Set<V>, Option<V>, Set<V>> split(K k) {
        F mapSet = F1Functions.mapSet(F1Functions.o(Option.fromSome(), P2.__2()), this.tree.ord().comap(F1Functions.o((F) P.p2().f(k), Option.some_())));
        return this.tree.split(P.p(k, Option.none())).map1(mapSet).map3(mapSet).map2(F1Functions.o(Option.join(), F1Functions.mapOption(P2.__2())));
    }

    public <W> TreeMap<K, W> map(F<V, W> f) {
        return new TreeMap<>(this.tree.map(ord(this.tree.ord().comap((F) Function.flip(P.p2()).f(Option.none()))), P2.map2_(F1Functions.mapOption(f))));
    }
}
