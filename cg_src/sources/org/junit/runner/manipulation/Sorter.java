package org.junit.runner.manipulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.runner.Description;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/manipulation/Sorter.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/manipulation/Sorter.class */
public class Sorter extends Ordering implements Comparator<Description> {
    public static final Sorter NULL = new Sorter(new Comparator<Description>() { // from class: org.junit.runner.manipulation.Sorter.1
        @Override // java.util.Comparator
        public int compare(Description o1, Description o2) {
            return 0;
        }
    });
    private final Comparator<Description> comparator;

    public Sorter(Comparator<Description> comparator) {
        this.comparator = comparator;
    }

    @Override // org.junit.runner.manipulation.Ordering
    public void apply(Object target) {
        if (target instanceof Sortable) {
            Sortable sortable = (Sortable) target;
            sortable.sort(this);
        }
    }

    @Override // java.util.Comparator
    public int compare(Description o1, Description o2) {
        return this.comparator.compare(o1, o2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runner.manipulation.Ordering
    public final List<Description> orderItems(Collection<Description> descriptions) {
        List<Description> sorted = new ArrayList<>(descriptions);
        Collections.sort(sorted, this);
        return sorted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.junit.runner.manipulation.Ordering
    public boolean validateOrderingIsCorrect() {
        return false;
    }
}
