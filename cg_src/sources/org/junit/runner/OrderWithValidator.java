package org.junit.runner;

import java.util.Collections;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.runners.model.TestClass;
import org.junit.validator.AnnotationValidator;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/OrderWithValidator.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/OrderWithValidator.class */
public final class OrderWithValidator extends AnnotationValidator {
    @Override // org.junit.validator.AnnotationValidator
    public List<Exception> validateAnnotatedClass(TestClass testClass) {
        if (testClass.getAnnotation(FixMethodOrder.class) != null) {
            return Collections.singletonList(new Exception("@FixMethodOrder cannot be combined with @OrderWith"));
        }
        return Collections.emptyList();
    }
}
