package junit.framework;

import org.junit.runner.Describable;
import org.junit.runner.Description;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/framework/JUnit4TestCaseFacade.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/framework/JUnit4TestCaseFacade.class */
public class JUnit4TestCaseFacade implements Test, Describable {
    private final Description fDescription;

    /* JADX INFO: Access modifiers changed from: package-private */
    public JUnit4TestCaseFacade(Description description) {
        this.fDescription = description;
    }

    public String toString() {
        return getDescription().toString();
    }

    @Override // junit.framework.Test
    public int countTestCases() {
        return 1;
    }

    @Override // junit.framework.Test
    public void run(TestResult result) {
        throw new RuntimeException("This test stub created only for informational purposes.");
    }

    @Override // org.junit.runner.Describable
    public Description getDescription() {
        return this.fDescription;
    }
}
