package org.junit.validator;

import java.util.List;
import org.junit.runners.model.TestClass;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/validator/TestClassValidator.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/validator/TestClassValidator.class */
public interface TestClassValidator {
    List<Exception> validateTestClass(TestClass testClass);
}
