package org.mockito;

import java.lang.reflect.Constructor;
import java.util.List;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockedConstruction.class */
public interface MockedConstruction<T> extends ScopedMock {

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockedConstruction$Context.class */
    public interface Context {
        int getCount();

        Constructor<?> constructor();

        List<?> arguments();
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockedConstruction$MockInitializer.class */
    public interface MockInitializer<T> {
        void prepare(T t, Context context) throws Throwable;
    }

    List<T> constructed();
}
