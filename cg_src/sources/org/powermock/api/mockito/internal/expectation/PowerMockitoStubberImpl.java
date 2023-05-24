package org.powermock.api.mockito.internal.expectation;

import java.lang.reflect.Method;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;
import org.powermock.api.mockito.expectation.PrivatelyExpectedArguments;
import org.powermock.api.mockito.invocation.MockitoMethodInvocationControl;
import org.powermock.core.MockRepository;
import org.powermock.reflect.Whitebox;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/expectation/PowerMockitoStubberImpl.class */
public class PowerMockitoStubberImpl implements PowerMockitoStubber, Stubber {
    private final Stubber stubber;

    public PowerMockitoStubberImpl(Stubber stubber) {
        this.stubber = stubber;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.mockito.stubbing.Stubber
    public <T> T when(T instanceMock) {
        T returnValue;
        MockitoMethodInvocationControl invocationControl = (MockitoMethodInvocationControl) MockRepository.getInstanceMethodInvocationControl(instanceMock);
        if (invocationControl == null) {
            returnValue = this.stubber.when(instanceMock);
        } else {
            Object mock = invocationControl.getMockHandlerAdaptor().getMock();
            this.stubber.when(mock);
            returnValue = instanceMock;
        }
        return returnValue;
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Throwable... toBeThrown) {
        return this.stubber.doThrow(toBeThrown);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return this.stubber.doThrow(toBeThrown);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>[] nextToBeThrown) {
        return this.stubber.doThrow(toBeThrown, nextToBeThrown);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doAnswer(Answer answer) {
        return this.stubber.doAnswer(answer);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doNothing() {
        return this.stubber.doNothing();
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doReturn(Object toBeReturned) {
        return this.stubber.doReturn(toBeReturned);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        return this.stubber.doReturn(toBeReturned, nextToBeReturned);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doCallRealMethod() {
        return this.stubber.doCallRealMethod();
    }

    @Override // org.powermock.api.mockito.expectation.PowerMockitoStubber
    public void when(Class<?> classMock) {
        MockitoMethodInvocationControl invocationControl = (MockitoMethodInvocationControl) MockRepository.getStaticMethodInvocationControl(classMock);
        Object mock = invocationControl.getMockHandlerAdaptor().getMock();
        this.stubber.when(mock);
    }

    @Override // org.powermock.api.mockito.expectation.PowerMockitoStubber
    public <T> PrivatelyExpectedArguments when(T mock, Method method) throws Exception {
        assertNotNull(mock, "mock");
        assertNotNull(method, XmlConstants.Attributes.method);
        when((PowerMockitoStubberImpl) mock);
        return new DefaultPrivatelyExpectedArguments(mock, method);
    }

    @Override // org.powermock.api.mockito.expectation.PowerMockitoStubber
    public <T> void when(T mock, Object... arguments) throws Exception {
        assertNotNull(mock, "mock");
        when((PowerMockitoStubberImpl) mock);
        Whitebox.invokeMethod(mock, arguments);
    }

    @Override // org.powermock.api.mockito.expectation.PowerMockitoStubber
    public <T> void when(T mock, String methodToExpect, Object... arguments) throws Exception {
        assertNotNull(mock, "mock");
        assertNotNull(methodToExpect, "methodToExpect");
        when((PowerMockitoStubberImpl) mock);
        Whitebox.invokeMethod(mock, methodToExpect, arguments);
    }

    @Override // org.powermock.api.mockito.expectation.PowerMockitoStubber
    public <T> void when(Class<T> classMock, Object... arguments) throws Exception {
        assertNotNull(classMock, "classMock");
        when((Class<?>) classMock);
        Whitebox.invokeMethod((Class<?>) classMock, arguments);
    }

    @Override // org.powermock.api.mockito.expectation.PowerMockitoStubber
    public <T> void when(Class<T> classMock, String methodToExpect, Object... parameters) throws Exception {
        assertNotNull(classMock, "classMock");
        assertNotNull(methodToExpect, "methodToExpect");
        when((Class<?>) classMock);
        Whitebox.invokeMethod((Class<?>) classMock, methodToExpect, parameters);
    }

    @Override // org.powermock.api.mockito.expectation.PowerMockitoStubber
    public <T> PrivatelyExpectedArguments when(Class<T> classMock, Method method) throws Exception {
        assertNotNull(classMock, "classMock");
        assertNotNull(method, XmlConstants.Attributes.method);
        when((Class<?>) classMock);
        return new DefaultPrivatelyExpectedArguments(classMock, method);
    }

    private void assertNotNull(Object object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }
}
