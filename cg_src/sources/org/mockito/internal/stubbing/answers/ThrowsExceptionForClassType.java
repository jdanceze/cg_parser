package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.configuration.plugins.Plugins;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/ThrowsExceptionForClassType.class */
public class ThrowsExceptionForClassType extends AbstractThrowsException implements Serializable {
    private final Class<? extends Throwable> throwableClass;

    public ThrowsExceptionForClassType(Class<? extends Throwable> throwableClass) {
        this.throwableClass = throwableClass;
    }

    @Override // org.mockito.internal.stubbing.answers.AbstractThrowsException
    protected Throwable getThrowable() {
        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(null);
        return (Throwable) instantiator.newInstance(this.throwableClass);
    }
}
