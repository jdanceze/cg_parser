package soot.dava.toolkits.base.finders;

import java.util.Comparator;
import java.util.TreeSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/IndexSetComparator.class */
class IndexSetComparator implements Comparator {
    IndexSetComparator() {
    }

    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        if (o1 == o2) {
            return 0;
        }
        Object o12 = ((TreeSet) o1).last();
        Object o22 = ((TreeSet) o2).last();
        if (o12 instanceof String) {
            return 1;
        }
        if (o22 instanceof String) {
            return -1;
        }
        return ((Integer) o12).intValue() - ((Integer) o22).intValue();
    }

    @Override // java.util.Comparator
    public boolean equals(Object o) {
        return o instanceof IndexSetComparator;
    }
}
