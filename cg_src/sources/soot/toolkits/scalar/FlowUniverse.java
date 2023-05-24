package soot.toolkits.scalar;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowUniverse.class */
public interface FlowUniverse<E> extends Iterable<E> {
    int size();

    @Override // java.lang.Iterable
    Iterator<E> iterator();

    E[] toArray();
}
