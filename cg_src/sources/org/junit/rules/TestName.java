package org.junit.rules;

import org.junit.runner.Description;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/TestName.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/TestName.class */
public class TestName extends TestWatcher {
    private volatile String name;

    @Override // org.junit.rules.TestWatcher
    protected void starting(Description d) {
        this.name = d.getMethodName();
    }

    public String getMethodName() {
        return this.name;
    }
}
