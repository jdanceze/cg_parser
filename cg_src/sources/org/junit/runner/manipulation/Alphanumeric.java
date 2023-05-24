package org.junit.runner.manipulation;

import java.util.Comparator;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Ordering;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/manipulation/Alphanumeric.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/manipulation/Alphanumeric.class */
public final class Alphanumeric extends Sorter implements Ordering.Factory {
    private static final Comparator<Description> COMPARATOR = new Comparator<Description>() { // from class: org.junit.runner.manipulation.Alphanumeric.1
        @Override // java.util.Comparator
        public int compare(Description o1, Description o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }
    };

    public Alphanumeric() {
        super(COMPARATOR);
    }

    @Override // org.junit.runner.manipulation.Ordering.Factory
    public Ordering create(Ordering.Context context) {
        return this;
    }
}
