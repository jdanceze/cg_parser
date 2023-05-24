package org.junit.runners.model;

import java.util.List;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/InvalidTestClassError.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/InvalidTestClassError.class */
public class InvalidTestClassError extends InitializationError {
    private static final long serialVersionUID = 1;
    private final String message;

    public InvalidTestClassError(Class<?> offendingTestClass, List<Throwable> validationErrors) {
        super(validationErrors);
        this.message = createMessage(offendingTestClass, validationErrors);
    }

    private static String createMessage(Class<?> testClass, List<Throwable> validationErrors) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Invalid test class '%s':", testClass.getName()));
        int i = 1;
        for (Throwable error : validationErrors) {
            int i2 = i;
            i++;
            sb.append("\n  " + i2 + ". " + error.getMessage());
        }
        return sb.toString();
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.message;
    }
}
