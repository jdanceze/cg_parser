package org.mockito;

import java.io.Serializable;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockSettings.class */
public interface MockSettings extends Serializable {
    MockSettings extraInterfaces(Class<?>... clsArr);

    MockSettings name(String str);

    MockSettings spiedInstance(Object obj);

    MockSettings defaultAnswer(Answer answer);

    MockSettings serializable();

    MockSettings serializable(SerializableMode serializableMode);

    MockSettings verboseLogging();

    MockSettings stubbingLookupListeners(StubbingLookupListener... stubbingLookupListenerArr);

    MockSettings invocationListeners(InvocationListener... invocationListenerArr);

    @Incubating
    MockSettings verificationStartedListeners(VerificationStartedListener... verificationStartedListenerArr);

    MockSettings stubOnly();

    @Incubating
    MockSettings useConstructor(Object... objArr);

    @Incubating
    MockSettings outerInstance(Object obj);

    @Incubating
    MockSettings withoutAnnotations();

    @Incubating
    <T> MockCreationSettings<T> build(Class<T> cls);

    @Incubating
    <T> MockCreationSettings<T> buildStatic(Class<T> cls);

    @Incubating
    MockSettings lenient();
}
