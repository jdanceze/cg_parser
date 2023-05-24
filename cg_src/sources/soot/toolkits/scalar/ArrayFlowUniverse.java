package soot.toolkits.scalar;

import java.util.Arrays;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ArrayFlowUniverse.class */
public class ArrayFlowUniverse<E> implements FlowUniverse<E> {
    protected final E[] elements;

    public ArrayFlowUniverse(E[] eArr) {
        this.elements = eArr;
    }

    @Override // soot.toolkits.scalar.FlowUniverse
    public int size() {
        return this.elements.length;
    }

    @Override // soot.toolkits.scalar.FlowUniverse, java.lang.Iterable
    public Iterator<E> iterator() {
        return Arrays.asList(this.elements).iterator();
    }

    @Override // soot.toolkits.scalar.FlowUniverse
    public E[] toArray() {
        return this.elements;
    }
}
