package org.junit.runner;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/FilterFactoryParams.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/FilterFactoryParams.class */
public final class FilterFactoryParams {
    private final Description topLevelDescription;
    private final String args;

    public FilterFactoryParams(Description topLevelDescription, String args) {
        if (args == null || topLevelDescription == null) {
            throw new NullPointerException();
        }
        this.topLevelDescription = topLevelDescription;
        this.args = args;
    }

    public String getArgs() {
        return this.args;
    }

    public Description getTopLevelDescription() {
        return this.topLevelDescription;
    }
}
