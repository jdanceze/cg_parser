package polyglot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/CachingTransformingList.class */
public class CachingTransformingList extends TransformingList {
    ArrayList cache;

    public CachingTransformingList(Collection underlying, Transformation trans) {
        this((List) new ArrayList(underlying), trans);
    }

    public CachingTransformingList(List underlying, Transformation trans) {
        super(underlying, trans);
        this.cache = new ArrayList(Collections.nCopies(underlying.size(), null));
    }

    @Override // polyglot.util.TransformingList, java.util.AbstractList, java.util.List
    public Object get(int index) {
        Object o = this.cache.get(index);
        if (o == null) {
            o = this.trans.transform(this.underlying.get(index));
            this.cache.set(index, o);
        }
        return o;
    }
}
