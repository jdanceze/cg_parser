package org.mockito.mock;

import java.util.List;
import java.util.Set;
import org.mockito.Incubating;
import org.mockito.NotExtensible;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.stubbing.Answer;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/mock/MockCreationSettings.class */
public interface MockCreationSettings<T> {
    Class<T> getTypeToMock();

    Set<Class<?>> getExtraInterfaces();

    MockName getMockName();

    Answer<?> getDefaultAnswer();

    Object getSpiedInstance();

    boolean isSerializable();

    SerializableMode getSerializableMode();

    boolean isStubOnly();

    boolean isStripAnnotations();

    List<StubbingLookupListener> getStubbingLookupListeners();

    List<InvocationListener> getInvocationListeners();

    @Incubating
    List<VerificationStartedListener> getVerificationStartedListeners();

    @Incubating
    boolean isUsingConstructor();

    @Incubating
    Object[] getConstructorArgs();

    @Incubating
    Object getOuterClassInstance();

    @Incubating
    boolean isLenient();
}
