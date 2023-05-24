package org.junit.runners.parameterized;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters.class */
public class BlockJUnit4ClassRunnerWithParameters extends BlockJUnit4ClassRunner {
    private final Object[] parameters;
    private final String name;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters$InjectionType.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters$InjectionType.class */
    public enum InjectionType {
        CONSTRUCTOR,
        FIELD
    }

    public BlockJUnit4ClassRunnerWithParameters(TestWithParameters test) throws InitializationError {
        super(test.getTestClass());
        this.parameters = test.getParameters().toArray(new Object[test.getParameters().size()]);
        this.name = test.getName();
    }

    @Override // org.junit.runners.BlockJUnit4ClassRunner
    public Object createTest() throws Exception {
        InjectionType injectionType = getInjectionType();
        switch (injectionType) {
            case CONSTRUCTOR:
                return createTestUsingConstructorInjection();
            case FIELD:
                return createTestUsingFieldInjection();
            default:
                throw new IllegalStateException("The injection type " + injectionType + " is not supported.");
        }
    }

    private Object createTestUsingConstructorInjection() throws Exception {
        return getTestClass().getOnlyConstructor().newInstance(this.parameters);
    }

    private Object createTestUsingFieldInjection() throws Exception {
        List<FrameworkField> annotatedFieldsByParameter = getAnnotatedFieldsByParameter();
        if (annotatedFieldsByParameter.size() != this.parameters.length) {
            throw new Exception("Wrong number of parameters and @Parameter fields. @Parameter fields counted: " + annotatedFieldsByParameter.size() + ", available parameters: " + this.parameters.length + ".");
        }
        Object testClassInstance = getTestClass().getJavaClass().newInstance();
        for (FrameworkField each : annotatedFieldsByParameter) {
            Field field = each.getField();
            Parameterized.Parameter annotation = (Parameterized.Parameter) field.getAnnotation(Parameterized.Parameter.class);
            int index = annotation.value();
            try {
                field.set(testClassInstance, this.parameters[index]);
            } catch (IllegalAccessException e) {
                IllegalAccessException wrappedException = new IllegalAccessException("Cannot set parameter '" + field.getName() + "'. Ensure that the field '" + field.getName() + "' is public.");
                wrappedException.initCause(e);
                throw wrappedException;
            } catch (IllegalArgumentException iare) {
                throw new Exception(getTestClass().getName() + ": Trying to set " + field.getName() + " with the value " + this.parameters[index] + " that is not the right type (" + this.parameters[index].getClass().getSimpleName() + " instead of " + field.getType().getSimpleName() + ").", iare);
            }
        }
        return testClassInstance;
    }

    @Override // org.junit.runners.ParentRunner
    protected String getName() {
        return this.name;
    }

    @Override // org.junit.runners.BlockJUnit4ClassRunner
    protected String testName(FrameworkMethod method) {
        return method.getName() + getName();
    }

    @Override // org.junit.runners.BlockJUnit4ClassRunner
    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
        if (getInjectionType() != InjectionType.CONSTRUCTOR) {
            validateZeroArgConstructor(errors);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runners.BlockJUnit4ClassRunner
    public void validateFields(List<Throwable> errors) {
        super.validateFields(errors);
        if (getInjectionType() == InjectionType.FIELD) {
            List<FrameworkField> annotatedFieldsByParameter = getAnnotatedFieldsByParameter();
            int[] usedIndices = new int[annotatedFieldsByParameter.size()];
            for (FrameworkField each : annotatedFieldsByParameter) {
                int index = ((Parameterized.Parameter) each.getField().getAnnotation(Parameterized.Parameter.class)).value();
                if (index < 0 || index > annotatedFieldsByParameter.size() - 1) {
                    errors.add(new Exception("Invalid @Parameter value: " + index + ". @Parameter fields counted: " + annotatedFieldsByParameter.size() + ". Please use an index between 0 and " + (annotatedFieldsByParameter.size() - 1) + "."));
                } else {
                    usedIndices[index] = usedIndices[index] + 1;
                }
            }
            for (int index2 = 0; index2 < usedIndices.length; index2++) {
                int numberOfUse = usedIndices[index2];
                if (numberOfUse == 0) {
                    errors.add(new Exception("@Parameter(" + index2 + ") is never used."));
                } else if (numberOfUse > 1) {
                    errors.add(new Exception("@Parameter(" + index2 + ") is used more than once (" + numberOfUse + ")."));
                }
            }
        }
    }

    @Override // org.junit.runners.ParentRunner
    protected Statement classBlock(RunNotifier notifier) {
        Statement statement = childrenInvoker(notifier);
        return withAfterParams(withBeforeParams(statement));
    }

    private Statement withBeforeParams(Statement statement) {
        List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(Parameterized.BeforeParam.class);
        return befores.isEmpty() ? statement : new RunBeforeParams(statement, befores);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters$RunBeforeParams.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters$RunBeforeParams.class */
    public class RunBeforeParams extends RunBefores {
        RunBeforeParams(Statement next, List<FrameworkMethod> befores) {
            super(next, befores, null);
        }

        @Override // org.junit.internal.runners.statements.RunBefores
        protected void invokeMethod(FrameworkMethod method) throws Throwable {
            int paramCount = method.getMethod().getParameterTypes().length;
            method.invokeExplosively(null, paramCount == 0 ? null : BlockJUnit4ClassRunnerWithParameters.this.parameters);
        }
    }

    private Statement withAfterParams(Statement statement) {
        List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(Parameterized.AfterParam.class);
        return afters.isEmpty() ? statement : new RunAfterParams(statement, afters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters$RunAfterParams.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParameters$RunAfterParams.class */
    public class RunAfterParams extends RunAfters {
        RunAfterParams(Statement next, List<FrameworkMethod> afters) {
            super(next, afters, null);
        }

        @Override // org.junit.internal.runners.statements.RunAfters
        protected void invokeMethod(FrameworkMethod method) throws Throwable {
            int paramCount = method.getMethod().getParameterTypes().length;
            method.invokeExplosively(null, paramCount == 0 ? null : BlockJUnit4ClassRunnerWithParameters.this.parameters);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.junit.runners.ParentRunner
    public Annotation[] getRunnerAnnotations() {
        Annotation[] allAnnotations = super.getRunnerAnnotations();
        Annotation[] annotationsWithoutRunWith = new Annotation[allAnnotations.length - 1];
        int i = 0;
        for (Annotation annotation : allAnnotations) {
            if (!annotation.annotationType().equals(RunWith.class)) {
                annotationsWithoutRunWith[i] = annotation;
                i++;
            }
        }
        return annotationsWithoutRunWith;
    }

    private List<FrameworkField> getAnnotatedFieldsByParameter() {
        return getTestClass().getAnnotatedFields(Parameterized.Parameter.class);
    }

    private InjectionType getInjectionType() {
        if (fieldsAreAnnotated()) {
            return InjectionType.FIELD;
        }
        return InjectionType.CONSTRUCTOR;
    }

    private boolean fieldsAreAnnotated() {
        return !getAnnotatedFieldsByParameter().isEmpty();
    }
}
