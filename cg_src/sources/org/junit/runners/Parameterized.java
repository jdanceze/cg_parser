package org.junit.runners;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InvalidTestClassError;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParametersFactory;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized.class */
public class Parameterized extends Suite {

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized$AfterParam.class
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized$AfterParam.class */
    public @interface AfterParam {
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized$BeforeParam.class
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized$BeforeParam.class */
    public @interface BeforeParam {
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized$Parameter.class
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized$Parameter.class */
    public @interface Parameter {
        int value() default 0;
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized$Parameters.class
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized$Parameters.class */
    public @interface Parameters {
        String name() default "{index}";
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized$UseParametersRunnerFactory.class
     */
    @Inherited
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized$UseParametersRunnerFactory.class */
    public @interface UseParametersRunnerFactory {
        Class<? extends ParametersRunnerFactory> value() default BlockJUnit4ClassRunnerWithParametersFactory.class;
    }

    public Parameterized(Class<?> klass) throws Throwable {
        this(klass, new RunnersFactory(klass));
    }

    private Parameterized(Class<?> klass, RunnersFactory runnersFactory) throws Exception {
        super(klass, runnersFactory.createRunners());
        validateBeforeParamAndAfterParamMethods(Integer.valueOf(runnersFactory.parameterCount));
    }

    private void validateBeforeParamAndAfterParamMethods(Integer parameterCount) throws InvalidTestClassError {
        List<Throwable> errors = new ArrayList<>();
        validatePublicStaticVoidMethods(BeforeParam.class, parameterCount, errors);
        validatePublicStaticVoidMethods(AfterParam.class, parameterCount, errors);
        if (!errors.isEmpty()) {
            throw new InvalidTestClassError(getTestClass().getJavaClass(), errors);
        }
    }

    private void validatePublicStaticVoidMethods(Class<? extends Annotation> annotation, Integer parameterCount, List<Throwable> errors) {
        int methodParameterCount;
        List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
        for (FrameworkMethod fm : methods) {
            fm.validatePublicVoid(true, errors);
            if (parameterCount != null && (methodParameterCount = fm.getMethod().getParameterTypes().length) != 0 && methodParameterCount != parameterCount.intValue()) {
                errors.add(new Exception("Method " + fm.getName() + "() should have 0 or " + parameterCount + " parameter(s)"));
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized$AssumptionViolationRunner.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized$AssumptionViolationRunner.class */
    private static class AssumptionViolationRunner extends Runner {
        private final Description description;
        private final AssumptionViolatedException exception;

        AssumptionViolationRunner(TestClass testClass, String methodName, AssumptionViolatedException exception) {
            this.description = Description.createTestDescription(testClass.getJavaClass(), methodName + "() assumption violation");
            this.exception = exception;
        }

        @Override // org.junit.runner.Runner, org.junit.runner.Describable
        public Description getDescription() {
            return this.description;
        }

        @Override // org.junit.runner.Runner
        public void run(RunNotifier notifier) {
            notifier.fireTestAssumptionFailed(new Failure(this.description, this.exception));
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/Parameterized$RunnersFactory.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/Parameterized$RunnersFactory.class */
    private static class RunnersFactory {
        private static final ParametersRunnerFactory DEFAULT_FACTORY = new BlockJUnit4ClassRunnerWithParametersFactory();
        private final TestClass testClass;
        private final FrameworkMethod parametersMethod;
        private final List<Object> allParameters;
        private final int parameterCount;
        private final Runner runnerOverride;

        private RunnersFactory(Class<?> klass) throws Throwable {
            List<Object> allParametersResult;
            this.testClass = new TestClass(klass);
            this.parametersMethod = getParametersMethod(this.testClass);
            AssumptionViolationRunner assumptionViolationRunner = null;
            try {
                allParametersResult = allParameters(this.testClass, this.parametersMethod);
            } catch (AssumptionViolatedException e) {
                allParametersResult = Collections.emptyList();
                assumptionViolationRunner = new AssumptionViolationRunner(this.testClass, this.parametersMethod.getName(), e);
            }
            this.allParameters = allParametersResult;
            this.runnerOverride = assumptionViolationRunner;
            this.parameterCount = this.allParameters.isEmpty() ? 0 : normalizeParameters(this.allParameters.get(0)).length;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public List<Runner> createRunners() throws Exception {
            if (this.runnerOverride != null) {
                return Collections.singletonList(this.runnerOverride);
            }
            Parameters parameters = (Parameters) this.parametersMethod.getAnnotation(Parameters.class);
            return Collections.unmodifiableList(createRunnersForParameters(this.allParameters, parameters.name(), getParametersRunnerFactory()));
        }

        private ParametersRunnerFactory getParametersRunnerFactory() throws InstantiationException, IllegalAccessException {
            UseParametersRunnerFactory annotation = (UseParametersRunnerFactory) this.testClass.getAnnotation(UseParametersRunnerFactory.class);
            if (annotation == null) {
                return DEFAULT_FACTORY;
            }
            Class<? extends ParametersRunnerFactory> factoryClass = annotation.value();
            return factoryClass.newInstance();
        }

        private TestWithParameters createTestWithNotNormalizedParameters(String pattern, int index, Object parametersOrSingleParameter) {
            Object[] parameters = normalizeParameters(parametersOrSingleParameter);
            return createTestWithParameters(this.testClass, pattern, index, parameters);
        }

        private static Object[] normalizeParameters(Object parametersOrSingleParameter) {
            return parametersOrSingleParameter instanceof Object[] ? (Object[]) parametersOrSingleParameter : new Object[]{parametersOrSingleParameter};
        }

        private static List<Object> allParameters(TestClass testClass, FrameworkMethod parametersMethod) throws Throwable {
            Object parameters = parametersMethod.invokeExplosively(null, new Object[0]);
            if (parameters instanceof List) {
                return (List) parameters;
            }
            if (parameters instanceof Collection) {
                return new ArrayList((Collection) parameters);
            }
            if (parameters instanceof Iterable) {
                List<Object> result = new ArrayList<>();
                for (Object entry : (Iterable) parameters) {
                    result.add(entry);
                }
                return result;
            } else if (parameters instanceof Object[]) {
                return Arrays.asList((Object[]) parameters);
            } else {
                throw parametersMethodReturnedWrongType(testClass, parametersMethod);
            }
        }

        private static FrameworkMethod getParametersMethod(TestClass testClass) throws Exception {
            List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Parameters.class);
            for (FrameworkMethod each : methods) {
                if (each.isStatic() && each.isPublic()) {
                    return each;
                }
            }
            throw new Exception("No public static parameters method on class " + testClass.getName());
        }

        private List<Runner> createRunnersForParameters(Iterable<Object> allParameters, String namePattern, ParametersRunnerFactory runnerFactory) throws Exception {
            try {
                List<TestWithParameters> tests = createTestsForParameters(allParameters, namePattern);
                List<Runner> runners = new ArrayList<>();
                for (TestWithParameters test : tests) {
                    runners.add(runnerFactory.createRunnerForTestWithParameters(test));
                }
                return runners;
            } catch (ClassCastException e) {
                throw parametersMethodReturnedWrongType(this.testClass, this.parametersMethod);
            }
        }

        private List<TestWithParameters> createTestsForParameters(Iterable<Object> allParameters, String namePattern) throws Exception {
            int i = 0;
            List<TestWithParameters> children = new ArrayList<>();
            for (Object parametersOfSingleTest : allParameters) {
                int i2 = i;
                i++;
                children.add(createTestWithNotNormalizedParameters(namePattern, i2, parametersOfSingleTest));
            }
            return children;
        }

        private static Exception parametersMethodReturnedWrongType(TestClass testClass, FrameworkMethod parametersMethod) throws Exception {
            String className = testClass.getName();
            String methodName = parametersMethod.getName();
            String message = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.", className, methodName);
            return new Exception(message);
        }

        private TestWithParameters createTestWithParameters(TestClass testClass, String pattern, int index, Object[] parameters) {
            String finalPattern = pattern.replaceAll("\\{index\\}", Integer.toString(index));
            String name = MessageFormat.format(finalPattern, parameters);
            return new TestWithParameters("[" + name + "]", testClass, Arrays.asList(parameters));
        }
    }
}
