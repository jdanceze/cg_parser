package org.junit.validator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.runners.model.Annotatable;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/validator/AnnotationsValidator.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/validator/AnnotationsValidator.class */
public final class AnnotationsValidator implements TestClassValidator {
    private static final List<AnnotatableValidator<?>> VALIDATORS = Arrays.asList(new ClassValidator(), new MethodValidator(), new FieldValidator());

    @Override // org.junit.validator.TestClassValidator
    public List<Exception> validateTestClass(TestClass testClass) {
        List<Exception> validationErrors = new ArrayList<>();
        for (AnnotatableValidator<?> validator : VALIDATORS) {
            List<Exception> additionalErrors = validator.validateTestClass(testClass);
            validationErrors.addAll(additionalErrors);
        }
        return validationErrors;
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/validator/AnnotationsValidator$AnnotatableValidator.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/validator/AnnotationsValidator$AnnotatableValidator.class */
    private static abstract class AnnotatableValidator<T extends Annotatable> {
        private static final AnnotationValidatorFactory ANNOTATION_VALIDATOR_FACTORY = new AnnotationValidatorFactory();

        abstract Iterable<T> getAnnotatablesForTestClass(TestClass testClass);

        abstract List<Exception> validateAnnotatable(AnnotationValidator annotationValidator, T t);

        private AnnotatableValidator() {
        }

        public List<Exception> validateTestClass(TestClass testClass) {
            List<Exception> validationErrors = new ArrayList<>();
            for (T annotatable : getAnnotatablesForTestClass(testClass)) {
                List<Exception> additionalErrors = validateAnnotatable(annotatable);
                validationErrors.addAll(additionalErrors);
            }
            return validationErrors;
        }

        private List<Exception> validateAnnotatable(T annotatable) {
            List<Exception> validationErrors = new ArrayList<>();
            Annotation[] arr$ = annotatable.getAnnotations();
            for (Annotation annotation : arr$) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                ValidateWith validateWith = (ValidateWith) annotationType.getAnnotation(ValidateWith.class);
                if (validateWith != null) {
                    AnnotationValidator annotationValidator = ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);
                    List<Exception> errors = validateAnnotatable(annotationValidator, annotatable);
                    validationErrors.addAll(errors);
                }
            }
            return validationErrors;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/validator/AnnotationsValidator$ClassValidator.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/validator/AnnotationsValidator$ClassValidator.class */
    private static class ClassValidator extends AnnotatableValidator<TestClass> {
        private ClassValidator() {
            super();
        }

        @Override // org.junit.validator.AnnotationsValidator.AnnotatableValidator
        Iterable<TestClass> getAnnotatablesForTestClass(TestClass testClass) {
            return Collections.singletonList(testClass);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.junit.validator.AnnotationsValidator.AnnotatableValidator
        public List<Exception> validateAnnotatable(AnnotationValidator validator, TestClass testClass) {
            return validator.validateAnnotatedClass(testClass);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/validator/AnnotationsValidator$MethodValidator.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/validator/AnnotationsValidator$MethodValidator.class */
    private static class MethodValidator extends AnnotatableValidator<FrameworkMethod> {
        private MethodValidator() {
            super();
        }

        @Override // org.junit.validator.AnnotationsValidator.AnnotatableValidator
        Iterable<FrameworkMethod> getAnnotatablesForTestClass(TestClass testClass) {
            return testClass.getAnnotatedMethods();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.junit.validator.AnnotationsValidator.AnnotatableValidator
        public List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkMethod method) {
            return validator.validateAnnotatedMethod(method);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/validator/AnnotationsValidator$FieldValidator.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/validator/AnnotationsValidator$FieldValidator.class */
    private static class FieldValidator extends AnnotatableValidator<FrameworkField> {
        private FieldValidator() {
            super();
        }

        @Override // org.junit.validator.AnnotationsValidator.AnnotatableValidator
        Iterable<FrameworkField> getAnnotatablesForTestClass(TestClass testClass) {
            return testClass.getAnnotatedFields();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.junit.validator.AnnotationsValidator.AnnotatableValidator
        public List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkField field) {
            return validator.validateAnnotatedField(field);
        }
    }
}
