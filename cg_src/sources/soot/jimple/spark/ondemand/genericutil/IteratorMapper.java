package soot.jimple.spark.ondemand.genericutil;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/IteratorMapper.class */
public class IteratorMapper<T, U> implements Iterator<U> {
    private final Mapper<T, U> mapper;
    private final Iterator<T> delegate;

    public IteratorMapper(Mapper<T, U> mapper, Iterator<T> delegate) {
        this.mapper = mapper;
        this.delegate = delegate;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override // java.util.Iterator
    public U next() {
        return this.mapper.map(this.delegate.next());
    }

    @Override // java.util.Iterator
    public void remove() {
        this.delegate.remove();
    }
}
