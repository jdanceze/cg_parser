package junit.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/framework/JUnit4TestAdapterCache.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/framework/JUnit4TestAdapterCache.class */
public class JUnit4TestAdapterCache extends HashMap<Description, Test> {
    private static final long serialVersionUID = 1;
    private static final JUnit4TestAdapterCache fInstance = new JUnit4TestAdapterCache();

    public static JUnit4TestAdapterCache getDefault() {
        return fInstance;
    }

    public Test asTest(Description description) {
        if (description.isSuite()) {
            return createTest(description);
        }
        if (!containsKey(description)) {
            put(description, createTest(description));
        }
        return get(description);
    }

    Test createTest(Description description) {
        if (description.isTest()) {
            return new JUnit4TestCaseFacade(description);
        }
        TestSuite suite = new TestSuite(description.getDisplayName());
        Iterator i$ = description.getChildren().iterator();
        while (i$.hasNext()) {
            Description child = i$.next();
            suite.addTest(asTest(child));
        }
        return suite;
    }

    public RunNotifier getNotifier(final TestResult result, JUnit4TestAdapter adapter) {
        RunNotifier notifier = new RunNotifier();
        notifier.addListener(new RunListener() { // from class: junit.framework.JUnit4TestAdapterCache.1
            @Override // org.junit.runner.notification.RunListener
            public void testFailure(Failure failure) throws Exception {
                result.addError(JUnit4TestAdapterCache.this.asTest(failure.getDescription()), failure.getException());
            }

            @Override // org.junit.runner.notification.RunListener
            public void testFinished(Description description) throws Exception {
                result.endTest(JUnit4TestAdapterCache.this.asTest(description));
            }

            @Override // org.junit.runner.notification.RunListener
            public void testStarted(Description description) throws Exception {
                result.startTest(JUnit4TestAdapterCache.this.asTest(description));
            }
        });
        return notifier;
    }

    public List<Test> asTestList(Description description) {
        if (description.isTest()) {
            return Arrays.asList(asTest(description));
        }
        List<Test> returnThis = new ArrayList<>();
        Iterator i$ = description.getChildren().iterator();
        while (i$.hasNext()) {
            Description child = i$.next();
            returnThis.add(asTest(child));
        }
        return returnThis;
    }
}
