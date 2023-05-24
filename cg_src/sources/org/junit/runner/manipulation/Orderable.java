package org.junit.runner.manipulation;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/manipulation/Orderable.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/manipulation/Orderable.class */
public interface Orderable extends Sortable {
    void order(Orderer orderer) throws InvalidOrderingException;
}
