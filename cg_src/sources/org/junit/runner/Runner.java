package org.junit.runner;

import org.junit.runner.notification.RunNotifier;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/Runner.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/Runner.class */
public abstract class Runner implements Describable {
    @Override // org.junit.runner.Describable
    public abstract Description getDescription();

    public abstract void run(RunNotifier runNotifier);

    public int testCount() {
        return getDescription().testCount();
    }
}
