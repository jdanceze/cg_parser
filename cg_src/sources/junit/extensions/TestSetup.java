package junit.extensions;

import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/extensions/TestSetup.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/extensions/TestSetup.class */
public class TestSetup extends TestDecorator {
    public TestSetup(Test test) {
        super(test);
    }

    @Override // junit.extensions.TestDecorator, junit.framework.Test
    public void run(final TestResult result) {
        Protectable p = new Protectable() { // from class: junit.extensions.TestSetup.1
            @Override // junit.framework.Protectable
            public void protect() throws Exception {
                TestSetup.this.setUp();
                TestSetup.this.basicRun(result);
                TestSetup.this.tearDown();
            }
        };
        result.runProtected(this, p);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
}
