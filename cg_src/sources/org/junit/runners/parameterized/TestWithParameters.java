package org.junit.runners.parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.internal.Checks;
import org.junit.runners.model.TestClass;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/parameterized/TestWithParameters.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/parameterized/TestWithParameters.class */
public class TestWithParameters {
    private final String name;
    private final TestClass testClass;
    private final List<Object> parameters;

    public TestWithParameters(String name, TestClass testClass, List<Object> parameters) {
        Checks.notNull(name, "The name is missing.");
        Checks.notNull(testClass, "The test class is missing.");
        Checks.notNull(parameters, "The parameters are missing.");
        this.name = name;
        this.testClass = testClass;
        this.parameters = Collections.unmodifiableList(new ArrayList(parameters));
    }

    public String getName() {
        return this.name;
    }

    public TestClass getTestClass() {
        return this.testClass;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public int hashCode() {
        int result = 14747 + this.name.hashCode();
        return (14747 * ((14747 * result) + this.testClass.hashCode())) + this.parameters.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TestWithParameters other = (TestWithParameters) obj;
        return this.name.equals(other.name) && this.parameters.equals(other.parameters) && this.testClass.equals(other.testClass);
    }

    public String toString() {
        return this.testClass.getName() + " '" + this.name + "' with parameters " + this.parameters;
    }
}
