package org.junit.runners;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.Checks;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.rules.RuleMemberValidator;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.InvalidOrderingException;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Orderable;
import org.junit.runner.manipulation.Orderer;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.RuleContainer;
import org.junit.runners.model.FrameworkMember;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.InvalidTestClassError;
import org.junit.runners.model.MemberValueConsumer;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.junit.validator.AnnotationsValidator;
import org.junit.validator.TestClassValidator;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/ParentRunner.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/ParentRunner.class */
public abstract class ParentRunner<T> extends Runner implements Filterable, Orderable {
    private static final List<TestClassValidator> VALIDATORS = Collections.singletonList(new AnnotationsValidator());
    private final TestClass testClass;
    private final Lock childrenLock = new ReentrantLock();
    private volatile List<T> filteredChildren = null;
    private volatile RunnerScheduler scheduler = new RunnerScheduler() { // from class: org.junit.runners.ParentRunner.1
        @Override // org.junit.runners.model.RunnerScheduler
        public void schedule(Runnable childStatement) {
            childStatement.run();
        }

        @Override // org.junit.runners.model.RunnerScheduler
        public void finished() {
        }
    };

    protected abstract List<T> getChildren();

    protected abstract Description describeChild(T t);

    protected abstract void runChild(T t, RunNotifier runNotifier);

    /* JADX INFO: Access modifiers changed from: protected */
    public ParentRunner(Class<?> testClass) throws InitializationError {
        this.testClass = createTestClass(testClass);
        validate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ParentRunner(TestClass testClass) throws InitializationError {
        this.testClass = (TestClass) Checks.notNull(testClass);
        validate();
    }

    @Deprecated
    protected TestClass createTestClass(Class<?> testClass) {
        return new TestClass(testClass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void collectInitializationErrors(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
        validateClassRules(errors);
        applyValidators(errors);
    }

    private void applyValidators(List<Throwable> errors) {
        if (getTestClass().getJavaClass() != null) {
            for (TestClassValidator each : VALIDATORS) {
                errors.addAll(each.validateTestClass(getTestClass()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
        }
    }

    private void validateClassRules(List<Throwable> errors) {
        RuleMemberValidator.CLASS_RULE_VALIDATOR.validate(getTestClass(), errors);
        RuleMemberValidator.CLASS_RULE_METHOD_VALIDATOR.validate(getTestClass(), errors);
    }

    protected Statement classBlock(RunNotifier notifier) {
        Statement statement = childrenInvoker(notifier);
        if (!areAllChildrenIgnored()) {
            statement = withInterruptIsolation(withClassRules(withAfterClasses(withBeforeClasses(statement))));
        }
        return statement;
    }

    private boolean areAllChildrenIgnored() {
        for (T child : getFilteredChildren()) {
            if (!isIgnored(child)) {
                return false;
            }
        }
        return true;
    }

    protected Statement withBeforeClasses(Statement statement) {
        List<FrameworkMethod> befores = this.testClass.getAnnotatedMethods(BeforeClass.class);
        return befores.isEmpty() ? statement : new RunBefores(statement, befores, null);
    }

    protected Statement withAfterClasses(Statement statement) {
        List<FrameworkMethod> afters = this.testClass.getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? statement : new RunAfters(statement, afters, null);
    }

    private Statement withClassRules(Statement statement) {
        List<TestRule> classRules = classRules();
        return classRules.isEmpty() ? statement : new RunRules(statement, classRules, getDescription());
    }

    protected List<TestRule> classRules() {
        ClassRuleCollector collector = new ClassRuleCollector();
        this.testClass.collectAnnotatedMethodValues(null, ClassRule.class, TestRule.class, collector);
        this.testClass.collectAnnotatedFieldValues(null, ClassRule.class, TestRule.class, collector);
        return collector.getOrderedRules();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() { // from class: org.junit.runners.ParentRunner.2
            @Override // org.junit.runners.model.Statement
            public void evaluate() {
                ParentRunner.this.runChildren(notifier);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Statement withInterruptIsolation(final Statement statement) {
        return new Statement() { // from class: org.junit.runners.ParentRunner.3
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                } finally {
                    Thread.interrupted();
                }
            }
        };
    }

    protected boolean isIgnored(T child) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runChildren(final RunNotifier notifier) {
        RunnerScheduler currentScheduler = this.scheduler;
        try {
            for (final T each : getFilteredChildren()) {
                currentScheduler.schedule(new Runnable() { // from class: org.junit.runners.ParentRunner.4
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.lang.Runnable
                    public void run() {
                        ParentRunner.this.runChild(each, notifier);
                    }
                });
            }
        } finally {
            currentScheduler.finished();
        }
    }

    protected String getName() {
        return this.testClass.getName();
    }

    public final TestClass getTestClass() {
        return this.testClass;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void runLeaf(Statement statement, Description description, RunNotifier notifier) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            try {
                statement.evaluate();
                eachNotifier.fireTestFinished();
            } catch (AssumptionViolatedException e) {
                eachNotifier.addFailedAssumption(e);
                eachNotifier.fireTestFinished();
            } catch (Throwable e2) {
                eachNotifier.addFailure(e2);
                eachNotifier.fireTestFinished();
            }
        } catch (Throwable th) {
            eachNotifier.fireTestFinished();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Annotation[] getRunnerAnnotations() {
        return this.testClass.getAnnotations();
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        Description description;
        Class<?> clazz = getTestClass().getJavaClass();
        if (clazz == null || !clazz.getName().equals(getName())) {
            description = Description.createSuiteDescription(getName(), getRunnerAnnotations());
        } else {
            description = Description.createSuiteDescription(clazz, getRunnerAnnotations());
        }
        for (T child : getFilteredChildren()) {
            description.addChild(describeChild(child));
        }
        return description;
    }

    @Override // org.junit.runner.Runner
    public void run(RunNotifier notifier) {
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        testNotifier.fireTestSuiteStarted();
        try {
            try {
                Statement statement = classBlock(notifier);
                statement.evaluate();
                testNotifier.fireTestSuiteFinished();
            } catch (AssumptionViolatedException e) {
                testNotifier.addFailedAssumption(e);
                testNotifier.fireTestSuiteFinished();
            } catch (StoppedByUserException e2) {
                throw e2;
            } catch (Throwable e3) {
                testNotifier.addFailure(e3);
                testNotifier.fireTestSuiteFinished();
            }
        } catch (Throwable th) {
            testNotifier.fireTestSuiteFinished();
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        this.childrenLock.lock();
        try {
            List<T> children = new ArrayList<>(getFilteredChildren());
            Iterator<T> iter = children.iterator();
            while (iter.hasNext()) {
                T each = iter.next();
                if (shouldRun(filter, each)) {
                    try {
                        filter.apply(each);
                    } catch (NoTestsRemainException e) {
                        iter.remove();
                    }
                } else {
                    iter.remove();
                }
            }
            this.filteredChildren = Collections.unmodifiableList(children);
            if (this.filteredChildren.isEmpty()) {
                throw new NoTestsRemainException();
            }
        } finally {
            this.childrenLock.unlock();
        }
    }

    @Override // org.junit.runner.manipulation.Sortable
    public void sort(Sorter sorter) {
        if (shouldNotReorder()) {
            return;
        }
        this.childrenLock.lock();
        try {
            for (T each : getFilteredChildren()) {
                sorter.apply(each);
            }
            List<T> sortedChildren = new ArrayList<>(getFilteredChildren());
            Collections.sort(sortedChildren, comparator(sorter));
            this.filteredChildren = Collections.unmodifiableList(sortedChildren);
            this.childrenLock.unlock();
        } catch (Throwable th) {
            this.childrenLock.unlock();
            throw th;
        }
    }

    @Override // org.junit.runner.manipulation.Orderable
    public void order(Orderer orderer) throws InvalidOrderingException {
        if (shouldNotReorder()) {
            return;
        }
        this.childrenLock.lock();
        try {
            List<T> children = getFilteredChildren();
            Map<Description, List<T>> childMap = new LinkedHashMap<>(children.size());
            for (T child : children) {
                Description description = describeChild(child);
                List<T> childrenWithDescription = childMap.get(description);
                if (childrenWithDescription == null) {
                    childrenWithDescription = new ArrayList<>(1);
                    childMap.put(description, childrenWithDescription);
                }
                childrenWithDescription.add(child);
                orderer.apply(child);
            }
            List<Description> inOrder = orderer.order(childMap.keySet());
            List<T> children2 = new ArrayList<>(children.size());
            for (Description description2 : inOrder) {
                children2.addAll(childMap.get(description2));
            }
            this.filteredChildren = Collections.unmodifiableList(children2);
            this.childrenLock.unlock();
        } catch (Throwable th) {
            this.childrenLock.unlock();
            throw th;
        }
    }

    private boolean shouldNotReorder() {
        return getDescription().getAnnotation(FixMethodOrder.class) != null;
    }

    private void validate() throws InitializationError {
        List<Throwable> errors = new ArrayList<>();
        collectInitializationErrors(errors);
        if (!errors.isEmpty()) {
            throw new InvalidTestClassError(this.testClass.getJavaClass(), errors);
        }
    }

    private List<T> getFilteredChildren() {
        if (this.filteredChildren == null) {
            this.childrenLock.lock();
            try {
                if (this.filteredChildren == null) {
                    this.filteredChildren = Collections.unmodifiableList(new ArrayList(getChildren()));
                }
            } finally {
                this.childrenLock.unlock();
            }
        }
        return this.filteredChildren;
    }

    private boolean shouldRun(Filter filter, T each) {
        return filter.shouldRun(describeChild(each));
    }

    private Comparator<? super T> comparator(final Sorter sorter) {
        return (Comparator<T>) new Comparator<T>() { // from class: org.junit.runners.ParentRunner.5
            @Override // java.util.Comparator
            public int compare(T o1, T o2) {
                return sorter.compare(ParentRunner.this.describeChild(o1), ParentRunner.this.describeChild(o2));
            }
        };
    }

    public void setScheduler(RunnerScheduler scheduler) {
        this.scheduler = scheduler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/ParentRunner$ClassRuleCollector.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/ParentRunner$ClassRuleCollector.class */
    public static class ClassRuleCollector implements MemberValueConsumer<TestRule> {
        final List<RuleContainer.RuleEntry> entries;

        private ClassRuleCollector() {
            this.entries = new ArrayList();
        }

        @Override // org.junit.runners.model.MemberValueConsumer
        public /* bridge */ /* synthetic */ void accept(FrameworkMember x0, TestRule testRule) {
            accept2((FrameworkMember<?>) x0, testRule);
        }

        /* renamed from: accept  reason: avoid collision after fix types in other method */
        public void accept2(FrameworkMember<?> member, TestRule value) {
            ClassRule rule = (ClassRule) member.getAnnotation(ClassRule.class);
            this.entries.add(new RuleContainer.RuleEntry(value, 1, rule != null ? Integer.valueOf(rule.order()) : null));
        }

        public List<TestRule> getOrderedRules() {
            Collections.sort(this.entries, RuleContainer.ENTRY_COMPARATOR);
            List<TestRule> result = new ArrayList<>(this.entries.size());
            for (RuleContainer.RuleEntry entry : this.entries) {
                result.add((TestRule) entry.rule);
            }
            return result;
        }
    }
}
