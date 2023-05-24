package org.hamcrest.internal;

import java.util.Iterator;
import org.hamcrest.SelfDescribing;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/internal/SelfDescribingValueIterator.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/internal/SelfDescribingValueIterator.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/internal/SelfDescribingValueIterator.class */
public class SelfDescribingValueIterator<T> implements Iterator<SelfDescribing> {
    private Iterator<T> values;

    public SelfDescribingValueIterator(Iterator<T> values) {
        this.values = values;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.values.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public SelfDescribing next() {
        return new SelfDescribingValue(this.values.next());
    }

    @Override // java.util.Iterator
    public void remove() {
        this.values.remove();
    }
}
