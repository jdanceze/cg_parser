package junit.extensions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/extensions/ActiveTestSuite.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/extensions/ActiveTestSuite.class */
public class ActiveTestSuite extends TestSuite {
    private volatile int fActiveTestDeathCount;

    public ActiveTestSuite() {
    }

    public ActiveTestSuite(Class<? extends TestCase> theClass) {
        super(theClass);
    }

    public ActiveTestSuite(String name) {
        super(name);
    }

    public ActiveTestSuite(Class<? extends TestCase> theClass, String name) {
        super(theClass, name);
    }

    @Override // junit.framework.TestSuite, junit.framework.Test
    public void run(TestResult result) {
        this.fActiveTestDeathCount = 0;
        super.run(result);
        waitUntilFinished();
    }

    @Override // junit.framework.TestSuite
    public void runTest(final Test test, final TestResult result) {
        Thread t = new Thread() { // from class: junit.extensions.ActiveTestSuite.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    test.run(result);
                    ActiveTestSuite.this.runFinished();
                } catch (Throwable th) {
                    ActiveTestSuite.this.runFinished();
                    throw th;
                }
            }
        };
        t.start();
    }

    synchronized void waitUntilFinished() {
        while (this.fActiveTestDeathCount < testCount()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public synchronized void runFinished() {
        this.fActiveTestDeathCount++;
        notifyAll();
    }
}
