package soot.dava.toolkits.base.finders;

import java.util.Comparator;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/IndexComparator.class */
class IndexComparator implements Comparator {
    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 instanceof String) {
            return 1;
        }
        if (o2 instanceof String) {
            return -1;
        }
        return ((Integer) o1).intValue() - ((Integer) o2).intValue();
    }

    @Override // java.util.Comparator
    public boolean equals(Object o) {
        return o instanceof IndexComparator;
    }
}
