package soot.util;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/util/IterableNumberer.class */
public interface IterableNumberer<E> extends Numberer<E>, Iterable<E> {
    Iterator<E> iterator();
}
