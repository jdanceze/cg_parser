package org.mockito;

import java.util.List;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.util.Primitives;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/ArgumentCaptor.class */
public class ArgumentCaptor<T> {
    private final CapturingMatcher<T> capturingMatcher = new CapturingMatcher<>();
    private final Class<? extends T> clazz;

    private ArgumentCaptor(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    public T capture() {
        Mockito.argThat(this.capturingMatcher);
        return (T) Primitives.defaultValue(this.clazz);
    }

    public T getValue() {
        return this.capturingMatcher.getLastValue();
    }

    public List<T> getAllValues() {
        return this.capturingMatcher.getAllValues();
    }

    public static <U, S extends U> ArgumentCaptor<U> forClass(Class<S> clazz) {
        return new ArgumentCaptor<>(clazz);
    }
}
