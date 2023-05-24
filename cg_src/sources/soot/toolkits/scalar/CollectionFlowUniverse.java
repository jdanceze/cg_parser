package soot.toolkits.scalar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/CollectionFlowUniverse.class */
public class CollectionFlowUniverse<E> implements FlowUniverse<E> {
    protected final Set<E> elements;

    public CollectionFlowUniverse(Collection<? extends E> elements) {
        this.elements = new HashSet(elements);
    }

    @Override // soot.toolkits.scalar.FlowUniverse
    public int size() {
        return this.elements.size();
    }

    @Override // soot.toolkits.scalar.FlowUniverse, java.lang.Iterable
    public Iterator<E> iterator() {
        return this.elements.iterator();
    }

    @Override // soot.toolkits.scalar.FlowUniverse
    public E[] toArray() {
        return (E[]) this.elements.toArray();
    }
}
