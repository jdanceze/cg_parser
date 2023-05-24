package org.junit.runners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.rules.RuleMemberValidator;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMember;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.MemberValueConsumer;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.junit.validator.PublicClassValidator;
import org.junit.validator.TestClassValidator;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/BlockJUnit4ClassRunner.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/BlockJUnit4ClassRunner.class */
public class BlockJUnit4ClassRunner extends ParentRunner<FrameworkMethod> {
    private final ConcurrentMap<FrameworkMethod, Description> methodDescriptions;
    private static TestClassValidator PUBLIC_CLASS_VALIDATOR = new PublicClassValidator();
    private static final ThreadLocal<RuleContainer> CURRENT_RULE_CONTAINER = new ThreadLocal<>();

    public BlockJUnit4ClassRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        this.methodDescriptions = new ConcurrentHashMap();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BlockJUnit4ClassRunner(TestClass testClass) throws InitializationError {
        super(testClass);
        this.methodDescriptions = new ConcurrentHashMap();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runners.ParentRunner
    public void runChild(final FrameworkMethod method, RunNotifier notifier) {
        Description description = describeChild(method);
        if (isIgnored(method)) {
            notifier.fireTestIgnored(description);
            return;
        }
        Statement statement = new Statement() { // from class: org.junit.runners.BlockJUnit4ClassRunner.1
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                BlockJUnit4ClassRunner.this.methodBlock(method).evaluate();
            }
        };
        runLeaf(statement, description, notifier);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runners.ParentRunner
    public boolean isIgnored(FrameworkMethod child) {
        return child.getAnnotation(Ignore.class) != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runners.ParentRunner
    public Description describeChild(FrameworkMethod method) {
        Description description = this.methodDescriptions.get(method);
        if (description == null) {
            description = Description.createTestDescription(getTestClass().getJavaClass(), testName(method), method.getAnnotations());
            this.methodDescriptions.putIfAbsent(method, description);
        }
        return description;
    }

    @Override // org.junit.runners.ParentRunner
    protected List<FrameworkMethod> getChildren() {
        return computeTestMethods();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<FrameworkMethod> computeTestMethods() {
        return getTestClass().getAnnotatedMethods(Test.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runners.ParentRunner
    public void collectInitializationErrors(List<Throwable> errors) {
        super.collectInitializationErrors(errors);
        validatePublicConstructor(errors);
        validateNoNonStaticInnerClass(errors);
        validateConstructor(errors);
        validateInstanceMethods(errors);
        validateFields(errors);
        validateMethods(errors);
    }

    private void validatePublicConstructor(List<Throwable> errors) {
        if (getTestClass().getJavaClass() != null) {
            errors.addAll(PUBLIC_CLASS_VALIDATOR.validateTestClass(getTestClass()));
        }
    }

    protected void validateNoNonStaticInnerClass(List<Throwable> errors) {
        if (getTestClass().isANonStaticInnerClass()) {
            String gripe = "The inner class " + getTestClass().getName() + " is not static.";
            errors.add(new Exception(gripe));
        }
    }

    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
        validateZeroArgConstructor(errors);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateOnlyOneConstructor(List<Throwable> errors) {
        if (!hasOneConstructor()) {
            errors.add(new Exception("Test class should have exactly one public constructor"));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateZeroArgConstructor(List<Throwable> errors) {
        if (!getTestClass().isANonStaticInnerClass() && hasOneConstructor() && getTestClass().getOnlyConstructor().getParameterTypes().length != 0) {
            errors.add(new Exception("Test class should have exactly one public zero-argument constructor"));
        }
    }

    private boolean hasOneConstructor() {
        return getTestClass().getJavaClass().getConstructors().length == 1;
    }

    @Deprecated
    protected void validateInstanceMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(After.class, false, errors);
        validatePublicVoidNoArgMethods(Before.class, false, errors);
        validateTestMethods(errors);
        if (computeTestMethods().isEmpty()) {
            errors.add(new Exception("No runnable methods"));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateFields(List<Throwable> errors) {
        RuleMemberValidator.RULE_VALIDATOR.validate(getTestClass(), errors);
    }

    private void validateMethods(List<Throwable> errors) {
        RuleMemberValidator.RULE_METHOD_VALIDATOR.validate(getTestClass(), errors);
    }

    protected void validateTestMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(Test.class, false, errors);
    }

    protected Object createTest() throws Exception {
        return getTestClass().getOnlyConstructor().newInstance(new Object[0]);
    }

    protected Object createTest(FrameworkMethod method) throws Exception {
        return createTest();
    }

    protected String testName(FrameworkMethod method) {
        return method.getName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Statement methodBlock(final FrameworkMethod method) {
        try {
            Object test = new ReflectiveCallable() { // from class: org.junit.runners.BlockJUnit4ClassRunner.2
                @Override // org.junit.internal.runners.model.ReflectiveCallable
                protected Object runReflectiveCall() throws Throwable {
                    return BlockJUnit4ClassRunner.this.createTest(method);
                }
            }.run();
            Statement statement = methodInvoker(method, test);
            return withInterruptIsolation(withRules(method, test, withAfters(method, test, withBefores(method, test, withPotentialTimeout(method, test, possiblyExpectingExceptions(method, test, statement))))));
        } catch (Throwable e) {
            return new Fail(e);
        }
    }

    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        return new InvokeMethod(method, test);
    }

    protected Statement possiblyExpectingExceptions(FrameworkMethod method, Object test, Statement next) {
        Test annotation = (Test) method.getAnnotation(Test.class);
        Class<? extends Throwable> expectedExceptionClass = getExpectedException(annotation);
        return expectedExceptionClass != null ? new ExpectException(next, expectedExceptionClass) : next;
    }

    @Deprecated
    protected Statement withPotentialTimeout(FrameworkMethod method, Object test, Statement next) {
        long timeout = getTimeout((Test) method.getAnnotation(Test.class));
        if (timeout <= 0) {
            return next;
        }
        return FailOnTimeout.builder().withTimeout(timeout, TimeUnit.MILLISECONDS).build(next);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(Before.class);
        return befores.isEmpty() ? statement : new RunBefores(statement, befores, target);
    }

    protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
        List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
        return afters.isEmpty() ? statement : new RunAfters(statement, afters, target);
    }

    private Statement withRules(FrameworkMethod method, Object target, Statement statement) {
        RuleContainer ruleContainer = new RuleContainer();
        CURRENT_RULE_CONTAINER.set(ruleContainer);
        try {
            List<TestRule> testRules = getTestRules(target);
            for (MethodRule each : rules(target)) {
                if (!(each instanceof TestRule) || !testRules.contains(each)) {
                    ruleContainer.add(each);
                }
            }
            for (TestRule rule : testRules) {
                ruleContainer.add(rule);
            }
            CURRENT_RULE_CONTAINER.remove();
            return ruleContainer.apply(method, describeChild(method), target, statement);
        } catch (Throwable th) {
            CURRENT_RULE_CONTAINER.remove();
            throw th;
        }
    }

    protected List<MethodRule> rules(Object target) {
        RuleCollector<MethodRule> collector = new RuleCollector<>();
        getTestClass().collectAnnotatedMethodValues(target, Rule.class, MethodRule.class, collector);
        getTestClass().collectAnnotatedFieldValues(target, Rule.class, MethodRule.class, collector);
        return collector.result;
    }

    protected List<TestRule> getTestRules(Object target) {
        RuleCollector<TestRule> collector = new RuleCollector<>();
        getTestClass().collectAnnotatedMethodValues(target, Rule.class, TestRule.class, collector);
        getTestClass().collectAnnotatedFieldValues(target, Rule.class, TestRule.class, collector);
        return collector.result;
    }

    private Class<? extends Throwable> getExpectedException(Test annotation) {
        if (annotation == null || annotation.expected() == Test.None.class) {
            return null;
        }
        return annotation.expected();
    }

    private long getTimeout(Test annotation) {
        if (annotation == null) {
            return 0L;
        }
        return annotation.timeout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/BlockJUnit4ClassRunner$RuleCollector.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/BlockJUnit4ClassRunner$RuleCollector.class */
    public static class RuleCollector<T> implements MemberValueConsumer<T> {
        final List<T> result;

        private RuleCollector() {
            this.result = new ArrayList();
        }

        @Override // org.junit.runners.model.MemberValueConsumer
        public void accept(FrameworkMember<?> member, T value) {
            RuleContainer container;
            Rule rule = (Rule) member.getAnnotation(Rule.class);
            if (rule != null && (container = (RuleContainer) BlockJUnit4ClassRunner.CURRENT_RULE_CONTAINER.get()) != null) {
                container.setOrder(value, rule.order());
            }
            this.result.add(value);
        }
    }
}
