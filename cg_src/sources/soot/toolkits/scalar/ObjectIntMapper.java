package soot.toolkits.scalar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ObjectIntMapper.class */
public class ObjectIntMapper<E> {
    private final Vector<E> intToObjects;
    private final Map<E, Integer> objectToInts;
    private int counter;

    public ObjectIntMapper() {
        this.intToObjects = new Vector<>();
        this.objectToInts = new HashMap();
        this.counter = 0;
    }

    public ObjectIntMapper(FlowUniverse<E> flowUniverse) {
        this(flowUniverse.iterator(), flowUniverse.size());
    }

    public ObjectIntMapper(Collection<E> collection) {
        this(collection.iterator(), collection.size());
    }

    private ObjectIntMapper(Iterator<E> it, int initSize) {
        this.intToObjects = new Vector<>(initSize);
        this.objectToInts = new HashMap(initSize);
        this.counter = 0;
        while (it.hasNext()) {
            add(it.next());
        }
    }

    public int add(E o) {
        this.objectToInts.put(o, Integer.valueOf(this.counter));
        this.intToObjects.add(o);
        int i = this.counter;
        this.counter = i + 1;
        return i;
    }

    public int getInt(E o) {
        Integer i = this.objectToInts.get(o);
        return i != null ? i.intValue() : add(o);
    }

    public E getObject(int i) {
        return this.intToObjects.get(i);
    }

    public boolean contains(Object o) {
        return this.objectToInts.containsKey(o);
    }

    public int size() {
        return this.counter;
    }
}
