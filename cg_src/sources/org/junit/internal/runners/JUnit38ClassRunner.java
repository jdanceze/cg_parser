package org.junit.internal.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import junit.extensions.TestDecorator;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.InvalidOrderingException;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Orderable;
import org.junit.runner.manipulation.Orderer;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/JUnit38ClassRunner.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/JUnit38ClassRunner.class */
public class JUnit38ClassRunner extends Runner implements Filterable, Orderable {
    private volatile Test test;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/JUnit38ClassRunner$OldTestClassAdaptingListener.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/JUnit38ClassRunner$OldTestClassAdaptingListener.class */
    public static final class OldTestClassAdaptingListener implements TestListener {
        private final RunNotifier notifier;

        private OldTestClassAdaptingListener(RunNotifier notifier) {
            this.notifier = notifier;
        }

        @Override // junit.framework.TestListener
        public void endTest(Test test) {
            this.notifier.fireTestFinished(asDescription(test));
        }

        @Override // junit.framework.TestListener
        public void startTest(Test test) {
            this.notifier.fireTestStarted(asDescription(test));
        }

        @Override // junit.framework.TestListener
        public void addError(Test test, Throwable e) {
            Failure failure = new Failure(asDescription(test), e);
            this.notifier.fireTestFailure(failure);
        }

        private Description asDescription(Test test) {
            if (test instanceof Describable) {
                Describable facade = (Describable) test;
                return facade.getDescription();
            }
            return Description.createTestDescription(getEffectiveClass(test), getName(test));
        }

        private Class<? extends Test> getEffectiveClass(Test test) {
            return test.getClass();
        }

        private String getName(Test test) {
            if (test instanceof TestCase) {
                return ((TestCase) test).getName();
            }
            return test.toString();
        }

        @Override // junit.framework.TestListener
        public void addFailure(Test test, AssertionFailedError t) {
            addError(test, t);
        }
    }

    public JUnit38ClassRunner(Class<?> klass) {
        this(new TestSuite(klass.asSubclass(TestCase.class)));
    }

    public JUnit38ClassRunner(Test test) {
        setTest(test);
    }

    @Override // org.junit.runner.Runner
    public void run(RunNotifier notifier) {
        TestResult result = new TestResult();
        result.addListener(createAdaptingListener(notifier));
        getTest().run(result);
    }

    public TestListener createAdaptingListener(RunNotifier notifier) {
        return new OldTestClassAdaptingListener(notifier);
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        return makeDescription(getTest());
    }

    private static Description makeDescription(Test test) {
        if (test instanceof TestCase) {
            TestCase tc = (TestCase) test;
            return Description.createTestDescription(tc.getClass(), tc.getName(), getAnnotations(tc));
        } else if (test instanceof TestSuite) {
            TestSuite ts = (TestSuite) test;
            String name = ts.getName() == null ? createSuiteDescription(ts) : ts.getName();
            Description description = Description.createSuiteDescription(name, new Annotation[0]);
            int n = ts.testCount();
            for (int i = 0; i < n; i++) {
                Description made = makeDescription(ts.testAt(i));
                description.addChild(made);
            }
            return description;
        } else if (test instanceof Describable) {
            Describable adapter = (Describable) test;
            return adapter.getDescription();
        } else if (test instanceof TestDecorator) {
            TestDecorator decorator = (TestDecorator) test;
            return makeDescription(decorator.getTest());
        } else {
            return Description.createSuiteDescription(test.getClass());
        }
    }

    private static Annotation[] getAnnotations(TestCase test) {
        try {
            Method m = test.getClass().getMethod(test.getName(), new Class[0]);
            return m.getDeclaredAnnotations();
        } catch (NoSuchMethodException | SecurityException e) {
            return new Annotation[0];
        }
    }

    private static String createSuiteDescription(TestSuite ts) {
        int count = ts.countTestCases();
        String example = count == 0 ? "" : String.format(" [example: %s]", ts.testAt(0));
        return String.format("TestSuite with %s tests%s", Integer.valueOf(count), example);
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        if (getTest() instanceof Filterable) {
            Filterable adapter = (Filterable) getTest();
            adapter.filter(filter);
        } else if (getTest() instanceof TestSuite) {
            TestSuite suite = (TestSuite) getTest();
            TestSuite filtered = new TestSuite(suite.getName());
            int n = suite.testCount();
            for (int i = 0; i < n; i++) {
                Test test = suite.testAt(i);
                if (filter.shouldRun(makeDescription(test))) {
                    filtered.addTest(test);
                }
            }
            setTest(filtered);
            if (filtered.testCount() == 0) {
                throw new NoTestsRemainException();
            }
        }
    }

    @Override // org.junit.runner.manipulation.Sortable
    public void sort(Sorter sorter) {
        if (getTest() instanceof Sortable) {
            Sortable adapter = (Sortable) getTest();
            adapter.sort(sorter);
        }
    }

    @Override // org.junit.runner.manipulation.Orderable
    public void order(Orderer orderer) throws InvalidOrderingException {
        if (getTest() instanceof Orderable) {
            Orderable adapter = (Orderable) getTest();
            adapter.order(orderer);
        }
    }

    private void setTest(Test test) {
        this.test = test;
    }

    private Test getTest() {
        return this.test;
    }
}
