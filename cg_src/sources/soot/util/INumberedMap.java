package soot.util;

import java.util.Iterator;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/util/INumberedMap.class */
public interface INumberedMap<K extends Numberable, V> {
    boolean put(K k, V v);

    V get(K k);

    Iterator<K> keyIterator();

    void remove(K k);
}
