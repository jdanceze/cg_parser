package org.junit.runner.manipulation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.runner.Description;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/manipulation/Orderer.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/manipulation/Orderer.class */
public final class Orderer {
    private final Ordering ordering;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Orderer(Ordering delegate) {
        this.ordering = delegate;
    }

    public List<Description> order(Collection<Description> descriptions) throws InvalidOrderingException {
        List<Description> inOrder = this.ordering.orderItems(Collections.unmodifiableCollection(descriptions));
        if (!this.ordering.validateOrderingIsCorrect()) {
            return inOrder;
        }
        Set<Description> uniqueDescriptions = new HashSet<>(descriptions);
        if (!uniqueDescriptions.containsAll(inOrder)) {
            throw new InvalidOrderingException("Ordering added items");
        }
        Set<Description> resultAsSet = new HashSet<>(inOrder);
        if (resultAsSet.size() != inOrder.size()) {
            throw new InvalidOrderingException("Ordering duplicated items");
        }
        if (!resultAsSet.containsAll(uniqueDescriptions)) {
            throw new InvalidOrderingException("Ordering removed items");
        }
        return inOrder;
    }

    public void apply(Object target) throws InvalidOrderingException {
        if (target instanceof Orderable) {
            Orderable orderable = (Orderable) target;
            orderable.order(this);
        }
    }
}
