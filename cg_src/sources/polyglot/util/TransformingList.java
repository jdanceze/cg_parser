package polyglot.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TransformingList.class */
public class TransformingList extends AbstractList {
    protected final Transformation trans;
    protected final List underlying;

    public TransformingList(Collection underlying, Transformation trans) {
        this((List) new ArrayList(underlying), trans);
    }

    public TransformingList(List underlying, Transformation trans) {
        this.underlying = underlying;
        this.trans = trans;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.underlying.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int index) {
        return this.trans.transform(this.underlying.get(index));
    }
}
