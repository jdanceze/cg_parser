package org.powermock.modules.junit4.internal.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestMethod;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.modules.junit4.internal.impl.PowerMockJUnit47RunnerDelegateImpl;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockJUnit49RunnerDelegateImpl.class */
public class PowerMockJUnit49RunnerDelegateImpl extends PowerMockJUnit47RunnerDelegateImpl {
    public PowerMockJUnit49RunnerDelegateImpl(Class<?> klass, String[] methodsToRun, PowerMockTestListener[] listeners) throws InitializationError {
        super(klass, methodsToRun, listeners);
    }

    public PowerMockJUnit49RunnerDelegateImpl(Class<?> klass, String[] methodsToRun) throws InitializationError {
        super(klass, methodsToRun);
    }

    public PowerMockJUnit49RunnerDelegateImpl(Class<?> klass) throws InitializationError {
        super(klass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.powermock.modules.junit4.internal.impl.PowerMockJUnit47RunnerDelegateImpl, org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl
    public PowerMockJUnit47RunnerDelegateImpl.PowerMockJUnit47MethodRunner createPowerMockRunner(Object testInstance, TestMethod testMethod, RunNotifier notifier, Description description, boolean extendsFromTestCase) {
        return new PowerMockJUnit49MethodRunner(testInstance, testMethod, notifier, description, extendsFromTestCase);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockJUnit49RunnerDelegateImpl$PowerMockJUnit49MethodRunner.class */
    public class PowerMockJUnit49MethodRunner extends PowerMockJUnit47RunnerDelegateImpl.PowerMockJUnit47MethodRunner {
        private Description description;

        protected PowerMockJUnit49MethodRunner(Object testInstance, TestMethod method, RunNotifier notifier, Description description, boolean extendsFromTestCase) {
            super(testInstance, method, notifier, description, extendsFromTestCase);
            this.description = description;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.powermock.modules.junit4.internal.impl.PowerMockJUnit47RunnerDelegateImpl.PowerMockJUnit47MethodRunner
        public Statement applyRuleToLastStatement(Method method, Object testInstance, Field field, Statement lastStatement) throws IllegalAccessException {
            Statement statement;
            Object fieldValue = field.get(testInstance);
            if (fieldValue instanceof MethodRule) {
                statement = super.applyRuleToLastStatement(method, testInstance, field, lastStatement);
            } else if (fieldValue instanceof TestRule) {
                TestRule rule = (TestRule) fieldValue;
                statement = rule.apply(lastStatement, this.description);
            } else {
                throw new IllegalStateException("Can only handle MethodRule and TestRule");
            }
            return statement;
        }
    }
}
