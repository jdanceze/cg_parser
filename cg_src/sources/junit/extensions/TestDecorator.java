package junit.extensions;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestResult;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/extensions/TestDecorator.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/extensions/TestDecorator.class */
public class TestDecorator extends Assert implements Test {
    protected Test fTest;

    public TestDecorator(Test test) {
        this.fTest = test;
    }

    public void basicRun(TestResult result) {
        this.fTest.run(result);
    }

    public int countTestCases() {
        return this.fTest.countTestCases();
    }

    public void run(TestResult result) {
        basicRun(result);
    }

    public String toString() {
        return this.fTest.toString();
    }

    public Test getTest() {
        return this.fTest;
    }
}
