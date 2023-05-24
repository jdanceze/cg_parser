package org.mockito.internal.stubbing.defaultanswers;

import java.io.IOException;
import java.io.Serializable;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsDeepStubs.class */
public class ReturnsDeepStubs implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -7105341425736035847L;

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());
        Class<?> rawType = returnTypeGenericMetadata.rawType();
        if (!mockitoCore().isTypeMockable(rawType)) {
            if (invocation.getMethod().getReturnType().equals(rawType)) {
                return delegate().answer(invocation);
            }
            return delegate().returnValueFor(rawType);
        } else if (rawType.equals(Object.class) && !returnTypeGenericMetadata.hasRawExtraInterfaces()) {
            return null;
        } else {
            return deepStub(invocation, returnTypeGenericMetadata);
        }
    }

    private Object deepStub(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
        InvocationContainerImpl container = MockUtil.getInvocationContainer(invocation.getMock());
        for (Stubbing stubbing : container.getStubbingsDescending()) {
            if (container.getInvocationForStubbing().matches(stubbing.getInvocation())) {
                return stubbing.answer(invocation);
            }
        }
        StubbedInvocationMatcher stubbing2 = recordDeepStubAnswer(newDeepStubMock(returnTypeGenericMetadata, invocation.getMock()), container);
        stubbing2.markStubUsed(stubbing2.getInvocation());
        return stubbing2.answer(invocation);
    }

    private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata, Object parentMock) {
        MockCreationSettings parentMockSettings = MockUtil.getMockSettings(parentMock);
        return mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata, parentMockSettings));
    }

    private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata, MockCreationSettings parentMockSettings) {
        MockSettings withSettings;
        if (returnTypeGenericMetadata.hasRawExtraInterfaces()) {
            withSettings = Mockito.withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());
        } else {
            withSettings = Mockito.withSettings();
        }
        MockSettings mockSettings = withSettings;
        return propagateSerializationSettings(mockSettings, parentMockSettings).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    private MockSettings propagateSerializationSettings(MockSettings mockSettings, MockCreationSettings parentMockSettings) {
        return mockSettings.serializable(parentMockSettings.getSerializableMode());
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
    }

    private StubbedInvocationMatcher recordDeepStubAnswer(Object mock, InvocationContainerImpl container) {
        DeeplyStubbedAnswer answer = new DeeplyStubbedAnswer(mock);
        return container.addAnswer(answer, false, null);
    }

    protected GenericMetadataSupport actualParameterizedType(Object mock) {
        CreationSettings mockSettings = (CreationSettings) MockUtil.getMockHandler(mock).getMockSettings();
        return GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsDeepStubs$ReturnsDeepStubsSerializationFallback.class */
    public static class ReturnsDeepStubsSerializationFallback extends ReturnsDeepStubs implements Serializable {
        private final GenericMetadataSupport returnTypeGenericMetadata;

        public ReturnsDeepStubsSerializationFallback(GenericMetadataSupport returnTypeGenericMetadata) {
            this.returnTypeGenericMetadata = returnTypeGenericMetadata;
        }

        @Override // org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs
        protected GenericMetadataSupport actualParameterizedType(Object mock) {
            return this.returnTypeGenericMetadata;
        }

        private Object writeReplace() throws IOException {
            return Mockito.RETURNS_DEEP_STUBS;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsDeepStubs$DeeplyStubbedAnswer.class */
    public static class DeeplyStubbedAnswer implements Answer<Object>, Serializable {
        private final Object mock;

        DeeplyStubbedAnswer(Object mock) {
            this.mock = mock;
        }

        @Override // org.mockito.stubbing.Answer
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return this.mock;
        }
    }

    private static MockitoCore mockitoCore() {
        return LazyHolder.MOCKITO_CORE;
    }

    private static ReturnsEmptyValues delegate() {
        return LazyHolder.DELEGATE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsDeepStubs$LazyHolder.class */
    public static class LazyHolder {
        private static final MockitoCore MOCKITO_CORE = new MockitoCore();
        private static final ReturnsEmptyValues DELEGATE = new ReturnsEmptyValues();

        private LazyHolder() {
        }
    }
}
