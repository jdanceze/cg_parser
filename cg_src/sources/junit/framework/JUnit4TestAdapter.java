package junit.framework;

import java.util.Iterator;
import java.util.List;
import org.junit.Ignore;
import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.InvalidOrderingException;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Orderable;
import org.junit.runner.manipulation.Orderer;
import org.junit.runner.manipulation.Sorter;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/framework/JUnit4TestAdapter.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/framework/JUnit4TestAdapter.class */
public class JUnit4TestAdapter implements Test, Filterable, Orderable, Describable {
    private final Class<?> fNewTestClass;
    private final Runner fRunner;
    private final JUnit4TestAdapterCache fCache;

    public JUnit4TestAdapter(Class<?> newTestClass) {
        this(newTestClass, JUnit4TestAdapterCache.getDefault());
    }

    public JUnit4TestAdapter(Class<?> newTestClass, JUnit4TestAdapterCache cache) {
        this.fCache = cache;
        this.fNewTestClass = newTestClass;
        this.fRunner = Request.classWithoutSuiteMethod(newTestClass).getRunner();
    }

    @Override // junit.framework.Test
    public int countTestCases() {
        return this.fRunner.testCount();
    }

    @Override // junit.framework.Test
    public void run(TestResult result) {
        this.fRunner.run(this.fCache.getNotifier(result, this));
    }

    public List<Test> getTests() {
        return this.fCache.asTestList(getDescription());
    }

    public Class<?> getTestClass() {
        return this.fNewTestClass;
    }

    @Override // org.junit.runner.Describable
    public Description getDescription() {
        Description description = this.fRunner.getDescription();
        return removeIgnored(description);
    }

    private Description removeIgnored(Description description) {
        if (isIgnored(description)) {
            return Description.EMPTY;
        }
        Description result = description.childlessCopy();
        Iterator i$ = description.getChildren().iterator();
        while (i$.hasNext()) {
            Description each = i$.next();
            Description child = removeIgnored(each);
            if (!child.isEmpty()) {
                result.addChild(child);
            }
        }
        return result;
    }

    private boolean isIgnored(Description description) {
        return description.getAnnotation(Ignore.class) != null;
    }

    public String toString() {
        return this.fNewTestClass.getName();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        filter.apply(this.fRunner);
    }

    @Override // org.junit.runner.manipulation.Sortable
    public void sort(Sorter sorter) {
        sorter.apply(this.fRunner);
    }

    @Override // org.junit.runner.manipulation.Orderable
    public void order(Orderer orderer) throws InvalidOrderingException {
        orderer.apply(this.fRunner);
    }
}
