package org.powermock.modules.junit4.internal.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.Rule;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestMethod;
import org.junit.rules.MethodRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockJUnit47RunnerDelegateImpl.class */
public class PowerMockJUnit47RunnerDelegateImpl extends PowerMockJUnit44RunnerDelegateImpl {
    public boolean hasRules;

    public PowerMockJUnit47RunnerDelegateImpl(Class<?> klass, String[] methodsToRun, PowerMockTestListener[] listeners) throws InitializationError {
        super(klass, methodsToRun, listeners);
    }

    public PowerMockJUnit47RunnerDelegateImpl(Class<?> klass, String[] methodsToRun) throws InitializationError {
        super(klass, methodsToRun);
    }

    public PowerMockJUnit47RunnerDelegateImpl(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override // org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl
    protected PowerMockJUnit44RunnerDelegateImpl.PowerMockJUnit44MethodRunner createPowerMockRunner(Object testInstance, TestMethod testMethod, RunNotifier notifier, Description description, boolean extendsFromTestCase) {
        return new PowerMockJUnit47MethodRunner(testInstance, testMethod, notifier, description, extendsFromTestCase);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockJUnit47RunnerDelegateImpl$PowerMockJUnit47MethodRunner.class */
    public class PowerMockJUnit47MethodRunner extends PowerMockJUnit44RunnerDelegateImpl.PowerMockJUnit44MethodRunner {
        private Throwable potentialTestFailure;

        /* JADX INFO: Access modifiers changed from: protected */
        public PowerMockJUnit47MethodRunner(Object testInstance, TestMethod method, RunNotifier notifier, Description description, boolean extendsFromTestCase) {
            super(testInstance, method, notifier, description, extendsFromTestCase);
        }

        @Override // org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl.PowerMockJUnit44MethodRunner
        public void executeTest(Method method, Object testInstance, Runnable test) {
            ClassLoader originalCL = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            try {
                Set<Field> rules = Whitebox.getFieldsAnnotatedWith(testInstance, Rule.class, new Class[0]);
                Thread.currentThread().setContextClassLoader(originalCL);
                PowerMockJUnit47RunnerDelegateImpl.this.hasRules = !rules.isEmpty();
                Statement statement = createStatement(method, testInstance, test, rules);
                evaluateStatement(statement);
            } catch (Throwable th) {
                Thread.currentThread().setContextClassLoader(originalCL);
                throw th;
            }
        }

        private Statement createStatement(Method method, Object testInstance, Runnable test, Set<Field> rules) {
            Statement statement = new TestExecutorStatement(test, testInstance, method);
            for (Field field : rules) {
                try {
                    statement = applyRuleToLastStatement(method, testInstance, field, statement);
                } catch (Throwable e) {
                    super.handleException(this.testMethod, e);
                }
            }
            return statement;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public Statement applyRuleToLastStatement(Method method, Object testInstance, Field field, Statement lastStatement) throws IllegalAccessException {
            MethodRule rule = (MethodRule) field.get(testInstance);
            Statement statement = rule.apply(lastStatement, new FrameworkMethod(method), testInstance);
            return statement;
        }

        private void evaluateStatement(Statement statement) {
            try {
                statement.evaluate();
            } catch (Throwable e) {
                super.handleException(this.testMethod, this.potentialTestFailure == null ? e : this.potentialTestFailure);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl.PowerMockJUnit44MethodRunner
        public void handleException(TestMethod testMethod, Throwable actualFailure) {
            if (PowerMockJUnit47RunnerDelegateImpl.this.hasRules) {
                this.potentialTestFailure = actualFailure;
            } else {
                super.handleException(testMethod, actualFailure);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void executeTestInSuper(Method method, Object testInstance, Runnable test) {
            super.executeTest(method, testInstance, test);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockJUnit47RunnerDelegateImpl$PowerMockJUnit47MethodRunner$TestExecutorStatement.class */
        public final class TestExecutorStatement extends Statement {
            private final Runnable test;
            private final Object testInstance;
            private final Method method;

            private TestExecutorStatement(Runnable test, Object testInstance, Method method) {
                this.test = test;
                this.testInstance = testInstance;
                this.method = method;
            }

            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                PowerMockJUnit47MethodRunner.this.executeTestInSuper(this.method, this.testInstance, this.test);
                if (PowerMockJUnit47MethodRunner.this.potentialTestFailure != null) {
                    throw PowerMockJUnit47MethodRunner.this.potentialTestFailure;
                }
            }
        }
    }
}
