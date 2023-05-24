package org.apache.tools.ant.types.selectors.modifiedselector;

import java.util.Comparator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/EqualComparator.class */
public class EqualComparator implements Comparator<Object> {
    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        if (o1 != null) {
            return o1.equals(o2) ? 0 : 1;
        } else if (o2 == null) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        return "EqualComparator";
    }
}
