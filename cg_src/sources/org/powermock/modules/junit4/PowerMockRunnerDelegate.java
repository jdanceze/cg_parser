package org.powermock.modules.junit4;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.AllTests;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.powermock.modules.junit4.common.internal.impl.JUnitVersion;
@Target({ElementType.TYPE})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/PowerMockRunnerDelegate.class */
public @interface PowerMockRunnerDelegate {
    Class<? extends Runner> value() default DefaultJUnitRunner.class;

    /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/PowerMockRunnerDelegate$DefaultJUnitRunner.class */
    public static final class DefaultJUnitRunner extends Runner {
        private final Runner wrappedDefaultRunner;

        public DefaultJUnitRunner(Class<?> testClass) throws Throwable {
            this.wrappedDefaultRunner = createDefaultRunner(testClass);
        }

        private static Runner createDefaultRunner(Class<?> testClass) throws Throwable {
            try {
                Method suiteMethod = testClass.getMethod("suite", new Class[0]);
                if (Test.class.isAssignableFrom(suiteMethod.getReturnType())) {
                    return new AllTests(testClass);
                }
            } catch (NoSuchMethodException e) {
            }
            if (TestCase.class.isAssignableFrom(testClass)) {
                return new JUnit38ClassRunner(testClass);
            }
            if (JUnitVersion.isGreaterThanOrEqualTo("4.5")) {
                return SinceJUnit_4_5.createRunnerDelegate(testClass);
            }
            return new JUnit4ClassRunner(testClass);
        }

        @Override // org.junit.runner.Runner, org.junit.runner.Describable
        public Description getDescription() {
            return this.wrappedDefaultRunner.getDescription();
        }

        @Override // org.junit.runner.Runner
        public void run(RunNotifier notifier) {
            this.wrappedDefaultRunner.run(notifier);
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/PowerMockRunnerDelegate$SinceJUnit_4_5.class */
    public static class SinceJUnit_4_5 {
        static Runner createRunnerDelegate(Class<?> testClass) throws InitializationError {
            return new JUnit4(testClass);
        }

        public static Class[] runnerAlternativeConstructorParams() {
            return new Class[]{Class.class, RunnerBuilder.class};
        }

        public static Object newRunnerBuilder() {
            return new AllDefaultPossibilitiesBuilder(false);
        }
    }
}
