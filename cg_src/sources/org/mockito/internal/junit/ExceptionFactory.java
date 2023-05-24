package org.mockito.internal.junit;

import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/ExceptionFactory.class */
public class ExceptionFactory {
    private static final ExceptionFactoryImpl factory;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/ExceptionFactory$ExceptionFactoryImpl.class */
    public interface ExceptionFactoryImpl {
        AssertionError create(String str, String str2, String str3);
    }

    private ExceptionFactory() {
    }

    static {
        ExceptionFactoryImpl theFactory = null;
        try {
            Class.forName("org.opentest4j.AssertionFailedError");
            theFactory = ArgumentsAreDifferent::new;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("junit.framework.ComparisonFailure");
                theFactory = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent::new;
            } catch (ClassNotFoundException e2) {
            }
        }
        factory = theFactory == null ? org.mockito.exceptions.verification.ArgumentsAreDifferent::new : theFactory;
    }

    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual) {
        return factory.create(message, wanted, actual);
    }
}
