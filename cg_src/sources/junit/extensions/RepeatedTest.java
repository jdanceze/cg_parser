package junit.extensions;

import junit.framework.Test;
import junit.framework.TestResult;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/extensions/RepeatedTest.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/extensions/RepeatedTest.class */
public class RepeatedTest extends TestDecorator {
    private int fTimesRepeat;

    public RepeatedTest(Test test, int repeat) {
        super(test);
        if (repeat < 0) {
            throw new IllegalArgumentException("Repetition count must be >= 0");
        }
        this.fTimesRepeat = repeat;
    }

    @Override // junit.extensions.TestDecorator, junit.framework.Test
    public int countTestCases() {
        return super.countTestCases() * this.fTimesRepeat;
    }

    @Override // junit.extensions.TestDecorator, junit.framework.Test
    public void run(TestResult result) {
        for (int i = 0; i < this.fTimesRepeat && !result.shouldStop(); i++) {
            super.run(result);
        }
    }

    @Override // junit.extensions.TestDecorator
    public String toString() {
        return super.toString() + "(repeated)";
    }
}
